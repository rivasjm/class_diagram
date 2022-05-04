package es.unican.rivasjm.classd.ui.model;

public class MInheritanceRelationship extends MRelationship {
	
	/*
	 * Getters and Setters
	 */
	
	public MClass getParent() {
		return getSource();
	}
	public void setParent(MClass parent) {
		setSource(parent);
	}
	
	public MClass getSubclass() {
		return getTarget();
	}
	public void setSubclass(MClass subclass) {
		setTarget(subclass);
	}
	
	public boolean isImplements() {
		return getSource().isInterface();
	}

}
