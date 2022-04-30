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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return name + "() : " + type;
	}
	
}
