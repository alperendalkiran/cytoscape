/* -*-Java-*-
********************************************************************************
*
* File:         HyperEdgeFactory.java
* RCS:          $Header: /cvs/cvsroot/lstl-lsi/HyperEdge/src/cytoscape/hyperedge/HyperEdgeFactory.java,v 1.1 2007/07/04 01:11:35 creech Exp $
* Description:
* Author:       Michael L. Creech
* Created:      Wed Sep 14 09:06:41 2005
* Modified:     Tue Nov 07 09:08:14 2006 (Michael L. Creech) creech@w235krbza760
* Language:     Java
* Package:
* Status:       Experimental (Do Not Distribute)
*
* (c) Copyright 2005, Agilent Technologies, all rights reserved.
*
********************************************************************************
*
* Revisions:
*
* Tue Nov 07 09:07:59 2006 (Michael L. Creech) creech@w235krbza760
*  Changed Edge-->CyEdge.
* Mon Nov 06 09:08:20 2006 (Michael L. Creech) creech@w235krbza760
*  Changed GraphPerspective-->CyNetwork, Node-->CyNode.
* Mon Sep 11 18:10:00 2006 (Michael L. Creech) creech@w235krbza760
*  Changed createHyperEdge(Collection...) to createHyperEdge(List...).
* Thu Aug 17 17:18:42 2006 (Michael L. Creech) creech@w235krbza760
*  Moved addNewObjectListener() and removeNewObjectListener() to HyperEdgeManager.
* Thu Sep 29 13:10:48 2005 (Michael L. Creech) creech@Dill
*    Added getEdgeTypeMap ().
* Tue Sep 27 16:30:47 2005 (Michael L. Creech) creech@Dill
*  Added to comments.
********************************************************************************
*/
package cytoscape.hyperedge;


import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.hyperedge.impl.HyperEdgeFactoryImpl;

import java.util.List;
import java.util.Map;


/**
 * Means of contructing HyperEdges, listening to new object events,
 * and obtaining HyperEdge components.
 *
 * <H4>HyperEdge Constructors</H4>
 *
 * All HyperEdge constructors take two or more CyNodes, each with a
 * corresponding edge interaction type (edgeIType). Each edgeIType
 * determines whether the corresponding CyNode will be the source or the
 * target of the CyEdge created that houses that CyNode.  This is
 * performed by using edgeIType as a key in the EdgeTypeMap to
 * determine the EdgeRole associated with that edgeIType. If the value
 * associated with the edgeIType key is EdgeRole.SOURCE, then the
 * corresponding CyNode will be the source of the newly created CyEdge. If
 * the value associated with the edgeIType key is EdgeRole.TARGET,
 * then the corresponding CyNode will be the target of the newly created
 * CyEdge.  If edgeIType is not the key for any EdgeRole in the
 * EdgeTypeMap, then the coresponding node will be the source of the
 * newly created CyEdge.
 *
 * <P>The CyNetwork argument (net) to all constructors specifies
 * the GraphPerpsective to which to add this new HyperEdge. If null,
 * the HyperEdge is not added to any CyNetwork.
 *
 * @author Michael L. Creech
 * @version 1.1
 */
public interface HyperEdgeFactory {
    HyperEdgeFactory INSTANCE = HyperEdgeFactoryImpl.createInstance();

    /**
     * Return the singleton HyperEdgeManager.
     */
    HyperEdgeManager getHyperEdgeManager();

    /**
     * Return the singleton EdgeTypeMap. Thie EdgeTypeMap is used for
     * deciding how CyNodes added to HyperEdges or CyNodes in the
     * construction of a new HyperEdge, will be treated with respect to
     * their role (source or target) within the Edges created.
    
     */
    EdgeTypeMap getEdgeTypeMap();

    /**
     * Create and return a HyperEdge connecting two CyNodes.  The
     * HyperEdge will contain a newly created ConnectorNode.  Two
     * CyEdges will be created that connect each CyNode to this Connector
     * CyNode.  The edge interaction types specify the type attribute to
     * associate with each CyEdge created and whether each node is the
     * source or target of each CyEdge.
     * @param node1 the first node to add to the HyperEdge.
     * @param edgeIType1 the edge interaction type of node1.
     * edgeIType1 also determines the Role played by node1 within
     * the newly created CyEdge. For details, see the discussion in this
     * class description and the example in EdgeTypeMap.
     * @param node2 the second node to add to the HyperEdge.
     * @param edgeIType2 the edge interaction type of node2.
     * edgeIType2 also determines the Role played by node2 within
     * the newly created CyEdge.  For details, see the discussion in this
     * class description and the example in EdgeTypeMap.
     * @return the newly created HyperEdge.
     * After the HyperEdge is created, all HyperEdgeManager
     * NewObjectListeners are notified by invoking their {@link
     * cytoscape.hyperedge.event.NewObjectListener#objectCreated
     * NewObjectListener.objectCreated()} methods about the creation of
     * this new HyperEdge.
     * @see HyperEdgeFactory
     * @see EdgeTypeMap
     * @throws IllegalArgumentException if net, node1, node2, edgeIType1,
     * edgeIType2 are null, or if node1 or node2 are ConnectorNodes.
     */
    HyperEdge createHyperEdge(CyNode node1, String edgeIType1, CyNode node2,
                              String edgeIType2, CyNetwork net);

