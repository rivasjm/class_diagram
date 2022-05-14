package es.unican.rivasjm.modeler.model.impl;

import static java.util.Collections.emptyList;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import es.unican.rivasjm.modeler.model.EVisibility;
import es.unican.rivasjm.modeler.model.IClass;
import es.unican.rivasjm.modeler.model.IField;
import es.unican.rivasjm.modeler.model.IMethod;

public class ClassImpl implements IClass {
	
	private final AbstractTypeDeclaration type;
	private ITypeBinding binding;
	
	private final List<IMethod> methods;
	private final List<IField> fields;

	public ClassImpl(AbstractTypeDeclaration type) {
		this.type = Objects.requireNonNull(type);
		this.binding = Objects.requireNonNull(type.resolveBinding());
		this.methods = new ArrayList<>();
		this.fields = new ArrayList<>();
	}

	@Override
	public String getFullyQualifiedName() {
		return binding.getQualifiedName();
	}

	@Override
	public String getName() {
		return JDTUtils.getName(type.getName());
	}

	@Override
	public EVisibility getVisibility() {
		return JDTUtils.getVibility(type.getModifiers());
	}

	@Override
	public boolean isEnum() {
		return false;
	}

	@Override
	public boolean isInterface() {
		return Modifier.isInterface(type.getModifiers());
	}

	@Override
	public boolean isAbstract() {
		return Modifier.isAbstract(type.getModifiers());
	}

	@Override
	public boolean isFinal() {
		return Modifier.isFinal(type.getModifiers());
	}

	@Override
	public List<IMethod> getMethods() {
		return methods;
	}

	@Override
	public List<IField> getFields() {
		return fields;
	}

	@Override
	public List<String> getEnumValues() {
		return emptyList();
	}

}
