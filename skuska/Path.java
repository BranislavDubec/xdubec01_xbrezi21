import java.util.List;

public class Path {
	private List<Coordinate> path;
	
	public Path(List<Coordinate> path) {
		this.path = path;
	}
	
	private double coordDistance(Coordinate first, Coordinate second) {
		return Math.sqrt(Math.pow(first.getX() - second.getX(), 2) + Math.pow(first.getY() - second.getY(), 2));
	}
	
	public Coordinate getCoord(double distance) {
		double potentionalDistance = 0;
		
		Coordinate first = null;
		Coordinate second = null;
		for(int i = 0; i < path.size() - 1; i++) {
			first = path.get(i);
			second = path.get(i+1);
			
			if(potentionalDistance + coordDistance(first, second) > distance) break;
			
			potentionalDistance += coordDistance(first, second);
		}
		double traveled = (distance - potentionalDistance) / coordDistance(first, second);
		return Coordinate.create(first.getX() + (first.getX() - second.getX()) * traveled, first.getY() + (first.getY() - second.getY()) * traveled);
	}
}
