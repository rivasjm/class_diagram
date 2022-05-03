package es.unican.rivasjm.classd.ui.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import es.unican.rivasjm.classd.ui.model.MClass;
import es.unican.rivasjm.classd.ui.model.MContentionRelationship;

public class ClassDiagramLabelProvider extends LabelProvider implements IFigureProvider, ISelfStyleProvider, IConnectionStyleProvider {

	/*
	 * Classes Style
	 */
	
	@Override
	public IFigure getFigure(Object element) {
		if (element instanceof MClass) {
			ClassFigure figure = new ClassFigure((MClass) element);
			figure.setSize(figure.getPreferredSize());
			return figure;
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		return null;
	}
	
	
	/*
	 * Relationship styles
	 */

	@Override
	public int getConnectionStyle(Object rel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color getColor(Object rel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getHighlightColor(Object rel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLineWidth(Object rel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IFigure getTooltip(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * Self style providers
	 */

	@Override
	public void selfStyleConnection(Object element, GraphConnection connection) {
		MContentionRelationship rel = (MContentionRelationship) element;
		
		if (connection.getConnectionFigure() instanceof PolylineConnection) {
			PolylineConnection cf = (PolylineConnection) connection.getConnectionFigure();
			
			// contention decoration
			{
				PolygonDecoration decoration = new PolygonDecoration();
				PointList decorationPointList = new PointList();
				decorationPointList.addPoint(0, 0);
				decorationPointList.addPoint(-2, 2);
				decorationPointList.addPoint(-4, 0);
				decorationPointList.addPoint(-2, -2);
				decoration.setTemplate(decorationPointList);
				cf.setSourceDecoration(decoration);
			}
			
			// reference label
			{
				ConnectionEndpointLocator relationshipLocator = new ConnectionEndpointLocator(cf, true);
				relationshipLocator.setUDistance(10);
				relationshipLocator.setVDistance(-5);
				Label relationshipLabel = new Label(rel.getName() + " " + rel.getMultiplicity());
				cf.add(relationshipLabel, relationshipLocator);
			}
			
		}
		
	}

	@Override
	public void selfStyleNode(Object element, GraphNode node) {
		// TODO Auto-generated method stub
		
	}

}
