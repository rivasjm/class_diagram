package es.unican.rivasjm.classd.ui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import es.unican.rivasjm.classd.ui.model.MClassDiagram;

public class ClassDiagramPart {
	
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
	
	private void initDND(Control control) {
		final DropTarget dropTarget = new DropTarget(control, DND.DROP_COPY | DND.DROP_DEFAULT);
		dropTarget.setTransfer(new Transfer[] {LocalSelectionTransfer.getTransfer()});
		dropTarget.addDropListener(new ClassDiagramDropTargetListener(this));
	}

	public void setInput(MClassDiagram diagram) {
		graph.setInput(diagram);
	}
	
	@Focus
	public void setFocus() {
		graph.getGraphControl().setFocus();
		
	}
	
	public boolean saveAsImage(String path) {
		boolean correct = false;
		final GC gc = new GC(graph.getControl());
		final Rectangle bounds = graph.getControl().getBounds();
		Image image = new Image(graph.getControl().getDisplay(), bounds);
		try {
		    gc.copyArea(image, 0, 0);
		    ImageLoader imageLoader = new ImageLoader();
		    imageLoader.data = new ImageData[] { image.getImageData() };
		    imageLoader.save(path, SWT.IMAGE_PNG);
		    correct = true;
		
		} catch (Exception e) {
			
		} finally {
		    image.dispose();
		    gc.dispose();
		}
		
		return correct;
	}
	
	/*
	 * Other methods
	 */
	
	/**
	 * This method is kept for E3 compatiblity. You can remove it if you do not
	 * mix E3 and E4 code. <br/>
	 * With E4 code you will set directly the selection in ESelectionService and
	 * you do not receive a ISelection
	 * 
	 * @param s
	 *            the selection received from JFace (E3 mode)
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection s) {
		if (s==null || s.isEmpty())
			return;

		if (s instanceof IStructuredSelection) {
			IStructuredSelection iss = (IStructuredSelection) s;
			if (iss.size() == 1)
				setSelection(iss.getFirstElement());
			else
				setSelection(iss.toArray());
		}
	}

	/**
	 * This method manages the selection of your current object. In this example
	 * we listen to a single Object (even the ISelection already captured in E3
	 * mode). <br/>
	 * You should change the parameter type of your received Object to manage
	 * your specific selection
	 * 
	 * @param o
	 *            : the current object received
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o) {

		// Remove the 2 following lines in pure E4 mode, keep them in mixed mode
		if (o instanceof ISelection) // Already captured
			return;

		// Test if label exists (inject methods are called before PostConstruct)
//		if (myLabelInView != null)
//			myLabelInView.setText("Current single selection class is : " + o.getClass());
	}

	/**
	 * This method manages the multiple selection of your current objects. <br/>
	 * You should change the parameter type of your array of Objects to manage
	 * your specific selection
	 * 
	 * @param o
	 *            : the current array of objects received in case of multiple selection
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object[] selectedObjects) {

		// Test if label exists (inject methods are called before PostConstruct)
//		if (myLabelInView != null)
//			myLabelInView.setText("This is a multiple selection of " + selectedObjects.length + " objects");
	}
}
