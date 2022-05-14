package es.unican.rivasjm.modeler.model.impl;

import java.util.Objects;

import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import es.unican.rivasjm.modeler.model.IMethodParameter;

public class MethodParameterImpl implements IMethodParameter {
	
	private final SingleVariableDeclaration variable;

	public MethodParameterImpl(SingleVariableDeclaration variable) {
		this.variable = Objects.requireNonNull(variable);
	}

	@Override
	public boolean isFinal() {
		return Modifier.isFinal(variable.getModifiers());
	}

	@Override
	public String getName() {
		return JDTUtils.getName(variable.getName());
	}

	@Override
	public String getType() {
		return JDTUtils.getTypeString(variable.getType());
	}

}
