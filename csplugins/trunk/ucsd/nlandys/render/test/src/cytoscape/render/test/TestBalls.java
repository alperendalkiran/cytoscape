package cytoscape.render.test;

import cytoscape.render.immed.GraphGraphics;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class TestBalls extends Frame
{

  public final static void main(String[] args) throws Exception
  {
    EventQueue.invokeAndWait(new Runnable() {
        public void run() {
          Frame f = new TestBalls();
          f.show();
          f.addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent e) {
                System.exit(0); } }); } });
  }

  private final int m_imgWidth = 600;
  private final int m_imgHeight = 480;
  private final Image m_img;
  private final GraphGraphics m_grafx;

  public TestBalls()
  {
    super();
    addNotify();
    m_img = createImage(m_imgWidth, m_imgHeight);
    m_grafx = new GraphGraphics(m_img, Color.white, true);
    generateImage();
  }

  public void paint(final Graphics g)
  {
    final Insets insets = insets();
    g.drawImage(m_img, insets.left, insets.top, null);
    resize(m_imgWidth + insets.left + insets.right,
           m_imgHeight + insets.top + insets.bottom);
  }

  private final void generateImage()
  {
    m_grafx.clear(0.0d, 0.0d, 1.0d);
    float xMin = -300.0f;
    float yMin = -240.0f;
    float xMax = -100.0f;
    float yMax = -130.0f;
    float border = 10.0f;
    m_grafx.drawNodeFull(GraphGraphics.SHAPE_ELLIPSE,
                         xMin, yMin, xMax, yMax,
                         Color.red, border, Color.black); 
    final float[] xsectCoords = new float[2];
    float offset = 20.0f;
    float ptX = 200.0f;
    float ptY = 100.0f;
    float edgeThickness = 3.0f;
    float dashLength = 0.0f;
    if (m_grafx.computeEdgeIntersection
        (GraphGraphics.SHAPE_ELLIPSE, xMin, yMin, xMax, yMax, offset,
         ptX, ptY, xsectCoords)) {
      m_grafx.drawEdgeFull(GraphGraphics.ARROW_NONE, 0.0f, null,
                           GraphGraphics.ARROW_DISC, offset * 2.0f, Color.blue,
                           ptX, ptY, xsectCoords[0], xsectCoords[1],
                           edgeThickness, Color.green, dashLength); }
    ptX = 200.0f;
    ptY = -50.0f;
    float deltaSize = offset * 2.0f;
    offset = 0.0f;
    if (m_grafx.computeEdgeIntersection
        (GraphGraphics.SHAPE_ELLIPSE, xMin, yMin, xMax, yMax, offset,
         ptX, ptY, xsectCoords)) {
      m_grafx.drawEdgeFull(GraphGraphics.ARROW_NONE, 0.0f, null,
                           GraphGraphics.ARROW_DELTA, deltaSize, Color.orange,
                           ptX, ptY, xsectCoords[0], xsectCoords[1],
                           edgeThickness, Color.green, dashLength); }
  }

  public boolean isResizable() { return false; }

}
