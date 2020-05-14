import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class Street implements Print{
    private String ID;
    private Coordinate start;
    private Coordinate end;
    private List<Stop> street_stops = new ArrayList<Stop>();
    private List<Coordinate> street_coordinates = new ArrayList<Coordinate>();
    
    public Street(String ID, Coordinate start, Coordinate end){
        this.ID = ID;
        this.start = start;
        this.end = end;
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
    
    public boolean follows(Street s){
        if(this.street_coordinates.size() == 0 || s.getCoordinates().size() == 0) return false;
        if(
            this.street_coordinates.get(0).equals(s.getCoordinates().get(0))                                                              ||
            this.street_coordinates.get(0).equals(s.getCoordinates().get(s.getCoordinates().size() - 1))                                  ||
            this.street_coordinates.get(this.street_coordinates.size() - 1).equals(s.getCoordinates().get(0))                             ||
            this.street_coordinates.get(this.street_coordinates.size() - 1).equals(s.getCoordinates().get(s.getCoordinates().size() - 1)) 
        ) return true;
        return false;
        
        
        
    }
    
    public boolean intersect() {
    	return true;
    }
    /*
    private int orientation(Street s) {
        // for derivation of the formula 
        int val = (p2.y - p1.y) * (p3.x - p2.x) - (p2.x - p1.x) * (p3.y - p2.y); 
       
        if (val == 0) return 0;  // colinear 
       
        // clock or counterclock wise 
        return (val > 0)? 1: 2;  
    }
    */
    public java.util.List<Coordinate> getCoordinates(){
    	java.util.List<Coordinate> list = new ArrayList<Coordinate>();
    	list.add(begin());
    	list.add(end());
        return list;
    }
    
	@Override
	public List<Shape> printShapes() {
		int centerTextX = start.getX() > end.getX() ? end.getX() : start.getX();
		int centerTextY = start.getY() > end.getY() ? end.getY() : start.getY();
		
		return Arrays.asList(
				new Text(Math.abs(start.getX() - end.getX()) / 2 + centerTextX, Math.abs(start.getY() - end.getY()) / 2 + centerTextY - 5, this.ID),
				new Line(start.getX(), start.getY(), end.getX(), end.getY())
				);
	}
}