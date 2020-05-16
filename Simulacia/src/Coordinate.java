package src;

import java.util.List;

public class Coordinate {
    private double x, y;

    public static Coordinate create(double x2, double y2) {
      if (x2 < 0 || y2 < 0) return null;
      Coordinate created = new Coordinate();
      created.x = x2;
      created.y = y2;
      return created;
    }
  
    public double diffX(Coordinate c){
      double abs = this.x - c.x;
      return abs < 0 ? -abs : abs;
    }

    public double diffY(Coordinate c){
      double abs = this.y - c.y;
      return abs < 0 ? -abs : abs;
    }

    public double getX() {
      return this.x;
    }
    
    public double getY() {
      return this.y;
    }

    public double distance(Coordinate x) {
    	return Math.sqrt(Math.pow(this.x - x.getX(), 2) + Math.pow(this.y - x.getY(), 2));
    }
    public double getSpeedofStreet(List<Street> streets) {
    	for(Street str : streets) {
    		if (str.onStreet(this)) {
    			return 1/str.speed;
    		}
    	}
    	return 1;
    }
    
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Coordinate)) {
        return false;
      }
  
      Coordinate fml = (Coordinate) o;
      if (Math.round(this.getX()*10000000) == Math.round(fml.getX()*10000000) && Math.round(this.getY()*10000000) == Math.round(fml.getY()*10000000)) {
        return true;
      }
      else {
        return false;
      }
    }
}
