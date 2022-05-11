package es.unican.rivasjm.classd.ui.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import es.unican.rivasjm.classd.ui.diagram.CompartmentFigure;
import es.unican.rivasjm.classd.ui.model.MAttribute;
import es.unican.rivasjm.classd.ui.model.MClass;
import es.unican.rivasjm.classd.ui.model.MElement;
import es.unican.rivasjm.classd.ui.model.MOperation;

public class ClassFigure extends Figure {
	
	public static Color classColor = new Color(null, 255, 255, 206);

	private final MClass clazz;
	private final ResourceManager resManager;
	
	private CompartmentFigure attributeFigure = new CompartmentFigure();
	private CompartmentFigure methodFigure = new CompartmentFigure();
	
	public ClassFigure(MClass clazz, ResourceManager resManager) {
		this.clazz = clazz;
		this.resManager = resManager;
		
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(classColor);
		setOpaque(true);
		
		fill();
	}

	private void fill() {
		// title
		{
			if (clazz.isInterface()) {
				add(new Label("<<interface>>"));
			} else if (clazz.isEnum()) {
				add(new Label("<<enumeration>>"));
			}
			
			Label label = new Label(clazz.getName());
			setFontStyle(label, clazz);
			add(label);
		}
		
		// enum values (if enum)
		if (clazz.isEnum()) {
			for (String value : clazz.getValues()) {
				Label label = new Label(value);
				attributeFigure.add(label);
			}
		}
		
		// attributes
		for (MAttribute attribute : clazz.getAttributes()) {
			Label label = new Label(attribute.toString());
			setFontStyle(label, attribute);
			attributeFigure.add(label);
		}
		
		// operations
		for (MOperation operation : clazz.getOperations()) {
			Label label = new Label(operation.toString());
			setFontStyle(label, operation);
			methodFigure.add(label);
		}
		
		// add attributes and methods subfigures
		if (!attributeFigure.getChildren().isEmpty()) {
			add(attributeFigure);			
		}
		if (!methodFigure.getChildren().isEmpty()) {
			add(methodFigure);			
		}
	}
	
	private void setFontStyle(Label label, MElement element) {
		Font font = label.getFont();
		if (font == null) {
			font = JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT);
		}
		FontDescriptor descriptor = FontDescriptor.createFrom(font);
		
		boolean modified = false;
		if (element.isAbstract()) {
			descriptor = descriptor.setStyle(SWT.ITALIC);
			modified = true;
		}
		
		if (element.isStatic()) {
			descriptor = descriptor.setStyle(SWT.UNDERLINE_SINGLE);
			modified = true;
		}
		
		if (modified) {
			label.setFont(resManager.createFont(descriptor));
		}
	}

}
