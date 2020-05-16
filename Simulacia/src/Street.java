package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class Street implements Print{

    private Coordinate start;
    private Coordinate end;
    private List<Stop> street_stops = new ArrayList<Stop>();
    
    public double speed;
    public String ID;
    public Street(String ID, Coordinate start, Coordinate end){
        this.ID = ID;
        this.start = start;
        this.end = end;
        this.speed = 1;
    }

    public Coordinate begin(){
        return start;
    }

    public Coordinate end(){
        return end;
    }

    public java.lang.String getId(){
        return this.ID;
    }

    public java.util.List<Stop> getStops(){
        return this.street_stops;
    }

    public boolean addStop(Stop stop){
    	Coordinate stopCoords = stop.getCoordinate();
    	if(onStreet(stopCoords)) {
    		this.street_stops.add(stop);
	        stop.setStreet(this);
	        return true;
    	}
    	else {    		
    		return false;
    	}
    }
    
    public boolean onStreet(Coordinate c) {
    	if(c.distance(begin()) + c.distance(end()) == begin().distance(end()) ) return true;
    	else return false;
    }
    
    public Coordinate pointOfIntersection(Coordinate A, Coordinate B, Coordinate C, Coordinate D) {
    	// Line AB represented as a1x + b1y = c1 
        double a1 = B.getY() - A.getY(); 
        double b1 = A.getX() - B.getX(); 
        double c1 = a1*(A.getX()) + b1*(A.getY()); 
       
        // Line CD represented as a2x + b2y = c2 
        double a2 = D.getY() - C.getY(); 
        double b2 = C.getX() - D.getX(); 
        double c2 = a2*(C.getX())+ b2*(C.getY()); 
       
        double determinant = a1*b2 - a2*b1; 
       
        if (determinant == 0) 
        { 
            // The lines are parallel. This is simplified 
            // by returning a pair of FLT_MAX 
            return null; 
        } 
        else
        { 
            double x = (b2*c1 - b1*c2)/determinant; 
            double y = (a1*c2 - a2*c1)/determinant; 
            return Coordinate.create(x, y); 
        } 
    }
    
    public boolean intersects(Street s) {
    	// Find the four orientations needed for general and 
        // special cases 
        int o1 = orientation(begin(), end(), s.begin()); 
        int o2 = orientation(begin(), end(), s.end()); 
        int o3 = orientation(s.begin(), s.end(), begin()); 
        int o4 = orientation(s.begin(), s.end(), end()); 
      
        // General case 
        if (o1 != o2 && o3 != o4) 
            return true; 
      
        // Special Cases 
        if (o1 == 0 && onSegment(begin(), end(), s.begin())) return true; 
      
        if (o2 == 0 && onSegment(begin(), s.end(), s.begin())) return true; 
      
        if (o3 == 0 && onSegment(end(), begin(), s.end())) return true; 
      
        if (o4 == 0 && onSegment(end(), s.begin(), s.end())) return true; 
      
        return false; // Doesn't fall in any of the above cases 
    }
    
    private int orientation(Coordinate a, Coordinate b, Coordinate c) {
        // for derivation of the formula 
        double val = (b.getY() - a.getY()) * (c.getX() - b.getX()) - (b.getX() - a.getX()) * (c.getY() - b.getY()); 
       
        if (val == 0) return 0;  // colinear 
       
        // clock or counterclock wise 
        return (val > 0)? 1: 2;  
    }
    
    private boolean onSegment(Coordinate p, Coordinate q, Coordinate r) {
    	 if (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) && q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY())) 
    		    return true; 
    	 return false; 
    }
    
    public java.util.List<Coordinate> getCoordinates(){
    	java.util.List<Coordinate> list = new ArrayList<Coordinate>();
    	list.add(begin());
    	list.add(end());
        return list;
    }
    
	@Override
	public List<Shape> printShapes() {
		double centerTextX = start.getX() > end.getX() ? end.getX() : start.getX();
		double centerTextY = start.getY() > end.getY() ? end.getY() : start.getY();
		
		return Arrays.asList(
				new Text(Math.abs(start.getX() - end.getX()) / 2 + centerTextX, Math.abs(start.getY() - end.getY()) / 2 + centerTextY - 5, this.ID),
				new Line(start.getX(), start.getY(), end.getX(), end.getY())
				);
	}
}