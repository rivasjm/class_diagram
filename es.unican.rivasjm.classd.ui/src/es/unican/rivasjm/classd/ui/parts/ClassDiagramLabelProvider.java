package es.unican.rivasjm.classd.ui.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;

import es.unican.rivasjm.classd.ui.model.MClass;
import es.unican.rivasjm.classd.ui.model.MAssociationRelationship;
import es.unican.rivasjm.classd.ui.model.MInheritanceRelationship;

public class ClassDiagramLabelProvider extends LabelProvider implements IFigureProvider, ISelfStyleProvider, IConnectionStyleProvider {

	private final ResourceManager resManager;

	public ClassDiagramLabelProvider(ResourceManager resManager) {
		this.resManager = resManager;
	}
	
	/*
	 * Classes Style
	 */
	
	@Override
	public IFigure getFigure(Object element) {
		if (element instanceof MClass) {
			ClassFigure figure = new ClassFigure((MClass) element, resManager);
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
		return ZestStyles.CONNECTIONS_SOLID;
	}

	@Override
	public Color getColor(Object rel) {
		return ColorConstants.black;
	}

	@Override
	public Color getHighlightColor(Object rel) {
		return ColorConstants.black;
	}

	@Override
	public int getLineWidth(Object rel) {
		return 1;
	}

	@Override
	public IFigure getTooltip(Object entity) {
		return null;
	}
	
	/*
	 * Self style providers
	 */

	@Override
	public void selfStyleConnection(Object element, GraphConnection connection) {
		if (connection.getConnectionFigure() instanceof PolylineConnection) {
			PolylineConnection cf = (PolylineConnection) connection.getConnectionFigure();
			cf.setConnectionRouter(new ManhattanConnectionRouter());
		}
		
		if (element instanceof MAssociationRelationship) {
			doStyleContentionRelationship((MAssociationRelationship) element, connection);
		
		} else if (element instanceof MInheritanceRelationship) {
			doStyleInheritanceRelationship((MInheritanceRelationship)element, connection);
		}
	}
	
	private void doStyleInheritanceRelationship(MInheritanceRelationship rel, GraphConnection connection) {
		if (connection.getConnectionFigure() instanceof PolylineConnection) {
			PolylineConnection cf = (PolylineConnection) connection.getConnectionFigure();
			
			// inheritance decoration
			{
				PolygonDecoration decoration = new PolygonDecoration();
				PointList decorationPointList = new PointList();
				decorationPointList.addPoint(0, 0);
				decorationPointList.addPoint(-1, 1);
				decorationPointList.addPoint(-1, -1);
				decoration.setBackgroundColor(ColorConstants.white);
				decoration.setForegroundColor(ColorConstants.black);
				decoration.setTemplate(decorationPointList);
				cf.setSourceDecoration(decoration);
			}
		}
	}

	private void doStyleContentionRelationship(MAssociationRelationship rel, GraphConnection connection) {
		if (connection.getConnectionFigure() instanceof PolylineConnection) {
			PolylineConnection cf = (PolylineConnection) connection.getConnectionFigure();
			
			// contention decoration
			{
				PolygonDecoration decoration = new PolygonDecoration();
				PointList decorationPointList = new PointList();
				decorationPointList.addPoint(0, 0);
				decorationPointList.addPoint(-1, 1);
				decorationPointList.addPoint(-2, 0);
				decorationPointList.addPoint(-1, -1);
				decoration.setForegroundColor(ColorConstants.black);
				decoration.setBackgroundColor(ColorConstants.black);
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
