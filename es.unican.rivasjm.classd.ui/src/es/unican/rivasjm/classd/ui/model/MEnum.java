package es.unican.rivasjm.classd.ui.model;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public class MEnum extends MClass {
	
	private final List<String> values;

	public MEnum() {
		super();
		this.values = new ArrayList<>();
	}
	
	public void addValue(String value) {
		if (value != null) {
			this.values.add(value);
		}
	}
	
	public List<String> getValues() {
		return unmodifiableList(values);
	}

}
