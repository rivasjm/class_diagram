package es.unican.rivasjm.modeler.model.impl;

import java.util.Objects;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;

import es.unican.rivasjm.modeler.model.EVisibility;
import es.unican.rivasjm.modeler.model.IField;

public class FieldImpl implements IField {
	
	private final FieldDeclaration field;

	public FieldImpl(FieldDeclaration method) {
		this.field = Objects.requireNonNull(method);
	}

	@Override
	public String getName() {
		return JDTUtils.getFieldName(field);
	}

	@Override
	public EVisibility getVisibility() {
		return JDTUtils.getVibility(field.getModifiers());
	}

	@Override
	public boolean isFinal() {
		return Modifier.isFinal(field.getModifiers());
	}

	@Override
	public String getType() {
		return JDTUtils.getTypeString(field.getType());
	}

	@Override
	public String getQualifiedNameForUML() {
		Type type = JDTUtils.resolveTypeForUML(field.getType());
		return JDTUtils.getFullyQualifiedName(type);
	}

}
