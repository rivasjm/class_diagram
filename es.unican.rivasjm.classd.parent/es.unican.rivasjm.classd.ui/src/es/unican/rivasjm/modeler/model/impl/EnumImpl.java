package es.unican.rivasjm.modeler.model.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.EnumDeclaration;

public class EnumImpl extends ClassImpl {
	
	private final EnumDeclaration type;

	public EnumImpl(EnumDeclaration type) {
		super(type);
		this.type = type;
	}

	@Override
	public List<String> getEnumValues() {
		return JDTUtils.getEnumConstants(type);
	}

	@Override
	public boolean isEnum() {
		return true;
	}

	@Override
	public boolean isInterface() {
		return false;
	}	

}
