package fileloader;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.swing.*;

import cytoscape.*;
import cytoscape.view.*;
import cytoscape.data.readers.*;
import cytoscape.plugin.*;
import cytoscape.data.*;

import giny.model.*;

public class LoaderPlugin extends CytoscapePlugin {

  public LoaderPlugin () {
    initialize();
  }

  protected void initialize () {

    Cytoscape.getDesktop().getCyMenus().getMenuBar().getMenu("File.Load").add( new JMenuItem ( new AbstractAction( "Load Spread Sheet" ) {
        public void actionPerformed ( java.awt.event.ActionEvent e ) {
          // Do this in the GUI Event Dispatch thread...
          SwingUtilities.invokeLater( new Runnable() {
              public void run() {
                FileLoaderUI ui = new FileLoaderUI();
              }
            } ); } } ) );


    
    Cytoscape.getDesktop().getCyMenus().getMenuBar().getMenu("File.Load").add( new JMenuItem ( new AbstractAction( "Load Cytoscape Project" ) {
        public void actionPerformed ( java.awt.event.ActionEvent e ) {
          // Do this in the GUI Event Dispatch thread...
          SwingUtilities.invokeLater( new Runnable() {
              public void run() {
                try {
                  
                  FileInputStream fis = new FileInputStream("/users/xmas/CSBI/cytoscape/temp/outfile.zip");

                  ///////////////
                  // nodes.txt

                  // first get nodes.txt
                  ZipInputStream zis = new  ZipInputStream(new BufferedInputStream(fis));
                  ZipEntry entry;
                  while( (entry = zis.getNextEntry()) != null) {
                    System.out.println("Extracting: " +entry.getName());
                    
                    if ( entry.getName().equals( "nodes.txt") ) {
                    
                      System.out.println("Extracting Only Nodes: " +entry);
                      // nodes.txt found
                      StringBuffer entry_buffer = new StringBuffer();
                      byte[] buf = new byte[1024];
                      int len;
                      while ( (len = zis.read(buf) ) > 0) {
                        entry_buffer.append( new String( buf ) );
                      }

                      // use SpreadSheet loading stuff to load
                    
                      String s = entry_buffer.toString();
                      String[] sa = s.split( "\n" );

                      // get titles
                      Vector titles = new Vector();
                      String[] ta = sa[0].split(";");
                      for ( int i = 0; i < ta.length; ++i )
                        titles.add( ta[i] );
                    

                      // load nodes by row
                      for ( int i = 1; i < sa.length; ++i ) {
                        System.out.println( i+ ".) "+sa[i] );
                        FileLoader.loadRow( sa[i].split(";"), titles, true ); 
                      }
                    
                    
                      break;
                    }
                  }
                  zis.close();
                  
                } catch ( Exception e ) {
                  e.printStackTrace();
                }


                try {
                  ///////////////
                  // edges.txt
                  FileInputStream fis = new FileInputStream("/users/xmas/CSBI/cytoscape/temp/outfile.zip");

                  // first get edges.txt
                  ZipInputStream zis = new  ZipInputStream(new BufferedInputStream(fis));
                  ZipEntry entry;
                  while( (entry = zis.getNextEntry()) != null) {
                    System.out.println("Extracting: " +entry);
                    
                    if ( entry.getName().equals("edges.txt") ) {
                     
                      System.out.println("Extracting Only Edges: " +entry);
                      // edges.txt found
                      StringBuffer entry_buffer = new StringBuffer();
                      byte[] buf = new byte[1024];
                      int len;
                      while ( (len = zis.read(buf) ) > 0) {
                        entry_buffer.append( new String( buf ) );
                      }

                      // use SpreadSheet loading stuff to load
                      String s = entry_buffer.toString();
                      String[] sa = s.split( "\n" );

                      // get titles
                      Vector titles = new Vector();
                      String[] ta = sa[0].split(";");
                      for ( int i = 0; i < ta.length; ++i )
                        titles.add( ta[i] );
                    

                      // load nodes by row
                      for ( int i = 1; i < sa.length; ++i ) {
                        System.out.println( i+ ".) "+sa[i] );
                        FileLoader.loadRow( sa[i].split(";"), titles, false ); 
                      }
                                                          
                      break;
                    }
                  }
                  zis.close();
                  
                } catch ( Exception e ) {
                  e.printStackTrace();
                }

                try {
                  ///////////////
                  // networks..
              
                  FileInputStream fis = new FileInputStream("/users/xmas/CSBI/cytoscape/temp/outfile.zip");

                  ZipInputStream zis = new  ZipInputStream(new BufferedInputStream(fis));
                  ZipEntry entry;
                  while((entry = zis.getNextEntry()) != null) {
                    
                    
                    if ( entry.getName() == "edges.txt" || entry.getName() == "nodes.txt" ) {
                      continue;
                    }

                    System.out.println("Extracting: " +entry);
                    

                    

                    StringBuffer entry_buffer = new StringBuffer();
                    byte[] buf = new byte[1024];
                    int len;
                    while ( (len = zis.read(buf) ) > 0) {
                      entry_buffer.append( new String( buf ) );
                    }

                    GraphReader reader = null;
                    if ( entry.getName().endsWith( "gml" ) ) {
                      // gml file found
                      // pass the whole string to the GMLTree for reading
                      reader = new GMLReader( entry_buffer.toString(), true );
                        // have the GraphReader read the given file
                      
                    } else if ( entry.getName().endsWith( "sif" ) ) {
                      // create the sif file reader
                      reader = new InteractionsReader( Cytoscape.getCytoscapeObj().getBioDataServer(), 
                                                       Semantics.getDefaultSpecies(Cytoscape.getCurrentNetwork(), Cytoscape.getCytoscapeObj() ),
                                                       entry_buffer.toString(), 
                                                       true);
                      
                    } else {
                      continue;
                    }

                    try {
                      reader.read();
                    } catch ( Exception e ) {
                         System.err.println( "Loader plugin unable to load entry: "+entry );
                         e.printStackTrace();
                         continue;
                    }



                    // get the RootGraph indices of the nodes and
                    // edges that were just created
                    int[] nodes = reader.getNodeIndicesArray();
                    int[] edges = reader.getEdgeIndicesArray();
                    
                    if ( nodes == null ) {
                      System.err.println( "reader returned null nodes" );
                    }
                    
                    if ( edges == null ) {
                      System.err.println( "reader returned null edges" );
                    }
                    
                    // Create a new cytoscape.data.CyNetwork from these nodes and edges
                    CyNetwork network = Cytoscape.createNetwork( nodes, edges, entry.getName() );
                    
                    if (  entry.getName().endsWith( "gml" ) ) {
                      network.putClientData( "GML", reader );
                       
                      System.out.println( "LNV: "+Cytoscape.getNetworkView( network.getIdentifier() ) );
                    
                      if ( Cytoscape.getNetworkView( network.getIdentifier() ) != null ) {
                        reader.layout( Cytoscape.getNetworkView( network.getIdentifier() ) );
                      }
                    }
                                  

                  }
                  zis.close();
                  
                } catch ( Exception e ) {
                  e.printStackTrace();
                }
              }
            } ); } } ) );



    Cytoscape.getDesktop().getCyMenus().getMenuBar().getMenu("File.Save").add( new JMenuItem ( new AbstractAction( "Save Cytoscape Project" ) {
        public void actionPerformed ( java.awt.event.ActionEvent e ) {
          // Do this in the GUI Event Dispatch thread...
          SwingUtilities.invokeLater( new Runnable() {
              public void run() {
               
                try {
                  String lineSep = System.getProperty("line.separator");
                  String outFilename = "/users/xmas/CSBI/cytoscape/temp/outfile.zip";
                  ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(outFilename));


                  //////////////
                  // NODES.TXT

                  // first save all of the nodes and their attributes as a tab delimited text file
                  zip.putNextEntry( new ZipEntry("nodes.txt") );
                  List nodes_list = Cytoscape.getRootGraph().nodesList();
                  String[] node_attributes = Cytoscape.getNodeAttributesList();
                
                  StringBuffer buffer = new StringBuffer();
                  for ( int i = 0; i < node_attributes.length; ++i ) {
                    buffer.append( node_attributes[i]+";" );
                  }
                  buffer.append( lineSep );
                  byte[] bytes = buffer.toString().getBytes();
                  zip.write( bytes, 0, bytes.length );

                  for ( Iterator it = nodes_list.iterator(); it.hasNext(); ) {
                    CyNode node = ( CyNode )it.next();
                    buffer = new StringBuffer();
                    for ( int i = 0; i < node_attributes.length; ++i ) {
                      buffer.append( Cytoscape.getNodeAttributeValue( node, node_attributes[i] )+";" );
                    }
                    buffer.append( lineSep );
                    bytes = buffer.toString().getBytes();
                    zip.write( bytes, 0, bytes.length );
                  }
                  zip.closeEntry();

                  //////////////
                  // EDGES.TXT
                
                  // save all of the edges as aif file
                  zip.putNextEntry( new ZipEntry("edges.txt") );
                  List edges_list = Cytoscape.getRootGraph().edgesList();
                  String[] edge_attributes = Cytoscape.getEdgeAttributesList();
                
                  buffer = new StringBuffer();
                  buffer.append( "Source;Interaction;Target;" );
                  for ( int i = 0; i < edge_attributes.length; ++i ) {
                    buffer.append( edge_attributes[i]+";" );
                  }
                  buffer.append( lineSep );
                  bytes = buffer.toString().getBytes();
                  zip.write( bytes, 0, bytes.length );

                  for ( Iterator it = edges_list.iterator(); it.hasNext(); ) {
                    CyEdge edge = ( CyEdge )it.next();
                    buffer = new StringBuffer();
                    buffer.append( edge.getSourceNode().getIdentifier()+";" );
                    buffer.append( Cytoscape.getEdgeAttributeValue( edge, Semantics.INTERACTION )+";" );
                    buffer.append( edge.getTargetNode().getIdentifier()+";" );

                    for ( int i = 0; i < edge_attributes.length; ++i ) {
                      buffer.append( Cytoscape.getEdgeAttributeValue( edge, edge_attributes[i] )+";" );
                    }
                    buffer.append( lineSep );
                    bytes = buffer.toString().getBytes();
                    zip.write( bytes, 0, bytes.length );
                  }
                  zip.closeEntry();


                  ///////////////
                  // Networks 

                  // save each Network with a view as GML file,
                  // and save each Network without a view as a SIF file
                
                  Set networks = Cytoscape.getNetworkSet();
                  for ( Iterator iter = networks.iterator(); iter.hasNext(); ) {
                    String id = ( String )iter.next();
                    CyNetwork network = Cytoscape.getNetwork( id );
                    if ( Cytoscape.getNetworkView( id ) == null ) {
                      // create SIF file from the network
                      zip.putNextEntry( new ZipEntry(network.getTitle()+".sif" ));
                   
                    
                      List nodeList = network.nodesList();
                      Node[] nodes = (Node[]) nodeList.toArray ( new Node[0] );
                      for (int i=0; i < nodes.length; i++) {
                        StringBuffer sb = new StringBuffer ();
                        Node node = nodes[i];
                        String canonicalName = Cytoscape.getNodeAttributeValue( node, Semantics.CANONICAL_NAME ).toString();
                        List edges = network.getAdjacentEdgesList(node, true, true, true); 
                  
                        if (edges.size() == 0) {
                          sb.append(canonicalName + lineSep);
                        } else {
                          Iterator it = edges.iterator();
                          while ( it.hasNext() ) {
                            Edge edge = (Edge)it.next();
                            if (node == edge.getSource()){ //do only for outgoing edges
                              Node target = edge.getTarget();
                              String canonicalTargetName = Cytoscape.getNodeAttributeValue( target, Semantics.CANONICAL_NAME ).toString();
                              String edgeName = Cytoscape.getEdgeAttributeValue( edge, Semantics.CANONICAL_NAME ).toString();
                              String interactionName =  Cytoscape.getEdgeAttributeValue( edge, Semantics.INTERACTION ).toString();

                              if (interactionName == null) {interactionName = "xx";}
                              sb.append(canonicalName);
                              sb.append(";");
                              sb.append(interactionName);
                              sb.append(";");
                              sb.append(canonicalTargetName);
                              sb.append(lineSep);
                            }
                          } 
                        } 
                        bytes = sb.toString().getBytes();
                        zip.write( bytes, 0, bytes.length );
                      }
                    } else {
                      // view exits, create GML from the network view
                      zip.putNextEntry( new ZipEntry(network.getTitle()+".gml") );
                      GMLTree result = new GMLTree( Cytoscape.getNetworkView(id) );
                      bytes = result.toString().getBytes();
                      zip.write( bytes, 0, bytes.length );
                    }
                    zip.closeEntry();
                  }

                  ///////////////
                  // OTHER FILES WILL GO HERE
                
                  // network inheritance tree
                  // filters
                  // properties

                  zip.close();

                } catch ( Exception e ) {
                  e.printStackTrace();
                }
              }
            } ); } } ) );


  }


}
