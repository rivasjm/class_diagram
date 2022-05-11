package es.unican.rivasjm.classd.ui.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.zest.core.viewers.IGraphEntityRelationshipContentProvider;

import es.unican.rivasjm.classd.ui.model.MClass;
import es.unican.rivasjm.classd.ui.model.MClassDiagram;
import es.unican.rivasjm.classd.ui.model.MRelationship;

public class ClassDiagramContentProvider implements IGraphEntityRelationshipContentProvider {
	
	public static final ClassDiagramContentProvider INSTANCE = new ClassDiagramContentProvider();
	
	private ClassDiagramContentProvider() {
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<Object> res = new ArrayList<>();
		
		if (inputElement instanceof MClassDiagram) {
			MClassDiagram diagram = (MClassDiagram) inputElement;
			res.addAll(diagram.getClasses());
		}
		
		return res.toArray();
	}

	@Override
	public Object[] getRelationships(Object source, Object dest) {
		List<Object> res = new ArrayList<>();
		
		if (source instanceof MClass && dest instanceof MClass) {
			MClass s = (MClass) source;
			MClass d = (MClass) dest;
			List<MRelationship> relationships = s.getDiagram().findRelationships(s, d);
			res.addAll(relationships);
		}
		
		return res.toArray();
	}

}
