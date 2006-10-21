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
package org.cytoscape.coreplugin.psi_mi.model;

import org.cytoscape.coreplugin.psi_mi.schema.mi25.CvType;

import java.util.HashMap;

/**
 * Encapsulates a Generic Bag of Attributes.
 *
 * @author Ethan Cerami
 */
public class AttributeBag {
    private HashMap attributes = new HashMap();
    private ExternalReference[] externalRefs;
    private CvType cvType;

    public int getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(int interactionId) {
        //System.out.println("Setting interaction ID:" + interactionId);
        this.interactionId = interactionId;
    }

    private int interactionId;

    public CvType getCvType() {
        return cvType;
    }

    public void setCvType(CvType cvType) {
        this.cvType = cvType;
    }     


    /**
     * Gets Attribute with specified key.
     *
     * @param key Attribute Key.
     * @return Attribute object value.
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * Gets all Attributes.
     *
     * @return HashMap of all attributes.
     */
    public HashMap getAllAttributes() {
        return attributes;
    }

    /**
     * Adds new Attribute.
     *
     * @param name  Attribute key.
     * @param value Object value.
     */
    public void addAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * Gets the External References.
     *
     * @return Array of External Reference objects.
     */
    public ExternalReference[] getExternalRefs() {
        return externalRefs;
    }

    /**
     * Sets the External References.
     *
     * @param externalRefs Array of External Reference objects.
     */
    public void setExternalRefs(ExternalReference[] externalRefs) {
        this.externalRefs = externalRefs;
    }
}