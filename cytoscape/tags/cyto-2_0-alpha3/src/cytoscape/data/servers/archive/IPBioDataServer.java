// IPBioDataServer.java:  an "in-process" (non-RMI) version of BioDataServer

/** Copyright (c) 2002 Institute for Systems Biology and the Whitehead Institute
 **
 ** This library is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Lesser General Public License as published
 ** by the Free Software Foundation; either version 2.1 of the License, or
 ** any later version.
 ** 
 ** This library is distributed in the hope that it will be useful, but
 ** WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 ** MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 ** documentation provided hereunder is on an "as is" basis, and the
 ** Institute for Systems Biology and the Whitehead Institute 
 ** have no obligations to provide maintenance, support,
 ** updates, enhancements or modifications.  In no event shall the
 ** Institute for Systems Biology and the Whitehead Institute 
 ** be liable to any party for direct, indirect, special,
 ** incidental or consequential damages, including lost profits, arising
 ** out of the use of this software and its documentation, even if the
 ** Institute for Systems Biology and the Whitehead Institute 
 ** have been advised of the possibility of such damage.  See
 ** the GNU Lesser General Public License for more details.
 ** 
 ** You should have received a copy of the GNU Lesser General Public License
 ** along with this library; if not, write to the Free Software Foundation,
 ** Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 **/

//------------------------------------------------------------------------------
// $Revision$   
// $Date$ 
// $Author$
//-----------------------------------------------------------------------------------
package cytoscape.data.servers;
//-----------------------------------------------------------------------------------
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;

import cytoscape.data.*;
import cytoscape.data.readers.*;
//------------------------------------------------------------------------------
/**
 *  the "in-process" BioDataServer, providing data &amp; metadata on biological entities
 */
public class IPBioDataServer implements BioDataServer {

  String dataDirectory;
  Vector bindingPairs = new Vector ();
  Vector geneProducts = new Vector ();

    // gene name synonyms are represented by name pairs, in the GeneSynonym
    // class:  (alterativeName, canonicalName)
    // the full set of synonyms are stored in two different data structures
    // here, to reflect the two very different ways in which this information
    // is used:
    //   1. synonymsByVariant: a hash of (alternativeName, canonicalName) pairs,
    //      useful for quick, single lookups, when you want the canonical name
    //      of a single given gene
    //   2. synonymsByCanonicalName: a hash of (canonicalName, [altName_1, ..., altName_n]),
    //      useful for en masse lookups

  Hashtable synonymsByVariant = new Hashtable ();
  Hashtable synonymsByCanonicalName = new Hashtable ();

    // gene -> go bio process ID
  Hashtable bioProcessIdMap = new Hashtable ();
    // gene -> go molecular function ID
  Hashtable molecularFunctionIdMap = new Hashtable ();
    // gene -> go cellular component ID
  Hashtable cellularComponentIdMap = new Hashtable ();
  Hashtable goTermHash = new Hashtable ();

    // every GO term implicitly specifies an ontological path, from
    // the term up to the roots of the ontology.  there may be more
    // that one path per term.
    // for analytical purposes (for instance, in gw_bio.cc) the
    // whole path may be usefully compressed to a single term
    // at some appropriate level in the path.  the following vector
    // contains all of the 'terminators' currently in use.

