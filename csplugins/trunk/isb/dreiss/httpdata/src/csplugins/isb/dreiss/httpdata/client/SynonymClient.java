package csplugins.isb.dreiss.httpdata.client;

import java.util.*;
import java.io.IOException;
import org.apache.xmlrpc.*;

import csplugins.isb.dreiss.httpdata.xmlrpc.*;

/**
 * Class SynonymClient automatically generated by
 * csplugins.isb.dreiss.httpdata.xmlrpc.WriteXmlRpcClient based on class
 * SynonymHandler by dreiss (dreiss@systemsbiology.org)
 **/

public class SynonymClient extends AuthenticatedDataClient {
   public SynonymClient( String url ) throws XmlRpcException,
						    java.net.MalformedURLException {
      super( url );
      SERVICE_NAME = "synonym";
   }

   public boolean put( String arg0, String arg1, String arg2 ) throws Exception {
      Object out = call( SERVICE_NAME + ".put", arg0, arg1, arg2 );
      return ( (Boolean) out ).booleanValue();
   }

   public boolean addSynonym( String arg0, String arg1, String arg2 ) throws Exception {
      Object out = call( SERVICE_NAME + ".addSynonym", arg0, arg1, arg2 );
      return ( (Boolean) out ).booleanValue();
   }

   public Vector getSynonyms( String arg0, String arg1 ) throws Exception {
      Object out = call( SERVICE_NAME + ".getSynonyms", arg0, arg1 );
      return (Vector) out;
   }

   public Vector getSynonyms( Vector arg0, String arg1 ) throws Exception {
      Object out = call( SERVICE_NAME + ".getSynonyms", arg0, arg1 );
      return (Vector) out;
   }

   public Vector getSynonyms( Vector arg0, Vector arg1 ) throws Exception {
      Object out = call( SERVICE_NAME + ".getSynonyms", arg0, arg1 );
      return (Vector) out;
   }

   public String getSynonymsString( String arg0, String arg1 ) throws Exception {
      Object out = call( SERVICE_NAME + ".getSynonymsString", arg0, arg1 );
      return (String) out;
   }

   public String getSynonymsString( Vector arg0, String arg1 ) throws Exception {
      Object out = call( SERVICE_NAME + ".getSynonymsString", arg0, arg1 );
      return (String) out;
   }

   public String getSynonymsString( Vector arg0, Vector arg1 ) throws Exception {
      Object out = call( SERVICE_NAME + ".getSynonymsString", arg0, arg1 );
      return (String) out;
   }

   public void test() throws Exception {
      put( "commonName1", "canonicalName1", "species1"/*, true*/ );
      put( "commonName1", "canonicalName2", "species1" );
      //System.out.println( "SYNONYM: " + getSynonym( "commonName1", "species1" ) );
      System.out.println( "SYNONYMS: " + getSynonyms( "commonName1", "species1" ) );
   }

   public static void main( String args[] ) {
      try {
	 SynonymClient client = new SynonymClient( args[ 0 ] );
	 client.test();
      } catch( Exception e ) { e.printStackTrace(); }
   }
}
