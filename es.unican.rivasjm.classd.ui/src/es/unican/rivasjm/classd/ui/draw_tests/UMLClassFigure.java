package es.unican.rivasjm.classd.ui.draw_tests;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;

public class UMLClassFigure extends Figure {
	
	public static Color classColor = new Color(null, 255, 255, 206);
	
	private CompartmentFigure attributeFigure = new CompartmentFigure();
	private CompartmentFigure methodFigure = new CompartmentFigure();

	public UMLClassFigure(Label name) {
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(classColor);
		setOpaque(true);

		add(name);
		add(attributeFigure);
		add(methodFigure);
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseHover(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent me) {
//				System.out.println(me);
				
			}
		});
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClicked(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	public CompartmentFigure getAttributesCompartment() {
		return attributeFigure;
	}

	public CompartmentFigure getMethodsCompartment() {
		return methodFigure;
	}
}