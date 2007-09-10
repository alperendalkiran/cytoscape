package gpml;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import org.pathvisio.model.PathwayElement;

import ding.view.DGraphView;
import ding.view.InnerCanvas;
import ding.view.ViewportChangeListener;

public abstract class Annotation extends JComponent implements ViewportChangeListener {
		PathwayElement pwElm;
		
		protected BufferedImage image;
		protected DGraphView view;
		private double scaleFactor = 1;
		
		public Annotation(PathwayElement pwElm, DGraphView view) {
			this.pwElm = pwElm;
			this.view = view;
			
			setBounds(getVOutline().getBounds());
			
			view.addViewportChangeListener(this);
		}
		
		protected int getVLeft() {
			return (int)GpmlImporter.mToV(pwElm.getMLeft());
		}
		protected int getVTop() {
			return (int)GpmlImporter.mToV(pwElm.getMTop());
		}
		protected int getVWidth() {
			return (int)GpmlImporter.mToV(pwElm.getMWidth());
		}
		protected int getVHeight() {
			return (int)GpmlImporter.mToV(pwElm.getMHeight());
		}
		protected double getVCenterX() {
			return GpmlImporter.mToV(pwElm.getMCenterX());
		}
		protected double getVCenterY() {
			return GpmlImporter.mToV(pwElm.getMCenterY());
		}
		
		public abstract java.awt.Shape getVOutline();

		protected double getStrokeWidth() {
			return 1;
		}
		
		public void setBounds(double x, double y, double width, double height) {
			setBounds((int)x, (int)y, (int)width, (int)height);
		}
		
		public void setBounds(int x, int y, int width, int height) {
			super.setBounds(x, y, width, height);

			if ((width > 0) && (height > 0)) {
				image = new BufferedImage(width,
						height, BufferedImage.TYPE_INT_ARGB);
			}
		}
		
		protected java.awt.Shape relativeToBounds(java.awt.Shape s) {
			Rectangle r = getBounds();
			AffineTransform f = new AffineTransform();
			f.translate(-r.x, -r.y);
			return f.createTransformedShape(s);
		}
		
		protected java.awt.Shape viewportTransform(java.awt.Shape s) {
			InnerCanvas canvas = view.getCanvas();
					
			AffineTransform f = canvas.getAffineTransform();
			if(f != null) 	return f.createTransformedShape(s);
			else 			return s;
		}
		
		protected double getCurrentScaleFactor() { return scaleFactor; }
		
		public void viewportChanged(int w, int h, double newXCenter, double newYCenter, double newScaleFactor) {
			InnerCanvas canvas = view.getCanvas();
			
			AffineTransform f = canvas.getAffineTransform();
						
			if(f == null) return;
			
			java.awt.Shape outline = getVOutline();
			
			Rectangle b = outline.getBounds();
			Point2D pstart = f.transform(new Point2D.Double(b.x, b.y), null);
			setBounds(pstart.getX(), pstart.getY(), b.width * newScaleFactor, b.height * newScaleFactor);
			scaleFactor = newScaleFactor;
		}
}
