package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MClass {
		
	private String name;
	private String qualifiedName;
	private boolean _abstract;
	private boolean _interface;
	
	private final List<MAttribute> attributes;
	private final List<MOperation> operations;
	
	public MClass() {		
		this.attributes = new ArrayList<>();
		this.operations = new ArrayList<>();
	}

	/*
	 * Getters and setters
	 */
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAbstract() {
		return _abstract;
	}

	public void setAbstract(boolean _abstract) {
		this._abstract = _abstract;
	}

	public boolean isInterface() {
		return _interface;
	}

	public void setInterface(boolean _interface) {
		this._interface = _interface;
	}
	
	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	
	public void addOperation(MOperation operation) {
		if (operation != null) {
			operations.add(operation);
		}
	}
	
	public void addAttribute(MAttribute attribute) {
		if (attribute != null) {
			attributes.add(attribute);
		}
	}
	
	/*
	 * Other methods
	 */

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("-----\n");
		sb.append("Class ").append(name).append(" [").append(qualifiedName).append("]\n");
		attributes.forEach(a -> sb.append(a.toString() + "\n"));
		operations.forEach(o -> sb.append(o.toString() + "\n"));
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(qualifiedName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MClass other = (MClass) obj;
		return Objects.equals(qualifiedName, other.qualifiedName);
	}

}
