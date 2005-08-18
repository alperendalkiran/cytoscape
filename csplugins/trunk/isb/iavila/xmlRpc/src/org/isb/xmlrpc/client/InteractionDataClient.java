package org.isb.xmlrpc.client;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.isb.xmlrpc.handler.interactions.InteractionsDataSource;

/**
 * The class that applications use to get interactions from the server
 *
 * @author <a href="mailto:iavila@systemsbiology.org">Iliana Avila-Campillo</a>
 * @version 1.0
 */
public class InteractionDataClient extends AuthenticatedDataClient {
	
	public static final String SERVICE_NAME = "interactions";
	
	/**
	 * Constructor
	 */
	public InteractionDataClient (String server_url) throws XmlRpcException,
    	java.net.MalformedURLException{
		super(server_url);
		this.serviceName = SERVICE_NAME;
	}	
	
	/**
	 * 
	 * @param source_class
	 *            the fully specified class of an InteractionsDataSource to be
	 *            removed
	 * @return true if the InteractionsDataSource with the given class was found
	 *         and removed
	 */
	public boolean removeSource (String source_class) throws XmlRpcException, IOException {
		Object out = call(this.serviceName + ".removeSource", source_class);
		return ((Boolean)out).booleanValue();
	}

	/**
	 * Add a source of interactions
	 * 
	 * @param interaction_source
	 *            fully specified class of the InteractionDataSource to add
	 * @return true if the source was added, false if it was not added (e.g. if
	 *         it was already there or there was an exception during creation)
	 */
	public boolean addSource(String source_class) throws XmlRpcException, IOException{
		Object out = call(this.serviceName + ".addSource", source_class);
		return ((Boolean)out).booleanValue();
	}

	/**
	 * Add a source of interactions
	 * 
	 * @param interaction_source
	 *            fully specified class of the InteractionDataSource to add
	 * @param arg
	 *            an argument to be used to create the InteractionDataSource
	 * @return true if the source was added, false if it was not added (e.g. if
	 *         it was already there or there was an exception during creation)
	 */
	public boolean addSource(String source_class, Object arg) throws XmlRpcException, IOException{
		Object out = call(this.serviceName + ".addSource", source_class, arg);
		return ((Boolean)out).booleanValue();
	}

	/**
	 * Add a source of interactions
	 * 
	 * @param interaction_source
	 *            fully specified class of the InteractionDataSource to add
	 * @param arg1
	 *            an argument to be used to create the InteractionDataSource
	 * @param arg2
	 *            an argument to be used to create the InteractionDataSource
	 * @return true if the source was added, false if it was not added (e.g. if
	 *         it was already there or there was an exception during creation)
	 */
	public boolean addSource(String source_class, Object arg1, Object arg2) throws XmlRpcException, IOException{
		Object out = call(this.serviceName + ".addSource", source_class, arg1, arg2);
		return ((Boolean)out).booleanValue();
	}

	/**
	 * Add a source of interactions
	 * 
	 * @param interaction_source
	 *            fully specified class of the InteractionDataSource to add
	 * @param arg1
	 *            an argument to be used to create the InteractionDataSource
	 * @param arg2
	 *            an argument to be used to create the InteractionDataSource
	 * @param arg3
	 *            an argument to be used to create the InteractionDataSource
	 * @return true if the source was added, false if it was not added (e.g. if
	 *         it was already there or there was an exception during creation)
	 */
	public boolean addSource(String source_class, Object arg1, Object arg2,
			Object arg3) throws XmlRpcException, IOException{
		Object out = call(this.serviceName + ".addSource", source_class, arg1, arg2, arg3);
		return ((Boolean)out).booleanValue();

	}

	/**
	 * @return the fully specified classes of the InteractionDataSources in this
	 *         handler
	 */
	public Vector getSources() throws XmlRpcException, IOException{
		Object out = call(this.serviceName + ".getSources");
		return (Vector)out;
	}

	/**
	 * @param source_class
	 *            the fully specified class of the InteractionsDataSource to
	 *            check
	 * @return true if an InteractionsDataSource with the given class already
	 *         exists, false otherwise
	 */
	public boolean containsSource(String source_class) throws XmlRpcException, IOException{
		Object out = call(this.serviceName + ".containsSource", source_class);
		return ((Boolean)out).booleanValue();
	}

	/**
	 * Calls a method in a class that implements InteractionsDataSource and
	 * returns the answer to this call
	 * 
	 * @param source_class
	 *            the fully specified class of the InteractionsDataSource for
	 *            which the method will be called
	 * @param method_name
	 *            the name of the method
	 * @param args
	 *            the arguments for the method (possibly empty)
	 * @return the returned object by the called method (Java XML-RPC compliant)
	 */
	public Object callSourceMethod(String source_class, String method_name,
			Vector args) throws XmlRpcException, IOException{
		Object out = call(this.serviceName + ".callSourceMethod", source_class, method_name, args);
		return out;
	}
	
	
	/**
	   * @return a Vector of Strings that specify types of IDs that this InteractionsDataSource accepts
	   * for example, "ORF","GI", etc.
	   */
	  public Vector getIDtypes () throws XmlRpcException, IOException{
		  Object out = call( this.serviceName + ".getIDtypes" );
	      return (Vector)out;
	  }
	  
