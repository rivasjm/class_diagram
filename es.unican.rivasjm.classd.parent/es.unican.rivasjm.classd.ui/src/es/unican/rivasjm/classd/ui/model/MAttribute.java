package es.unican.rivasjm.classd.ui.model;

public class MAttribute extends MElement {
	
	private String name;
	private String type;
	private EVisibility visibility = EVisibility.PUBLIC;

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

	@Override
	public String toString() {
		return visibility.getSymbol() + name + " : " + type;
	}

}
