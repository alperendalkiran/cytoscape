package csplugins.test.quickfind.test;

import csplugins.quickfind.util.CyAttributesUtil;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.data.CyAttributesImpl;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tests the CyAttributes Util class.
 *
 * @author Ethan Cerami.
 */
public class TestCyAttributesUtil extends TestCase {
    private static final String ID = "id1";
    private static final String BOOLEAN_TYPE = "boolean_type";
    private static final String INTEGER_TYPE = "integer_type";
    private static final String FLOATING_TYPE = "floating_type";
    private static final String STRING_TYPE = "string_type";
    private static final String LIST_TYPE = "list_type";
    private static final String MAP_TYPE = "map_type";

    /**
     * Tests the getAttributeValues() method.
     */
    public void testGetAttributeValues() {
        CyAttributes cyAttributes = new CyAttributesImpl();

        //  Test with Boolean value
        cyAttributes.setAttribute(ID, BOOLEAN_TYPE, Boolean.TRUE);
        String values[] = CyAttributesUtil.getAttributeValues(cyAttributes,
                ID, BOOLEAN_TYPE);
        assertEquals(1, values.length);
        assertEquals("true", values[0]);

        //  Test with Integer value
        cyAttributes.setAttribute(ID, INTEGER_TYPE, new Integer(25));
        values = CyAttributesUtil.getAttributeValues(cyAttributes,
                ID, INTEGER_TYPE);
        assertEquals(1, values.length);
        assertEquals("25", values[0]);

        //  Test with Floating value
        cyAttributes.setAttribute(ID, FLOATING_TYPE, new Double(25.0));
        values = CyAttributesUtil.getAttributeValues(cyAttributes,
                ID, FLOATING_TYPE);
        assertEquals(1, values.length);
        assertEquals("25.0", values[0]);

        //  Test with Simple List
        ArrayList list = new ArrayList();
        list.add("apple");
        list.add("banana");
        cyAttributes.setAttributeList(ID, LIST_TYPE, list);
        values = CyAttributesUtil.getAttributeValues(cyAttributes,
                ID, LIST_TYPE);
        assertEquals(2, values.length);
        assertEquals("apple", values[0]);
        assertEquals("banana", values[1]);

        //  Test with Simple Map
        HashMap map = new HashMap();
        map.put("first_name", "Ethan");
        map.put("last_name", "Cerami");
        cyAttributes.setAttributeMap(ID, MAP_TYPE, map);
        values = CyAttributesUtil.getAttributeValues(cyAttributes,
                ID, MAP_TYPE);
        assertEquals(2, values.length);
        boolean firstNameCheck = false, lastNameCheck = false;
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals("Ethan")) {
                firstNameCheck = true;
            } else if (values[i].equals("Cerami")) {
                lastNameCheck = true;
            }
        }
        assertTrue(firstNameCheck);
        assertTrue(lastNameCheck);

        //  Test with invalid attribute key
        values = CyAttributesUtil.getAttributeValues(cyAttributes,
                ID, "CELLULAR_LOCATION");
        assertEquals(null, values);
    }

    /**
     * Tests the getDistinctAttributeValues method, Take 1.
     */
    public static void testGetDistinctAttributeValues1() {
        CyAttributes cyAttributes = new CyAttributesImpl();
        cyAttributes.setAttribute("ID1", STRING_TYPE, "A");
        cyAttributes.setAttribute("ID2", STRING_TYPE, "A");
        cyAttributes.setAttribute("ID3", STRING_TYPE, "B");
        cyAttributes.setAttribute("ID4", STRING_TYPE, "B");
        cyAttributes.setAttribute("ID5", STRING_TYPE, "C");
        cyAttributes.setAttribute("ID6", STRING_TYPE, "C");
        CyNetwork network = Cytoscape.createNetwork("csplugins.test");

        createNode("ID1", network);
        createNode("ID2", network);
        createNode("ID3", network);
        createNode("ID4", network);
        createNode("ID5", network);
        createNode("ID6", network);

        String values [] = CyAttributesUtil.getDistinctAttributeValues
                (network.nodesIterator(),
                        cyAttributes, STRING_TYPE, 5);
        assertEquals(3, values.length);
        boolean aCheck = false, bCheck = false, cCheck = false;
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals("A")) {
                aCheck = true;
            } else if (values[i].equals("B")) {
                bCheck = true;
            } else if (values[i].equals("C")) {
                cCheck = true;
            }
        }
        assertTrue(aCheck);
        assertTrue(bCheck);
        assertTrue(cCheck);
    }

    /**
     * Tests the getDistinctAttributeValues method, Take 2.
     */
    public static void testGetDistinctAttributeValues2() {
        CyAttributes cyAttributes = new CyAttributesImpl();

        //  Test with Simple List
        ArrayList list = new ArrayList();
        list.add("apple");
        list.add("banana");

        cyAttributes.setAttributeList("ID1", LIST_TYPE, list);
        CyNetwork network = Cytoscape.createNetwork("csplugins.test");
        createNode("ID1", network);

        String values [] = CyAttributesUtil.getDistinctAttributeValues
                (network.nodesIterator(),
                        cyAttributes, LIST_TYPE, 5);
        assertEquals(1, values.length);
        assertEquals("apple, banana", values[0]);
    }

    private static void createNode(String id, CyNetwork network) {
        CyNode node = Cytoscape.getCyNode(id, true);
        network.addNode(node);
    }
}
