package es.unican.rivasjm.classd.ui.parts;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

import es.unican.rivasjm.classd.ui.model.ClassDiagramFactory;
import es.unican.rivasjm.classd.ui.model.MClassDiagram;

public class ClassDiagramDropTargetListener implements DropTargetListener{
	
	private final ClassDiagramPart part;

	ClassDiagramDropTargetListener(ClassDiagramPart part) {
		this.part = Objects.requireNonNull(part);
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		// do nothing
	}

	@Override
	public void dragLeave(DropTargetEvent event) {		
		// do nothing
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		event.detail = DND.DROP_COPY;
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.detail=DND.DROP_COPY;	
	}
	
	public static IJavaElement[] getJavaElementsFromTreeSelection(TreeSelection selection) {
		List<IJavaElement> elements = new ArrayList<>();
		
		for (Object s : selection) {
			if (s instanceof IJavaElement) {
				elements.add((IJavaElement) s);
			
			} else if (s instanceof IProject) {
				IJavaProject javaProject = JavaCore.create((IProject) s);
				elements.add(javaProject);
			}
		}
		
		return elements.toArray(new IJavaElement[0]);
	}

	@Override
	public void drop(DropTargetEvent event) {
		ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
		if (selection != null && selection instanceof TreeSelection) {
			TreeSelection tselection = (TreeSelection) selection;
			IJavaElement[] javaElements = getJavaElementsFromTreeSelection(tselection);
			ClassDiagramFactory factory = new ClassDiagramFactory(javaElements);
			MClassDiagram diagram = factory.get();
			part.setInput(diagram);
		}
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		// do nothing
		
	}

}
