package cytoscape.actions;

import cytoscape.Cytoscape;
import cytoscape.foo.GraphConverter;
import cytoscape.graph.layout.algorithm.MutablePolyEdgeGraphLayout;
import cytoscape.graph.layout.impl.SpringEmbeddedLayouter2;
import cytoscape.task.RunStoppable;
import cytoscape.task.Stoppable;
import cytoscape.task.Task;
import cytoscape.task.TaskMonitor;
import cytoscape.task.ui.ProgressUI;
import cytoscape.task.ui.ProgressUIControl;
import cytoscape.util.CytoscapeAction;
import java.awt.event.ActionEvent;

public class SpringEmbeddedLayoutAction extends CytoscapeAction
{

  public SpringEmbeddedLayoutAction()
  {
    super("Apply Spring Embedded Layout");
    setPreferredMenu("Layout");
  }

  public void actionPerformed(ActionEvent e)
  {
    final MutablePolyEdgeGraphLayout nativeGraph =
      GraphConverter.getGraphCopy(0.0d, false);
    SpringEmbeddedLayouter2 layoutAlg =
      new SpringEmbeddedLayouter2(nativeGraph);

    //////////////////////////////////////////////////////////
    // BEGIN: The thread and process related code starts here.
    //////////////////////////////////////////////////////////

    final RunStoppable runStop = new RunStoppable((Task) layoutAlg);
    final boolean[] stoppd = new boolean[] { false }; // Monitor "Stop" button.
    final ProgressUIControl progCtrl = ProgressUI.startProgress
      (Cytoscape.getDesktop(),
       layoutAlg.getTaskTitle(),
       new Stoppable() {
         public void stop() {
           stoppd[0] = true; // Use this to detect a pushed "Stop" button.
           ((Stoppable) runStop).stop(); } });
    layoutAlg.setTaskMonitor((TaskMonitor) progCtrl);
    Runnable runAndDispose = new Runnable() {
        public void run() {
          runStop.run(); // It's important that we call run() on the
                         // RunStoppable and NOT on the LayoutAlgorithm -
                         // otherwise the RunStoppable won't block on stop().
          // If our LayoutAlgorithm throws a RuntimeException in its run()
          // method, the modal dialog will remain forever.  If we don't want
          // this behavior, we should use 'try' block for run() and
          // 'finally' block for dispose().
          progCtrl.dispose(); } };
    (new Thread(runAndDispose)).start();
    progCtrl.show(); // This blocks until progCtrl.dispose() is called.
    if (stoppd[0]) return; // Return without laying out graph if stopped.

    //////////////////////////////////////////////////////
    // END: The thread and process related code ends here.
    //////////////////////////////////////////////////////

    GraphConverter.updateCytoscapeLayout(nativeGraph);
  }

}
