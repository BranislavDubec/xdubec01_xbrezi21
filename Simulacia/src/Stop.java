package src;

import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Stop implements Print{
    private String id;
    private Coordinate coordinates = null;
    private Street stop_street = null;
    
    public Stop(String s, Coordinate c){
        this.id = s;
        this.coordinates = c;
    }

    public String getId(){
        return this.id;
    }

    public Coordinate getCoordinate(){
        return this.coordinates;
    }
    
    public void setStreet(Street s){
        this.stop_street = s;
    }
    
    public Street getStreet(){
        return this.stop_street;
    }
 

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Stop)) {
        return false;
        }

        Stop fml = (Stop) o;
        if (this.getId() == fml.getId()) {
        return true;
        }
        else {
        return false;
        }
    }

	@Override
	public List<Shape> printShapes() {
		return Arrays.asList(
				new Circle(this.coordinates.getX(), this.coordinates.getY(), 3, Color.color(1, 0, 0))
				);
	}
}
