package org.genmapp.subgeneviewer.splice;

import giny.view.GraphView;
import giny.view.NodeView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.genmapp.subgeneviewer.splice.controller.SpliceController;
import org.genmapp.subgeneviewer.view.SubgeneNetworkView;

import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.view.CyNetworkView;
import ding.view.DGraphView;

/**
 * 
 */
public class SpliceViewPanel extends JPanel {

	private static final int PADDING = 20;

	private CyNetworkView view;

	private CyNetworkView oldView;

	private static CyNetwork dummyNet;

	private Color background;

	/*
	 * Dummy graph component
	 */
	private static CyNode node;

	private static List<CyNode> nodes = new ArrayList<CyNode>();

	private static List edges = new ArrayList();

	private Component canvas = null;

	/**
	 * Creates a new NodeFullDetailView object.
	 */
	public SpliceViewPanel() {
		String nodeId = SpliceController.get_nodeId();
		CyAttributes nodeAttribs = Cytoscape.getNodeAttributes();
		List<String> featureList = nodeAttribs.getListAttribute(nodeId,
				"SubgeneViewer_Regions");

		nodes.clear();
		for (String featureId : featureList) {
			node = Cytoscape.getCyNode(featureId, true);
			nodes.add(node);
		}

		dummyNet = Cytoscape.getRootGraph().createNetwork(nodes, edges);
		dummyNet.setTitle(nodeId);

		oldView = Cytoscape.getVisualMappingManager().getNetworkView();

		background = Cytoscape.getVisualMappingManager().getVisualStyle()
				.getGlobalAppearanceCalculator().getDefaultBackgroundColor();
		this.setBackground(background);
	}

	protected void updateBackgroungColor(final Color newColor) {
		background = newColor;
		this.setBackground(background);
		repaint();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Component getCanvas() {
		return canvas;
	}

	private static final int NODE_HEIGHT = 20;

	private static final int NODE_WIDTH = 40;

	private static final int VGAP = NODE_HEIGHT / 2;

	private static final int HGAP = NODE_WIDTH / 2;

	int xOffset = HGAP;

	/**
	 * Create dummy network
	 */
	protected void createDummyNetworkView() {
		view = new SubgeneNetworkView(dummyNet, "Default Appearence");

		view.setIdentifier(dummyNet.getIdentifier());
		view.setTitle(dummyNet.getTitle());

		for (Object node : nodes) {
			NodeView nodeView = view.getNodeView((CyNode) node);
			nodeView.setOffset(xOffset + HGAP / 2 + 1.0, VGAP + 1.0);
			xOffset += HGAP + NODE_WIDTH;
		}

		Cytoscape.getVisualMappingManager().setNetworkView(view);
	}

	/**
	 * DOCUMENT ME!
	 */
	public void clean() {
		Cytoscape.destroyNetwork(dummyNet);
		Cytoscape.getVisualMappingManager().setNetworkView(oldView);
		dummyNet = null;
		canvas = null;
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void updateView() {
		if (view != null) {
			Cytoscape.getVisualMappingManager().setNetworkView(view);
			view.setVisualStyle(Cytoscape.getVisualMappingManager()
					.getVisualStyle().getName());

			final Dimension panelSize = this.getSize();
			((DGraphView) view).getCanvas().setSize(
					new Dimension((int) panelSize.getWidth() - PADDING,
							(int) panelSize.getHeight() - PADDING));
			view.fitContent();
			canvas = (view.getComponent());

			// for (MouseListener listener : canvas.getMouseListeners())
			// canvas.removeMouseListener(listener);

			this.removeAll();
			this.add(canvas);

			canvas.setLocation(PADDING / 2, PADDING / 2);
			Cytoscape.getVisualMappingManager().applyAppearances();

			if ((background != null) && (canvas != null)) {
				canvas.setBackground(background);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public GraphView getView() {
		return view;
	}
}
