package es.unican.rivasjm.classd.ui.model;

public abstract class MElement {
	
	private boolean isAbstract = false;
	private boolean isStatic = false;
	
	
	public boolean isAbstract() {
		return isAbstract;
	}
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	public boolean isStatic() {
		return isStatic;
	}
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	
}
