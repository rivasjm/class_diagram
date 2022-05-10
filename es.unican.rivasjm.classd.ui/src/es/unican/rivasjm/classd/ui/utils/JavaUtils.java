package es.unican.rivasjm.classd.ui.utils;

import java.util.Collection;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Type;

public class JavaUtils {
	
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

}
