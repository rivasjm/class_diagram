package es.unican.rivasjm.modeler.model;

import java.util.List;

public interface IClass {
			
	public String getFullyQualifiedName();
	
	public String getName();
	
	public EVisibility getVisibility();

	public boolean isEnum();
	
	public boolean isInterface();
	
	public boolean isAbstract();
	
	public boolean isFinal();
	
	public List<IMethod> getMethods();
	
	public List<IField> getFields();
	
	public List<String> getEnumValues();

}
