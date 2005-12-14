package cytoscape.render.test;

import cytoscape.geom.rtree.RTree;
import cytoscape.graph.dynamic.DynamicGraph;
import cytoscape.graph.dynamic.util.DynamicGraphFactory;
import cytoscape.render.immed.GraphGraphics;
import cytoscape.render.stateful.EdgeDetails;
import cytoscape.render.stateful.GraphLOD;
import cytoscape.render.stateful.GraphRenderer;
import cytoscape.render.stateful.NodeDetails;
import cytoscape.util.intr.IntHash;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SampleNetwork1
  extends Frame implements MouseListener, MouseMotionListener
{

  public static void main(String[] args) throws Exception
  {
    EventQueue.invokeAndWait(new Runnable() {
        public void run() {
          Frame f = new SampleNetwork1();
          f.show();
          f.addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent e) {
                System.exit(0); } }); } });
  }


  private final int m_imgWidth = 800;
  private final int m_imgHeight = 600;
  private final DynamicGraph m_graph;
  private final RTree m_rtree;
  private final GraphLOD m_lod;
  private final NodeDetails m_nodeDetails;
  private final EdgeDetails m_edgeDetails;
  private final IntHash m_hash;
  private final Image m_img;
  private final GraphGraphics m_grafx;

  private double m_currXCenter = 0.0d;
  private double m_currYCenter = 0.0d;
  private double m_currScale = 1.0d;
  private int m_currMouseButton = 0; // 0: none; 2: middle; 3: right.
  private int m_lastXMousePos = 0;
  private int m_lastYMousePos = 0;

  public SampleNetwork1()
  {
    super();
    m_graph = DynamicGraphFactory.instantiateDynamicGraph();
    m_rtree = new RTree();
    {
      b_net = m_graph.nodeCreate();
      b_bsdi = m_graph.nodeCreate();
      b_sun = m_graph.nodeCreate();
      b_svr4 = m_graph.nodeCreate();
      slip = m_graph.nodeCreate();
      bsdi = m_graph.nodeCreate();
      sun = m_graph.nodeCreate();
      svr4 = m_graph.nodeCreate();
      b_modem = m_graph.nodeCreate();
      t_modem = m_graph.nodeCreate();
      netb = m_graph.nodeCreate();
      t_netb = m_graph.nodeCreate();
      t_net = m_graph.nodeCreate();
      b_aix = m_graph.nodeCreate();
      b_solaris = m_graph.nodeCreate();
      b_gemini = m_graph.nodeCreate();
      b_gateway = m_graph.nodeCreate();
      aix = m_graph.nodeCreate();
      solaris = m_graph.nodeCreate();
      gemini = m_graph.nodeCreate();
      gateway = m_graph.nodeCreate();
      internet = m_graph.nodeCreate();
      m_rtree.insert(b_net, 33, 10, 100, 11);
      m_rtree.insert(b_bsdi, 42.5f, 10, 43.5f, 11);
      m_rtree.insert(b_sun, 66.5f, 10, 67.5f, 11);
      m_rtree.insert(b_svr4, 90.5f, 10, 91.5f, 11);
      m_rtree.insert(slip, 5, 15, 17, 25);
      m_rtree.insert(bsdi, 37, 15, 49, 25);
      m_rtree.insert(sun, 61, 15, 73, 25);
      m_rtree.insert(svr4, 85, 15, 97, 25);
      m_rtree.insert(b_modem, 63, 33, 71, 39);
      m_rtree.insert(t_modem, 63, 50, 71, 56);
      m_rtree.insert(netb, 61, 64, 73, 74);
      m_rtree.insert(t_netb, 66.5f, 78, 67.5f, 79);
      m_rtree.insert(t_net, 0, 78, 92, 79);
      m_rtree.insert(b_aix, 9.5f, 78, 10.5f, 79);
      m_rtree.insert(b_solaris, 33.5f, 78, 34.5f, 79);
      m_rtree.insert(b_gemini, 57.5f, 78, 58.5f, 79);
      m_rtree.insert(b_gateway, 81.5f, 78, 82.5f, 79);
      m_rtree.insert(aix, 4, 83, 16, 93);
      m_rtree.insert(solaris, 28, 83, 40, 93);
      m_rtree.insert(gemini, 52, 83, 64, 93);
      m_rtree.insert(gateway, 76, 83, 88, 93);
      m_rtree.insert(internet, 81.5f, 100, 82.5f, 101);
      e_bsdi = m_graph.edgeCreate(bsdi, b_bsdi, true);
      e_sun = m_graph.edgeCreate(sun, b_sun, true);
      e_svr4 = m_graph.edgeCreate(svr4, b_svr4, true);
      e_slip = m_graph.edgeCreate(slip, bsdi, true);
      e_b_modem = m_graph.edgeCreate(sun, b_modem, true);
      e_dialup = m_graph.edgeCreate(b_modem, t_modem, true);
      e_t_modem = m_graph.edgeCreate(netb, t_modem, true);
      e_netb = m_graph.edgeCreate(netb, t_netb, true);
      e_aix = m_graph.edgeCreate(aix, b_aix, true);
      e_solaris = m_graph.edgeCreate(solaris, b_solaris, true);
      e_gemini = m_graph.edgeCreate(gemini, b_gemini, true);
      e_gateway = m_graph.edgeCreate(gateway, b_gateway, true);
      e_internet = m_graph.edgeCreate(gateway, internet, true);
    }
    m_lod = new GraphLOD();
    m_nodeDetails = new NodeDetails() {
        public Paint fillPaint(int node) {
          if (node == b_net || node == t_net) {
            return Color.black; }
          else {
            return new Color(0, 0, 0, 0); } }
        public float borderWidth(int node) {
          if (node == b_net || node == b_bsdi || node == b_sun ||
              node == b_svr4 || node == t_netb || node == t_net ||
              node == b_aix || node == b_solaris || node == b_gemini ||
              node == b_gateway || node == internet) {
            return 0.0f; }
          else {
            return 0.3f; } }
        public Paint borderPaint(int node) {
          return Color.black; } };
    m_edgeDetails = new EdgeDetails();
    m_hash = new IntHash();
    addNotify();
    m_img = createImage(m_imgWidth, m_imgHeight);
    m_grafx = new GraphGraphics(m_img, true);
    updateImage();
    addMouseListener(this);
    addMouseMotionListener(this);
  }

  final int b_net;
  final int b_bsdi;
  final int b_sun;
  final int b_svr4;
  final int slip;
  final int bsdi;
  final int sun;
  final int svr4;
  final int b_modem;
  final int t_modem;
  final int netb;
  final int t_netb;
  final int t_net;
  final int b_aix;
  final int b_solaris;
  final int b_gemini;
  final int b_gateway;
  final int aix;
  final int solaris;
  final int gemini;
  final int gateway;
  final int internet;
  final int e_bsdi;
  final int e_sun;
  final int e_svr4;
  final int e_slip;
  final int e_b_modem;
  final int e_dialup;
  final int e_t_modem;
  final int e_netb;
  final int e_aix;
  final int e_solaris;
  final int e_gemini;
  final int e_gateway;
  final int e_internet;

  public void paint(Graphics g)
  {
    final Insets insets = insets();
    g.drawImage(m_img, insets.left, insets.top, null);
    resize(m_imgWidth + insets.left + insets.right,
           m_imgHeight + insets.top + insets.bottom);
  }

  public void update(Graphics g)
  {
    final Insets insets = insets();
    updateImage();
    g.drawImage(m_img, insets.left, insets.top, null);
    resize(m_imgWidth + insets.left + insets.right,
           m_imgHeight + insets.top + insets.bottom);
  }

  private void updateImage()
  {
    GraphRenderer.renderGraph(m_graph, m_rtree, m_lod, m_nodeDetails,
                              m_edgeDetails, m_hash, m_grafx, Color.white,
                              m_currXCenter, m_currYCenter, m_currScale);
  }

  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}

  public void mousePressed(MouseEvent e)
  {
    if (e.getButton() == MouseEvent.BUTTON3) {
      m_currMouseButton = 3;
      m_lastXMousePos = e.getX();
      m_lastYMousePos = e.getY(); }
    else if (e.getButton() == MouseEvent.BUTTON2) {
      m_currMouseButton = 2;
      m_lastXMousePos = e.getX();
      m_lastYMousePos = e.getY(); }
  }

  public void mouseReleased(MouseEvent e)
  {
    if (e.getButton() == MouseEvent.BUTTON3) {
      if (m_currMouseButton == 3) m_currMouseButton = 0; }
    else if (e.getButton() == MouseEvent.BUTTON2) {
      if (m_currMouseButton == 2) m_currMouseButton = 0; }
  }

  public void mouseDragged(MouseEvent e)
  {
    if (m_currMouseButton == 3) {
      double deltaX = e.getX() - m_lastXMousePos;
      double deltaY = e.getY() - m_lastYMousePos;
      m_lastXMousePos = e.getX();
      m_lastYMousePos = e.getY();
      m_currXCenter -= deltaX / m_currScale;
      m_currYCenter += deltaY / m_currScale; // y orientations are opposite.
      repaint(); }
    else if (m_currMouseButton == 2) {
      double deltaY = e.getY() - m_lastYMousePos;
      m_lastXMousePos = e.getX();
      m_lastYMousePos = e.getY();
      m_currScale *= Math.pow(2, -deltaY / 300.0d);
      repaint(); }
  }

  public void mouseMoved(MouseEvent e) {}

  public boolean isResizable() { return false; }

}
