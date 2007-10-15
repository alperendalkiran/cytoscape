package csplugins.mcode;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.actions.GinyUtils;
import cytoscape.data.CyAttributes;
import cytoscape.util.CyFileFilter;
import cytoscape.util.FileUtil;
import cytoscape.view.CyNetworkView;
import cytoscape.view.CytoscapeDesktop;
import cytoscape.view.cytopanels.CytoPanel;
import cytoscape.view.cytopanels.CytoPanelState;
import ding.view.DGraphView;
import giny.model.GraphPerspective;
import giny.model.Node;
import giny.view.NodeView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

/**
 * * Copyright (c) 2004 Memorial Sloan-Kettering Cancer Center
 * *
 * * Code written by: Gary Bader
 * * Authors: Gary Bader, Ethan Cerami, Chris Sander
 * *
 * * This library is free software; you can redistribute it and/or modify it
 * * under the terms of the GNU Lesser General Public License as published
 * * by the Free Software Foundation; either version 2.1 of the License, or
 * * any later version.
 * *
 * * This library is distributed in the hope that it will be useful, but
 * * WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 * * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 * * documentation provided hereunder is on an "as is" basis, and
 * * Memorial Sloan-Kettering Cancer Center
 * * has no obligations to provide maintenance, support,
 * * updates, enhancements or modifications.  In no event shall the
 * * Memorial Sloan-Kettering Cancer Center
 * * be liable to any party for direct, indirect, special,
 * * incidental or consequential damages, including lost profits, arising
 * * out of the use of this software and its documentation, even if
 * * Memorial Sloan-Kettering Cancer Center
 * * has been advised of the possibility of such damage.  See
 * * the GNU Lesser General Public License for more details.
 * *
 * * You should have received a copy of the GNU Lesser General Public License
 * * along with this library; if not, write to the Free Software Foundation,
 * * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * *
 * * User: Vuk Pavlovic
 * * Description: The Results Panel displaying found clusters
 */

/**
 * Reports the results of MCODE cluster finding. This class sets up the UI.
 */
public class MCODEResultsPanel extends JPanel {
    protected String resultTitle;
    protected MCODEAlgorithm alg;
    protected MCODECluster[] clusters;
    protected JTable table;
    protected MCODEResultsPanel.MCODEClusterBrowserTableModel modelBrowser;
    //table size parameters
    protected final int graphPicSize = 80;
    protected final int defaultRowHeight = graphPicSize + 8;
    protected int preferredTableWidth = 0; // incremented below
    //Actual cluster data
    CyNetwork network;                     //Keep a record of the original input record for use in the
                                           //table row selection listener
    CyNetworkView networkView;             //Keep a record of this too, if it exists
    MCODECollapsablePanel explorePanel;
    JPanel[] exploreContent;
    MCODEParameterSet currentParamsCopy;
    int enumerationSelection = 0;          //Keep track of selected attribute for enumeration so it stays selected for all cluster explorations

    //Graphical classes
    GraphDrawer drawer;
    MCODELoader loader;

