package es.unican.rivasjm.modeler.model;

public interface IField {
	
	public String getName();
	
	public EVisibility getVisibility();
	
	public boolean isFinal();
		
	public String getType();
	
	public String getQualifiedNameForUML();

}
