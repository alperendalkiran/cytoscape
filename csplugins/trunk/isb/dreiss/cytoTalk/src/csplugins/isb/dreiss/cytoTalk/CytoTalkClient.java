package csplugins.isb.dreiss.cytoTalk;

import java.util.*;
import java.io.IOException;
import org.apache.xmlrpc.*;

/**
 * Class CytoTalkClient automatically generated by
 * csplugins.isb.dreiss.httpdata.xmlrpc.WriteXmlRpcClient based on handler class
 * csplugins.isb.dreiss.cytoTalk.CytoTalkHandler by dreiss (dreiss@systemsbiology.org)
 **/

public class CytoTalkClient {
	static final String SERVICE_NAME = "cy";
	Vector args = new Vector();
	XmlRpcClient client;

	public boolean doLayout(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".doLayout", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean setTitle( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".setTitle", args );
		return ( (Boolean) out ).booleanValue();
	}

	public int isRunning(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".isRunning", args );
		return ( (Integer) out ).intValue();
	}

	public boolean exposeNodeAttributes(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".exposeNodeAttributes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean exposeEdgeAttributes(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".exposeEdgeAttributes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean exposeCytoscapeDesktop(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".exposeCytoscapeDesktop", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean exposeAll(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".exposeAll", args );
		return ( (Boolean) out ).booleanValue();
	}

	public Vector getSelectedNodes(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getSelectedNodes", args );
		return (Vector) out;
	}

	public Vector getSelectedNodeCommonNames(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getSelectedNodeCommonNames", args );
		return (Vector) out;
	}

	public String getCommonName( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getCommonName", args );
		return (String) out;
	}

	public int countSelectedNodes(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".countSelectedNodes", args );
		return ( (Integer) out ).intValue();
	}

	public boolean storeSelection(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".storeSelection", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean doesNodeExist( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".doesNodeExist", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean isNodeSelected( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".isNodeSelected", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectionChanged(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".selectionChanged", args );
		return ( (Boolean) out ).booleanValue();
	}

	public Vector getAllNodes(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getAllNodes", args );
		return (Vector) out;
	}

	public int countAllNodes(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".countAllNodes", args );
		return ( (Integer) out ).intValue();
	}

	public Vector getSelectedEdges(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getSelectedEdges", args );
		return (Vector) out;
	}

	public int countSelectedEdges(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".countSelectedEdges", args );
		return ( (Integer) out ).intValue();
	}

	public Vector getAllEdges(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getAllEdges", args );
		return (Vector) out;
	}

	public int countAllEdges(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".countAllEdges", args );
		return ( (Integer) out ).intValue();
	}

	public boolean clearSelection(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".clearSelection", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean clearNodeSelection(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".clearNodeSelection", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean clearEdgeSelection(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".clearEdgeSelection", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectNode( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectNode", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean deselectNode( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".deselectNode", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectNodes( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectNodes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectEdge( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectEdge", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectEdges( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectEdges", args );
		return ( (Boolean) out ).booleanValue();
	}

	public Vector getNodeAttributeNames(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getNodeAttributeNames", args );
		return (Vector) out;
	}

	public Vector getEdgeAttributeNames(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getEdgeAttributeNames", args );
		return (Vector) out;
	}

	public boolean graphHasNodeAttribute( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".graphHasNodeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean hasNodeAttribute( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".hasNodeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean graphHasEdgeAttribute( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".graphHasEdgeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean hasEdgeAttribute( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".hasEdgeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public String getNodeSpecies( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getNodeSpecies", args );
		return (String) out;
	}

	public Vector getNodesWithAttribute( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getNodesWithAttribute", args );
		return (Vector) out;
	}

	public Vector getEdgesWithAttribute( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getEdgesWithAttribute", args );
		return (Vector) out;
	}

	public Vector getNodeAttribute( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".getNodeAttribute", args );
		return (Vector) out;
	}

	public Vector getEdgeAttribute( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".getEdgeAttribute", args );
		return (Vector) out;
	}

	public Vector getNodesAttribute( Vector arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".getNodesAttribute", args );
		return (Vector) out;
	}

	public Vector getEdgesAttribute( Vector arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".getEdgesAttribute", args );
		return (Vector) out;
	}

	public Hashtable getNodeAttributes( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getNodeAttributes", args );
		return (Hashtable) out;
	}

	public Hashtable getEdgeAttributes( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getEdgeAttributes", args );
		return (Hashtable) out;
	}

	public Vector getNodesAttributes( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getNodesAttributes", args );
		return (Vector) out;
	}

	public Vector getEdgesAttributes( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getEdgesAttributes", args );
		return (Vector) out;
	}

	public boolean addNodeAttribute( String arg0, String arg1, String arg2 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		args.add( arg2 );
		Object out = client.execute( SERVICE_NAME + ".addNodeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean addNodeAttributes( String arg0, Hashtable arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".addNodeAttributes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean setNodeAttribute( String arg0, String arg1, String arg2 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		args.add( arg2 );
		Object out = client.execute( SERVICE_NAME + ".setNodeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean setNodeAttributes( String arg0, Hashtable arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".setNodeAttributes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean addEdgeAttribute( String arg0, String arg1, String arg2 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		args.add( arg2 );
		Object out = client.execute( SERVICE_NAME + ".addEdgeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean addEdgeAttributes( String arg0, Hashtable arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".addEdgeAttributes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean setEdgeAttribute( String arg0, String arg1, String arg2 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		args.add( arg2 );
		Object out = client.execute( SERVICE_NAME + ".setEdgeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean setEdgeAttributes( String arg0, Hashtable arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".setEdgeAttributes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean deleteAllNodeAttribute( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".deleteAllNodeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean deleteNodeAttribute( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".deleteNodeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean deleteAllEdgeAttribute( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".deleteAllEdgeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean deleteEdgeAttribute( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".deleteEdgeAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public Vector getNeighbors( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getNeighbors", args );
		return (Vector) out;
	}

	public Vector getAdjacentNodes( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getAdjacentNodes", args );
		return (Vector) out;
	}

	public Vector getAdjacentEdges( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getAdjacentEdges", args );
		return (Vector) out;
	}

	public String getConnectedEdge( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".getConnectedEdge", args );
		return (String) out;
	}

	public Vector getConnectedEdges( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".getConnectedEdges", args );
		return (Vector) out;
	}

	public Vector getConnectedEdges( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getConnectedEdges", args );
		return (Vector) out;
	}

	public Vector getNodesNeighbors( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getNodesNeighbors", args );
		return (Vector) out;
	}

	public Vector getNodesConnectedEdges( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getNodesConnectedEdges", args );
		return (Vector) out;
	}

	public Vector getEdgesAdjacentNodes( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getEdgesAdjacentNodes", args );
		return (Vector) out;
	}

	public Vector getNetworkAsList(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getNetworkAsList", args );
		return (Vector) out;
	}

	public Vector getSelectedNetworkAsList(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getSelectedNetworkAsList", args );
		return (Vector) out;
	}

	public Hashtable getNetworkAsHash(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getNetworkAsHash", args );
		return (Hashtable) out;
	}

	public Vector getNetworkAsMatrix(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getNetworkAsMatrix", args );
		return (Vector) out;
	}

	public Vector getSelectedNetworkAsMatrix(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getSelectedNetworkAsMatrix", args );
		return (Vector) out;
	}

	public boolean selectNeighbors( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectNeighbors", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectAdjacentEdges( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectAdjacentEdges", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectConnectedNodes( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectConnectedNodes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectNodesNeighbors( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectNodesNeighbors", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectNodesConnectedEdges( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectNodesConnectedEdges", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectEdgesAdjacentNodes( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectEdgesAdjacentNodes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean selectNodesWithoutAttribute( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".selectNodesWithoutAttribute", args );
		return ( (Boolean) out ).booleanValue();
	}

	public String createEdge( String arg0, String arg1, String arg2 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		args.add( arg2 );
		Object out = client.execute( SERVICE_NAME + ".createEdge", args );
		return (String) out;
	}

	public boolean createEdges( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".createEdges", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean createNode( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".createNode", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean createNodes( Vector arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".createNodes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean redrawGraph(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".redrawGraph", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean relayoutGraph(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".relayoutGraph", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean setWaitCursor(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".setWaitCursor", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean setDefaultCursor(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".setDefaultCursor", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean hideSelectedNodes(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".hideSelectedNodes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean hideSelectedEdges(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".hideSelectedEdges", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean hideNode( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".hideNode", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean hideEdge( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".hideEdge", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean hideEdge( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".hideEdge", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean removeNode( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".removeNode", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean removeEdge( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".removeEdge", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean removeEdge( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".removeEdge", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean unhideNode( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".unhideNode", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean unhideEdge( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".unhideEdge", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean unhideEdge( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".unhideEdge", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean hideAll(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".hideAll", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean hideAllNodes(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".hideAllNodes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean hideAllEdges(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".hideAllEdges", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean unhideAll(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".unhideAll", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean unhideAllNodes(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".unhideAllNodes", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean unhideAllEdges(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".unhideAllEdges", args );
		return ( (Boolean) out ).booleanValue();
	}

	public Vector getConditionNames(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getConditionNames", args );
		return (Vector) out;
	}

	public Vector getGeneNames(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".getGeneNames", args );
		return (Vector) out;
	}

	public double getMeasurement( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".getMeasurement", args );
		return ( (Double) out ).doubleValue();
	}

	public double getMeasurementSignificance( String arg0, String arg1 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		args.add( arg1 );
		Object out = client.execute( SERVICE_NAME + ".getMeasurementSignificance", args );
		return ( (Double) out ).doubleValue();
	}

	public Vector getMeasurements( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getMeasurements", args );
		return (Vector) out;
	}

	public Vector getMeasurementSignificances( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getMeasurementSignificances", args );
		return (Vector) out;
	}

	public boolean addSelectionListener( csplugins.isb.dreiss.cytoTalk.CytoTalkSelectionListener arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".addSelectionListener", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean removeSelectionListener( csplugins.isb.dreiss.cytoTalk.CytoTalkSelectionListener arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".removeSelectionListener", args );
		return ( (Boolean) out ).booleanValue();
	}

	public boolean closeWindow(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".closeWindow", args );
		return ( (Boolean) out ).booleanValue();
	}

	public String getNodeAttributeClass( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getNodeAttributeClass", args );
		return (String) out;
	}

	public Vector getUniqueNodeAttributeValues( String arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( arg0 );
		Object out = client.execute( SERVICE_NAME + ".getUniqueNodeAttributeValues", args );
		return (Vector) out;
	}

	public int newWindowEmpty(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".newWindowEmpty", args );
		return ( (Integer) out ).intValue();
	}

	public int newWindowSelectedNodes(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".newWindowSelectedNodes", args );
		return ( (Integer) out ).intValue();
	}

	public int newWindowSelected(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".newWindowSelected", args );
		return ( (Integer) out ).intValue();
	}

	public int newWindowClone(  ) throws XmlRpcException, IOException {
		args.clear();
		Object out = client.execute( SERVICE_NAME + ".newWindowClone", args );
		return ( (Integer) out ).intValue();
	}

	public boolean splitAttributesOnSemis( boolean arg0 ) throws XmlRpcException, IOException {
		args.clear();
		args.add( new Boolean( arg0 ) );
		Object out = client.execute( SERVICE_NAME + ".splitAttributesOnSemis", args );
		return ( (Boolean) out ).booleanValue();
	}

	public CytoTalkClient( String url ) throws XmlRpcException,
				java.net.MalformedURLException {
		client = new XmlRpcClient( url );
	}

	public XmlRpcClient getClient() {
		return client;
	}

	public static void main( String args[] ) {
		try {
			CytoTalkClient client = new CytoTalkClient( args[ 0 ] );

			//... do your stuff here...

		} catch( Exception e ) { e.printStackTrace(); }
	}
}