    /**
     * Constructor for the Results Panel which displays the clusters in a browswer table and
     * explore panels for each cluster.
     *
     * @param clusters Found clusters from the MCODEScoreAndFindTask
     * @param alg A reference to the alg for this particular network
     * @param network Network were these clusters were found
     * @param imageList A list of images of the found clusters
     * @param resultTitle Title of this result as determined by MCODESCoreAndFindAction
     */
    public MCODEResultsPanel(MCODECluster[] clusters, MCODEAlgorithm alg, CyNetwork network, Image[] imageList, String resultTitle) {
        setLayout(new BorderLayout());

        this.alg = alg;
        this.resultTitle = resultTitle;
        this.clusters = clusters;
        this.network = network;
        //the view may not exist, but we only test for that when we need to (in the
        //TableRowSelectionHandler below)
        networkView = Cytoscape.getNetworkView(network.getIdentifier());

        currentParamsCopy = MCODECurrentParameters.getInstance().getResultParams(resultTitle);

        JPanel clusterBrowserPanel = createClusterBrowserPanel(imageList);
        JPanel bottomPanel = createBottomPanel();

        add(clusterBrowserPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        drawer = new GraphDrawer();
        loader = new MCODELoader(table, graphPicSize, graphPicSize);

        this.setSize(this.getMinimumSize());
    }

    /**
     * Creates a panel that contains the browser table with a scroll bar.
     *
     * @param imageList images of cluster graphs
     * @return panel
     */
    private JPanel createClusterBrowserPanel(Image imageList[]) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cluster Browser"));

        //main data table
        modelBrowser = new MCODEResultsPanel.MCODEClusterBrowserTableModel(imageList);

        table = new JTable(modelBrowser);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(StringBuffer.class, new MCODEResultsPanel.JTextAreaRenderer(defaultRowHeight));
        table.setIntercellSpacing(new Dimension(0, 4));   //gives a little vertical room between clusters
        table.setFocusable(false);  //removes an outline that appears when the user clicks on the images

        //Ask to be notified of selection changes.
        ListSelectionModel rowSM = table.getSelectionModel();
        rowSM.addListSelectionListener(new MCODEResultsPanel.TableRowSelectionHandler());

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a panel containing the explore collapsable panel and result set specific buttons
     *
     * @return Panel containing the explore cluster collapsable panel and button panel
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        explorePanel = new MCODECollapsablePanel("Explore");
        explorePanel.setCollapsed(false);
        explorePanel.setVisible(false);

        JPanel buttonPanel = new JPanel();

        //The Export button
        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(new MCODEResultsPanel.ExportAction());
        exportButton.setToolTipText("Export result set to a text file");

        //The close button
        JButton closeButton = new JButton("Discard Result");
        closeButton.addActionListener(new MCODEResultsPanel.CloseAction(this));

        buttonPanel.add(exportButton);
        buttonPanel.add(closeButton);

        panel.add(explorePanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * This method creates a JPanel containing a node score cutoff slider and a node attribute enumeration viewer
     *
     * @param selectedRow The cluster that is selected in the cluster browser
     * @return panel A JPanel with the contents of the explore panel, get's added to the explore collapsable panel's content pane
     */
    private JPanel createExploreContent(int selectedRow) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel sizePanel = new JPanel(new BorderLayout());
        sizePanel.setBorder(BorderFactory.createTitledBorder("Size Threshold"));

        //Create a slider to manipulate node score cutoff (goes to 1000 so that we get a more precise double variable out of it)
        JSlider sizeSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int) (currentParamsCopy.getNodeScoreCutoff() * 1000)) {
            public JToolTip createToolTip() {
                return new JMultiLineToolTip();
            }
        };
        //Turn on ticks and labels at major and minor intervals.
        sizeSlider.setMajorTickSpacing(200);
        sizeSlider.setMinorTickSpacing(50);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        //Set labels ranging from 0 to 100
        Hashtable labelTable = new Hashtable();
        labelTable.put(new Integer(0), new JLabel("Min"));
        //labelTable.put(new Integer(200), new JLabel("20"));
        //labelTable.put(new Integer(400), new JLabel("40"));
        //labelTable.put(new Integer(600), new JLabel("60"));
        //labelTable.put(new Integer(800), new JLabel("80"));
        labelTable.put(new Integer(1000), new JLabel("Max"));
        //Make a special label for the initial position
        labelTable.put(new Integer((int) (currentParamsCopy.getNodeScoreCutoff() * 1000)), new JLabel("^"));

        sizeSlider.setLabelTable(labelTable);
        sizeSlider.setFont(new Font("Arial", Font.PLAIN, 8));

        String sizeTip = "Move the slider to include or\nexclude nodes from the cluster";
        sizeSlider.setToolTipText(sizeTip);

        sizePanel.add(sizeSlider, BorderLayout.NORTH);

        //Node attributes enumerator
        JPanel nodeAttributesPanel = new JPanel(new BorderLayout());
        nodeAttributesPanel.setBorder(BorderFactory.createTitledBorder("Node Attribute Enumerator"));

        String[] availableAttributes = Cytoscape.getNodeAttributes().getAttributeNames();
        Arrays.sort(availableAttributes, String.CASE_INSENSITIVE_ORDER);

        String[] attributesList = new String[availableAttributes.length+1];
        System.arraycopy(availableAttributes, 0, attributesList, 1, availableAttributes.length);
        attributesList[0] = "Please Select";
        JComboBox nodeAttributesComboBox = new JComboBox(attributesList);

        sizeSlider.addChangeListener(new MCODEResultsPanel.SizeAction(selectedRow, nodeAttributesComboBox));

        //Create a table listing the node attributes and their enumerations
        MCODEResultsPanel.MCODEResultsEnumeratorTableModel modelEnumerator;
        modelEnumerator = new MCODEResultsPanel.MCODEResultsEnumeratorTableModel(new HashMap());

        JTable enumerationsTable = new JTable(modelEnumerator);

        JScrollPane tableScrollPane = new JScrollPane(enumerationsTable);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        enumerationsTable.setPreferredScrollableViewportSize(new Dimension(100, graphPicSize));
        enumerationsTable.setGridColor(Color.LIGHT_GRAY);
        enumerationsTable.setFont(new Font(enumerationsTable.getFont().getFontName(), Font.PLAIN, 11));
        enumerationsTable.setDefaultRenderer(StringBuffer.class, new MCODEResultsPanel.JTextAreaRenderer(0));
        enumerationsTable.setFocusable(false);

        //Create a combo box that lists all the available node attributes for enumeration
        nodeAttributesComboBox.addActionListener(new MCODEResultsPanel.enumerateAction(enumerationsTable, modelEnumerator, selectedRow));

        nodeAttributesPanel.add(nodeAttributesComboBox, BorderLayout.NORTH);
        nodeAttributesPanel.add(tableScrollPane, BorderLayout.SOUTH);

