package es.unican.rivasjm.classd.ui.parts;

import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

public class IncreaseSpacingLayoutAlgorithm extends AbstractLayoutAlgorithm {
	
//	private static final double DELTA_X = 40;
//	private static final double DELTA_Y = 80;

	public IncreaseSpacingLayoutAlgorithm(int styles) {
		super(styles);
	}


	@Override
	protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider,
			double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		
//		StringBuilder sb = new StringBuilder();
//		for (InternalNode node : entitiesToLayout) {
//			sb.append(String.format("%f %f %f %f\n", node.getCurrentX(), node.getCurrentY(), node.getHeightInLayout(), node.getInternalHeight()));
//			
//		}
//		sb.append(" \n" );
//		System.out.println(sb.toString());
		
//		for (InternalNode node : entitiesToLayout) {
//			node.setLocation(node.getCurrentX()+DELTA_X, node.getCurrentY()+DELTA_Y);
//		}
	}

	@Override
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider,
			double x, double y, double width, double height) {
		
	}

	@Override
	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider) {
		
	}
	
	@Override
	public void setLayoutArea(double x, double y, double width, double height) {
		// ignored
	}
	
	@Override
	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
		return true;
	}

	@Override
	protected int getTotalNumberOfLayoutSteps() {
		return 0;  // ignored
	}

	@Override
	protected int getCurrentLayoutStep() {
		return 0;  // ignored
	}

}
