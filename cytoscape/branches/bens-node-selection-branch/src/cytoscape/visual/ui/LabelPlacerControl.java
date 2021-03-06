/*
 File: LabelPlacerControl.java

 Copyright (c) 2006, The Cytoscape Consortium (www.cytoscape.org)

 The Cytoscape Consortium is:
 - Institute for Systems Biology
 - University of California San Diego
 - Memorial Sloan-Kettering Cancer Center
 - Institut Pasteur
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
package cytoscape.visual.ui;

import cytoscape.visual.LabelPosition;

import giny.view.Label;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import java.beans.*;

import java.util.*;

import javax.swing.*;
import javax.swing.border.*;


/**
 * A group of widgets that specifies the placement of
 * node labels.
 */
class LabelPlacerControl extends JPanel
    implements ActionListener, PropertyChangeListener {
    private LabelPosition lp;
    private JComboBox justifyCombo;
    private JTextField xoffsetBox;
    private JTextField yoffsetBox;
    private JComboBox nodeAnchors;
    private JComboBox labelAnchors;
    private LabelPlacerGraphic labelPlacer;
    private boolean ignoreEvents;

    LabelPlacerControl(LabelPosition pos) {
        super();

        if (pos == null)
            lp = new LabelPosition(Label.NONE, Label.NONE,
                    Label.JUSTIFY_CENTER, 0.0, 0.0);
        else
            lp = pos;

        ignoreEvents = false;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel anchorNames = new JPanel();
        anchorNames.setLayout(new GridLayout(2, 2));

        String[] points = LabelPosition.getAnchorNames();

        JLabel nodeAnchorLabel = new JLabel("Node Anchor Points ");
        nodeAnchors = new JComboBox(points);
        nodeAnchors.addActionListener(this);
        anchorNames.add(nodeAnchorLabel);
        anchorNames.add(nodeAnchors);

        JLabel labelAnchorLabel = new JLabel("Label Anchor Points");
        labelAnchors = new JComboBox(points);
        labelAnchors.addActionListener(this);
        anchorNames.add(labelAnchorLabel);
        anchorNames.add(labelAnchors);

        add(anchorNames);

        JPanel justifyPanel = new JPanel();
        justifyPanel.setLayout(new GridLayout(1, 2));

        JLabel justifyLabel = new JLabel("Label Justification");
        String[] justifyTypes = LabelPosition.getJustifyNames();
        justifyCombo = new JComboBox(justifyTypes);
        justifyCombo.addActionListener(this);
        justifyPanel.add(justifyLabel);
        justifyPanel.add(justifyCombo);

        add(justifyPanel);

        JPanel offsetPanel = new JPanel();
        offsetPanel.setLayout(new GridLayout(2, 2));

        JLabel xoffsetLabel = new JLabel("X Offset Value (can be negative)");
        xoffsetBox = new JTextField("0", 8);
        xoffsetBox.addActionListener(this);
        offsetPanel.add(xoffsetLabel);
        offsetPanel.add(xoffsetBox);

        JLabel yoffsetLabel = new JLabel("Y Offset Value (can be negative)");
        yoffsetBox = new JTextField("0", 8);
        yoffsetBox.addActionListener(this);
        offsetPanel.add(yoffsetLabel);
        offsetPanel.add(yoffsetBox);

        add(offsetPanel);

        applyPosition();
    }

    private void applyPosition() {
        ignoreEvents = true; // so that we don't pay attention to events generated from these calls 

        int nodeAnchor = lp.getTargetAnchor();
        int labelAnchor = lp.getLabelAnchor();

        if (nodeAnchor == Label.NONE)
            nodeAnchors.setSelectedIndex(-1);
        else
            nodeAnchors.setSelectedItem(LabelPosition.convert(nodeAnchor));

        if (labelAnchor == Label.NONE)
            labelAnchors.setSelectedIndex(-1);
        else
            labelAnchors.setSelectedItem(LabelPosition.convert(labelAnchor));

        justifyCombo.setSelectedItem(LabelPosition.convert(lp.getJustify()));
        xoffsetBox.setText(new Integer((int) lp.getOffsetX()).toString());
        yoffsetBox.setText(new Integer((int) lp.getOffsetY()).toString());
        ignoreEvents = false;
        repaint();
    }

    /**
     *  DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        // ignore events that are generated by setting values to match
        // the graphic
        if (ignoreEvents)
            return;

        Object source = e.getSource();
        boolean changed = false;

        if (source == nodeAnchors) {
            lp.setTargetAnchor(
                LabelPosition.convert((String) nodeAnchors.getSelectedItem()));
            changed = true;
        }

        if (source == labelAnchors) {
            lp.setLabelAnchor(
                LabelPosition.convert((String) labelAnchors.getSelectedItem()));
            changed = true;
        }

        if (source == justifyCombo) {
            lp.setJustify(
                LabelPosition.convert((String) justifyCombo.getSelectedItem()));
            changed = true;
        }

        // handle both at the same time since people might forget to press enter
        if ((getOffset(xoffsetBox) != lp.getOffsetX()) ||
                (getOffset(yoffsetBox) != lp.getOffsetY())) {
            lp.setOffsetX(getOffset(xoffsetBox));
            lp.setOffsetY(getOffset(yoffsetBox));
            changed = true;
        }

        if (!changed)
            return; // nothing we care about has changed

        firePropertyChange("LABEL_POSITION_CHANGED", null, lp);
    }

    private double getOffset(JTextField jtf) {
        try {
            double d = Double.parseDouble(jtf.getText());

            return d;
        } catch (Exception ex) {
            System.err.println("not a number!");
            jtf.setText("0");

            return 0.0;
        }
    }

    /**
     *  DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent e) {
        String type = e.getPropertyName();

        if (type.equals("LABEL_POSITION_CHANGED")) {
            lp = (LabelPosition) e.getNewValue();
            applyPosition();
        }
    }
}
