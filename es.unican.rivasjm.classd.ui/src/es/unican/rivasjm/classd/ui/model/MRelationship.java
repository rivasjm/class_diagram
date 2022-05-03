package es.unican.rivasjm.classd.ui.model;

public abstract class MRelationship {
	
	private MClass source;
	private MClass target;
	
	/*
	 * Getters and setters
	 */
	
	public MClass getSource() {
		return source;
	}
	public void setSource(MClass source) {
		this.source = source;
	}
	public MClass getTarget() {
		return target;
	}
	public void setTarget(MClass target) {
		this.target = target;
	}

}
