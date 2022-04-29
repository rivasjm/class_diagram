package es.unican.rivasjm.classd.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.core.IType;

public class ClassModel {
	
	private final IType type;
	
	private final List<AttributeModel> attributes;
	private final List<ReferenceModel> references;
	private final List<OperationModel> operations;
	
	public ClassModel(IType type) {
		this.type = Objects.requireNonNull(type);
		
		this.attributes = new ArrayList<>();
		this.references = new ArrayList<>();
		this.operations = new ArrayList<>();
		
		init(type);
	}

	private void init(IType type) {
		// TODO Auto-generated method stub
		
	}

}
