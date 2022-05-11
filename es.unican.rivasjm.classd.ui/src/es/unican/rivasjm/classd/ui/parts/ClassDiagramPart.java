package es.unican.rivasjm.classd.ui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import es.unican.rivasjm.classd.ui.model.MClassDiagram;

public class ClassDiagramPart {
	
	private static final int IMAGE_SAVE_PADDING = 10;
	
	private GraphViewer graph;
	
	private ResourceManager resManager;

	@PostConstruct
	public void createPartControl(Composite parent, MPart part) {
		part.getContext().set(ClassDiagramPart.class, this); // make it available to tool-bar items
		
		resManager = new LocalResourceManager(JFaceResources.getResources(), parent);
		
		graph = new GraphViewer(parent, SWT.NONE);
		graph.setContentProvider(ClassDiagramContentProvider.INSTANCE);
		graph.setLabelProvider(new ClassDiagramLabelProvider(resManager));
	
		graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));

//		graph.setLayoutAlgorithm(new CompositeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING,
//		new LayoutAlgorithm[] {
//				new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),
//				new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING),
//		}));
		
//		graph.setLayoutAlgorithm(new CompositeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING,
//				new LayoutAlgorithm[] {
//						new DirectedGraphLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),
//						new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING),
//				}));
//
//		graph.setLayoutAlgorithm(new DirectedGraphLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));

//		graph.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		
		initDND(graph.getControl());
	}
	
	/**
	 * Initialize the drag and drop functionality of this part. 
	 * @param control
	 */
	private void initDND(Control control) {
		final DropTarget dropTarget = new DropTarget(control, DND.DROP_COPY | DND.DROP_DEFAULT);
		dropTarget.setTransfer(new Transfer[] {LocalSelectionTransfer.getTransfer()});
		dropTarget.addDropListener(new ClassDiagramDropTargetListener(this));
	}

	public void setInput(MClassDiagram diagram) {
		graph.setInput(diagram);
	}
	
	public void clear() {
		graph.setInput(null);
	}
	
	@Focus
	public void setFocus() {
		graph.getGraphControl().setFocus();
		
	}
	
	public boolean saveAsImage(String path) {
		boolean correct = false;
		
		final IFigure contents = graph.getGraphControl().getContents();		
		final Dimension size = contents.getSize();
		final Image image = new Image(graph.getControl().getDisplay(), 
				size.width + 2 * IMAGE_SAVE_PADDING, 
				size.height + 2 * IMAGE_SAVE_PADDING);
		final GC gc = new GC(image);
		
		try {
			final SWTGraphics graphics = new SWTGraphics(gc);
			graphics.translate(IMAGE_SAVE_PADDING, IMAGE_SAVE_PADDING);
			graphics.translate(contents.getBounds().getLocation().getNegated());
			contents.paint(graphics);

			final ImageData imageData = image.getImageData();
			ImageLoader imageLoader = new ImageLoader();
			imageLoader.data = new ImageData[] { imageData };
			imageLoader.save(path, SWT.IMAGE_PNG);
			correct = true;
			
		} catch (Exception e) {

		} finally {
			image.dispose();
			gc.dispose();
		}
		
		return correct;
	}
	
}
