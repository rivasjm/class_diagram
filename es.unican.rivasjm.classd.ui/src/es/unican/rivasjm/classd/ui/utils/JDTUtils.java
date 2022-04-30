package es.unican.rivasjm.classd.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class JDTUtils {
	
	public static List<IType> getTypes(IJavaElement element) {
		List<IType> ret = new ArrayList<>();
		
		if (element == null) {
			return ret;
		}
		
		try {
			if (element instanceof ICompilationUnit) {
				for (IType type : ((ICompilationUnit) element).getAllTypes()) {
					ret.add(type);
				}
				
			}  else if (element instanceof IPackageFragment) {
				for (IJavaElement child : ((IPackageFragment) element).getChildren()) {
					ret.addAll(getTypes(child));
				}
			}
			
		} catch (JavaModelException e) {
			return new ArrayList<>();
		}
		
		return ret;
	}
	
	/**
	 * Get the {@code IType} of a given field.
	 * 
	 * <p> It uses the algorithm from : <a href="https://stackoverflow.com/questions/16713269/jdt-check-if-ifield-is-an-reference-type">
	 * https://stackoverflow.com/questions/16713269/jdt-check-if-ifield-is-an-reference-type</a>
	 * @param field the given field
	 * @return the {@link IType}
	 */
	public static IType getFieldType(final IField field) {
		final ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
		parser.setResolveBindings(true);
	    parser.setSource(field.getCompilationUnit());
	    final ASTNode unitNode = parser.createAST(new NullProgressMonitor());
	    	    
	    FieldTypeASTVisitor visitor = new FieldTypeASTVisitor(field);
	    unitNode.accept(visitor);
		return visitor.getFieldType();
	}
	
	private static class FieldTypeASTVisitor extends ASTVisitor {
		
		private final IField field;
		private IType fieldType;

		public FieldTypeASTVisitor(IField field) {
			this.field = field;
		}

		@Override
		public boolean visit(VariableDeclarationFragment node) {
			IJavaElement element = node.resolveBinding().getJavaElement();
            if (field.equals(element)) {
                FieldDeclaration fieldDeclaration = (FieldDeclaration)node.getParent();
                fieldType = (IType)fieldDeclaration.getType().resolveBinding().getJavaElement();
            }
            return false;
		}
		
		public IType getFieldType() {
			return fieldType;
		}
	}

}
