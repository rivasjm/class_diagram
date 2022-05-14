package es.unican.rivasjm.modeler.model;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import es.unican.rivasjm.modeler.model.impl.ClassImpl;
import es.unican.rivasjm.modeler.model.impl.EnumImpl;
import es.unican.rivasjm.modeler.model.impl.FieldImpl;
import es.unican.rivasjm.modeler.model.impl.JDTUtils;
import es.unican.rivasjm.modeler.model.impl.MethodImpl;
import es.unican.rivasjm.modeler.model.impl.MethodParameterImpl;

public class ClassFactory {
	
	public static IClass createClass(AbstractTypeDeclaration type) {
		IClass clazz = null;
		
		if (type instanceof TypeDeclaration) {
			clazz = new ClassImpl(type);
		} else if (type instanceof EnumDeclaration) {
			clazz = new EnumImpl((EnumDeclaration) type);
		}
		
		if (clazz == null) {
			return clazz;
		}
		
		// add methods
		for (MethodDeclaration method : JDTUtils.getMethodDeclarations(type)) {
			MethodImpl methodImpl = new MethodImpl(method);
			clazz.getMethods().add(methodImpl);
			
			// add method parameters
			for (SingleVariableDeclaration param : JDTUtils.getMethodParameters(method)) {
				MethodParameterImpl parameterImpl = new MethodParameterImpl(param);
				methodImpl.getParameters().add(parameterImpl);
			}
		}
		
		// add fields
		for (FieldDeclaration field : JDTUtils.getFieldDeclarations(type)) {
			FieldImpl fieldImpl = new FieldImpl(field);
			clazz.getFields().add(fieldImpl);
		}
		
		return clazz;
	}

}
