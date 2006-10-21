/** Copyright (c) 2004 Memorial Sloan-Kettering Cancer Center.
 **
 ** Code written by: Ethan Cerami
 ** Authors: Ethan Cerami, Gary Bader, Chris Sander
 **
 ** This library is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Lesser General Public License as published
 ** by the Free Software Foundation; either version 2.1 of the License, or
 ** any later version.
 **
 ** This library is distributed in the hope that it will be useful, but
 ** WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 ** MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 ** documentation provided hereunder is on an "as is" basis, and
 ** Memorial Sloan-Kettering Cancer Center
 ** has no obligations to provide maintenance, support,
 ** updates, enhancements or modifications.  In no event shall
 ** Memorial Sloan-Kettering Cancer Center
 ** be liable to any party for direct, indirect, special,
 ** incidental or consequential damages, including lost profits, arising
 ** out of the use of this software and its documentation, even if
 ** Memorial Sloan-Kettering Cancer Center
 ** has been advised of the possibility of such damage.  See
 ** the GNU Lesser General Public License for more details.
 **
 ** You should have received a copy of the GNU Lesser General Public License
 ** along with this library; if not, write to the Free Software Foundation,
 ** Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 **/
package org.cytoscape.coreplugin.psi_mi.test.cyto_mapper;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import junit.framework.TestCase;
import org.cytoscape.coreplugin.psi_mi.util.ContentReader;
import org.cytoscape.coreplugin.psi_mi.cyto_mapper.MapToCytoscape;
import org.cytoscape.coreplugin.psi_mi.cyto_mapper.MapFromCytoscape;
import org.cytoscape.coreplugin.psi_mi.data_mapper.MapPsiOneToInteractions;
import org.cytoscape.coreplugin.psi_mi.model.Interaction;
import org.cytoscape.coreplugin.psi_mi.model.ExternalReference;

import java.util.ArrayList;

/**
 * Tests the MapFromCytoscape Class.
 *
 * @author Ethan Cerami.
 */
public class TestMapFromCytoscape extends TestCase {

    /**
     * Tests the Mapper.
     *
     * @throws Exception All Exceptions.
     */
    public void testMapper1() throws Exception {
        //  First, get some interactions from sample data file.
        ArrayList interactions = new ArrayList();
        ContentReader reader = new ContentReader();
        String xml = reader.retrieveContent("testData/psi_sample1.xml");

        //  Map from PSI --> DataService Interaction Objects.
        MapPsiOneToInteractions mapper1 = new MapPsiOneToInteractions
                (xml, interactions);
        mapper1.doMapping();

        //  Now Map from Data Server --> Cytocape Network Objects.
        CyNetwork cyNetwork = Cytoscape.createNetwork("network1");
        MapToCytoscape mapper2 = new MapToCytoscape
                (interactions, cyNetwork, MapToCytoscape.MATRIX_VIEW);
        mapper2.doMapping();

        //  And, now map back from Cytoscape --> DataService Interaction Objects
        MapFromCytoscape mapper3 = new MapFromCytoscape(cyNetwork);
        mapper3.doMapping();

        //  Verify that we have 6 interactions still
        interactions = mapper3.getInteractions();
        assertEquals(6, interactions.size());

        //  Verify that Sample Interaction with XRefs are mapped over.
        Interaction interaction = (Interaction) interactions.get(0);
        assertEquals("Interaction:   [YCR038C] [YDR532C]",
                interaction.toString());
        ExternalReference refs[] = interaction.getExternalRefs();
        assertEquals(2, refs.length);
    }
}