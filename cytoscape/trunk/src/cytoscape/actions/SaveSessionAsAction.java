
/*
  File: SaveSessionAsAction.java 
  
  Copyright (c) 2006, The Cytoscape Consortium (www.cytoscape.org)
  
  The Cytoscape Consortium is: 
  - Institute for Systems Biology
  - University of California San Diego
  - Memorial Sloan-Kettering Cancer Center
  - Pasteur Institute
  - Agilent Technologies
  
  This library is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as published
  by the Free Software Foundation; either version 2.1 of the License, or
  any later version.
  
  This library is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
  MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
  documentation provided hereunder is on an "as is" basis, and the
  Institute for Systems Biology and the Whitehead Institute 
  have no obligations to provide maintenance, support,
  updates, enhancements or modifications.  In no event shall the
  Institute for Systems Biology and the Whitehead Institute 
  be liable to any party for direct, indirect, special,
  incidental or consequential damages, including lost profits, arising
  out of the use of this software and its documentation, even if the
  Institute for Systems Biology and the Whitehead Institute 
  have been advised of the possibility of such damage.  See
  the GNU Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public License
  along with this library; if not, write to the Free Software Foundation,
  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
*/

package cytoscape.actions;

import java.awt.event.ActionEvent;

import cytoscape.Cytoscape;
import cytoscape.task.ui.JTaskConfig;
import cytoscape.task.util.TaskManager;
import cytoscape.util.CyFileFilter;
import cytoscape.util.CytoscapeAction;
import cytoscape.util.FileUtil;
import cytoscape.view.CyMenus;


/**
 * "Save Session As" Action
 * -- Same as SaveSessionAction, but always opens file
 *     chooser.
 */
public class SaveSessionAsAction extends CytoscapeAction {

	// Extension for the new cytoscape session file
	public static String SESSION_EXT = ".cys";
	
	/**
	 * Constructor.
	 *
	 */
	public SaveSessionAsAction(String label) {
		super(label);
		setPreferredMenu("File");
		setAcceleratorCombo( java.awt.event.KeyEvent.VK_S, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK) ;
	}
	
	public SaveSessionAsAction() {
		super();
	}

	// If no current session file exists, open dialog box to save new session,
	// and if it exists, overwrite the file.
	public void actionPerformed(ActionEvent e) {

		String name; // file name

		// Open Dialog to ask user the file name.
		try {
			name = FileUtil.getFile("Save Current Session as CYS File",
					FileUtil.SAVE, new CyFileFilter[] {}).toString();
		} catch (Exception exp) {
			// this is because the selection was canceled
			return;
		}

		if (!name.endsWith(SESSION_EXT))
			name = name + SESSION_EXT;
		
		Cytoscape.setCurrentSessionFileName(name);
		
		// Create Task
		SaveSessionTask task = new SaveSessionTask(name);

		// Configure JTask Dialog Pop-Up Box
		JTaskConfig jTaskConfig = new JTaskConfig();
		jTaskConfig.setOwner(Cytoscape.getDesktop());
		jTaskConfig.displayCloseButton(true);
		jTaskConfig.displayCancelButton(true);
		jTaskConfig.displayStatus(true);
		jTaskConfig.setAutoDispose(false);

		// Execute Task in New Thread; pop open JTask Dialog Box.
		TaskManager.executeTask(task, jTaskConfig);
	}
} // SaveAsGMLAction

