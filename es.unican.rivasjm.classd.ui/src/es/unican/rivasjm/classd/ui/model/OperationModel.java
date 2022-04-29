package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;

public class OperationModel {

	private String name;
	private String type;
	
	private final List<OperationParameterModel> parameters;
	
	public OperationModel() {
		this.parameters = new ArrayList<>();		
	}
	
}
