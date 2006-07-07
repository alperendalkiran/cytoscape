/*$Id$*/
package linkout;

import ding.view.*;
import javax.swing.*;
import giny.view.NodeView;
import java.awt.*;

/**
 * LinkOutContextMenuListener implements NodeContextMenuListener
 * When a node is selected it calls LinkOut that adds the linkout menu to the node's popup menu
 */
public class LinkOutContextMenuListener implements NodeContextMenuListener {

    public LinkOutContextMenuListener(){
        //System.out.println("[LinkOutContextMenuListener]: Constructor called");
    }

    /**
     * @param nodeView The clicked NodeView
     * @param menu popup menu to add the LinkOut menu
     */
    public void addNodeContextMenuItems (NodeView nodeView, JPopupMenu menu){
        //System.out.println("[LinkOutContextMenuListener]: addNodeContextMenuItem called");

        
        LinkOut lo= new LinkOut();
        if(menu==null){
            menu=new JPopupMenu();
        }
        menu.add(lo.addLinks(nodeView));
    }

}
