package src;


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
	public List<Shape> printable;
	private Path path;
	public boolean wait;
	public double timeWait;
	private TimeSchedule timeSchedule;
	
	public Bus(Coordinate position, double speed, Path path, double distance, List<Stop> stops, LocalTime start) {
		this.position = position;
		this.speed = speed;
		this.distance = distance;
		this.printable = new ArrayList<>();
		this.printable.add(new Circle(position.getX(), position.getY(), 5, Color.BLUE));
		this.path = path;
		this.wait = false;
		this.timeSchedule = new TimeSchedule(stops, path, start);
	}

	public boolean update(long period) {
		this.distance += this.speed * (((double)period/1000000000)/10);
		Coordinate c = this.path.getCoord(this.distance);
		Shape shape = this.printable.get(0);
		if(c == null) return false;
		shape.setTranslateX((c.getX()-this.position.getX())+shape.getTranslateX());
		shape.setTranslateY((c.getY()-this.position.getY())+shape.getTranslateY());
		this.position = c;
		return true;
	}
	
	@Override
	public List<Shape> printShapes() {
		return printable;
	}
	
	public Coordinate getPosition() {
		return this.position;
	}
	
	public Path getPath() {
		return this.path;
	}
	
	public TimeSchedule getTimeSchedule() {
		return this.timeSchedule;
	}
}
