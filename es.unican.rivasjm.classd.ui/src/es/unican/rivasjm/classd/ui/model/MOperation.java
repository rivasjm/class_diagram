package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;

public class MOperation {

	private String name;
	private String type;
	
	private final List<MOperationParameter> parameters;
	
	public MOperation() {
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
	
	public void addParameter(MOperationParameter parameter) {
		if (parameter != null) {
			parameters.add(parameter);
		}
	}

	@Override
	public String toString() {
		return name + "() : " + type;
	}
	
}
