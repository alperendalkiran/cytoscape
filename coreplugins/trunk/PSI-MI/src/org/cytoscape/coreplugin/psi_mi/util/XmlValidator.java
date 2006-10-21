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
package org.cytoscape.coreplugin.psi_mi.util;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * provides XML validation for mi1 documents against schema.
 *
 * @author Robert Sheridan modified from Ethan Cerami
 */
public class XmlValidator extends DefaultHandler {

    /**
     * Default parser name.
     */
    protected static final String DEFAULT_PARSER_NAME =
            "org.apache.xerces.parsers.SAXParser";

    // Validation feature id
    protected static final String VALIDATION_FEATURE_ID =
            "http://xml.org/sax/features/validation";

    //  Schema validation feature id.
    protected static final String SCHEMA_VALIDATION_FEATURE_ID =
            "http://apache.org/xml/features/validation/schema";

    /* Schema full checking feature id
    protected static final String SCHEMA_FULL_CHECKING_FEATURE_ID =
            "http://apache.org/xml/features/validation/schema-full-checking"; */

    // Dynamic validation feature id
    protected static final String DYNAMIC_VALIDATION_FEATURE_ID
            = "http://apache.org/xml/features/validation/dynamic";

    /* Schema location property name
    protected static final String SCHEMA_LOCATION_PROPERTY_NAME
            = "http://apache.org/xml/properties/schema/" +
            "external-schemaLocation"; */

    /* Schema location property value
    protected static final String SCHEMA_LOCATION_PROPERTY_VALUE
            = "http://www.cbio.mskcc.org/cpath/xml/MIF" + "\n"
            + "http://www.cbio.mskcc.org/cpath/xml/MIF.xsd"; */

    /**
     * Warning.
     *
     * @param ex SAXParseException Object.
     * @throws org.xml.sax.SAXException SAXException.
     */
    public void warning(SAXParseException ex) throws SAXException {
        System.out.println(makeErrorMessage("Warning", ex));
    }

    /**
     * Error.
     *
     * @param ex SAXParseException Object.
     * @throws org.xml.sax.SAXException SAXException.
     */
    public void error(SAXParseException ex) throws SAXException {
        throw new SAXException(makeErrorMessage("Error", ex));
    }

    /**
     * Fatal Error.
     *
     * @param ex SAXParseException Object.
     * @throws org.xml.sax.SAXException SAXException.
     */
    public void fatalError(SAXParseException ex) throws SAXException {
        throw new SAXException(makeErrorMessage("Fatal Error", ex));
    }

    /**
     * formats an error message.
     */
    protected String makeErrorMessage(String type, SAXParseException ex) {

        if (ex == null) {
            return "[" + type + "] " + "!!!";
        }
        String systemId = ex.getSystemId();
        if (systemId != null) {
            int index = systemId.lastIndexOf('/');
            if (index != -1) {
                systemId = systemId.substring(index + 1);
            }
        }
        return "[" + type + "] "
                + ':' + ex.getLineNumber() + ':' + ex.getColumnNumber() + ": "
                + ex.getMessage();
    }

    /**
     * validates the content of an XML document according to a schema.
     * Errors or fatal errors cause exceptions to be thrown.
     * Warnings are printed to System.out and then ignored.
     *
     * @param content a string containing the XML content to validate.
     * @throws org.mskcc.dataservices.core.DataServiceException
     *          DataServiceException.
     */
    public static void validate(String content) throws DataServiceException {

        XmlValidator validator = new XmlValidator();
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader
                    (DEFAULT_PARSER_NAME);
            parser.setFeature(VALIDATION_FEATURE_ID, true);
            parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, true);
            /* parser.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID, true); */
            parser.setFeature(DYNAMIC_VALIDATION_FEATURE_ID, true);
            parser.setContentHandler(validator);
            parser.setErrorHandler(validator);
            /* parser.setProperty(SCHEMA_LOCATION_PROPERTY_NAME,
                    SCHEMA_LOCATION_PROPERTY_VALUE); */
            parser.parse(new InputSource(new StringReader(content)));
        } catch (SAXNotRecognizedException e) {
            throw new DataServiceException(e.toString());
        } catch (SAXNotSupportedException e) {
            throw new DataServiceException(e.toString());
        } catch (SAXParseException e) {
            throw new DataServiceException(e.toString());
        } catch (SAXException e) {
            throw new DataServiceException(e.toString());
        } catch (IOException e) {
            throw new DataServiceException(e.toString());
        }
    }

}




