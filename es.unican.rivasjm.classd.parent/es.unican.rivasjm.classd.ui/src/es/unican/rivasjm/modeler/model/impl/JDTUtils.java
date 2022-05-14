package es.unican.rivasjm.modeler.model.impl;

import static java.util.Collections.emptyList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
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
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import es.unican.rivasjm.modeler.model.EVisibility;

public class JDTUtils {
	
	/**
	 * Get all compilation units from the given java element
	 * @param element
	 * @return
	 */
	public static Set<ICompilationUnit> getCompilationUnits(IJavaElement element) {
		final Set<ICompilationUnit> units = new HashSet<>();
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
	
	/**
	 * Get all types and enum's declared in the given compilation units
	 * @param units
	 * @return
	 */
	public static Set<AbstractTypeDeclaration> getDeclaredTypesAndEnums(Set<ICompilationUnit> units) {
		final Set<AbstractTypeDeclaration> types = new HashSet<>();
		
		for (ICompilationUnit unit : units) {
			final ASTParser parser = ASTParser.newParser(getLatestJLSLevel());
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
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static String getFullyQualifiedName(Type type) {
		if (type == null) {
			return null;
		}
		
		final ITypeBinding binding = type.resolveBinding();
		return binding != null ? binding.getQualifiedName() : null;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static String getFullyQualifiedName(AbstractTypeDeclaration type) {
		if (type == null) {
			return null;
		}
		
		final ITypeBinding binding = type.resolveBinding();
		return binding != null ? binding.getQualifiedName() : null;
	}
	
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static Type resolveTypeForUML(Type type) {
		if (type == null) {
			return null;
		
		} else if (isMultiple(type) && type.isParameterizedType()) {
			ParameterizedType pt = (ParameterizedType) type;
			if (pt.typeArguments().size() == 1) {
				return (Type) pt.typeArguments().get(0);
			}
			
		} else if (isMultiple(type) && type.isArrayType()) {
			ArrayType at = (ArrayType) type;
			return at.getElementType();
			
		} else if (type.isParameterizedType()) {
			ParameterizedType pt = (ParameterizedType) type;
			return pt.getType();
			
		} else {
			return type;
		}
		
		return null;
	}
	
	/**
	 * Returns true if the given type has a multiplicity higher than 1.
	 * 
	 * <p> To have a multiplicity higher than 1, the type either be an array, or implement
	 * the interface {@code Collection}
	 * @param type the given type
	 * @return true if the given type has a multiplicity higher than 1.
	 */
	public static boolean isMultiple(Type type) {
		if (type == null) {
			return false;
		
		} else if (type.isArrayType()) {
			return true;
		
		} else if (type.isParameterizedType()) {
			return implementsInterface(type, Collection.class);
			
		}
		
		return false;
	}
	
	/**
	 * Returns true if the given type is an instance of the given Java class
	 * @param type
	 * @param clazz
	 * @return
	 */
	public static boolean implementsInterface(Type type, Class<?> clazz) {
		if (type == null || type.resolveBinding() == null || clazz == null) {
			return false;
		}
		
		try {
			IType iType = (IType) type.resolveBinding().getJavaElement();
			ITypeHierarchy hierarchy = iType.newSupertypeHierarchy(new NullProgressMonitor());
			for (IType inter : hierarchy.getAllInterfaces()) {
				String qualifiedName = inter.getFullyQualifiedName();
				if (clazz.getName().equals(qualifiedName)) {
					return true;
				}
			}
		
		} catch (JavaModelException e) {}
		
		return false;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static String getTypeString(Type type) {		
		if (type == null) {
			return ""; 
		}
		
		return type.toString();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static String getName(Name name) {
		if (name.isSimpleName()) {
			SimpleName sn = (SimpleName) name;
			return sn.getIdentifier();
		
		} else if (name.isQualifiedName()) {
			QualifiedName qn = (QualifiedName)name;
			return getName(qn.getName());
		}
		
		return "";
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	public static String getFieldName(FieldDeclaration field) {
		final StringBuilder sb = new StringBuilder();
		field.accept(new ASTVisitor() {
			@Override
			public boolean visit(VariableDeclarationFragment node) {
				sb.append(getName(node.getName()));
				return false;
			}
		});
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static List<Type> getSuperInterfaces(AbstractTypeDeclaration type) {
		List<Type> interfaces = new ArrayList<>();
		
		@SuppressWarnings("rawtypes")
		List superInterfaceTypes = null;
		if (type != null && type instanceof TypeDeclaration) {
			superInterfaceTypes = ((TypeDeclaration)type).superInterfaceTypes();
		} else if (type != null && type instanceof EnumDeclaration) {
			superInterfaceTypes = ((EnumDeclaration)type).superInterfaceTypes();
		}
		
		if (superInterfaceTypes != null) {
			for (Object object : superInterfaceTypes) {
				if (object instanceof Type) {
					interfaces.add((Type) object);
				}
			}
		}
		
		return interfaces;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static List<FieldDeclaration> getFieldDeclarations(AbstractTypeDeclaration type) {
		List<FieldDeclaration> fields = new ArrayList<>();
		for (Object decl : type.bodyDeclarations()) {
			if (decl instanceof FieldDeclaration) {
				fields.add((FieldDeclaration) decl);
			}
		}			
		return fields;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static List<MethodDeclaration> getMethodDeclarations(AbstractTypeDeclaration type) {
		List<MethodDeclaration> methods = new ArrayList<>();
		for (Object decl : type.bodyDeclarations()) {
			if (decl instanceof MethodDeclaration) {
				methods.add((MethodDeclaration) decl);
			}
		}		
		return methods;
	}
	
	/**
	 * 
	 * @param method
	 * @return
	 */
	public static List<SingleVariableDeclaration> getMethodParameters(MethodDeclaration method) {
		List<SingleVariableDeclaration> params = new ArrayList<>();
		if (method == null) {
			return params;
		}
		
		for (Object object : method.parameters()) {
			if (object instanceof SingleVariableDeclaration) {
				params.add((SingleVariableDeclaration) object);
			}
		}
		
		return params;
	}
	
	public static List<String> getEnumConstants(EnumDeclaration type) {
		List<String> constants = new ArrayList<>();
		if (type == null) {
			return constants;
		}
		
		for (Object object : type.enumConstants()) {
			if (object instanceof EnumConstantDeclaration) {
				SimpleName name = ((EnumConstantDeclaration) object).getName();
				constants.add(getName(name));
			}
		}
		
		return constants;
	}
	
	/**
	 * 
	 * @param modifiers
	 * @return
	 */
	public static EVisibility getVibility(int modifiers) {
		if (Modifier.isPublic(modifiers)) {
			return EVisibility.PUBLIC;
		
		} else if (Modifier.isProtected(modifiers)) {
			return EVisibility.PROTECTED;
		
		} else if (Modifier.isPrivate(modifiers)) {
			return EVisibility.PRIVATE;
		}
		
		return EVisibility.PACKAGE;
	}
	
	/**
	 * Returns the latest available JLS level code. 
	 * 
	 * <p> The method to get this code has been changing during the last versions of JDT.
	 * To make this plug-in independent on the version of JDT, this method tries to
	 * determine the latest JLS code using Reflection
	 * 
	 * @return the latest available JLS level code
	 */
	static int getLatestJLSLevel() {
		Class<?> clazz = AST.class;
		
		// try calling getLatestJLS (this is in the latest API)
		try {
			Method method = clazz.getMethod("getJLSLatest");
			Object level = method.invoke(clazz);
			if (level instanceof Integer) {
				return (Integer)level;
			}
		} catch (Exception e) {
		}
		
		// if it failed, try accessing JLS_Latest
		try {
			Field field = clazz.getField("JLS_Latest");
			field.setAccessible(true);
			Object level = field.get(clazz);
			if (level instanceof Integer) {
				return (Integer)level;
			}
		} catch (Exception e) {
		}
		
		// if it failed, try accessing the highest JLSx value available, starting from 11
		for (int i = 11; i > 1; i--) {
			try {	
				Field field = clazz.getField("JLS" + i);
				field.setAccessible(true);
				Object level = field.get(clazz);
				if (level instanceof Integer) {
					return (Integer)level;
				}
			} catch (Exception e) {
			}
		}
		
		// if absolutely everything fails, return hard-coded value 4, which should be JLS4
		return 4;
	}

}
