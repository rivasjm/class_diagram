package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;

public class MOperation extends MElement {

	private String name;
	private String type;
	private EVisibility visibility = EVisibility.PUBLIC;
	
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
	
	public EVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(EVisibility visibility) {
		this.visibility = visibility;
	}
	
	public void addParameter(MOperationParameter parameter) {
		if (parameter != null) {
			parameters.add(parameter);
		}
	}

	@Override
	public String toString() {
		return visibility.getSymbol() + name + "() : " + type;
	}
	
}
