package es.unican.rivasjm.classd.ui.model;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MClass extends MElement {
		
	private MClassDiagram diagram;
	
	private String name;
	private String qualifiedName;
	private boolean isInterface = false;
	private boolean isEnum = false;
	
	private final List<MAttribute> attributes;
	private final List<MOperation> operations;
	
	private final List<String> enumValues;
	
	
	public MClass() {		
		this.attributes = new ArrayList<>();
		this.operations = new ArrayList<>();
		this.enumValues = new ArrayList<>();
	}

	/*
	 * Getters and setters
	 */
	
	public MClassDiagram getDiagram() {
		return diagram;
	}
	
	void setDiagram(MClassDiagram diagram) {
		this.diagram = diagram;
	}	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean _interface) {
		this.isInterface = _interface;
	}
	
	public boolean isEnum() {
		return isEnum;
	}

	public void setEnum(boolean _enum) {
		this.isEnum = _enum;
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
	
	public void addEnumValue(String value) {
		if (value != null) {
			this.enumValues.add(value);
		}
	}
	
	public List<MAttribute> getAttributes() {
		return unmodifiableList(attributes);
	}
	
	public List<MOperation> getOperations() {
		return unmodifiableList(operations);
	}
	
	
	public List<String> getValues() {
		return unmodifiableList(enumValues);
	}
	
	
	/*
	 * Other methods
	 */

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---------\n");
		sb.append("Class ").append(name).append(" [").append(qualifiedName).append("]\n");
		attributes.forEach(a -> sb.append(" " + a.toString() + "\n"));
		sb.append("\n");
		operations.forEach(o -> sb.append(" " + o.toString() + "\n"));
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