        JPanel bottomExplorePanel = createBottomExplorePanel(selectedRow);

        panel.add(sizePanel);
        panel.add(nodeAttributesPanel);
        panel.add(bottomExplorePanel);

        return panel;
    }

    /**
     * Creates a panel containing buttons for the cluster explore collapsable panel
     *
     * @param selectedRow Currently selected row in the cluster browser table
     * @return panel
     */
    private JPanel createBottomExplorePanel(int selectedRow) {
        JPanel panel = new JPanel();
        JButton createChildButton = new JButton("Create Sub-Network");
        createChildButton.addActionListener(new MCODEResultsPanel.CreateChildAction(this, selectedRow));
        panel.add(createChildButton);
        return panel;
    }

    /**
     * Sets the network node attributes to the current result set's scores and clusters.
     * This method is accessed from MCODEVisualStyleAction only when a results panel is selected in the east cytopanel.
     *
     * @return the maximal score in the network given the parameters that were used for scoring at the time
     */
    public double setNodeAttributesAndGetMaxScore() {
        Cytoscape.getNodeAttributes().deleteAttribute("MCODE_Cluster");
        for (Iterator nodes = network.nodesIterator(); nodes.hasNext();) {
            Node n = (Node) nodes.next();
            int rgi = n.getRootGraphIndex();
            Cytoscape.getNodeAttributes().setAttribute(n.getIdentifier(), "MCODE_Node_Status", "Unclustered");
            for (int c = 0; c < clusters.length; c++) {
                MCODECluster cluster = clusters[c];
                if (cluster.getALCluster().contains(new Integer(rgi))) {
                    ArrayList clusterArrayList = new ArrayList();
                    if (Cytoscape.getNodeAttributes().getAttributeList(n.getIdentifier(), "MCODE_Cluster") != null) {
                        clusterArrayList = (ArrayList) Cytoscape.getNodeAttributes().getAttributeList(n.getIdentifier(), "MCODE_Cluster");
                        clusterArrayList.add(cluster.getClusterName());
                    } else {
                        clusterArrayList.add(cluster.getClusterName());
                    }
                    Cytoscape.getNodeAttributes().setAttributeList(n.getIdentifier(), "MCODE_Cluster", clusterArrayList);

                    if (cluster.getSeedNode().intValue() == rgi) {
                        Cytoscape.getNodeAttributes().setAttribute(n.getIdentifier(), "MCODE_Node_Status", "Seed");
                    } else {
                        Cytoscape.getNodeAttributes().setAttribute(n.getIdentifier(), "MCODE_Node_Status", "Clustered");
                    }
                }
            }
            Cytoscape.getNodeAttributes().setAttribute(n.getIdentifier(), "MCODE_Score", alg.getNodeScore(n.getRootGraphIndex(), resultTitle));
        }
        return alg.getMaxScore(resultTitle);
    }

    /**
     * Handles the create child network press in the cluster exploration panel
     */
    private class CreateChildAction extends AbstractAction {
        int selectedRow;
        MCODEResultsPanel trigger;

        CreateChildAction (MCODEResultsPanel trigger, int selectedRow) {
            this.selectedRow = selectedRow;
            this.trigger = trigger;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(3);
            final MCODECluster cluster = clusters[selectedRow];
            final GraphPerspective gpCluster = cluster.getGPCluster();
            final String title = trigger.getResultTitle() + ": " + cluster.getClusterName() + " (Score: "+ nf.format(cluster.getClusterScore()) + ")";
            //create the child network and view
            final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    CyNetwork newNetwork = Cytoscape.createNetwork(gpCluster.getNodeIndicesArray(), gpCluster.getEdgeIndicesArray(), title, network);
                    DGraphView view = (DGraphView) Cytoscape.createNetworkView(newNetwork);
                    //layout new cluster and fit it to window
                    //randomize node positions before layout so that they don't all layout in a line
                    //(so they don't fall into a local minimum for the SpringEmbedder)
                    //If the SpringEmbedder implementation changes, this code may need to be removed
                    NodeView nv;
                    boolean layoutNecessary = false;
                    for (Iterator in = view.getNodeViewsIterator(); in.hasNext();) {
                        nv = (NodeView) in.next();
                        if (cluster.getPGView() != null && cluster.getPGView().getNodeView(nv.getNode().getRootGraphIndex()) != null) {
                            //If it does, then we take the layout position that was already generated for it
                            nv.setXPosition(cluster.getPGView().getNodeView(nv.getNode().getRootGraphIndex()).getXPosition());
                            nv.setYPosition(cluster.getPGView().getNodeView(nv.getNode().getRootGraphIndex()).getYPosition());
                        } else {
                            //this will likely never occur
                            //Otherwise, randomize node positions before layout so that they don't all layout in a line
                            //(so they don't fall into a local minimum for the SpringEmbedder)
                            //If the SpringEmbedder implementation changes, this code may need to be removed
                            nv.setXPosition(view.getCanvas().getWidth() * Math.random());
                            //height is small for many default drawn graphs, thus +100
                            nv.setYPosition((view.getCanvas().getHeight() + 100) * Math.random());
                            layoutNecessary = true;
                        }
                    }
                    if (layoutNecessary) {
                        SpringEmbeddedLayouter lay = new SpringEmbeddedLayouter(view);
                        lay.doLayout(0, 0, 0, null);
                    }
                    view.fitContent();
                    return null;
                }
            };
            worker.start();
        }
    }

    /**
     * Handles the data to be displayed in the cluster browser table
     */
    private class MCODEClusterBrowserTableModel extends AbstractTableModel {

        //Create column headings
        String[] columnNames = {"Network", "Details"};
        Object[][] data;    //the actual table data

        public MCODEClusterBrowserTableModel(Image imageList[]) {
            exploreContent = new JPanel[clusters.length];

            data = new Object[clusters.length][columnNames.length];
            for (int i = 0; i < clusters.length; i++) {
                clusters[i].setRank(i);
                StringBuffer details = new StringBuffer(getClusterDetails(clusters[i]));
                data[i][1] = new StringBuffer(details);
                //create an image for each cluster - make it a nice layout of the cluster
                Image image;
                if (imageList != null) {
                    image = imageList[i];
                } else {
                    image = MCODEUtil.convertNetworkToImage(null, clusters[i], graphPicSize, graphPicSize, null, true);
                }
                data[i][0] = new ImageIcon(image);
            }
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public void setValueAt(Object object, int row, int col) {
            data[row][col] = object;
            fireTableCellUpdated(row, col);
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }

    /**
     * Generates a string buffer with the cluster's details
     * 
     * @param cluster The cluster
     * @return details String buffer containing the details
     */
    private StringBuffer getClusterDetails(MCODECluster cluster) {
        StringBuffer details = new StringBuffer();

        details.append("Rank: ");
        details.append((new Integer(cluster.getRank() + 1)).toString());

        details.append("\n");
        details.append("Score: ");
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        details.append(nf.format(cluster.getClusterScore()));

        details.append("\n");
        details.append("Nodes: ");
        details.append(cluster.getGPCluster().getNodeCount());

        details.append("\n");
        details.append("Edges: ");
        details.append(cluster.getGPCluster().getEdgeCount());

        return details;
    }

    /**
     * Handles the data to be displayed in the node attribute enumeration table
     */
    private class MCODEResultsEnumeratorTableModel extends AbstractTableModel {

        //Create column headings
        String[] columnNames = {"Value", "Occurrence"};
        Object[][] data = new Object[0][columnNames.length];    //the actual table data

        public MCODEResultsEnumeratorTableModel(HashMap enumerations) {
            listIt(enumerations);
        }

        public void listIt(HashMap enumerations) {
            //first we sort the hash map of attributes values and their occurances
            ArrayList enumerationsSorted = sortMap(enumerations);
            //then we put it into the data array in reverse order so that the most
            //frequent attribute value is on top
            Object[][] newData = new Object[enumerationsSorted.size()][columnNames.length];
            int c = enumerationsSorted.size()-1;
            for (Iterator i = enumerationsSorted.iterator(); i.hasNext();) {
                Map.Entry mp = (Map.Entry) i.next();
                newData[c][0] = new StringBuffer(mp.getKey().toString());
                newData[c][1] = new String(mp.getValue().toString());
                c--;
            }
            //finally we redraw the table, however, in order to prevent constant flickering
            //we only fire the data change if the number or rows is altered.  That way,
            //when the number of rows stays the same, which is most of the time, there is no
            //flicker.
            if (getRowCount() == newData.length) {
                data = new Object[newData.length][columnNames.length];
                System.arraycopy(newData, 0, data, 0, data.length);
                fireTableRowsUpdated(0, getRowCount());
            } else {
                data = new Object[newData.length][columnNames.length];
                System.arraycopy(newData, 0, data, 0, data.length);
                fireTableDataChanged();
            }
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public int getRowCount() {
            return data.length;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public void setValueAt(Object object, int row, int col) {
            data[row][col] = object;
            fireTableCellUpdated(row, col);
        }
        
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    /**
    * This method uses Arrays.sort for sorting a Map by the entries' values
    *
    * @param map Has values mapped to keys
    * @return outputList of Map.Entries
    */
    private ArrayList sortMap(Map map) {
        ArrayList outputList = null;
        int count = 0;
        Set set = null;
        Map.Entry[] entries = null;

        set = (Set) map.entrySet();
        Iterator iterator = set.iterator();
        entries = new Map.Entry[set.size()];
        while(iterator.hasNext()) {
            entries[count++] = (Map.Entry) iterator.next();
        }

        // Sort the entries with own comparator for the values:
        Arrays.sort(entries, new Comparator() {
            public int compareTo(Object o1, Object o2) {
                Map.Entry le = (Map.Entry)o1;
                Map.Entry re = (Map.Entry)o2;
                return ((Comparable)le.getValue()).compareTo((Comparable)re.getValue());
            }

            public int compare(Object o1, Object o2) {
                Map.Entry le = (Map.Entry)o1;
                Map.Entry re = (Map.Entry)o2;
                return ((Comparable)le.getValue()).compareTo((Comparable)re.getValue());
            }
        });
        outputList = new ArrayList();
        for(int i = 0; i < entries.length; i++) {
            outputList.add(entries[i]);
        }
        return outputList;
    }

    /**
     * Handles the selection of all available node attributes for the enumeration within the cluster
     */
    private class enumerateAction extends AbstractAction {
        JTable enumerationsTable;
        int selectedRow;
        MCODEResultsPanel.MCODEResultsEnumeratorTableModel modelEnumerator;

        enumerateAction(JTable enumerationsTable, MCODEResultsPanel.MCODEResultsEnumeratorTableModel modelEnumerator, int selectedRow) {
            this.selectedRow = selectedRow;
            this.enumerationsTable = enumerationsTable;
            this.modelEnumerator = modelEnumerator;
        }

        public void actionPerformed(ActionEvent e) {
            HashMap attributeEnumerations = new HashMap(); //the key is the attribute value and the value is the number of times that value appears in the cluster
            //First we want to see which attribute was selected in the combo box
            String attributeName = (String) ((JComboBox) e.getSource()).getSelectedItem();
            int selectionIndex = (int) ((JComboBox) e.getSource()).getSelectedIndex();
            //If its the generic 'please select' option then we don't do any enumeration
            if (!attributeName.equals("Please Select")) {
                //otherwise, we want to get the selected attribute's value for each node in the selected cluster
                for (Iterator i = clusters[selectedRow].getGPCluster().nodesIterator(); i.hasNext();) {
                    Node node = (Node) i.next();
                    //The attribute value will be stored as a string no matter what it is but we need an array list
                    //because some attributes are maps or lists of any size
                    ArrayList attributeValue = new ArrayList();
                    //Every type of attribute has its own get method so we have to see which one to use
                    //When we find the type, we get its value(s)
                    if (Cytoscape.getNodeAttributes().getType(attributeName) == CyAttributes.TYPE_STRING) {
                        attributeValue.add(Cytoscape.getNodeAttributes().getStringAttribute(node.getIdentifier(), attributeName));
                    } else if (Cytoscape.getNodeAttributes().getType(attributeName) == CyAttributes.TYPE_FLOATING) {
                        attributeValue.add(Cytoscape.getNodeAttributes().getDoubleAttribute(node.getIdentifier(), attributeName));
                    } else if (Cytoscape.getNodeAttributes().getType(attributeName) == CyAttributes.TYPE_INTEGER) {
                        attributeValue.add(Cytoscape.getNodeAttributes().getIntegerAttribute(node.getIdentifier(), attributeName));
                    } else if (Cytoscape.getNodeAttributes().getType(attributeName) == CyAttributes.TYPE_BOOLEAN) {
                        attributeValue.add(Cytoscape.getNodeAttributes().getBooleanAttribute(node.getIdentifier(), attributeName));
                    } else if (Cytoscape.getNodeAttributes().getType(attributeName) == CyAttributes.TYPE_SIMPLE_LIST) {
                        List valueList = Cytoscape.getNodeAttributes().getAttributeList(node.getIdentifier(), attributeName);
                        for (Iterator vli = valueList.iterator(); vli.hasNext();) {
                            attributeValue.add(vli.next());
                        }
                    } else if (Cytoscape.getNodeAttributes().getType(attributeName) == CyAttributes.TYPE_SIMPLE_MAP) {
                        Map valueMap = Cytoscape.getNodeAttributes().getAttributeMap(node.getIdentifier(), attributeName);
                        for (Iterator vmki = valueMap.keySet().iterator(); vmki.hasNext();) {
                            String key = (String) vmki.next();
                            Object value = valueMap.get(key);
                            attributeValue.add(new String(key + " -> " + value));
                        }
                    }
                    //Next we must make a non-repeating list with the attribute values and enumerate the repetitions
                    for (Iterator avi = attributeValue.iterator(); avi.hasNext();) {
                        Object aviElement = avi.next();
                        if (aviElement != null) {
                            String value = aviElement.toString();

                            if (!attributeEnumerations.containsKey(value)) {
                                //If the attribute value appears for the first time, we give it an enumeration of 1 and add it to the enumerations
                                attributeEnumerations.put(value, new Integer(1));
                            } else {
                                //If it already appeared before, we want to add to the enumeration of the value
                                Integer enumeration = (Integer) attributeEnumerations.get(value);
                                enumeration = new Integer(enumeration.intValue()+1);
                                attributeEnumerations.put(value, enumeration);
                            }
                        }
                    }
                }
            }
            modelEnumerator.listIt(attributeEnumerations);
            //Finally we make sure that the selection is stored so that all the cluster explorations are looking at the already selected attribute
            enumerationSelection = selectionIndex;
        }
    }

    /**
     * Handles the close press for this results panel
     */
    private class CloseAction extends AbstractAction {
        MCODEResultsPanel trigger;

        CloseAction(MCODEResultsPanel trigger) {
            this.trigger = trigger;
        }

        public void actionPerformed(ActionEvent e) {
            CytoscapeDesktop desktop = Cytoscape.getDesktop();
            CytoPanel cytoPanel = desktop.getCytoPanel(SwingConstants.EAST);
            //Must make sure the user wants to close this results panel
            String message = "You are about to dispose of " + resultTitle + ".\nDo you wish to continue?";
            int result = JOptionPane.showOptionDialog(Cytoscape.getDesktop(), new Object[]{message}, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (result == JOptionPane.YES_OPTION) {
                cytoPanel.remove(trigger);
                MCODECurrentParameters.removeResultParams(getResultTitle());
            }
            //If there are no more tabs in the cytopanel then we hide it
            if (cytoPanel.getCytoPanelComponentCount() == 0) {
                cytoPanel.setState(CytoPanelState.HIDE);
            }
        }
    }

    /**
     * Handles the Export press for this panel (export results to a text file)
     */
    private class ExportAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            //call save method in MCODE
            //get the file name
            File file = FileUtil.getFile("Export Graph as Interactions",
                    FileUtil.SAVE, new CyFileFilter[]{});

            if (file != null) {
                String fileName = file.getAbsolutePath();
                MCODEUtil.exportMCODEResults(alg, clusters, network, fileName);
            }
        }
    }

    /**
     * Handler to select nodes in graph when a row is selected
     */
    private class TableRowSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            //Ignore extra messages.
            if (e.getValueIsAdjusting()) return;
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            final GraphPerspective gpCluster;

            if (!lsm.isSelectionEmpty()) {
                final int selectedRow = lsm.getMinSelectionIndex();
                gpCluster = clusters[selectedRow].getGPCluster();
                selectCluster(gpCluster);

                //Upon selection of a cluster, we must show the corresponding explore panel content
                //First we test if this cluster has been selected yet and if its content exists
                //If it does not, we create it
                if (exploreContent[selectedRow] == null) {
                    exploreContent[selectedRow] = createExploreContent(selectedRow);
                }
                //Next, if this is the first time explore panel content is being displayed, then the
                //explore panel is not visible yet, and there is no content in it yet, so we do not
                //have to remove it, otherwise, if the panel is visible, then it must have content
                //which needs to be removed
                if (explorePanel.isVisible()) {
                    explorePanel.getContentPane().remove(0);
                }
                //Now we add the currently selected cluster's explore panel content
                explorePanel.getContentPane().add(exploreContent[selectedRow], BorderLayout.CENTER);
                //and set the explore panel to visible so that it can be seen (this only happens once
                //after the first time the user selects a cluster
                if (!explorePanel.isVisible()){
                    explorePanel.setVisible(true);
                }
                //Finally the explore panel must be redrawn upon the selection event to display the
                //new content with the name of the cluster, if it exists
                String title = "Explore: ";
                if (clusters[selectedRow].getClusterName() != null) {
                    title = title + clusters[selectedRow].getClusterName();
                } else {
                    title = title + "Cluster " + (selectedRow + 1);
                }
                explorePanel.setTitleComponentText(title);
                explorePanel.updateUI();

                //In order for the enumeration to be conducted for this cluster on the same attribute that might already have been selected
                //we get a reference to the combo box within the explore content
                JComboBox nodeAttributesComboBox = (JComboBox) ((JPanel) exploreContent[selectedRow].getComponent(1)).getComponent(0);
                //and fire the enumeration action
                nodeAttributesComboBox.setSelectedIndex(enumerationSelection);

                //TODO: table.scrollRectToVisible(table.getCellRect(selectedRow, 0, true));//this needs to be called when the explore panel shows up so that any selection in the cluster browser is centered in the visible portion of the table, but it doesn't work from here
            }
        }
    }

    /**
     * Selects a cluster in the view that is selected by the user in the browser table
     *
     * @param gpCluster Cluster to be selected
     */
    public void selectCluster(GraphPerspective gpCluster) {
        NodeView nv;
        if (gpCluster != null) {
            //only do this if a view has been created on this network
            if (networkView != null) {
                //start with no selected nodes
                GinyUtils.deselectAllNodes(networkView);
                network.setSelectedNodeState(gpCluster.nodesList(), true);
                //We want the focus to switch to the appropriate network view but only if the cytopanel is docked
                //If it is not docked then it is best if the focus stays on the panel
                if(Cytoscape.getDesktop().getCytoPanel(SwingConstants.EAST).getState() == CytoPanelState.DOCK) {
                    Cytoscape.getDesktop().setFocus(networkView.getIdentifier());
                }
            } else {
                //Warn user that nothing will happen in this case because there is no view to select nodes with
                JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "You must have a network view\ncreated to select nodes.", "No Network View", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (networkView != null) {
            GinyUtils.deselectAllNodes(networkView);
        }
    }

    /**
     * A text area renderer that creates a line wrapped, non-editable text area
     */
    private class JTextAreaRenderer extends JTextArea implements TableCellRenderer {
        int minHeight;

        /**
         * Constructor
         *
         * @param minHeight The minimum height of the row, either the size of the graph picture or zero
         */
        public JTextAreaRenderer(int minHeight) {
            this.setLineWrap(true);
            this.setWrapStyleWord(true);
            this.setEditable(false);
            this.setFont(new Font(this.getFont().getFontName(), Font.PLAIN, 11));
            this.minHeight = minHeight;
        }

        /**
         * Used to render a table cell.  Handles selection color and cell heigh and width.
         * Note: Be careful changing this code as there could easily be infinite loops created
         * when calculating preferred cell size as the user changes the dialog box size.
         *
         * @param table      Parent table of cell
         * @param value      Value of cell
         * @param isSelected True if cell is selected
         * @param hasFocus   True if cell has focus
         * @param row        The row of this cell
         * @param column     The column of this cell
         * @return The cell to render by the calling code
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            StringBuffer sb = (StringBuffer) value;
            this.setText(sb.toString());

            if (isSelected) {
                this.setBackground(table.getSelectionBackground());
                this.setForeground(table.getSelectionForeground());
            } else {
                this.setBackground(table.getBackground());
                this.setForeground(table.getForeground());
            }
            //row height calculations
            int currentRowHeight = table.getRowHeight(row);
            int rowMargin = table.getRowMargin();
            this.setSize(table.getColumnModel().getColumn(column).getWidth(), currentRowHeight - (2 * rowMargin));
            int textAreaPreferredHeight = (int) this.getPreferredSize().getHeight();
            //JTextArea can grow and shrink here
            if (currentRowHeight != Math.max(textAreaPreferredHeight + (2 * rowMargin) , minHeight + (2 * rowMargin))) {
                table.setRowHeight(row, Math.max(textAreaPreferredHeight + (2 * rowMargin), minHeight + (2 * rowMargin)));
            }
            return this;
        }
    }

    //Getter and Setter for this Result Set's Title/ID
    public String getResultTitle() {
        return resultTitle;
    }

    public void setResultTitle(String title) {
        resultTitle = title;
    }

    /**
     * Getter for the browser table used in MCODEVisualStyleAction to reselect the selected
     * cluster whenever the user focuses on this result set
     */
    public JTable getClusterBrowserTable() {
        return table;
    }

    /**
     * Handles the dynamic cluster size manipulation via the JSlider
     */
    private class SizeAction implements ChangeListener {
        private int selectedRow;
        public boolean loaderSet = false;
        private JComboBox nodeAttributesComboBox;
        private GraphDrawer drawer;
        private boolean drawPlaceHolder;

        /**
         * Constructor
         *
         * @param selectedRow The selected cluster
         * @param nodeAttributesComboBox Reference to the attribute enumeration picker
         */
        SizeAction(int selectedRow, JComboBox nodeAttributesComboBox){
            this.selectedRow = selectedRow;
            this.nodeAttributesComboBox = nodeAttributesComboBox;
            drawer = new GraphDrawer();
            loaderSet = false;
        }

        public void stateChanged(ChangeEvent e) {
            //This method as been written so that the slider is responsive to the user's input at all times, despite
            //the computationally challenging drawing, layout out, and selection of large clusters.  As such, we only
            //perform real work if the slider produces a different cluster, and furthermore we only perform the quick
            //processes here, while the heavy weights are handled by the drawer's thread.
            JSlider source = (JSlider)e.getSource();
            double nodeScoreCutoff = (((double)source.getValue())/1000);
            
            //Store current cluster content for comparison
            ArrayList oldCluster = clusters[selectedRow].getALCluster();

            //Find the new cluster given the node score cutoff
            MCODECluster cluster = alg.exploreCluster(clusters[selectedRow], nodeScoreCutoff, network, resultTitle);

            //We only want to do the following work if the newly found cluster is actually different
            //So we get the new cluster content
            ArrayList newCluster = cluster.getALCluster();

            //If the new cluster is too large to draw within a reasonable time and won't look understandable in the
            //table cell then we draw a place holder
            drawPlaceHolder = newCluster.size() > 300;

            //And compare the old and new
            if (!newCluster.equals(oldCluster)) {
                //If the cluster has changed, then we conduct all non-rate-limiting steps:
                //Interrupt the drawing
                drawer.interruptDrawing();
                //Update the cluster array
                clusters[selectedRow] = cluster;
                //Update the details
                StringBuffer details = getClusterDetails(cluster);
                table.setValueAt(details, selectedRow, 1);
                //Fire the enumeration action
                nodeAttributesComboBox.setSelectedIndex(nodeAttributesComboBox.getSelectedIndex());

                //Ensure that a loader is set with the selected row and table object
                //Also, we want to set the loader only once during continuous exploration
                //It is only set again when a graph is fully loaded and placed in the table
                if (!loaderSet && !drawPlaceHolder) {
                    //internally, the loader is only drawn into the appropriate cell after a short sleep period
                    //to ensure that quick loads are not displayed unecessarily
                    loader.setLoader(selectedRow, table);
                    loaderSet = true;
                }
                //There is a small difference between expanding and retracting the cluster size
                //When expanding, new nodes need random position and thus must go through the layout
                //When retracting, we simply use the layout that was generated and stored
                //This speeds up the drawing process greatly
                boolean layoutNecessary = newCluster.size() > oldCluster.size();
                //Draw Graph and select the cluster in the view in a separate thread so that it can be
                //interrupted by the slider movement
                drawer.drawGraph(cluster, layoutNecessary, this, drawPlaceHolder);
            }
        }
    }

    /**
     * Threaded method for drawing exploration graphs which allows the slider to move uninterruptedly despite MCODE's
     * drawing efforts
     */
    private class GraphDrawer implements Runnable {
        private Thread t;
        private boolean drawGraph; //run switch
        private boolean placeHolderDrawn;
        private boolean drawPlaceHolder;
        MCODECluster cluster;
        SpringEmbeddedLayouter layouter;
        MCODEResultsPanel.SizeAction trigger;
        boolean layoutNecessary;
        boolean clusterSelected;

        GraphDrawer () {
            drawGraph = false;
            drawPlaceHolder = false;
            layouter = new SpringEmbeddedLayouter();
            t = new Thread(this);
            t.start();
        }

        /**
         * Constructor for drawing graphs during exploration
         *
         * @param cluster Cluster to be drawn
         * @param layoutNecessary True only if the cluster is expanding in size or lacks a PGView
         * @param trigger Reference to the slider size action
         * @param drawPlaceHolder Determines if the cluster should be drawn or a place holder in the case of big clusters
         */
        public void drawGraph(MCODECluster cluster, boolean layoutNecessary, MCODEResultsPanel.SizeAction trigger, boolean drawPlaceHolder) {
            this.cluster = cluster;
            this.trigger = trigger;
            this.layoutNecessary = layoutNecessary;

            //Graph drawing will only occur if the cluster is not too large, otherwise a place holder will be drawn
            drawGraph = !drawPlaceHolder;
            this.drawPlaceHolder = drawPlaceHolder;
            clusterSelected = false;
        }

        public void run () {
            try {
                while (true) {
                    //This ensures that the drawing of this cluster is only attempted once
                    //if it is unsuccessful it is because the setup or layout process was interrupted by the slider movement
                    //In that case the drawing must occur for a new cluster using the drawGraph method
                    if (drawGraph && !drawPlaceHolder) {
                        Image image = MCODEUtil.convertNetworkToImage(loader, cluster, graphPicSize, graphPicSize, layouter, layoutNecessary);
                        //If the drawing process was interrupted, a new cluster must have been found and
                        //this will have returned null, the drawing will be recalled (with the new cluster)
                        //However, if the graphing was successfull, we update the table, sotp the loader from animating and select the new cluster
                        if (image != null && drawGraph) {
                            //Select the new cluster (surprisingly a time consuming step)
                            loader.setProgress(100, "Selecting Nodes");
                            selectCluster(cluster.getGPCluster());
                            clusterSelected = true;
                            //Update the table
                            table.setValueAt(new ImageIcon(image), cluster.getRank(), 0);
                            //Loader is no longer showing, so we let the SizeAction know that
                            trigger.loaderSet = false;
                            //stop loader from animating and taking up computer processing power
                            loader.loaded();

                            drawGraph = false;
                        }
                        //This is here just in case to reset the variable
                        placeHolderDrawn = false;
                    } else if (drawPlaceHolder && !placeHolderDrawn) {
                        //draw place holder, only once though (as per the if statement)
                        Image image = MCODEUtil.getPlaceHolderImage(graphPicSize, graphPicSize);
                        //Update the table
                        table.setValueAt(new ImageIcon(image), cluster.getRank(), 0);
                        //select the cluster
                        selectCluster(cluster.getGPCluster());
                        //Loader is no longer showing, so we let the SizeAction know that
                        trigger.loaderSet = false;
                        //stop loader from animating and taking up computer processing power
                        loader.loaded();
                        drawGraph = false;
                        //Make sure this block is not run again unless if we need to reload the image
                        placeHolderDrawn = true;
                        
                    } else if (!drawGraph && drawPlaceHolder && !clusterSelected) {
                        selectCluster(cluster.getGPCluster());
                        clusterSelected = true;
                    }
                    //This sleep time produces the drawing response time of 1 20th of a second
                    Thread.sleep(100);
                }
            } catch (Exception e) {}
        }

        public void interruptDrawing() {
            drawGraph = false;
            layouter.interruptDoLayout();
            MCODEUtil.interruptLoading();
        }
    }
}