package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MClassDiagram {
	
	private final List<MClass> classes;
	private final List<MRelationship> relationships;
	
	public MClassDiagram() {
		this.classes = new ArrayList<>();
		this.relationships = new ArrayList<>();
	}
	
	public void addClass(MClass clazz) {
		this.classes.add(Objects.requireNonNull(clazz));
	}
	
	public void addRelationship(MRelationship relationship) {
		this.relationships.add(Objects.requireNonNull(relationship));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		classes.forEach(c -> sb.append(c.toString() + "\n"));
		sb.append("\n");
		relationships.forEach(r -> sb.append(r.toString() + "\n"));
		return sb.toString();
	}

}
