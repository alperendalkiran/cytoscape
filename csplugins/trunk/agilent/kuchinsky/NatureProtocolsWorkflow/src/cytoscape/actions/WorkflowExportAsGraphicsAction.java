package cytoscape.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import cytoscape.Cytoscape;
import cytoscape.util.CytoscapeAction;

public class WorkflowExportAsGraphicsAction extends WorkflowPanelAction {
	
	CytoscapeAction exportAsGraphicsAction = new ExportAsGraphicsAction();  // the Cytoscape action
	
	public WorkflowExportAsGraphicsAction (String name)
	{
		super(name);
	}

	public String getToolTipText()
	{
		return new String("<html> Export this network view as PDF, SVG, JPG, PNG, or BMP graphics.</html>"
				);
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (Cytoscape.getCurrentNetwork() == Cytoscape.getNullNetwork())
		{
			JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "Sorry, you must first have a network to export");
		}
		else
		{
			exportAsGraphicsAction.actionPerformed(null);
		}
	}

}

	