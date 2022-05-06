package es.unican.rivasjm.classd.ui.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class JdtDomUtils {
	
	public static Set<ICompilationUnit> getCompilationUnits(IJavaElement element) {
		Set<ICompilationUnit> units = new HashSet<>();
		if (element == null) {
			return units;
		}
		
		if (element instanceof ICompilationUnit) {
			units.add((ICompilationUnit) element);
		
		} else if (element instanceof IJavaProject || element instanceof IPackageFragmentRoot || element instanceof IPackageFragment) {
			try {
				for (IJavaElement child : ((IParent) element).getChildren()) {
					units.addAll(getCompilationUnits(child));
				}
				
			} catch (JavaModelException e) {}	
		}
		
		return units;
	}
	
	public static Set<AbstractTypeDeclaration> getDeclaredTypesAndEnums(Set<ICompilationUnit> units) {
		final Set<AbstractTypeDeclaration> types = new HashSet<>();
		
		for (ICompilationUnit unit : units) {
			final ASTParser parser = ASTParser.newParser(JDTUtils.getLatestJLSLevel());
			parser.setResolveBindings(true);
			parser.setSource(unit);
			final ASTNode node = parser.createAST(new NullProgressMonitor());

			node.accept(new ASTVisitor() {
				@Override
				public boolean visit(TypeDeclaration node) {
					types.add(node);
					return super.visit(node);
				}
				
				@Override
				public boolean visit(EnumDeclaration node) {
					types.add(node);
					return super.visit(node);
				}
			});
		}
		
		return types;
	}
	
	public static boolean fieldIsReference(FieldDeclaration field, Set<TypeDeclaration> knownTypes) {
		ITypeBinding umlBinding = resolveUMLBinding(field.getType());
		
		for (TypeDeclaration known : knownTypes) {
			ITypeBinding knownBinding = known.resolveBinding();
			boolean isKnown = typesAreEquals(knownBinding, umlBinding);
			if (isKnown) {
				return true;
			}
		}

		return false;
	}
	
	public static ITypeBinding resolveUMLBinding(Type type) {
		if (type == null) {
			return null;
		}
		
		if (type.isParameterizedType() && isMultiple(type)) {
			ParameterizedType pt = (ParameterizedType) type;
			Type typeArg = (Type) pt.typeArguments().get(0);
			return resolveUMLBinding(typeArg);
			
		} else if (type.isParameterizedType()) {
			ParameterizedType pt = (ParameterizedType) type;
			return resolveUMLBinding(pt.getType());
		
		} else if (type.isSimpleType() || type.isPrimitiveType()) {
			return type.resolveBinding();
		
		} else if (type.isArrayType()) {
			ArrayType at = (ArrayType) type;
			return resolveUMLBinding(at.getElementType());
		
		} else {
			return null;
		}
	}
	
	public static boolean isMultiple(Type type) {
		if (type == null) {
			return false;
		}
		
		if (type.isArrayType()) {
			return true;
		
		} else if (type.isParameterizedType()) {
			ITypeBinding binding = type.resolveBinding();
			return JDTUtils.isCollection((IType) binding.getJavaElement());
		}
		
		return false;
	}
	
//	public static ITypeBinding getMultipleType(Type type) {
//		if (type == null || !isMultiple(type)) {
//			return null;
//			
//		} else if (type.isParameterizedType()) {
//			ParameterizedType pt = (ParameterizedType) type;
//			
//			@SuppressWarnings("rawtypes")
//			List args = pt.typeArguments();
//			if (args.size() == 1 && args.get(0) instanceof Type) {
//				return ((Type)args.get(0)).resolveBinding();
//			}
//			
//		} else if (type.isArrayType()) {
//			ArrayType at = (ArrayType) type;
//			if (at.getElementType() instanceof SimpleType) {
//				return at.getElementType().resolveBinding();
//			}
//		}
//		
//		return null;
//	}
	
	public static String getFieldName(FieldDeclaration field) {
		final StringBuilder sb = new StringBuilder();
		field.accept(new ASTVisitor() {
			@Override
			public boolean visit(VariableDeclarationFragment node) {
				sb.append(node.getName().getIdentifier());
				return false;
			}
		});
		
		return sb.toString();
	}
	
	public static String getTypeString(Type type) {		
		if (type == null) {
			return null;
		}
		
		if (type.isPrimitiveType()) {
			PrimitiveType pt = (PrimitiveType) type;
			return pt.getPrimitiveTypeCode().toString();
			
		} else if (type.isArrayType()) {
			ArrayType at = (ArrayType) type;
			return getTypeString(at.getElementType());
			
		} else if (type.isSimpleType()) {
			SimpleType st = (SimpleType) type;
			return getName(st.getName());
			
		} else if (type.isParameterizedType() && isMultiple(type)) {
			ParameterizedType pt = (ParameterizedType) type;
			@SuppressWarnings("rawtypes")
			List typeArguments = pt.typeArguments();
			Type typeArg = (Type) typeArguments.get(0);
			return getTypeString(typeArg);
			
		} else {
			return type.toString();
			
		}
	}
	
	public static String getName(Name name) {
		if (name.isSimpleName()) {
			SimpleName sn = (SimpleName) name;
			return sn.getIdentifier();
		
		} else if (name.isQualifiedName()) {
			QualifiedName qn = (QualifiedName)name;
			return getName(qn.getName());
		}
		
		return null;
	}
	
	public static String getQualifiedName(Type type) {
		return type.resolveBinding().getQualifiedName();
	}
	
	public static boolean typesAreEquals(ITypeBinding b1, ITypeBinding b2) {
		if (b1 == null || b2 == null) {
			return false;
		}
		
		return b1.getQualifiedName().equals(b2.getQualifiedName());
	}
	
	@SuppressWarnings("unchecked")
	public static List<FieldDeclaration> getFieldDeclarations(AbstractTypeDeclaration type) {
		List<FieldDeclaration> fields = new ArrayList<>();
		type.bodyDeclarations().stream().filter(b -> b instanceof FieldDeclaration).forEach(f -> fields.add((FieldDeclaration) f));
		return fields;
	}
	
	@SuppressWarnings("unchecked")
	public static List<MethodDeclaration> getMethodDeclarations(AbstractTypeDeclaration type) {
		List<MethodDeclaration> methods = new ArrayList<>();
		type.bodyDeclarations().stream().filter(b -> b instanceof MethodDeclaration).forEach(m -> methods.add((MethodDeclaration) m));
		return methods;
	}

}
