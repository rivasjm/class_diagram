package es.unican.rivasjm.classd.ui.model;

public class MContentionRelationship extends MRelationship {
	
	private String name;
	private String multiplicity;
	
	/*
	 * Getters and Setters
	 */
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMultiplicity() {
		return multiplicity;
	}
	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}
	
	/*
	 * Other methods
	 */
	
	@Override
	public String toString() {
		return getSource().getName() + " <>-- " + name + "(" + multiplicity + ")" + getTarget().getName();
	}	
	
}
