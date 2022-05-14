package es.unican.rivasjm.modeler.model;

import java.util.List;

public interface IMethod {
	
	public String getName();
		
	public EVisibility getVisibility();
	
	public boolean isAbstract();
	
	public boolean isFinal();
	
	public String getReturnType();
	
	public List<IMethodParameter> getParameters();

}