	  //------------------------ get interactions en masse --------------------
	  /**
	   * @param species
	   * @return a Vector of Hashtables, each hash contains information about an
	   * interaction and is required to contain the following entries:<br>
	   * INTERACTOR_1 --> String <br>
	   * INTERACTOR_2 --> String <br>
	   * INTERACTION_TYPE -->String <br>
	   * Each implementing class can add additional entries to the Hashtables
	   */
	  public Vector getAllInteractions (String species) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getAllInteractions", species);
		  return (Vector) out;
	  }
	  
	  /**
	   * @param species
	   * @param args a table of String->Object entries that the implementing
	   * class understands (for example, p-value thresholds, directed interactions, etc)
	   * @return a Vector of Hashtables, each hash contains information about an
	   * interaction and is required to contain the following entries:<br>
	   * INTERACTOR_1 --> String <br>
	   * INTERACTOR_2 --> String <br>
	   * INTERACTION_TYPE -->String <br>
	   * Each implementing class can add additional entries to the Hashtables
	   */
	  public Vector getAllInteractions (String species, Hashtable args) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + "getAllInteractions", species, args);
		  return (Vector)out;
	  }
	  
	  
	  //-------------------------- 1st neighbor methods ---------------------------
	    
	  /**
	   * @param interactor an id that the data source understands
	   * @param species the species
	   * @return a Vector of Strings of all the nodes that
	   * have a direct interaction with "interactor", or an empty vector
	   * if no interactors are found, the interactor is not in the
	   * data source, or, the species is not supported
	   */
	  public Vector getFirstNeighbors (String interactor, String species) throws XmlRpcException, IOException {
		  Object out = call(this.serviceName + ".getFirstNeighbors", interactor, species);
		  return (Vector) out;
	  }
	  
	  /**
	   * @param interactor an id that the data source understands
	   * @param species the species
	   * @param args a table of String->Object entries that the implementing
	   * class understands (for example, p-value thresholds, directed interactions, etc)
	   * @return a Vector of Strings of all the nodes that
	   * have a direct interaction with "interactor" and that take into
	   * account additional parameters given in the Hashtable (an empty
	   * vector if the interactor is not found, the interactor has no interactions,
	   * or the data source does not contain infomation for the given interactor)
	   */
	  public Vector getFirstNeighbors (String interactor, String species, Hashtable args) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getFirstNeighbors", interactor, species, args);
		  return (Vector) out;
	  }


	  /**
	   * @param interactors a Vector of Strings (ids that the data source understands)
	   * @param species the species
	   * @return a Vector of Vectors of String ids of all the nodes that
	   * have a direct interaction with the interactors in the given input vector, positions
	   * in the input and output vectors are matched (parallel vectors)
	   */
	  public Vector getFirstNeighbors (Vector interactors, String species) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getFirstNeighbors", interactors, species);
		  return (Vector) out;
	  }
	  
	  /**
	   * @param interactor a Vector of Strings (ids that the data source understands)
	   * @param species the species
	   * @param args a table of String->Object entries that the implementing
	   * class understands (for example, p-value thresholds, directed interactions, etc)
	   * @return a Vector of Vectors of String ids of all the nodes that
	   * have a direct interaction with the interactors in the given input vector, positions
	   * in the input and output vectors are matched (parallel vectors)
	   */
	  public Vector getFirstNeighbors (Vector interactors, String species, Hashtable args) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getFirstNeighbors", interactors, species, args);
		  return (Vector) out;
	  }

	  /**
	   * @param interactor an id that the data source understands
	   * @param species the species
	   * @return a Vector of Hashtables, each hash contains information about an
	   * interaction (they are required to contain the following entries:)<br>
	   * INTERACTOR_1 --> String <br>
	   * INTERACTOR_2 --> String <br>
	   * INTERACTION_TYPE -->String <br>
	   * Each implementing class can add additional entries to the Hashtables
	   */
	  public Vector getAdjacentInteractions (String interactor, String species) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getAdjacentInteractions", interactor, species);
		  return (Vector) out;
	  }


	  /**
	   * @param interactor an id that the data source understands
	   * @param species the species
	   * @param args a table of String->Object entries that the implementing
	   * class understands (for example, p-value thresholds, directed interactions only, etc)
	   * @return a Vector of Hashtables, each hash contains information about an
	   * interaction (they are required to contain the following entries:)<br>
	   * INTERACTOR_1 --> String <br>
	   * INTERACTOR_2 --> String <br>
	   * INTERACTION_TYPE -->String <br>
	   * Each implementing class can add additional entries to the Hashtables
	   */
	  public Vector getAdjacentInteractions (String interactor, String species, Hashtable args) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getAdjacentInteractions", interactor, species, args);
		  return (Vector) out;
	  }


	    /**
	   * @param interactors a Vector of Strings (ids that the data source understands)
	   * @param species the species
	   * @return a Vector of Vectors of Hashtables, each hash contains information about an
	   * interaction (they are required to contain the following entries:)<br>
	   * INTERACTOR_1 --> String <br>
	   * INTERACTOR_2 --> String <br>
	   * INTERACTION_TYPE -->String <br>
	   * Each implementing class can add additional entries to the Hashtables.<br>
	   * The input and output vectors are parallel.
	   */
	  public Vector getAdjacentInteractions (Vector interactors, String species) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getAdjacentInteractions", interactors, species);
		  return (Vector) out;
	  }


	  /**
	   * @param interactor a Vector of Strings (ids that the data source understands)
	   * @param species the species
	   * @param args a table of String->Object entries that the implementing
	   * class understands (for example, p-value thresholds, directed interactions only, etc)
	   * @return a Vector of Vectors of Hashtables, each hash contains information about an
	   * interaction (they are required to contain the following entries:)<br>
	   * INTERACTOR_1 --> String <br>
	   * INTERACTOR_2 --> String <br>
	   * INTERACTION_TYPE -->String <br>
	   * Each implementing class can add additional entries to the Hashtables.<br>
	   * The input and output vectors are parallel.
	   */
	  public Vector getAdjacentInteractions (Vector interactors, String species, Hashtable args) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getAdjacentInteractions", interactors, species, args);
		  return (Vector) out;
	  }

	  //-------------------------- connecting interactions methods -----------------------

	  /**
	   * @param interactor1
	   * @param interactor2
	   * @param species
	   * @return a Vector of Hashtables, each hash contains information about an
	   * interaction between the two interactors, each hash contains these entries:<br>
	   * INTERACTOR_1 --> String <br>
	   * INTERACTOR_2 --> String <br>
	   * INTERACTION_TYPE -->String <br>
	   * Each implementing class can add additional entries to the Hashtables 
	   */
	  public Vector getConnectingInteractions (String interactor1, String interactor2, String species) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getConnectingInteractions", interactor1, interactor2, species);
		  return (Vector) out;
	  }
	  
	  /**
	   * @param interactor1
	   * @param interactor2
	   * @param species
	   * @param args a table of String->Object entries that the implementing
	   * class understands (for example, p-value thresholds, directed interactions only, etc)
	   * @return a Vector of Hashtables, each hash contains information about an
	   * interaction between the two interactors, each hash contains these entries:<br>
	   * INTERACTOR_1 --> String <br>
	   * INTERACTOR_2 --> String <br>
	   * INTERACTION_TYPE -->String <br>
	   * Each implementing class can add additional entries to the Hashtables 
	   */
	  public Vector getConnectingInteractions (String interactor1, String interactor2, 
	                                           String species, Hashtable args) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getConnectingInteractions", interactor1, interactor2, species, args);
		  return (Vector) out;
	  }
	  /**
	   * @param interactors
	   * @param species
	   * @return a Vector of Hashtables, each hash contains information about an
	   * interaction between the two interactors, each hash contains these entries:<br>
	   * INTERACTOR_1 --> String <br>
	   * INTERACTOR_2 --> String <br>
	   * INTERACTION_TYPE -->String <br>
	   * Each implementing class can add additional entries to the Hashtables 
	   */
	  public Vector getConnectingInteractions (Vector interactors, String species) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getConnectingInteractions", interactors, species);
		  return (Vector) out;
	  }
	  
	  /**
	   * @param interactors
	   * @param species
	   * @param args a table of String->Object entries that the implementing
	   * class understands (for example, p-value thresholds, directed interactions only, etc)
	   * @return a Vector of Hashtables, each hash contains information about an
	   * interaction between the two interactors, each hash contains these entries:<br>
	   * INTERACTOR_1 --> String <br>
	   * INTERACTOR_2 --> String <br>
	   * INTERACTION_TYPE -->String <br>
	   * Each implementing class can add additional entries to the Hashtables 
	   */
	  public Vector getConnectingInteractions (Vector interactors, String species, Hashtable args) throws XmlRpcException, IOException{
		  Object out = call(this.serviceName + ".getConnectingInteractions", interactors, species, args);
		  return (Vector) out;
	  }
	  
	/**
	 * Not implemented in MyDataClient (to be implemented by implementing
	 * classes)
	 */
	public void test() throws Exception{}

}