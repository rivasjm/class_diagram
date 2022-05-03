package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

import es.unican.rivasjm.classd.ui.utils.JDTUtils;

public class ClassDiagramFactoryOld {
	
	public static MClassDiagram create(IJavaElement element) {
		List<IType> types = JDTUtils.getTypes(element);
		MClassDiagram diagramModel = create(types);
		return diagramModel;
	}
	
	private static MClassDiagram create(List<IType> types) {
		final MClassDiagram model = new MClassDiagram();
		
		for (IType type: types) {
			MClass clazz = new MClass();
			model.addClass(clazz);
			clazz.setName(type.getElementName());
			clazz.setQualifiedName(type.getFullyQualifiedName());
			
			// operations
			getOperations(type).forEach(clazz::addOperation);
			
			// attributes
			getAttributes(type).forEach(clazz::addAttribute);
		}
		
		return model;
	}
	
	private static List<MOperation> getOperations(IType type) {
		List<MOperation> operations = new ArrayList<>();
		try {
			for (IMethod method : type.getMethods()) {
				MOperation operation = getOperation(method);
				operations.add(operation);
			}
		} catch (JavaModelException e) {}
		return operations;
	}
	
	private static MOperation getOperation(IMethod method) {
		MOperation operation = new MOperation();		
		
		try {
			operation.setName(method.getElementName());
			operation.setType(Signature.toString(method.getReturnType()));
		} catch (JavaModelException e) {}
		
		return operation;
	}
	
	private static List<MAttribute> getAttributes(IType type) {
		List<MAttribute> attributes = new ArrayList<>();
		
		try {
			for (IField field : type.getFields()) {
				MAttribute attribute = getAttribute(field);
				attributes.add(attribute);
			}

		} catch (JavaModelException e) {}
		
		return attributes;
	}

	private static MAttribute getAttribute(IField field) {
		MAttribute attribute = new MAttribute();		
		try {
			attribute.setName(field.getElementName());
			attribute.setType(Signature.toString(field.getTypeSignature()));
			
			IType type = JDTUtils.getFieldType(field);
//			System.out.println(type);
			System.out.println(field.getKey());
			System.out.println(JDTUtils.isCollection(type) ? "*" : "");
						
		} catch (JavaModelException e) {
		}
		
		return attribute;
	}

}