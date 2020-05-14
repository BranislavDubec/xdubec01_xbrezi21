import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Bus implements Print{
	private Coordinate position;
	private double speed;
	private List<Shape> printable;
	
	public Bus(Coordinate position, double speed) {
		this.position = position;
		this.speed = speed;
		printable = new ArrayList<>();
		printable.add(new Circle(position.getX(), position.getY(), 5));
	}

	@Override
	public List<Shape> printShapes() {
		return printable;
	}
}
