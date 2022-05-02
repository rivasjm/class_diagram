package es.unican.rivasjm.classd.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class JdtDomUtils {
	
	public static List<ICompilationUnit> getCompilationUnits(IJavaElement element) {
		List<ICompilationUnit> units = new ArrayList<>();
		if (element == null) {
			return units;
		}
		
		if (element instanceof ICompilationUnit) {
			units.add((ICompilationUnit) element);
		
		} else if (element instanceof IPackageFragment) {
			try {
				for (IJavaElement child : ((IPackageFragment) element).getChildren()) {
					units.addAll(getCompilationUnits(child));
				}
				
			} catch (JavaModelException e) {}	
		}
		
		return units;
	}
	
	public static List<TypeDeclaration> getTypes(List<ICompilationUnit> units) {
		final List<TypeDeclaration> types = new ArrayList<>();
		
		for (ICompilationUnit unit : units) {
			final ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
			parser.setResolveBindings(true);
			parser.setSource(unit);
			final ASTNode node = parser.createAST(new NullProgressMonitor());

			node.accept(new ASTVisitor() {
				@Override
				public boolean visit(TypeDeclaration node) {
					types.add(node);
					return super.visit(node);
				}
			});
		}
		
		return types;
	}
	
	public static boolean fieldIsKnown(FieldDeclaration field, List<TypeDeclaration> knownTypes) {
		Type type = field.getType();
		
		if (type instanceof PrimitiveType) {
			return false;
		
		} else if (type instanceof ArrayType) {
			
			
		}
		
		return false;
	}
	
	public static boolean isMultiple(Type type) {
		if (type == null) {
			return false;
		
		} else if (type.isArrayType()) {
			return true;
			
		} else if (type.isParameterizedType()) {
			ParameterizedType pt = (ParameterizedType) type;
			if (pt.getType().isSimpleType()) {
				SimpleType st = (SimpleType) pt.getType();
				st.resolveBinding()
			}
			
		}
		
		return false;
	}
	
	public static void getMultipleType(Type type) {
		
	}

}
