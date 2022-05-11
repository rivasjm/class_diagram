package es.unican.rivasjm.classd.ui.model;

public class MAssociationRelationship extends MRelationship {
	
	private String name;
	private String multiplicity;
	private EVisibility visibility = EVisibility.PUBLIC;
	
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
	
	public EVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(EVisibility visibility) {
		this.visibility = visibility;
	}
	
	/*
	 * Other methods
	 */
	
	@Override
	public String toString() {
		return getSource().getName() + " <>-- " + name + "(" + multiplicity + ")" + getTarget().getName();
	}	
	
}
