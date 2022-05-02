package es.unican.rivasjm.classd.ui.model;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import es.unican.rivasjm.classd.ui.utils.JdtDomUtils;

public class ClassDiagramFactory {
	
	final IJavaElement element;
	final List<TypeDeclaration> knownTypes;
	
	public ClassDiagramFactory(IJavaElement element) {
		this.element = element;
		this.knownTypes = JdtDomUtils.getTypes(JdtDomUtils.getCompilationUnits(element));
	}
	
	public MClassDiagram get() {
		MClassDiagram diagram = new MClassDiagram();
		init(diagram);
		return diagram;
	}
	
	private void init(MClassDiagram diagram) {
		// TODO Auto-generated method stub
		
	}

	private MClass getMClass(TypeDeclaration type) {
		MClass clazz = new MClass();
		
		clazz.setName(type.getName().getIdentifier());
		clazz.setQualifiedName(type.getName().getFullyQualifiedName());
		clazz.setInterface(type.isInterface());
		clazz.setAbstract(Modifier.isAbstract(type.getModifiers()));		7
		
		// add methods
		for (MethodDeclaration method : type.getMethods()) {
			MOperation operation = getMOperation(method);
			clazz.addOperation(operation);
		}
		
		// add attributes (ignore references to knownTypes, these will become MReferece's)
		for (FieldDeclaration field : type.getFields()) {
			
		}
		
		return clazz;
	}
	
	private static MOperation getMOperation(MethodDeclaration method) {
		MOperation operation = new MOperation();
		
		operation.setName(method.getName().getIdentifier());
		operation.setType(method.getReturnType2().toString());
		
		return operation;
	}

}
