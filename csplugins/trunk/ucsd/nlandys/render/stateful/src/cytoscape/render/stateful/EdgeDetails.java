package cytoscape.render.stateful;

import cytoscape.render.immed.EdgeAnchors;
import cytoscape.render.immed.GraphGraphics;
import java.awt.Color;

/**
 * Defines the visual properties of an edge.  Even though this class is not
 * declared abstract, in most situations it makes sense to override at least
 * some of its methods (especially thickness()) in order to gain control over
 * edge visual properties.<p>
 * To understand the significance of each method's return value, it makes
 * sense to become familiar with the API cytoscape.render.immed.GraphGraphics.
 */
public class EdgeDetails
{

  public Color colorLowDetail(final int edge) {
    return Color.blue; }

  /**
   * Returns a GraphGraphics.ARROW_* constant; this defines the arrow
   * to use when rendering the edge endpoint touching source node.
   * By default this method returns GraphGraphics.ARROW_NONE.
   * Take note of certain constraints specified in
   * GraphGraphics.drawEdgeFull().
   */
  public byte arrow0(final int edge) {
    return GraphGraphics.ARROW_NONE; }

  /**
   * Returns the size of the arrow at edge endpoint touching source node.
   * By default this method returns zero.  This return value is ignored
   * if arrow0(edge) returns GraphGraphics.ARROW_NONE.
   * Take note of certain constraints specified in
   * GraphGraphics.drawEdgeFull().
   */
  public float arrow0Size(final int edge) {
    return 0.0f; }

  /**
   * Returns the color of the arrow at edge endpoint touching source node.
   * By default this method returns null.  This return value is ignored if
   * arrow0(edge) returns GraphGraphics.ARROW_NONE or
   * GraphGraphics.ARROW_BIDIRECTIONAL; otherwise, it is an error to return
   * null.
   */
  public Color arrow0Color(final int edge) {
    return null; }

  /**
   * Returns a GraphGraphics.ARROW_* constant; this defines the arrow
   * to use when rendering the edge endpoint at the target node.
   * By default this method returns GraphGraphics.ARROW_NONE.
   * Take note of certain constraints specified in
   * GraphGraphics.drawEdgeFull().
   */
  public byte arrow1(final int edge) {
    return GraphGraphics.ARROW_NONE; }

  /**
   * Returns the size of the arrow at edge endpoint touching target node.
   * By default this method returns zero.  This return value is ignored
   * if arrow1(edge) returns GraphGraphics.ARROW_NONE or
   * GraphGraphics.ARROW_MONO.  Take note of certain constraints specified
   * in GraphGraphics.drawEdgeFull().
   */
  public float arrow1Size(final int edge) {
    return 0.0f; }

  /**
   * Returns the color of the arrow at edge endpoint touching target node.
   * By default this method returns null.  This return value is ignored if
   * arrow1(edge) returns GraphGraphics.ARROW_NONE,
   * GraphGraphics.ARROW_BIDIRECTIONAL, or GraphGraphics.ARROW_MONO;
   * otherwise, it is an error to return null.
   */
  public Color arrow1Color(final int edge) {
    return null; }

  /**
   * Returns edge anchors to use when rendering this edge.
   * By default this method returns null; returning null is the optimal
   * way to specify that this edge has no anchors.  Take note of certain
   * constraints, specified in GraphGraphics.drawEdgeFull(), pertaining to
   * edge anchors.
   */
  public EdgeAnchors anchors(final int edge) {
    return null; }

  /**
   * Returns the thickness of the edge segment.
   * <font color="red">By default this method returns zero.</font>
   * Take note of certain constraints specified in
   * GraphGraphics.drawEdgeFull().
   */
  public float thickness(final int edge) {
    return 0.0f; }

  /**
   * Returns the color of the edge segment.
   * By default this method returns Color.blue.  It is an error to
   * return null in this method.
   */
  public Color color(final int edge) {
    return Color.blue; }

  /**
   * Returns the length of dashes on edge segment, or zero to indicate
   * that the edge segment is solid.  By default this method returns zero.
   */
  public float dashLength(final int edge) {
    return 0.0f; }

}
