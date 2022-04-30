package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

import es.unican.rivasjm.classd.ui.utils.JDTUtils;

public class ClassDiagramFactory {
	
	public static ClassDiagramModel create(IJavaElement element) {
		List<IType> types = JDTUtils.getTypes(element);
		ClassDiagramModel diagramModel = create(types);
		return diagramModel;
	}
	
	private static ClassDiagramModel create(List<IType> types) {
		final ClassDiagramModel model = new ClassDiagramModel();
		
		for (IType type: types) {
			ClassModel clazz = new ClassModel();
			model.addClass(clazz);
			clazz.setName(type.getElementName());
			
			// operations
			getOperations(type).forEach(clazz::addOperation);
			
			// attributes
			getAttributes(type).forEach(clazz::addAttribute);
		}
		
		return model;
	}
	
	private static List<OperationModel> getOperations(IType type) {
		List<OperationModel> operations = new ArrayList<>();
		try {
			for (IMethod method : type.getMethods()) {
				OperationModel operation = getOperation(method);
				operations.add(operation);
			}
		} catch (JavaModelException e) {}
		return operations;
	}
	
	private static OperationModel getOperation(IMethod method) {
		OperationModel operation = new OperationModel();		
		
		try {
			operation.setName(method.getElementName());
			operation.setType(Signature.toString(method.getReturnType()));
		} catch (JavaModelException e) {}
		
		return operation;
	}
	
	private static List<AttributeModel> getAttributes(IType type) {
		List<AttributeModel> attributes = new ArrayList<>();
		
		try {
			for (IField field : type.getFields()) {
				AttributeModel attribute = getAttribute(field);
				attributes.add(attribute);
			}

		} catch (JavaModelException e) {}
		
		return attributes;
	}

	private static AttributeModel getAttribute(IField field) {
		AttributeModel attribute = new AttributeModel();		
		try {
			attribute.setName(field.getElementName());
			attribute.setType(Signature.toString(field.getTypeSignature()));
			
			IType type = JDTUtils.getFieldType(field);
			System.out.println(type);
						
		} catch (JavaModelException e) {
		}
		
		return attribute;
	}

}
