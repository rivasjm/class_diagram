package es.unican.rivasjm.classd.ui.parts;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import es.unican.rivasjm.classd.ui.model.ClassDiagramFactory;
import es.unican.rivasjm.classd.ui.model.ClassDiagramModel;
import es.unican.rivasjm.classd.ui.utils.JDTUtils;

public class ClassDiagramPart {
	
	private Canvas canvas;

	@PostConstruct
	public void createPartControl(Composite parent) {
		canvas = new Canvas(parent, SWT.NONE);
		initCanvas(canvas);
	}

	private void initCanvas(Canvas canvas) {
		final DropTarget dropTarget = new DropTarget(canvas, DND.DROP_COPY | DND.DROP_DEFAULT);
		dropTarget.setTransfer(new Transfer[] {LocalSelectionTransfer.getTransfer()});
		
		dropTarget.addDropListener(new DropTargetListener() {
			
			@Override
			public void dropAccept(DropTargetEvent event) {
//				System.out.println("dropAccept");
//				ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
//				System.out.println(selection);
				
			}
			
			@Override
			public void drop(DropTargetEvent event) {
//				System.out.println("drop");
				ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
				if (selection != null && selection instanceof TreeSelection) {
					TreeSelection tselection = (TreeSelection) selection;
					Object firstElement = tselection.getFirstElement();
					if (firstElement instanceof IJavaElement) {
						ClassDiagramModel model = ClassDiagramFactory.create((IJavaElement) firstElement);
						System.out.println(model.toString());
					}
				}
			}
			
			@Override
			public void dragOver(DropTargetEvent event) {
//				System.out.println("dragOver");
				event.detail=DND.DROP_COPY;
				
			}
			
			@Override
			public void dragOperationChanged(DropTargetEvent event) {
//				System.out.println("dragOperationChanged");
				event.detail = DND.DROP_COPY;
				
			}
			
			@Override
			public void dragLeave(DropTargetEvent event) {
//				System.out.println("dragLeave");
				
			}
			
			@Override
			public void dragEnter(DropTargetEvent event) {
//				System.out.println("dragEnter");
				
			}
		});
		
	}

	@Focus
	public void setFocus() {
		canvas.setFocus();

	}

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
