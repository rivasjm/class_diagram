package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;


public class ClassModel {
		
	private String name;
	private boolean _abstract;
	private boolean _interface;

	private ClassModel parent;
		
	private final List<AttributeModel> attributes;
	private final List<ReferenceModel> references;
	private final List<OperationModel> operations;
	
	public ClassModel() {		
		this.attributes = new ArrayList<>();
		this.references = new ArrayList<>();
		this.operations = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean is_abstract() {
		return _abstract;
	}

	public void set_abstract(boolean _abstract) {
		this._abstract = _abstract;
	}

	public boolean is_interface() {
		return _interface;
	}

	public void set_interface(boolean _interface) {
		this._interface = _interface;
	}

	public ClassModel getParent() {
		return parent;
	}

	public void setParent(ClassModel parent) {
		this.parent = parent;
	}
	
	public void addOperation(OperationModel operation) {
		if (operation != null) {
			operations.add(operation);
		}
	}
	
	public void addAttribute(AttributeModel attribute) {
		if (attribute != null) {
			attributes.add(attribute);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("-----\n");
		sb.append("Class ").append(name).append("\n");
		attributes.forEach(a -> sb.append(a.toString() + "\n"));
		operations.forEach(o -> sb.append(o.toString() + "\n"));
		return sb.toString();
	}


	
	

}
