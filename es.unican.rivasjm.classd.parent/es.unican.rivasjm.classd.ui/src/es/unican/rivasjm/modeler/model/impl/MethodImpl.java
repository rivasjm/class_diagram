package es.unican.rivasjm.modeler.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

import es.unican.rivasjm.modeler.model.EVisibility;
import es.unican.rivasjm.modeler.model.IMethod;
import es.unican.rivasjm.modeler.model.IMethodParameter;

public class MethodImpl implements IMethod {
	
	private final MethodDeclaration method;
	private final List<IMethodParameter> parameters;
	

	public MethodImpl(MethodDeclaration method) {
		this.method = Objects.requireNonNull(method);
		this.parameters = new ArrayList<>();
	}

	@Override
	public String getName() {
		return JDTUtils.getName(method.getName());
	}

	@Override
	public EVisibility getVisibility() {
		return JDTUtils.getVibility(method.getModifiers());
	}

	@Override
	public boolean isAbstract() {
		return Modifier.isAbstract(method.getModifiers());
	}

	@Override
	public boolean isFinal() {
		return Modifier.isFinal(method.getModifiers());
	}

	@Override
	public String getReturnType() {
		return JDTUtils.getTypeString(method.getReturnType2());
	}

	@Override
	public List<IMethodParameter> getParameters() {
		return parameters;
	}

}
