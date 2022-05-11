 
package es.unican.rivasjm.classd.ui.parts.handlers;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import es.unican.rivasjm.classd.ui.parts.ClassDiagramPart;

public class SaveImageHandler {
	
	@Execute
	public void execute(ClassDiagramPart part, Shell shell) {
		final FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.png" });
		dialog.setFilterNames(new String[] { "PNG" });
		dialog.setFileName("diagram.png");
		dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
		
		String path = dialog.open();
		if (path != null && !path.isBlank()) {
			if (!path.toLowerCase().endsWith(".png")) {
				path += ".png";
			}
		
			boolean done = part.saveAsImage(path);
			if (done) {
				MessageDialog.openInformation(shell, "Image saved", "Image saved succesfully");

			} else {
				MessageDialog.openError(shell, "Error saving image", "Image couldn't be saved");	
			}
		}
	}
		
}