  Vector goPathTerminators = new Vector ();

//------------------------------------------------------------------------------
public IPBioDataServer (String directory)
{ 
  dataDirectory = directory;
  load ();
}
//------------------------------------------------------------------------------
public String getServerType ()
{
  return "in-process biodata server";
}
//------------------------------------------------------------------------------
private void load ()
{

  File synonymsFile = new File (dataDirectory, "synonyms.xml");
  File yeastBioProcessesFile = new File (dataDirectory, 
                                     "yeastGenesAndBiologicalProcessIDs.txt");
  File yeastMolFuncsFile = new File (dataDirectory, 
                                     "yeastGenesAndMolecularFunctionIDs.txt");
  File yeastCellularComponentsFile = new File (dataDirectory, 
                                     "yeastGenesAndCellularComponentIDs.txt");
  File goTermsFile = new File (dataDirectory, "goTerms.xml");
  File goPathTerminatorsFile = new File (dataDirectory, "goPathTerminators.txt");
  File humanBioProcessesFile = new File (dataDirectory, "humanBiologicalProcessIDs.txt");
  File humanMolFuncsFile = new File (dataDirectory, "humanMolecularFunctionIDs.txt");
  File humanCellularComponentsFile = new File (dataDirectory, "humanCellularComponentIDs.txt");

  boolean allFilesAreReadable = true;

  if (!synonymsFile.canRead ()) {
    System.err.println ("--- error.  cannot read gene name synonyms from " + synonymsFile);
    allFilesAreReadable = false;
    }   

  if (!goTermsFile.canRead ()) {
    System.err.println ("--- error.  cannot read GO terms from " + goTermsFile);
    allFilesAreReadable = false;
    }   
 
  if (!goPathTerminatorsFile.canRead ()) {
    System.err.println ("--- error.  cannot read GO path terminators from " + 
                        goPathTerminatorsFile);
    }   

  if (!allFilesAreReadable) 
    return;
 
  try {
    loadSynonyms (synonymsFile);
    loadGoTerms (goTermsFile);
    loadGoPathTerminators (goPathTerminatorsFile);

    if (yeastBioProcessesFile.canRead ())
      loadBioProcesses (yeastBioProcessesFile);

    if (yeastMolFuncsFile.canRead ())
      loadMolecularFunctions (yeastMolFuncsFile);

    if (yeastCellularComponentsFile.canRead ())
      loadCellularComponents (yeastCellularComponentsFile);

    if (humanBioProcessesFile.canRead ())
      loadBioProcesses (humanBioProcessesFile);

    if (humanMolFuncsFile.canRead ())
      loadMolecularFunctions (humanMolFuncsFile);

    if (humanCellularComponentsFile.canRead ())
      loadCellularComponents (humanCellularComponentsFile);


    System.out.println (describe ());
    }
  catch (Exception e) {
    e.printStackTrace ();
    }

} // load
//------------------------------------------------------------------------------
private void loadSynonyms (File file) throws Exception
{
  System.out.println ("reading " + file);
  GeneSynonymXmlReader reader = new GeneSynonymXmlReader (file);
  Vector result = reader.read ();
  System.out.println ("loading " + result.size () + " gene synonyms...");
  addGeneSynonyms (result);

} // loadSynonyms
//------------------------------------------------------------------------------
private void loadBioProcesses (File file) throws Exception
{
  System.out.println ("reading " + file);
  GeneAndGoTermTextReader reader = new GeneAndGoTermTextReader (file.getPath ());
  Hashtable hash = reader.read ();
  Enumeration keys = hash.keys ();
  int totalCount = 0;
  while (keys.hasMoreElements ()) {
    String key = (String) keys.nextElement ();
    Vector list = (Vector) hash.get (key);
    totalCount += list.size ();
    }

  addBiologicalProcesses (hash);

}// loadBioProcesses
//------------------------------------------------------------------------------
private void loadMolecularFunctions (File file) throws Exception
{
  System.out.println ("reading " + file);
  GeneAndGoTermTextReader reader = new GeneAndGoTermTextReader (file.getPath ());
  Hashtable hash = reader.read ();

  Enumeration keys = hash.keys ();
  int totalCount = 0;
 
  while (keys.hasMoreElements ()) {
    String key = (String) keys.nextElement ();
    Vector list = (Vector) hash.get (key);
    totalCount += list.size ();
    }

  addMolecularFunctions (hash);

}// loadMolecularFunctions
//------------------------------------------------------------------------------
private void loadCellularComponents (File file) throws Exception
{
  System.out.println ("reading " + file);
  GeneAndGoTermTextReader reader = new GeneAndGoTermTextReader (file.getPath ());
  Hashtable hash = reader.read ();

  Enumeration keys = hash.keys ();
  int totalCount = 0;
 
  while (keys.hasMoreElements ()) {
    String key = (String) keys.nextElement ();
    Vector list = (Vector) hash.get (key);
    totalCount += list.size ();
    }

  addCellularComponents (hash);

} // loadCellularComponents
//------------------------------------------------------------------------------
private void loadGoTerms (File file) throws Exception
{
  GoTermXmlReader reader = new GoTermXmlReader (file);
  Vector result = reader.read ();
  System.out.println ("loading " + result.size () + " GO terms...");
  addGoTerms (result);

} // loadGoTerms
//------------------------------------------------------------------------------
private void loadGoPathTerminators (File file) throws Exception
{
  GoPathTerminatorTextReader reader = new GoPathTerminatorTextReader (file.getPath ());
  Vector result = reader.read ();
  System.out.println ("loading " + result.size () + " gene goPathTerminatorss...");
  addGoPathTerminators (result);

} // loadGoPathTerminators
//------------------------------------------------------------------------------
public void addBindingPairs (Vector newBindingPairs)
// note!  the data structure here is the triple (a, b, species)
// note!  a and b are geneNames which should be canonicalized
{
  System.out.println ("IPBioDataServer.addBindingPairs (" +
                      newBindingPairs.size () + ")");

  for (int i=0; i < newBindingPairs.size (); i++) {
    bindingPairs.addElement (newBindingPairs.elementAt (i));
    }

} // addBindingPairs
//------------------------------------------------------------------------------
public void addGeneProducts (Vector newGeneProducts)
{
  System.out.println ("IPBioDataServer.addGeneProducts (" +
                      newGeneProducts.size () + ")");

  for (int i=0; i < newGeneProducts.size (); i++) {
    geneProducts.addElement (newGeneProducts.elementAt (i));
    }

} // addGeneProducts
//------------------------------------------------------------------------------
public void addGeneSynonyms (Vector newSynonyms)
// fill both synonym data structures at the same time
{
  System.out.println ("IPBioDataServer.addSynonyms (" +
                      newSynonyms.size () + ")");

  for (int i=0; i < newSynonyms.size (); i++) {
    GeneSynonym gs = (GeneSynonym) newSynonyms.elementAt (i);
    String variant = gs.getName ();
    String canonicalName = gs.getValue ();
    synonymsByVariant.put (variant.toUpperCase (), canonicalName);
    Vector syns;
    if (synonymsByCanonicalName.containsKey (canonicalName))
      syns = (Vector) synonymsByCanonicalName.get (canonicalName);
    else
      syns = new Vector ();
    syns.addElement (variant);
    synonymsByCanonicalName.put (canonicalName.toUpperCase(), syns);
    }

} // addGeneSynonyms
//------------------------------------------------------------------------------
/*
 * input is a Vector of GoTerm objects.  use the integer ID of each
 * as the key to store each object in a hash
 *
**/
public void addGoTerms (Vector goTerms)
{
  System.out.println ("IPBioDataServer.addGoTerms (" +
                      goTerms.size () + ")");

  for (int i=0; i < goTerms.size (); i++) {
    GoTerm term = (GoTerm) goTerms.elementAt (i);
    goTermHash.put (new Integer (term.getId ()), term);
    }

} // addGoTerms
//------------------------------------------------------------------------------
/* 
 * input is a hash (on geneName) of vectors of biologicalProcess id's from
 * the GO database.  map the incoming geneName to the canonical name
 * whenever possible.
 *
**/
public void addBiologicalProcesses (Hashtable newHash)
{
  Enumeration geneNames = newHash.keys ();
  while (geneNames.hasMoreElements ()) {
    String geneName = (String) geneNames.nextElement ();
    String canonicalName = getCanonicalName (geneName);
    String newKey = canonicalName;
    if (newKey == null || newKey.length () == 0) {
      newKey = geneName;
      //System.err.println (
      //  "using supplied (but non-canonical) name to store processes for " + newKey);
      }
    Vector list = (Vector) newHash.get (geneName);
    bioProcessIdMap.put (newKey, list);
    } // while

} // addBiologicalProcesses
//------------------------------------------------------------------------------
/* 
 * input is a hash (on geneName) of vectors of molecular function id's from
 * the GO database.  map the incoming geneName to the canonical name
 * whenever possible.
 *
**/

public void addMolecularFunctions (Hashtable newHash)
{
  Enumeration geneNames = newHash.keys ();
  while (geneNames.hasMoreElements ()) {
    String geneName = (String) geneNames.nextElement ();
    String canonicalName = getCanonicalName (geneName);
    String newKey = canonicalName;
    if (newKey == null || newKey.length () == 0) {
      newKey = geneName;
      //System.err.println (
      //  "using supplied (but non-canonical) name to store processes for " + newKey);
      }
    Vector list = (Vector) newHash.get (geneName);
    molecularFunctionIdMap.put (newKey, list);
    } // while

} // addMolecularFunctions
//------------------------------------------------------------------------------
/* 
 * input is a hash (on geneName) of vectors of cellular component id's from
 * the GO database.  map the incoming geneName to the canonical name
 * whenever possible.
 *
**/

public void addCellularComponents (Hashtable newHash)
{
  Enumeration geneNames = newHash.keys ();
  while (geneNames.hasMoreElements ()) {
    String geneName = (String) geneNames.nextElement ();
    String canonicalName = getCanonicalName (geneName);
    String newKey = canonicalName;
    if (newKey == null || newKey.length () == 0) {
      newKey = geneName;
      //System.err.println (
      //  "using supplied (but non-canonical) name to store processes for " + newKey);
      }
    Vector list = (Vector) newHash.get (geneName);
    cellularComponentIdMap.put (newKey, list);
    } // while

} // addMolecularFunctions
//------------------------------------------------------------------------------
public void clearGoPathTerminators ()
{
  goPathTerminators = new Vector ();
 
} // clearGoPathTerminators
//------------------------------------------------------------------------------
public void addGoPathTerminators (Vector newTerminators)
{
  for (int i=0; i < newTerminators.size (); i++)
    goPathTerminators.addElement (newTerminators.elementAt (i));
 
} // addGoPathTerminators
//------------------------------------------------------------------------------
/*
 * GO's biological process ontology is a directed acyclic graph:  any
 * leaf node may have two (or more) parent nodes.  this multiple parentage
 * may apply to a node at any position in the graph: a leaf node, or an
 * internal node.   but -- almost always -- the multiple parentage is
 * of a leaf node.
 *
 * to qualify this further:  multiple parentage interests us (the ISB) right 
 * now (aug 2001)  when it applies to leaf nodes -- the biological process 
 * ontology ids associated with a specific gene.
 *
 * this method retrieves the GoTerm object associated with the given
 * GO id, and checks to see if it has multiple parents.
 *
 * an example of dual parentage:
 *
 *      yeast gene (orf name):  YHR141C
 *   GO biological process ID:  6412
 * GO biological process name:  protein biosynthesis
 * 2 bio process ontology hierarchy fragments ---- 
 *
 *      %protein metabolism and modification ; GO:0006411
 *        %protein biosynthesis ; GO:0006412, GO:0006416, GO:0006453 ; 
 *          synonym:translation % macromolecule biosynthesis ; GO:0009059
 *
 *       %macromolecule biosynthesis ; GO:0009059
 *        %protein biosynthesis ; GO:0006412, GO:0006416, GO:0006453 ; 
 *          synonym:translation % protein metabolism and modification ; GO:0006411


**/
private boolean bioProcessHasMultipleImmediateParents (int id)
{
  Integer ID = new Integer (id);
  GoTerm term = (GoTerm) goTermHash.get (ID);
  return (term.numberOfParentsAndContainers () > 1);

} // bioProcessHasMultipleImmediateParents
//------------------------------------------------------------------------------
/*
**/
private boolean bioProcessListIncludesMultipleParentage (Vector idList)
{
  for (int i=0; i < idList.size (); i++) {
    Integer ID = (Integer) idList.elementAt (i);
    if (bioProcessHasMultipleImmediateParents (ID.intValue ()))
      return true;
    }

  return false;

} // bioProcessListIncludesMultipleParentage
//------------------------------------------------------------------------------
/*
 * return a gene name (a canonical name) which has multiple GO database
 * process id's associated with it.  this will be useful for the unitTest
 * of this class
**/
public String getGeneWithHighestBiologicalProcessCount (boolean multipleParentage)
{
  Enumeration keys = bioProcessIdMap.keys ();
  String bestGene = null; // the one with the longest list of GO process ID's
  int currentMaxIDs = 0;

  while (keys.hasMoreElements ()) {
    String gene = (String) keys.nextElement ();
    Vector idList = (Vector) bioProcessIdMap.get (gene);
    if (idList.size () > currentMaxIDs) {
      boolean hasMultipleParents = bioProcessListIncludesMultipleParentage (idList);
      if ((multipleParentage && hasMultipleParents) ||
          (!multipleParentage && !hasMultipleParents)) {
        currentMaxIDs = idList.size ();
        bestGene = gene;
        } // candidate verified
      } // if:  candidate based on size
    } // while

  return bestGene;

} // getGeneWithHightestBiologicalProcessCount
//------------------------------------------------------------------------------
/*
 * find the first gene with the specified number of GO biological process IDs.
 * insist that at least one leaf node has multiple parents if caller requests.
 * return null on failure
 *
**/
public String getBioProcessTestGene (int numberOfBioProcessesSought, 
                                     boolean multipleParentage)
{
  Enumeration keys = bioProcessIdMap.keys ();

  while (keys.hasMoreElements ()) {
    String gene = (String) keys.nextElement ();
    Vector idList = (Vector) bioProcessIdMap.get (gene);
    if (idList.size () == numberOfBioProcessesSought) {
      boolean hasMultipleParents = bioProcessListIncludesMultipleParentage (idList);
      if ((multipleParentage && hasMultipleParents) ||
          (!multipleParentage && !hasMultipleParents))
        return gene;
      } // if: candidate based on size
    } // while

  return null;

} // getBioProcessTestGene
//------------------------------------------------------------------------------
public String getCanonicalName (String geneName)
{
  String upperCaseGeneName = geneName.toUpperCase ();
  // System.out.println ("---- getting canonical name for " + upperCaseGeneName);

  if (synonymsByVariant.containsKey (upperCaseGeneName))
    return (String) synonymsByVariant.get (upperCaseGeneName);
  else
    return geneName;

} // getCanonicalName
//------------------------------------------------------------------------------
public String [] getSynonyms (String geneName)
{
  Vector tmp = new Vector ();
  String canonicalName = getCanonicalName (geneName);
  String [] result;
  Vector syns = (Vector) synonymsByCanonicalName.get (canonicalName);

  if (syns == null) {
    result = new String [0];
    return result;
    }

  Vector resultVector = new Vector (); //String [syns.size ()];
  for (int i=0; i < syns.size (); i++) {
    String candidate = (String) syns.elementAt (i); 
    boolean actualSynonym = !candidate.equalsIgnoreCase (geneName);
    String paren = ")";
    boolean nonParenthesized = candidate.indexOf (paren) == -1;
    if (actualSynonym && nonParenthesized)
      resultVector.addElement (candidate);
    } // for i

  result = new String [resultVector.size ()];
  for (int i=0; i < resultVector.size (); i++)
    result [i] = (String) resultVector.elementAt (i);

  return result;

} // getSynonyms
//------------------------------------------------------------------------------
public String [] getNamesStartingWith (String pattern)
// input:  <pattern> is the name (or the start of a name) of any gene. it is
//         checked against all names, canonical or synonym
//
// output: an array of the canonical names of all of the genes whose
//         canonicalName or any synonym starts with <pattern>
//
// algorithm: traverse the synonymsByVariant hash -- (variant, canonicalName) pairs --
//         recording every variant for which variant.startsWith (pattern) is true.
//         then find the canonical name of every matched variant.  return
//         a unique'd version of that list
//
// example:  
//       getNamesStartingWith ("gal") discovers the following gal synonyms,
//       and maps them to their canonical names
//
//           GAL83 -> YER027C
//           GAL81 -> YPL248C
//           GAL80 -> YML051W
//           GAL22 -> YNL236W
//           GAL11 -> YOL051W
//           GAL10 -> YBR019C
//           GAL7 -> YBR018C
//           GAL6 -> YNL239W
//           GAL5 -> YMR105C
//           GAL4 -> YPL248C
//           GAL3 -> YDR009W
//           GAL2 -> YLR081W
//           GAL1 -> YBR020W
//
//  note that GAL81 and GAL4 both synonyms for YPL248C; the 'uniquing' step
//  makes sure that only one instance of YPL248C is returned.
//
{
  Vector tmp = new Vector ();
  Enumeration allVariants = synonymsByVariant.keys ();

    // first: get all names (canonical or synonym) which
    // start with <pattern>

  while (allVariants.hasMoreElements ()) {
    String candidate = (String) allVariants.nextElement ();
    if (candidate.toLowerCase().startsWith (pattern.toLowerCase()))
      tmp.addElement (candidate);
    }

    // second: canonicalize all of these names, using a hash
    // as a lazy way to make sure there are no duplicates.

  Hashtable uniqueCanonicalNames = new Hashtable ();
  for (int i=0; i < tmp.size (); i++) {
    String candidate = (String) tmp.elementAt (i);
    String canonicalName = getCanonicalName (candidate);
    uniqueCanonicalNames.put (canonicalName, new Integer (0));
    }

  int count = uniqueCanonicalNames.size ();
  String [] result = new String [count];
  int i=0;
  Enumeration uniqueNames = uniqueCanonicalNames.keys ();
  while (uniqueNames.hasMoreElements ())
    result [i++] = (String) uniqueNames.nextElement ();
 
  return result;

} // getNamesStartingWith
//------------------------------------------------------------------------------
public String getGeneInfo (String geneName)
{
  StringBuffer sb = new StringBuffer ();
  for (int i=0; i < geneProducts.size (); i++) {
    GeneProduct gp = (GeneProduct) geneProducts.elementAt (i);
    String symbol = gp.getSymbol ();
    String canonicalName = getCanonicalName (geneName);
    if (symbol.equalsIgnoreCase (canonicalName)) {
      sb.append (symbol);
      sb.append (":");
      sb.append (gp.getMolecularFunction ());
      sb.append (":");
      sb.append (gp.getBiologicalProcess ());
      sb.append (":");
      sb.append (gp.getCellularComponent ());
      sb.append (":\n");
      return sb.toString ();
      } // matched
    } // for i

  return "";

} // getGeneInfo
//------------------------------------------------------------------------------
public String getBindingPairs (String geneName)
// just as 'addBindingPairs' does not yet use canonical gene names,
// this method does not either.
{
  StringBuffer sb = new StringBuffer ();
  for (int i=0; i < bindingPairs.size (); i++) {
    BindingPair bp = (BindingPair) bindingPairs.elementAt (i);
    String a = bp.getA ();
    String b = bp.getB ();
    if (geneName.equalsIgnoreCase (a)) {
      sb.append (geneName);
      sb.append (" binds to ");
      sb.append (b);
      sb.append ("\n");
      sb.append (getGeneInfo (b));
      sb.append ("\n");
      }
    else if (geneName.equalsIgnoreCase (b)) {
      sb.append (a);
      sb.append (" binds to ");
      sb.append (geneName);
      sb.append ("\n");
      sb.append (getGeneInfo (a));
      sb.append ("\n");
      }
    } // for i

  return sb.toString ();

} // getBindingPairs
//------------------------------------------------------------------------------
public Vector getAllBioProcessPaths (int bioProcessID)
{
  return getAllGoHierarchyPaths (bioProcessID);

} // getAllBioProcessPaths
//------------------------------------------------------------------------------
public Vector getAllMolecularFunctionPaths (int molecularFunctionID)
{
  return getAllGoHierarchyPaths (molecularFunctionID);
}
//------------------------------------------------------------------------------
public Vector getAllCellularComponentPaths (int cellularComponentID)
{
  return getAllGoHierarchyPaths (cellularComponentID);
}
//------------------------------------------------------------------------------
public Vector getAllGoHierarchyPaths (int goTermID)
{
  Vector nestedLists = recursiveGetGoPath (goTermID, new Vector ());
  if (nestedLists.size () == 0)
    return nestedLists;

  FlattenIntVectors flattener = new FlattenIntVectors (nestedLists);
  Vector flattenedList = flattener.getResult ();
  return flattenedList;

} // getAllGoHierarchyPaths
//------------------------------------------------------------------------------
private Vector recursiveGetGoPath (int goTermID, Vector path)
{
  Integer ID = new Integer (goTermID);

  if (goTermHash.containsKey (ID)) {
    GoTerm term = (GoTerm) goTermHash.get (ID);
    int parentCount = term.numberOfParentsAndContainers ();
    if (parentCount == 0) {
      path.addElement (ID);
      return path;
      }
    else if (parentCount == 1) {
      path.addElement (ID);
      int parentID = term.getParentsAndContainers () [0];
      return (recursiveGetGoPath (parentID, path));
      }        
    else { // assume for now:  (parentCunt == 2) 
      path.addElement (ID);
      Vector newPath = new Vector ();
      Vector path1 = (Vector) path.clone ();
      Vector path2 = (Vector) path.clone ();
      int parent1 = term.getParentsAndContainers () [0];
      int parent2 = term.getParentsAndContainers () [1];
      newPath.addElement (recursiveGetGoPath (parent1, path1));
      newPath.addElement (recursiveGetGoPath (parent2, path2));
      return newPath;
      }
   }
  return path;

} // recursiveGetGoPath
//------------------------------------------------------------------------------
/*
 * return the set of genes defined by "all genes in the synonym table with
 * name == value"
 *
**/
public String [] getAllGenes ()
{
  Vector tmp = new Vector ();
  Enumeration keys = synonymsByVariant.keys ();
  while (keys.hasMoreElements ()) {
    String canonicalName = (String) keys.nextElement ();
    String value = (String) synonymsByVariant.get (canonicalName);
    if (canonicalName.equalsIgnoreCase (value))
      tmp.addElement (canonicalName);
    }

  String [] result = new String [tmp.size ()];
  for (int i=0; i < tmp.size (); i++)
    result [i] = (String) tmp.elementAt (i);

  return result;

} // getAllGenes
//------------------------------------------------------------------------------
public String getGoTermName (int id)
{
  GoTerm term = (GoTerm) goTermHash.get (new Integer (id));
  if (term != null)
    return term.getName ();
  else
    return "";

}
//------------------------------------------------------------------------------
public String getMolecularFunctionName (int id)
{
  return getGoTermName (id);   
}
//------------------------------------------------------------------------------
public String getCellularComponentName (int id)
{
  return getGoTermName (id);   
}
//------------------------------------------------------------------------------
public GoTerm getGoBioProcess (int id)
{
  return (GoTerm) goTermHash.get (new Integer (id));
   
}
//------------------------------------------------------------------------------
public String getBioProcessName (int id)
{
  return getGoTermName (id);   
   
}
//------------------------------------------------------------------------------
private int locateStringInArray (String [] array, String target)
{
  for (int i=0; i < array.length; i++)
    if (target.equalsIgnoreCase (array [i]))
      return i;
  
  return -1;

} // locateStringInArray
//------------------------------------------------------------------------------
/*
 * 
**/
public String mapGoPathToSingleNode (Vector path)
{
  int indexOfMatch = 0;  // return the leaf node if no matches are found
  int max = path.size () - 1;
  //System.out.println ("mapping this vector: " + path);
  // System.out.println ("goPathTerminators: " + goPathTerminators);

  for (int i=max; i >= 0; i--) {
    //System.out.println (i + ") contains? " + (Integer) path.elementAt (i));
    if (goPathTerminators.contains (path.elementAt (i)))
      indexOfMatch = i;
    } // for i

  int id = ((Integer) path.elementAt (indexOfMatch)).intValue ();
  //System.out.println ("settled on index: " + indexOfMatch);
  //System.out.println ("id: " + id + ": " + getGoTermName (id));
  return getGoTermName (id);

} // mapGoPathToSingleNode
//------------------------------------------------------------------------------
public String oldMapBioProcessHierarchyToSingleCategory (Vector hierarchy) 
// <hierarchy> is a Vector of Integers, each a node in a path from
// leaf to root.  reduce this to a single String according to the
// current mapping scheme; right now, this is just one category
/*
 * many mapping schemes are possible.  for right now, we will use the
 * one employed by Trey & Vesteinn for their Spring 2001 Science paper,
 * which collapses most detail down to the nine top-level categories.
 *
 * exceptions:  
 *
 *    cell growth and/or maintenance:metabolism:*
 *    cell growth and/or maintenance:*
 *
 * in each of these cases, the '*' term is returned.
 *
 * the top-level categories (august 2001)
 *
 *    %behavior ; GO:0007610
 *    %biological_process unknown ; GO:0000004
 *    %cell communication ; GO:0007154
 *    %cell growth and/or maintenance ; GO:0008151
 *    %death ; GO:0016265
 *    %developmental processes ; GO:0007275
 *    %obsolete ; GO:0008371
 *    %physiological processes ; GO:0007582
 *    %viral life cycle ; GO:0016032
 *
**/
{
  int count = hierarchy.size ();
  String [] categories = new String [count];
  for (int i=0; i < count; i++) {
    int id = ((Integer) hierarchy.elementAt (i)).intValue ();
    categories [i] = getBioProcessName (id);
    // System.out.println ("category [" + i + "]: " + categories [i]);
    }
 
  String result;
  if (count == 0)
    return "Error!";   // TODO: throw IllegalArgumentException;
  else if (count == 1)
    return categories [0];
  else if (count == 2)
    return categories [1];


  final String CGM = "cell growth and/or maintenance";
  int positionOfCGM = locateStringInArray (categories, CGM);
  if (positionOfCGM >= 1) {
    final String M = "metabolism";
    int positionOfM = locateStringInArray (categories, M);
    if (positionOfM >= 1) 
       return categories [positionOfM - 1];
    else
       return categories [positionOfCGM - 1];
    }

    // top level, not CGM    <count-3>
    // biological_process    <count-2>
    // Gene_Ontology         <count-1>
  return categories [count - 3];

  
} // oldMapBioProcessHierarchyToSingleCategory
//------------------------------------------------------------------------------
/**
 *  mapping (i.e., truncating) bioProcess paths will usually eliminate detail, 
 *  and therefor is likely to create duplicate paths.  eliminate them here.
 */
public String [] eliminateDuplicatePaths (String [] paths)
{
  Vector list = new Vector ();
  for (int i=0; i < paths.length; i++)
    list.addElement (paths [i]);

  return eliminateDuplicatePaths (list);
}
//------------------------------------------------------------------------------
/**
 *  overload on argument type
 *
 */
public String [] eliminateDuplicatePaths (Vector paths)
{
  Hashtable hash = new Hashtable ();
  for (int i=0; i < paths.size (); i++) {
    String path = (String) paths.elementAt (i);
    if (!hash.containsKey (path))
      hash.put (path, new Integer (1));
    } // for i

  Enumeration keys = hash.keys ();
  String [] uniquePaths = new String [hash.size ()];
 
  int i=0;
  while (keys.hasMoreElements ()) {
    String path = (String) keys.nextElement ();
    uniquePaths [i++] = path;
    }

  return uniquePaths;

} // eliminateDuplicatePaths
//------------------------------------------------------------------------------
public int [] getBioProcessIDs (String geneName)
{
  int [] result = new int [0];

  String canonicalName = getCanonicalName (geneName);
  //if (canonicalName == null || canonicalName.length () == 0)
  //  canonicalName = geneName;
  if (bioProcessIdMap.containsKey (canonicalName)) {
    Vector list = (Vector) bioProcessIdMap.get (canonicalName);
    if (list != null) {
      result = new int [list.size ()];
      for (int i=0; i < list.size (); i++) {
        Integer id = (Integer) list.elementAt (i);
        result [i] = id.intValue ();
        } // for i
     } // if list
   } // if containsKey

  return result;

} // getBioProcessIDs
//------------------------------------------------------------------------------
public int [] getMolecularFunctionIDs (String geneName)
{
  int [] result = new int [0];

  String canonicalName = getCanonicalName (geneName);
  if (molecularFunctionIdMap.containsKey (canonicalName)) {
    Vector list = (Vector) molecularFunctionIdMap.get (canonicalName);
    if (list != null) {
      result = new int [list.size ()];
      for (int i=0; i < list.size (); i++) {
        Integer id = (Integer) list.elementAt (i);
        result [i] = id.intValue ();
        } // for i
     } // if list
   } // if containsKey

  return result;

} // getMolecularFunctionIDs
//------------------------------------------------------------------------------
public int [] getCellularComponentIDs (String geneName)
{
  int [] result = new int [0];

  String canonicalName = getCanonicalName (geneName);
  if (cellularComponentIdMap.containsKey (canonicalName)) {
    Vector list = (Vector) cellularComponentIdMap.get (canonicalName);
    if (list != null) {
      result = new int [list.size ()];
      for (int i=0; i < list.size (); i++) {
        Integer id = (Integer) list.elementAt (i);
        result [i] = id.intValue ();
        } // for i
     } // if list
   } // if containsKey

  return result;

} // getCellularComponentIDs
//------------------------------------------------------------------------------
public String dumpGeneProducts ()
{
  StringBuffer sb = new StringBuffer ();
  for (int i=0; i < geneProducts.size (); i++) {
    GeneProduct gp = (GeneProduct) geneProducts.elementAt (i);
    String symbol = gp.getSymbol ();
    sb.append (i);
    sb.append (": ");
    sb.append (symbol);
    sb.append ("\n");
    }
      
  return sb.toString ();

} // dumpGeneProducts
//------------------------------------------------------------------------------
public String dumpBindingPairs ()
{
  StringBuffer sb = new StringBuffer ();
  for (int i=0; i < bindingPairs.size (); i++) {
    BindingPair bp = (BindingPair) bindingPairs.elementAt (i);
    sb.append (i);
    sb.append (": ");
    sb.append (bp.getA ());
    sb.append (" -> ");
    sb.append (bp.getB ());
    sb.append ("\n");
    }
      
  return sb.toString ();

} // dumpBindingPairs
//------------------------------------------------------------------------------
public String describe ()
{
  StringBuffer sb = new StringBuffer ();

  sb.append ("                    binding pairs: ");
  sb.append (bindingPairs.size ());
  sb.append ("\n");

  sb.append ("                    gene products: ");
  sb.append (geneProducts.size ());
  sb.append ("\n");

  sb.append ("        gene synonyms, by variant: ");
  sb.append (synonymsByVariant.size ());
  sb.append ("\n");

  sb.append ("                         GO terms: ");
  sb.append (goTermHash.size ());
  sb.append ("\n");

  sb.append ("genes with biological process IDs: ");
  sb.append (bioProcessIdMap.size ());
  sb.append ("\n");

  sb.append ("genes with molecular function IDs: ");
  sb.append (molecularFunctionIdMap.size ());
  sb.append ("\n");

  sb.append ("genes with cellular component IDs: ");
  sb.append (cellularComponentIdMap.size ());
  sb.append ("\n");

  sb.append ("GO path terminators: ");
  sb.append (goPathTerminators.size ());
  sb.append ("\n");

  return sb.toString ();

} // describe
//------------------------------------------------------------------------------
public static void main (String [] args) 
{
   IPBioDataServer server = new IPBioDataServer (args [0]);
}
//------------------------------------------------------------------------------
} // IPBioDataServer



