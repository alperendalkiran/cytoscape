package gpml;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import org.pathvisio.view.swing.PathwayTransferable;

import cytoscape.util.CytoscapeAction;

public class PasteAction extends CytoscapeAction implements FlavorListener {
	GpmlImporter importer;
	
	public PasteAction(GpmlImporter importer) {
		super();
		this.importer = importer;
	}
	
	protected void initialize() {
		
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl V"));
	}
	public String getPreferredMenu() {
		return "Edit";
	}

	public boolean isInMenuBar() {
		return true;
	}
	
	public void actionPerformed(ActionEvent e) {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		importer.drop(c.getContents(this));
	}
	
	public void flavorsChanged(FlavorEvent e) {
		//Listene for supported clipboard contents
		Clipboard clip = (Clipboard)e.getSource();
		setEnabled(clip.isDataFlavorAvailable(PathwayTransferable.gpmlDataFlavor));
	}
}
