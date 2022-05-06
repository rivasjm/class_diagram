 
package es.unican.rivasjm.classd.ui.parts.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import es.unican.rivasjm.classd.ui.parts.ClassDiagramPart;

public class ClearPart {
	
	@Execute
	public void execute(ClassDiagramPart part) {
		part.clear();
	}
		
}