package whiteboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ResizableShapes extends JPanel {
	  private int SIZE = 8;
	  //Below are 3 points, points[0] and [1] and top-left and bottom-right of the shape.
	  // points[2] is the center of the shape
	  
	  // Handler
	  int xStart = 120;
	  int yStart = 120;
	  int xEnd = 200;
	  int yEnd = 300;

	  double xTopleft;
	  double yTopleft;
	  
	  int width = Math.abs(xStart - xEnd);
	  int height = Math.abs(yStart - yEnd);
	  
	  Color c = Color.BLACK;
	  int pixel_size = 3;
	  String keyword;
	  
	  	  
	  private Rectangle2D[] points = { new Rectangle2D.Double(xStart, yStart, SIZE, SIZE), 
	                                   new Rectangle2D.Double(xEnd, yEnd, SIZE, SIZE),
	                                   new Rectangle2D.Double((xStart+xEnd)/2, (yStart+yEnd)/2, SIZE, SIZE),
	                                   new Rectangle2D.Double(xEnd, yStart, SIZE, SIZE),
	                                   new Rectangle2D.Double(xStart, yEnd, SIZE, SIZE)};

	  ShapeResizeHandler ada = new ShapeResizeHandler();

	  public ResizableShapes() 
	  {
		  addMouseListener(ada);
		  addMouseMotionListener(ada);
	  }
	  
	  public ResizableShapes(String keyword, int xStart, int yStart, int xEnd, int yEnd, Color c, int pixel_size) 
	  {
		  this.keyword = keyword;
		  this.xStart = xStart;
		  this.yStart = yStart;
		  this.xEnd = xEnd;
		  this.yEnd = yEnd;
		  this.c = c;
		  this.pixel_size = pixel_size;
		  this.keyword = keyword;
		  if(keyword=="circle"||keyword=="square")
		  {
			  points[0] = new Rectangle2D.Double(xStart, yStart, SIZE, SIZE);
			  points[1] = new Rectangle2D.Double(xEnd, yStart+xEnd-xStart, SIZE, SIZE);
			  points[2] = new Rectangle2D.Double((xStart+xEnd)/2, (2*yStart+xEnd-xStart)/2, SIZE, SIZE);
			  points[3] = new Rectangle2D.Double(xEnd, yStart, SIZE, SIZE);
			  points[4] = new Rectangle2D.Double(xStart, yStart+xEnd-xStart, SIZE, SIZE);
		  }
		  else
		  {
			  points[0] = new Rectangle2D.Double(xStart, yStart, SIZE, SIZE);
			  points[1] = new Rectangle2D.Double(xEnd, yEnd, SIZE, SIZE);
			  points[2] = new Rectangle2D.Double((xStart+xEnd)/2, (yStart+yEnd)/2, SIZE, SIZE);
			  points[3] = new Rectangle2D.Double(xEnd, yStart, SIZE, SIZE);
			  points[4] = new Rectangle2D.Double(xStart, yEnd, SIZE, SIZE);
		  }
		  addMouseListener(ada);
		  addMouseMotionListener(ada);
	  }
	  
	  
	  @ Override
	  public void paintComponent(Graphics g) 
	  {
		    super.paintComponent(g);
		    Graphics2D g2 = (Graphics2D) g;
		    
		    for (int i = 0; i < points.length; i++) 
		    {
		      g2.fill(points[i]);
		    }
		    
		    if(points[1].getX()-points[0].getX()>0 && points[1].getY()-points[0].getY()>0)
		    {
		    	xTopleft = points[0].getCenterX();
		    	yTopleft = points[0].getCenterY();
		    }
		    else if(points[1].getX()-points[0].getX()<0 && points[1].getY()-points[0].getY()<0)
		    {
		    	xTopleft = points[1].getCenterX();
		    	yTopleft = points[1].getCenterY();
		    }
		    else if(points[1].getX()-points[0].getX()>0 && points[1].getY()-points[0].getY()<0)
		    {
		    	xTopleft = points[4].getCenterX();
		    	yTopleft = points[4].getCenterY();
		    }
		    else
		    {
		    	xTopleft = points[3].getCenterX();
		    	yTopleft = points[3].getCenterY();
		    }
		    
		    if(keyword=="oval"||keyword=="circle")
		    {
			    // Ellipse
			    Ellipse2D s = new Ellipse2D.Double();
			    s.setFrame(xTopleft, yTopleft,
			        Math.abs(points[1].getCenterX()-points[0].getCenterX()),
			        Math.abs(points[1].getCenterY()-points[0].getCenterY()));
				g2.setColor(c);
				BasicStroke bStroke = new BasicStroke(pixel_size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
				g2.setStroke(bStroke);
			    g2.draw(s);
		    }
		    else if(keyword=="rectangle"||keyword=="square")
		    {
			    // Rectangle
			    Rectangle2D s = new Rectangle2D.Double();
			    s.setFrame(xTopleft, yTopleft,
			        Math.abs(points[1].getCenterX()-points[0].getCenterX()),
			        Math.abs(points[1].getCenterY()-points[0].getCenterY()));
				g2.setColor(c);
				BasicStroke bStroke = new BasicStroke(pixel_size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
				g2.setStroke(bStroke);
			    g2.draw(s);
		    }
	  }

	  
	  class ShapeResizeHandler extends MouseAdapter 
	  {
		    private Point2D[] lastPoints = new Point2D[5];
		    private int pos = -1;
		    public void mousePressed(MouseEvent event) 
		    {
			      Point p = event.getPoint();
			      for (int i = 0; i < points.length; i++) 
			      {
				        if (points[i].contains(p)) 
				        {
					          pos = i;
					          // initialize preDrag points
					          for(int j = 0; j < 3; j++)
					          {
					              lastPoints[j] = new Point2D.Double(points[j].getX(), points[j].getY());
					          }
					          lastPoints[3] = new Point2D.Double(points[1].getX(),points[0].getY());
					          lastPoints[4] = new Point2D.Double(points[0].getX(),points[1].getY());
					          return;
				        }
			      }		    	
		    }

		    public void mouseReleased(MouseEvent event) {
		      pos = -1;
		    }

		    public void mouseDragged(MouseEvent event) {
			      if (pos == -1)
				        return;
				      if(pos != 2)
				      { //if 2, it's a shape drag
				    	  if(keyword=="circle"||keyword=="square")
				    	  {
				    		  if(lastPoints[pos].getX()>event.getPoint().x)
				    		  {
				    			  if(pos==0||pos==1)
				    			  {
				    				  points[pos].setRect(event.getPoint().x,lastPoints[pos].getY()-Math.abs(event.getPoint().x-lastPoints[pos].getX()),SIZE,SIZE);
				    			  }
				    			  else
				    			  {
				    				  points[pos].setRect(event.getPoint().x,lastPoints[pos].getY()+Math.abs(event.getPoint().x-lastPoints[pos].getX()),SIZE,SIZE);
				    			  }
				    		  }
				    		  else
				    		  {
				    			  if(pos==0||pos==1)
				    			  {
				    				  points[pos].setRect(event.getPoint().x,lastPoints[pos].getY()+Math.abs(event.getPoint().x-lastPoints[pos].getX()),SIZE,SIZE);
				    			  }
				    			  else
				    			  {
				    				  points[pos].setRect(event.getPoint().x,lastPoints[pos].getY()-Math.abs(event.getPoint().x-lastPoints[pos].getX()),SIZE,SIZE);
				    			  }
				    		  }
				    	  }
				    	  else if(keyword=="rectangle"||keyword=="oval")
				    	  {
					    	  points[pos].setRect(event.getPoint().x,event.getPoint().y,SIZE,SIZE);
				    	  }
				    	  
				    	  if(pos==0||pos==1)
				    	  {
				    		  points[3].setRect(points[1].getX(),points[0].getY(),SIZE,SIZE);
				    		  points[4].setRect(points[0].getX(),points[1].getY(),SIZE,SIZE);
				    	  }
				    	  else
				    	  {
				    		  points[0].setRect(points[4].getX(),points[3].getY(),SIZE,SIZE);
				    		  points[1].setRect(points[3].getX(),points[4].getY(),SIZE,SIZE);
				    	  }
				          //Get the x,y of the centre of the line joining the 2 new diagonal vertices, which will be new points[2]
				          double newPoint2X = ( points[0].getX() + points[1].getX() )/2;
				          double newPoint2Y = ( points[0].getY() + points[1].getY() )/2;
				          points[2].setRect(newPoint2X, newPoint2Y,SIZE,SIZE);
				      }
				      else
				      { 
				    	  //Shape drag, 1,2,3 points/marker rects need to move equal amounts
				          Double deltaX = event.getPoint().x - lastPoints[2].getX();
				          Double deltaY = event.getPoint().y - lastPoints[2].getY();
				          for(int j = 0; j < 5; j++)
				              points[j].setRect((lastPoints[j].getX() + deltaX),(lastPoints[j].getY() + deltaY),SIZE,SIZE);
				      }
				      repaint();		  		    	
		    }
	  }
	  
	  public void confirm(Graphics g) {
		  Graphics2D g2 = (Graphics2D) g;
		  Ellipse2D s = new Ellipse2D.Double();
		  s.setFrame(xTopleft, yTopleft,
			        Math.abs(points[1].getCenterX()-points[0].getCenterX()),
			        Math.abs(points[1].getCenterY()-points[0].getCenterY()));
		  g2.draw(s);
	  }
	  
	  public double returnX()
	  {
		  return xTopleft;
	  }
	  
	  public double returnY()
	  {
		  return yTopleft;
	  }
	  
	  public double returnWidth()
	  {
		  return Math.abs(points[1].getCenterX()-points[0].getCenterX());
	  }
	  
	  public double returnHeight()
	  {
		  return Math.abs(points[1].getCenterY()-points[0].getCenterY());
	  }

}