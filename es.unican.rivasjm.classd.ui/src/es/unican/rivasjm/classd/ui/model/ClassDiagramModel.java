package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassDiagramModel {
	
	private final List<ClassModel> classes;
	
	public ClassDiagramModel() {
		this.classes = new ArrayList<>();
	}
	
	public void addClass(ClassModel clazz) {
		this.classes.add(Objects.requireNonNull(clazz));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		classes.forEach(c -> sb.append(c.toString() + "\n"));
		return sb.toString();
	}

}