    /**
     * Create and return a HyperEdge connecting three CyNodes.  The
     * HyperEdge will contain a newly created ConnectorNode.  Three
     * CyEdges will be created that connect each CyNode to this Connector
     * CyNode. The edge interaction types specify the type attribute to
     * associate with each CyEdge created and whether each node is the
     * source or target of each CyEdge.
     * @param node1 the first node to add to the HyperEdge.
     * @param edgeIType1 the edge interaction type of node1.
     * edgeIType1 also determines the Role played by node1 within
     * the newly created CyEdge. For details, see the discussion in this
     * class description and the example in EdgeTypeMap.
     * @param node2 the second node to add to the HyperEdge.
     * @param edgeIType2 the edge interaction type of node2.
     * edgeIType2 also determines the Role played by node2 within
     * the newly created CyEdge.  For details, see the discussion in this
     * class description and the example in EdgeTypeMap.
     * @param node3 the third node to add to the HyperEdge.
     * @param edgeIType3 the edge interaction type of node3.
     * edgeIType3 also determines the Role played by node3 within
     * the newly created CyEdge.  For details, see the discussion in this
     * class description and the example in EdgeTypeMap.
     * @return the newly created HyperEdge.
     * After the HyperEdge is created, all HyperEdgeManager
     * NewObjectListeners are notified by invoking their {@link
     * cytoscape.hyperedge.event.NewObjectListener#objectCreated
     * NewObjectListener.objectCreated()} methods about the creation of
     * this new HyperEdge.
     * @see HyperEdgeFactory
     * @see EdgeTypeMap
     * @throws IllegalArgumentException if net, node1, node2, node3,
     * edgeIType1, edgeIType2, edgeIType3 are null, or if node1,
     * node2, or node3 are ConnectorNodes.
     */
    HyperEdge createHyperEdge(CyNode node1, String edgeIType1, CyNode node2,
                              String edgeIType2, CyNode node3,
                              String edgeIType3, CyNetwork net);

    /**
     * Creates a HyperEdge based on a an array of CyNodes along with a
     * corresponding array of Roles. The HyperEdge will contain a
     * newly created ConnectorNode.  CyEdges will be created what
     * connect each given CyNode to this ConnectorNode.
     * @param nodes an Array of CyNodes to be members of this HyperEdge.
     * @param edgeITypes an Array of corresponding edge interaction
     * types (one for each CyNode) used for setting the type attribute
     * of the CyEdges created and the role played by each node in each
     * created CyEdge.  For details, see the discussion in this class
     * description and the example in EdgeTypeMap.
     * @return the newly created HyperEdge.
     * After the HyperEdge is created, all HyperEdgeManager
     * NewObjectListeners are notified by invoking their {@link
     * cytoscape.hyperedge.event.NewObjectListener#objectCreated
     * NewObjectListener.objectCreated()} methods about the creation of
     * this new HyperEdge.
     * @see HyperEdgeFactory
     * @see EdgeTypeMap
     * @throws IllegalArgumentException if grp, arrays are null, arrays are
     * not the same size or not >= two in size, if any array elements are
     * null, or if any nodes are ConnectorNodes.
     */
    HyperEdge createHyperEdge(CyNode[] nodes, String[] edgeITypes, CyNetwork net);

    /**
     * Creates a HyperEdge based on a List of CyNodes along with a
     * corresponding List of CyEdge interaction types.  The HyperEdge will
     * contain a newly created ConnectorNode.  For each CyNode given,
     * an CyEdge will be created that connects this CyNode to the
     * ConnectorNode.
     * @param nodes a List of CyNodes to be members of this HyperEdge.
     * @param edgeITypes a List of corresponding edge
     * interaction types (one for each CyNode) used for setting the type
     * attribute of the CyEdges created and the role played by each node
     * in each create CyEdge.  For details, see the discussion in this
     * class description and the example in EdgeTypeMap.
     * @return the newly created HyperEdge.
     * After the HyperEdge is created, all HyperEdgeManager
     * NewObjectListeners are notified by invoking their {@link
     * cytoscape.hyperedge.event.NewObjectListener#objectCreated
     * NewObjectListener.objectCreated()} methods about the creation of
     * this new HyperEdge.
     * @see HyperEdgeFactory
     * @see EdgeTypeMap
     * @throws IllegalArgumentException if net is null, the Lists
     * are not the same size or not >= two in size, or if any
     * List elements are null, or if any nodes are ConnectorNodes.
     */
    HyperEdge createHyperEdge(List<CyNode> nodes, List<String> edgeITypes,
                              CyNetwork net);

    /**
     * Creates a HyperEdge based on a map of a CyNode key to an
     * CyEdge interaction type value.  Each key and value must be
     * non-null and all values must be a non-empty edge
     * interaction types.  The HyperEdge will contain a newly created
     * ConnectorNode.  For each edge interaction type value associated
     * with a CyNode key, an CyEdge will be created that connects the CyNode
     * key to the HyperEdge CyNode. The type attribute of this CyEdge will
     * be the edge interaction type value and specify whether the node
     * is the source or target of this CyEdge.  For details on how each
     * edge interaction type value determines the corresponding node's
     * role within each new CyEdge, see the discussion in this class
     * description and the example in EdgeTypeMap.
     * @return the newly created HyperEdge.
     * After the HyperEdge is created, all HyperEdgeManager
     * NewObjectListeners are notified by invoking their {@link
     * cytoscape.hyperedge.event.NewObjectListener#objectCreated
     * NewObjectListener.objectCreated()} methods about the creation of
     * this new HyperEdge.
     * @see HyperEdgeFactory
     * @see EdgeTypeMap
     * @throws IllegalArgumentException if net is null,
     * nodeEdgeITypeMap has less than two edge type values, any keys
     * or values are null, any keys are not CyNodes or are
     * ConnectorNodes, and if any values are not Strings.
     */
    HyperEdge createHyperEdge(Map<CyNode, String> nodeEdgeITypeMap,
                              CyNetwork net);
}
