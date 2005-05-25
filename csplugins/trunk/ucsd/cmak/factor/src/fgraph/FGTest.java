package fgraph;

import junit.framework.*;
import java.util.*;
import java.io.*;

import giny.model.*;

public class FGTest extends AbstractPathTest
{
    private String outputDir = "testOut";

    private String _testDir = "test-data" + File.separator;
    
    private String _sif = _testDir + "test2.sif";
    private String _dfs = _testDir + "pathtest.sif";
    private String _all = "all.sif";
    private String _allPvals = "/home/cmak/data/data_expts1-300_geneerr_test.pvals";
    private String _allEdge = "/home/cmak/data/all.edgeattr";

    private String _fg = _testDir + "fgtest.sif";
    private String _fgpval = _testDir + "fg.pvals";

    protected InteractionGraph ig;

    protected void setUp() throws Exception
    {
        System.out.println("Reading file: " + _fg);
        ig = InteractionGraphFactory.createFromSif(_fg);
        ig.setExpressionData(CytoscapeExpressionData.load(_fgpval, 1e-2));

        InteractionGraphFactory.loadEdgeData(ig, _testDir + "fgtest.eda");

        System.out.println(ig.toString());
    }

    public void testBuildA() throws Exception
    {
        buildGraph("fgtest_A_facgraph", new int[] {ig.name2Node("A")}, 5);
    }

    public void testBuildF() throws Exception
    {
        buildGraph("fgtest_F_facgraph", new int[] {ig.name2Node("F")}, 5);
    }

    public void testBuildAF() throws Exception
    {
        buildGraph("fgtest_AF_facgraph",
                   new int[] {ig.name2Node("A"), ig.name2Node("F")}, 10);
    }
    
    public void buildGraph(String fname,
                           int[] sources, int expectedPaths) throws Exception
    {

        PathResult r = runPath(ig, sources, 5, expectedPaths);
        System.out.println(r.toString(ig));
        
        PrintableFactorGraph fg = PrintableFactorGraph.createPrintable(ig, r); 
        
        fg.writeSif(outputDir + File.separator + fname);

        fg.writeNodeProbs(System.out);
    }

    /*
    public void testReadAllSif() throws Exception
    {
        
        System.out.println("Reading file: " + _all);
        InteractionGraph ig = InteractionGraphFactory.createFromSif(_all);

        System.out.println("Reading pvals: " + _allPvals);
        ig.loadExpressionData(_allPvals);
        ig.setExpressionPvalThreshold(.8);
        ig.loadEdgeData(_allEdge);
        //System.out.println(ig.toString());

        System.out.println("Running DFS");
        PathResult paths = runPath(ig, ig.name2Node("YGL200C"), 3, 539);
        //runPath(ig, ig.name2Node("YGL200C"), 4, 11532);
        //runPath(ig, ig.name2Node("YGL200C"), 5, 242559);

        FactorGraph fg = new FactorGraph(ig, paths);

        //System.out.println(fg);
        fg.writeSif("fgall_facgraph.sif");
        fg.writeNodeAttr("fgall_facgraph");
        fg.writeNodeProbs(System.out);
    }
    */    

}
