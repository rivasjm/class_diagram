package es.unican.rivasjm.classd.ui.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;

import es.unican.rivasjm.classd.ui.diagram.CompartmentFigure;
import es.unican.rivasjm.classd.ui.model.MAttribute;
import es.unican.rivasjm.classd.ui.model.MClass;
import es.unican.rivasjm.classd.ui.model.MOperation;

public class ClassFigure extends Figure {
	
	public static Color classColor = new Color(null, 255, 255, 206);

	private final MClass clazz;
	
	private CompartmentFigure attributeFigure = new CompartmentFigure();
	private CompartmentFigure methodFigure = new CompartmentFigure();
	
	public ClassFigure(MClass clazz) {
		this.clazz = clazz;
		
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(classColor);
		setOpaque(true);
		
		fill();
	}

	private void fill() {
		// title
		add(new Label(clazz.getName()));
		
		for (MAttribute attribute : clazz.getAttributes()) {
			attributeFigure.add(new Label(attribute.toString()));
		}
		
		for (MOperation operation : clazz.getOperations()) {
			methodFigure.add(new Label(operation.toString()));
		}
		
		add(attributeFigure);
		add(methodFigure);
	}

}
