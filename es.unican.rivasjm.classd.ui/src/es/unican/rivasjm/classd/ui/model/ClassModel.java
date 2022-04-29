package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;

public class ClassModel {
		
	private String name;
	private boolean _abstract;
	private boolean _interface;

	private ClassModel parent;
		
	private final List<AttributeModel> attributes;
	private final List<ReferenceModel> references;
	private final List<OperationModel> operations;
	
	public ClassModel() {		
		this.attributes = new ArrayList<>();
		this.references = new ArrayList<>();
		this.operations = new ArrayList<>();
	}

}
