//-------------------------------------------------------------------------
// $Revision$
// $Date$
// $Author$
//-------------------------------------------------------------------------


package csplugins.mdnodes;

import edu.umd.cs.piccolo.*;
import edu.umd.cs.piccolo.activities.*;
import edu.umd.cs.piccolo.event.*;
import edu.umd.cs.piccolo.nodes.*;
import edu.umd.cs.piccolo.util.*;
import edu.umd.cs.piccolox.*;
import edu.umd.cs.piccolox.handles.*;
import edu.umd.cs.piccolox.nodes.*;
import edu.umd.cs.piccolox.util.*;

import giny.model.*;

import giny.view.*;
import phoebe.util.*;
import  phoebe.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.beans.*;

import java.io.*;

import java.util.*;

import javax.swing.event.*;


public class GridNode extends PPath
  implements NodeView,
             PropertyChangeListener {

  /**
   * The index of this node in the RootGraph
   * note that this is always a negative number.
   */
  protected int rootGraphIndex;

  /**
   * The View to which we belong.
   */
  protected PGraphView view;
  
  /**
   * Our label 
   * TODO: more extendable
   */
  protected PLabel label;

  /**
   * Our Selection toggle
   */
  protected boolean selected;

  /**
   * Our Visibility
   */
  protected boolean visible;

  /**
   * A boolean that tells us if we are updated to the current 
   * position, i.e. after a layout
   */
  protected boolean notUpdated;
    protected int size = 6;
  double scale = 10; 		     
  protected Vector data = new Vector ();
  protected Vector lamda;
  protected float[] values = new float[size];
  protected String[] conditions;
  float[] X = new float [size];
  float[] Y = new float [size];
  
  protected Map wedgeMap;

  //----------------------------------------//
  // Constructors and Initialization
  //----------------------------------------//
  
  /**
*/
  public GridNode ( int node_index, PGraphView view, Vector data, Vector lamda, String[] conditions ) {
    this( node_index, view );
     NodeView node = view.getNodeView(getIndex());
    
    //label = new PLabel ( l, this);
   
    label = new PLabel ( node.getLabel().getText() , this);
    this.data = data;
    this.lamda = lamda;
    this.conditions = conditions;
    MDNodePlugin.colorInterpolatorNegative.addPropertyChangeListener( this );
    MDNodePlugin.colorInterpolatorPositive.addPropertyChangeListener( this );
    wedgeMap = new HashMap();
    // set up the node
    drawNodeView();
  }

  public GridNode ( int        node_index,
                     PGraphView view ) {
    this ( node_index,
           view,
           Double.MAX_VALUE,
           Double.MAX_VALUE,
           Integer.MAX_VALUE,
           ( Paint )null,
           ( Paint )null,
           ( Paint )null,
           Float.MAX_VALUE,
           Double.MAX_VALUE,
           Double.MAX_VALUE,
           ( String )null );
  }

  /**
   * Create a new GridNode with the given physical attributes.
   * @param node_index The RootGraph Index of this node 
   * @param view the PGraphVIew that we belong to
   * @param x_positon the x_positon desired for this node
   * @param y_positon the y_positon desired for this node
   * @param shape the shape type
   * @param paint the Paint for this node
   * @param selection_paint the Paint when this node is selected
   * @param border_paint the boder Paint
   * @param border_width the width of the border
   * @param width the width of the node
   * @param height the height of the node
   * @param label the String to display on the label
   */
  public GridNode ( int        node_index,
                     PGraphView view,
                     double     x_positon,
                     double     y_positon,
                     int        shape,
                     Paint      paint,
                     Paint      selection_paint,
                     Paint      border_paint,
                     float      border_width,
                     double     width,
                     double     height,
                     String     label ) {
    // Call PNode Super Constructor
    super();

    // Set the PGraphView that we belong to
    if ( view == null ) {
      throw new IllegalArgumentException( "A GridNode must belong to a PGraphView" );
    }
    this.view = view;

    // Set the Index
    if ( node_index == Integer.MAX_VALUE ) {
    throw new IllegalArgumentException( "A node_index must be passed to create a GridNode" ); }
    
    if ( node_index >= 0 ) {
      this.rootGraphIndex = view.getGraphPerspective().getRootGraphNodeIndex( node_index );
    } else {
      this.rootGraphIndex = node_index;
    }
        
    
    // set NODE_X_POSITION
    if ( x_positon != Double.MAX_VALUE ) {
      view.setNodeDoubleProperty( rootGraphIndex,
                                  PGraphView.NODE_X_POSITION,
                                  x_positon );
    }
    
    // set NODE_Y_POSITION
    if ( y_positon != Double.MAX_VALUE ) {
      view.setNodeDoubleProperty( rootGraphIndex,
                                  PGraphView.NODE_Y_POSITION,
                                  y_positon );
    }
    
    // set NODE_SHAPE
    if ( shape != Integer.MAX_VALUE ) {
    view.setNodeIntProperty( rootGraphIndex,
                             PGraphView.NODE_SHAPE,
                             shape );
    }
    
    // set NODE_PAINT
    if ( paint != null ) {
      view.setNodeObjectProperty( rootGraphIndex,
                                  PGraphView.NODE_PAINT,
                                  paint );
    }
    
    // set NODE_SELECTION_PAINT
    if ( paint != null ) {
      view.setNodeObjectProperty( rootGraphIndex,
                                  PGraphView.NODE_SELECTION_PAINT,
                                  selection_paint );
    }
    
    // set NODE_BORDER_PAINT
    if ( border_paint != null ) {
      view.setNodeObjectProperty( rootGraphIndex,
                                  PGraphView.NODE_BORDER_PAINT,
                                  border_paint );
    }
    
    // set NODE_BORDER_WIDTH
    if ( border_width != Float.MAX_VALUE ) {
      view.setNodeFloatProperty( rootGraphIndex,
                                 PGraphView.NODE_BORDER_WIDTH,
                                 border_width );
    }
    
    // set NODE_WIDTH
    if ( width != Double.MAX_VALUE ) {
      view.setNodeDoubleProperty( rootGraphIndex,
                                  PGraphView.NODE_WIDTH,
                                  width );
    }
    
    // set NODE_HEIGHT
    if ( height != Double.MAX_VALUE ) {
      view.setNodeDoubleProperty ( rootGraphIndex,
                                   PGraphView.NODE_HEIGHT,
                                   height );
    }
    
    // set NODE_LABEL
    if ( label != null ) {
      view.setNodeObjectProperty( rootGraphIndex,
                                  PGraphView.NODE_LABEL,
                                  label );
    }

    //initializeNodeView();

  }
  
  
  public void drawNodeView() {
	  
	   //label = new PLabel( "Grid Node Example", this );
    RootGraph graph = view.getRootGraph();
     Node nd = graph.getNode(getIndex());
     String l = nd.getIdentifier();
     label = new PLabel ( l, this);
    label.updatePosition();
    label.setPickable(false);
    label.setLabelLocation( PLabel.NORTH );
    

    // set the Node Position
    setOffset( view.getNodeDoubleProperty( rootGraphIndex, PGraphView.NODE_X_POSITION ),
               view.getNodeDoubleProperty( rootGraphIndex, PGraphView.NODE_Y_POSITION ) );
   
    // set the Stroke
    setStroke( (new BasicStroke( view.getNodeFloatProperty( rootGraphIndex, PGraphView.NODE_BORDER_WIDTH ) ) ) );
    size = data.size();
    
    setBounds( 0, 0, 25, size*10 );
   
    PNode rect = new PNode();
    rect.setBounds( -5, -5, 25 + 5, size*10 + 10 );
    rect.setPaint( java.awt.Color.white );
    addChild( rect );
    values = new float[size];
    
    double expression;
    int lamdaValue;

   
    
    for (  int n = 0; n < size; n++  ) {
      for ( int j = 0; j < 1; j ++ ) {//for one time cource for now
        expression = ( ( Double )data.get( n ) ).doubleValue();
        lamdaValue = ( ( Double )lamda.get( n ) ).intValue();
        expression = expression * 100;
        
        PNode node = createGridPart( "Condition:  " +conditions[n] + '\n'+ "Timecource: " + j + '\n' +"value: " + ( expression/100 )+ '\n' +"lamda: " + lamdaValue , lamdaValue, expression );
        addChild( node );
        node.offset( 0, n*10 );
      }
    }

    addChild( label );


    
    PLocator locator = new PLocator () {
        
        public double locateX () {
          return ( getX() + getWidth() * .5 );
        }

        public double locateY () {
          return ( getY() + getHeight() * .5 );
        }

        public Point2D locatePoint () {
          return new Point2D.Double( locateX(), locateY() );
        }
      };



    final PHandle h = new PHandle( locator ) {
                                              
        public void dragHandle(PDimension aLocalDimension, PInputEvent aEvent) {
				localToParent(aLocalDimension);
				getParent().translate(aLocalDimension.getWidth(), aLocalDimension.getHeight());
				updateOffset();
			}			
		};
		
		h.addInputEventListener(new PBasicInputEventHandler() {
			public void mousePressed(PInputEvent aEvent) {
				h.setPaint(Color.YELLOW);
			}
			
			public void mouseReleased(PInputEvent aEvent) {
				h.setPaint(Color.WHITE);
			}
		});

    this.addChild( h );
    h.setParent( this );


    // TODO: Remove?
    this.visible = true;
    this.selected = false;
    this.notUpdated = false;
    setPickable(true);
    invalidatePaint();
  }

  protected void initializeNodeView () {

    
    //label = new PLabel( "Grid Node Example", this );
    RootGraph graph = view.getRootGraph();
     Node n = graph.getNode(getIndex());
     String l = n.getIdentifier();
     label = new PLabel ( l, this);
    label.updatePosition();
    label.setPickable(false);
    label.setLabelLocation( PLabel.NORTH );
    

    // set the Node Position
    setOffset( view.getNodeDoubleProperty( rootGraphIndex, PGraphView.NODE_X_POSITION ),
               view.getNodeDoubleProperty( rootGraphIndex, PGraphView.NODE_Y_POSITION ) );
   
    // set the Stroke
    setStroke( (new BasicStroke( view.getNodeFloatProperty( rootGraphIndex, PGraphView.NODE_BORDER_WIDTH ) ) ) );


    setBounds( 0, 0, 100, 100 );
   
    PNode rect = new PNode();
    rect.setBounds( -5, -5, 105, 105 );
    rect.setPaint( java.awt.Color.white );
    addChild( rect );

    
    for ( float i = 0; i< 100; i += 25 ) {
      for ( float j = 0; j < 100; j += 25 ) {
        double value;
        //if ( i > 50 && j < 50 ) {
        //  value = -1;
        //} else {
        //  value = 1;
        //}
        value = (double)i + (double)j;

        PNode node = createGridPart( "X: "+i+" Y: "+j, value, value );
        addChild( node );
        node.offset( i, j );
      }
    }

    addChild( label );


    
    PLocator locator = new PLocator () {
        
        public double locateX () {
          return ( getX() + getWidth() * .5 );
        }

        public double locateY () {
          return ( getY() + getHeight() * .5 );
        }

        public Point2D locatePoint () {
          return new Point2D.Double( locateX(), locateY() );
        }
      };



    final PHandle h = new PHandle( locator ) {
                                              
        public void dragHandle(PDimension aLocalDimension, PInputEvent aEvent) {
				localToParent(aLocalDimension);
				getParent().translate(aLocalDimension.getWidth(), aLocalDimension.getHeight());
				updateOffset();
			}			
		};
		
		h.addInputEventListener(new PBasicInputEventHandler() {
			public void mousePressed(PInputEvent aEvent) {
				h.setPaint(Color.YELLOW);
			}
			
			public void mouseReleased(PInputEvent aEvent) {
				h.setPaint(Color.WHITE);
			}
		});

    this.addChild( h );
    h.setParent( this );


    // TODO: Remove?
    this.visible = true;
    this.selected = false;
    this.notUpdated = false;
    setPickable(true);
    invalidatePaint();

  }

  public PNode createGridPart ( String tooltip, double value, double expression ) {
    PNode node = new PNode();
    node.setBounds( 0, 0, 20, 10 );
    if ( expression >= 0 ) {
      node.setPaint( MDNodePlugin.colorInterpolatorPositive.colorFromValue(Math.abs(expression)));
    } else {
      node.setPaint( MDNodePlugin.colorInterpolatorNegative.colorFromValue(Math.abs(expression)));
    }
    //node.setPaint( phoebe.util.ColorInterpolator.colorFromValue( 0, java.awt.Color.red, 75, java.awt.Color.black, 150, java.awt.Color.blue, value ) );

    node.addClientProperty( "tooltip", tooltip );
    return node;
  }
                                                   
  /**
   * @param stroke the new stroke for the border
   */
  public void setBorder ( Stroke stroke ) {
    super.setStroke( stroke );
  }


  /** 
   * @return the current border
   */
  public Stroke getBorder () {
    return super.getStroke();
  }

public int getRootGraphIndex() {
	  return rootGraphIndex;
  }

  public int getIndex () {
    return rootGraphIndex;
  }


  /**
   * @return the view we are in
   */
  public GraphView getGraphView() {
    return view;
  }

  /**
   * @return The Node we are a view on
   */
  public Node getNode () {
    return view.getGraphPerspective().getNode( rootGraphIndex );
  }

  /**
   * @return the index of this node in the perspective to which we are in a view on.
   */
  public int getGraphPerspectiveIndex () {
    return view.getGraphPerspective().getNodeIndex( rootGraphIndex );
  }

  /**
   * @return The list of EdgeViews connecting these two nodes. Possibly null.
   */
  public java.util.List getEdgeViewsList(NodeView otherNode) {
    return view.getEdgeViewsList( getNode(), otherNode.getNode() );
  }

  //------------------------------------------------------//
  // Get and Set Methods for all Common Viewable Elements
  //------------------------------------------------------//

  /**
   * Shape is currently defined via predefined variables in 
   * the NodeView interface. To get the actual java.awt.Shape
   * use getPathReference()
   * @return the current int-tpye shape
   */
  public int getShape () {
    return view.getNodeIntProperty( rootGraphIndex,
                                    PGraphView.NODE_SHAPE );
  }
    /**
   * Shape is currently defined via predefined variables in 
   * the NodeView interface. To get the actual java.awt.Shape
   * use getPathReference()
   * @return the current int-tpye shape
   */
  public int getShapeType () {
    return view.getNodeIntProperty( rootGraphIndex,
                                    PGraphView.NODE_SHAPE );
  }
  
  
  /**
   * This sets the Paint that will be used by this node
   * when it is painted as selected.
   * @param paint The Paint to be used
   */
  public void setSelectedPaint (Paint paint) {
    view.setNodeObjectProperty( rootGraphIndex,
                                PGraphView.NODE_SELECTION_PAINT,
                                paint );
  }

  /**
   * @return the currently set selection Paint
   */
  public Paint getSelectedPaint () {
    return ( Paint )view.getNodeObjectProperty( rootGraphIndex,
                                                PGraphView.NODE_SELECTION_PAINT );
  }

  /**
   * Set the deafult paint of this node
   * @param paint the default Paint of this node
   */
  public void setUnselectedPaint ( Paint paint ) { 
    view.setNodeObjectProperty( rootGraphIndex,
                                PGraphView.NODE_PAINT,
                                paint );
  
    super.setPaint( paint );
  }

  /**
   * @return the currently set paint
   */
  public Paint getUnselectedPaint () {
    return ( Paint )view.getNodeObjectProperty( rootGraphIndex,
                                                PGraphView.NODE_PAINT );
  }

  /**
   * @param b_paint the paint the border will use
   */ 
  public void setBorderPaint ( Paint b_paint ) { 
    view.setNodeObjectProperty( rootGraphIndex,
                                PGraphView.NODE_BORDER_PAINT,
                                b_paint );
    super.setStrokePaint( b_paint );
  }
  
  /**
   * @return the currently set BOrder Paint
   */
  public Paint getBorderPaint () {
    return ( Paint )view.getNodeObjectProperty( rootGraphIndex,
                                                PGraphView.NODE_BORDER_PAINT );
  }

  /**
   * @param border_width The width of the border.
   */
  public void setBorderWidth ( float border_width ) {
    view.setNodeFloatProperty( rootGraphIndex,
                               PGraphView.NODE_BORDER_WIDTH,
                               border_width );
    super.setStroke( new BasicStroke( border_width ) );
  }

  /**
   * @return the currently set Border width
   */
  public float getBorderWidth () {
    return view.getNodeFloatProperty( rootGraphIndex,
                                      PGraphView.NODE_BORDER_WIDTH );
  }

  /**
   * TODO: Reconcile with Border Methods
   * @param width the currently set width of this node
   */
  public boolean setWidth ( double width ) {
    view.setNodeDoubleProperty( rootGraphIndex,
                               PGraphView.NODE_WIDTH,
                               width );
			       return true;
  }

  /**
   * TODO: Reconcile with Border Methods
   * @return the currently set width of this node
   */
  public double getWidth () {
    return super.getWidth();
  }

  /**
   * TODO: Reconcile with Border Methods
   * @param height the currently set height of this node
   */
  public boolean setHeight ( double height ) {
    view.setNodeDoubleProperty( rootGraphIndex,
                               PGraphView.NODE_HEIGHT,
                               height );
			       return true;
  }

  /**
   * TODO: Reconcile with Border Methods
   * @return the currently set height of this node
   */
  public double getHeight () {
    return super.getHeight();
  }

  /**
   * @param label the new value to be displayed by the Label
   */
  public void setLabel ( String label ) {
    view.setNodeObjectProperty( rootGraphIndex,
                                PGraphView.NODE_LABEL,
                                label );
    if( label != null ) {
	 this.label.setText( label );
    }
  
  
  }

  /**
   * @return The Value of the label
   */
  public giny.view.Label getLabel () {
	  return (giny.view.Label)label;
   // return ( String )view.getNodeObjectProperty( rootGraphIndex,
     //                                            PGraphView.NODE_LABEL);
  }
  public void moveBy(double x, double y)
  {
  }
  public void setCenter ( double x, double y)
{
}
public void setLocation (double x, double y)
  {
  }
  public void setSize (double x, double y )
  {
  }
  public void setSloppySelectionColor(Color c) {
  }
  
   /**
   * @return The Value of the label
   */
  public String getLabelText () {
    return ( String )view.getNodeObjectProperty( rootGraphIndex,
                                                 PGraphView.NODE_LABEL);
  }


  /**
   * @return the degree of the Node in the GraphPerspective.
   */
  public int getDegree() {
    return view.getGraphPerspective().getDegree(getNode());
  }

  /**
   * public void actionPerformed(ActionEvent e) { if (
   * view.getSelectionHandler().isSelected( this ) ) {
   * view.getSelectionHandler().unselect( this ); } else {
   * view.getSelectionHandler().select( this ); }}
   */
  /**
   * We want to be able to hear when a Node changes a property, like  its
   * identifier or selected state. If the identifier changes the Label
   * chages to the new value If selection changes, this NodeView draws
   * itself as selected.
   */
  public void propertyChange(PropertyChangeEvent evt) {
  
  }

  
   public void updateOffset()
  {
	  firePropertyChange("Offset", null, this);
  }
  /**
   * Set the location of this node
   */
  public void setOffset(
                        double x,
                        double y) {
    //Point2D oldOffset = getOffset();
    super.setOffset(x, y);
    firePropertyChange("Offset", null, this);
  }

  /**
   *  Set the location of this node
   */
  public void setOffset(java.awt.geom.Point2D point) {
    //Point2D oldOffset = getOffset();
    super.setOffset(point);
    firePropertyChange("Offset", null, this);
  }

  /**
   * Move this node relative to its current location
   */
  public void offset(
                     double dx,
                     double dy) {
    //Point2D oldOffset = getOffset();
    super.offset(dx, dy);
    firePropertyChange("Offset", null, this);
  }

 
  /**
   * @param the new_x_position for this node
   */
  public void setXPosition(double new_x_position) {
    setXPosition( new_x_position, true );
  }
  /**
   * @param new label for this node
   */
  public void setLabelText(String label) {
   view.setNodeObjectProperty( rootGraphIndex,
                                PGraphView.NODE_LABEL,
                                label );
    if( label != null ) {
	 this.label.setText( label );
    }
  }


  /**
   * Set udpdate to false in order to do a layout, and then call updateNode on all the nodes..
   // TODO -- HACKY
   * @param  new_x_position for this node
   * @param  update if this is true, the node will move immediatly. 
  */
  public void setXPosition ( double new_x_position, boolean update ) {
    view.setNodeDoubleProperty( rootGraphIndex,
                                PGraphView.NODE_X_POSITION,
                                new_x_position );
    if ( update ) {
      setNodePosition( false );
    } else {
      notUpdated = true;
    }
  }

  /**
   * @return the current x position of this node
   * @see setXPosition
   * note that unless updateNode() has been called, this may not be 
   * the "real" location of this node
   */
  public double getXPosition() {
    
    // Note that this is rather sneaky, hehe.
    // The reason is that to allow for nodes to be moved around by the 
    // mouse using common Piccolo methods, like the PSelectionEventHandler
    // it is necessary to return where the node was moved to

    if (notUpdated) {
      return view.getNodeDoubleProperty( rootGraphIndex,
                                         PGraphView.NODE_X_POSITION );
    } else {
      return getOffset().getX();
    }
  }

  /**
   * @param the new_y_position for this node
   */
  public void setYPosition(double new_y_position) {
    setYPosition( new_y_position, true );
  }

  /**
   * Set udpdate to false in order to do a layout, and then call updateNode on all the nodes..
   // TODO -- HACKY
   * @param  new_y_position for this node
   * @param  update if this is true, the node will move immediatly. 
  */
  public void setYPosition ( double new_y_position, boolean update ) {
    view.setNodeDoubleProperty( rootGraphIndex,
                                PGraphView.NODE_Y_POSITION,
                                new_y_position );
    if ( update ) {
      setNodePosition( false );
    } else {
      notUpdated = true;
    }
  }

  /**
   * @return the current y position of this node
   * @see setYPosition
   * note that unless updateNode() has been called, this may not be 
   * the "real" location of this node
   */
  public double getYPosition() {
    
    // Note that this is rather sneaky, hehe.
    // The reason is that to allow for nodes to be moved around by the 
    // mouse using common Piccolo methods, like the PSelectionEventHandler
    // it is necessary to return where the node was moved to

    if (notUpdated) {
      return view.getNodeDoubleProperty( rootGraphIndex,
                                         PGraphView.NODE_Y_POSITION );
    } else {
      return getOffset().getY();
    }
  }

  /**
   * moves this node to its stored x and y locations.
   */
  public void setNodePosition(boolean animate) {
    if (notUpdated) {
      if (animate) {
        PTransformActivity activity = 
          this.
          animateToPositionScaleRotation( 
          view.getNodeDoubleProperty( rootGraphIndex, PGraphView.NODE_X_POSITION ),
          view.getNodeDoubleProperty( rootGraphIndex, PGraphView.NODE_Y_POSITION ),
          1, 0, 500);
        //animate the movement to the new position
      }

      //just move to the new position
      setOffset( view.getNodeDoubleProperty( rootGraphIndex, PGraphView.NODE_X_POSITION ),
                 view.getNodeDoubleProperty( rootGraphIndex, PGraphView.NODE_Y_POSITION ) );
      firePropertyChange("Offset", null, this);
    }
    notUpdated = false;
  }




  /**
   * This draws us as selected
   */
  public void select() {
    selected = true;
    drawSelected();

    //graphNode.setSelected( true ); // TODO
  }

  /**
   * This draws us as unselected
   */
  public void unselect() {
    selected = false;
    drawUnSelected();

    //graphNode.setSelected( false ); // TODO
  }

  /**
   *
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   *
   */
  public boolean setSelected(boolean selected) {
    if (selected) {
      //  drawSelected();
      view.getSelectionHandler()
        .select(this);
    } else {
      //  drawUnSelected();
      view.getSelectionHandler()
        .unselect(this);
    }

    return selected;
  }
 

  //****************************************************************
  // Painting
  //****************************************************************

  /**
   *
   */
  protected void paint(PPaintContext paintContext) {
    super.paint( paintContext );
    // This Might be a good place to do some overriding
  }

  /**
   * Sets the Color to a darker shade
   */
  public void drawSelected() {
    
    super.setPaint( ( Paint )view.getNodeObjectProperty( rootGraphIndex,
                                                   PGraphView.NODE_SELECTION_PAINT ) );
  }

  /**
   * Draws the node with normal color
   */
  public void drawUnSelected() {
    super.setPaint( ( Paint )view.getNodeObjectProperty( rootGraphIndex,
                                                   PGraphView.NODE_PAINT ) );
  }

  

  /**
   * Overridden method so that this node is aware of its bounds being changed
   * so  that it can tell its label and edges to change their position
   * accordingly.
   */
  public boolean setBounds(
                           double x,
                           double y,
                           double width,
                           double height) {
    boolean b = super.setBounds(x, y, width, height);

    firePropertyChange("BoundsChanged", null, this);
    label.updatePosition();

    return b;
  }

  /**
   * Set a new shape for the Node, based on one of the pre-defined shapes
   * <B>Note:</B> calling setPathTo( Shape ), allows one to define their own
   * java.awt.Shape ( i.e. A picture of Johnny Cash )
   */
  public void setShape(int shape) {
  
    float x = ( new Float( view.getNodeDoubleProperty( rootGraphIndex,
                                         PGraphView.NODE_WIDTH ) ) ).floatValue();

    float y = ( new Float( view.getNodeDoubleProperty( rootGraphIndex,
                                         PGraphView.NODE_HEIGHT) ) ).floatValue();

    view.setNodeIntProperty( rootGraphIndex,
                             PGraphView.NODE_SHAPE,
                             shape );


    if (shape == TRIANGLE) {
      //make a trianlge
      setPathTo(
                (PPath.createPolyline(
                                      new float[] {
                                        .5f * x,
                                        0f * x,
                                        1f * x,
                                        .5f * x
                                      },
                                      new float[] {
                                        1f * y,
                                        0f * y,
                                        0f * y,
                                        1f * y
                                      })).getPathReference());
    } else if (shape == DIAMOND) {
      setPathTo(
                (PPath.createPolyline(
                                      new float[] {
                                        10f * x,
                                        20f * x,
                                        10f * x,
                                        0f * x,
                                        10f * x
                                      },
                                      new float[] {
                                        0f * y,
                                        10f * y,
                                        20f * y,
                                        10f * y,
                                        0f * y
                                      })).getPathReference());
    } else if (shape == ELLIPSE) {
      setPathTo(
                (PPath.createEllipse((float) getBounds().getX(),
                                     (float) getBounds().getY(), (float) getBounds().getWidth(),
                                     (float) getBounds().getHeight())).getPathReference());
    } else if (shape == HEXAGON) {
      setPathTo(
                (PPath.createPolyline(
                                      new float[] {
                                        0f * x,
                                        1f * x,
                                        2f * x,
                                        3f * x,
                                        2f * x,
                                        1f * x,
                                        0f * x
                                      },
                                      new float[] {
                                        1f * y,
                                        2f * y,
                                        2f * y,
                                        1f * y,
                                        0f * y,
                                        0f * y,
                                        1f * y
                                      })).getPathReference());
    } else if (shape == OCTAGON) {
      setPathTo(
                (PPath.createPolyline(
                                      new float[] {
                                        0f * x,
                                        0f * x,
                                        1f * x,
                                        2f * x,
                                        3f * x,
                                        3f * x,
                                        2f * x,
                                        1f * x,
                                        0f * x
                                      },
                                      new float[] {
                                        1f * y,
                                        2f * y,
                                        3f * y,
                                        3f * y,
                                        2f * y,
                                        1f * y,
                                        0f * y,
                                        0f * y,
                                        1f * y
                                      })).getPathReference());
    } else if (shape == PARALELLOGRAM) {
      setPathTo(
                (PPath.createPolyline(
                                      new float[] {
                                        0f * x,
                                        1f * x,
                                        3f * x,
                                        2f * x,
                                        0f * x
                                      },
                                      new float[] {
                                        0f * y,
                                        1f * y,
                                        1f * y,
                                        0f * y,
                                        0f * y
                                      })).getPathReference());
    } else if (shape == RECTANGLE) {
      setPathTo(
                (PPath.createRectangle((float) getBounds().getX(),
                                       (float) getBounds().getY(), (float) getBounds().getWidth(),
                                       (float) getBounds().getHeight())).getPathReference());
    }
  }

  /**
   * Set the new shape of the node, with a given
   * new height and width
   *
   * @param shape the shape type
   * @param width the new width
   * @param height the new height
   */
  public void setShape(
                       int shape,
                       double width,
                       double height) {
    setWidth( width );
    setHeight( height );
    setShape( shape );
  }
   /**
   * @deprecated
   * @see NodeView#setUnselectedPaint( Paint ) setUnselectedPaint
   */
  public void setFillColor ( Color color ) {
    setUnselectedPaint( color );
  }

  /**
   * @deprecated
   * @see NodeView#setBorderPaint( Paint ) setBorderPaint
   */  
  public void setLineColor ( Color color ) {
    setBorderPaint( color );
  }

  /**
   * @deprecated
   * @see NodeView#setBorder( Stroke ) setBorder
   * <B>Note:</B> The Y-Files "LineType" class is just a subclass of java.awt.BasicStroke,
   * so try using a java.awt.BasicStroke instead.  If needed I can make some defaults. 
   */
  public void setLineType ( Stroke stroke ) {
    setBorder( stroke );
  }

  /**
   * @deprecated
   * @see NodeView#setShape( int ) setShape
   * Although Y-Files uses ints, and I used ints, it shouldn't really matter.
   */
  public void setShapeType ( int shape ) {
    setShape( shape );
  }

  /**
   * @deprecated
   * @see NodeView#setLabel( String ) setLabel
   * <B>Note:</B> this replaces: <I>NodeLabel nl = nr.getLabel();
   *    nl.setText(na.getLabel());</I>
   */
  public void setText ( String label ) {
    setLabel( label );
  }
  
  /**
   * @deprecated
   * @see NodeView#setLabel( String ) setLabel
   * <B>Note:</B> this replaces: <I>NodeLabel nl = nr.getLabel();
   *    nl.setFont(na.getFont());</I>
   */
  public void setFont ( Font font ) {
    label.setFont( font );
  }

  /**
   * @deprecated
   * @see phoebe.PNodeView#addClientProperty( String, String ) setToolTip
   */
  public void setToolTip ( String tip ) {
    addClientProperty( "tooltip", tip );
  }
  

} //class GridNode
