package es.unican.rivasjm.classd.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

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

}
