package es.unican.rivasjm.classd.ui.model;

public class MInheritanceRelationship extends MRelationship {
	
	/*
	 * Getters and Setters
	 */
	
	public MClass getParent() {
		return getTarget();
	}
	public void setParent(MClass parent) {
		setTarget(parent);
	}
	public MClass getSubclass() {
		return getSource();
	}
	public void setSubclass(MClass subclass) {
		setSource(subclass);
	}
	public boolean isImplements() {
		return getParent().isInterface();
	}

}
