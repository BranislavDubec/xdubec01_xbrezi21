package src;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

public class Bus implements Print{
	private Coordinate position;
	private double prevDist;
	private double distance;
	private double speed;
	public List<Shape> printable;
	public Line trine;
	private List<Stop> stop;
	private Path path;
	public boolean wait;
	public double timeWait;
	private TimeSchedule timeSchedule;
	private Color c;
	public String line;
	public long delay = 0;
	public String lastStop;
	public Bus(Coordinate position, double speed, Path path, double distance, List<Stop> stops, LocalTime start, String line,Line trine) {
		if(line.equals("1")) {
			c = Color.BLUE;
		}
		if(line.equals("2")) {
			c = Color.PINK;
		}
		if(line.equals("3")) {
			c = Color.GREEN;
		}
		this.trine = trine;
		this.line = line;
		this.position = position;
		this.prevDist = distance;
		this.speed = speed;
		this.distance = distance;
		this.printable = new ArrayList<>();
		this.printable.add(new Circle(position.getX(), position.getY(), 5, c));
		this.path = path;
		this.wait = false;
		this.timeSchedule = new TimeSchedule(stops, path, start);
		this.lastStop = stops.get(0).var;
		this.stop = stops;
	}

	public boolean update(long period, List<Street> streets) {
		Coordinate c = this.path.getCoord(this.distance,this.prevDist);
		Shape shape = this.printable.get(0);
		if(c == null) return false;
		double streetSpeed = c.getSpeedofStreet(streets);
		this.prevDist = this.distance;
		if(streetSpeed < 1) {
			this.distance += (this.speed * (((double)period/1000000000)/10) * streetSpeed);
			this.distance =(double) Math.round(this.distance*10l)/10l;
		}
		else {
			this.distance += this.speed * (((double)period/1000000000)/10) ;
		}
		
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
	public String getNextStop() {
		for(int i = 0; i < this.stop.size()-1; i++) {
			if(this.stop.get(i).var.equals(this.lastStop)) {
				return this.stop.get(i+1).var;
			}
		}
		return this.stop.get(this.stop.size()-1).var;
	}
	public TimeSchedule getTimeSchedule() {
		return this.timeSchedule;
	}
}
