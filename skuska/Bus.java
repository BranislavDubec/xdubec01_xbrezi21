import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Bus implements Print{
	private Coordinate position;
	private double distance;
	private double speed;
	private List<Shape> printable;
	private Path path;
	
	public Bus(Coordinate position, double speed) {
		this.position = position;
		this.speed = speed;
		this.printable = new ArrayList<>();
		this.printable.add(new Circle(position.getX(), position.getY(), 5, Color.AZURE));
	}

	public void update() {
		this.distance += speed;
		Coordinate c = path.getCoord(distance);
		Shape shape = printable.get(0);
		shape.setTranslateX(c.getX()-position.getX()+shape.getTranslateX());
		shape.setTranslateY(c.getY()-position.getY()+shape.getTranslateY());
	}
	
	@Override
	public List<Shape> printShapes() {
		return printable;
	}
}
