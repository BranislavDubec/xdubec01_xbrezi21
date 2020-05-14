public class Coordinate {
    private int x, y;

    public static Coordinate create(int xx, int yy) {
      if (xx < 0 || yy < 0) return null;
      Coordinate created = new Coordinate();
      created.x = xx;
      created.y = yy;
      return created;
    }
  
    public int diffX(Coordinate c){
      int abs = this.x - c.x;
      return abs < 0 ? -abs : abs;
    }

    public int diffY(Coordinate c){
      int abs = this.y - c.y;
      return abs < 0 ? -abs : abs;
    }

    public int getX() {
      return this.x;
    }
    
    public int getY() {
      return this.y;
    }

    public double distance(Coordinate x) {
    	return Math.sqrt(Math.pow(this.x - x.getX(), 2) + Math.pow(this.y - x.getY(), 2));
    }
    
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Coordinate)) {
        return false;
      }
  
      Coordinate fml = (Coordinate) o;
      if (this.getX() == fml.getX() && this.getY() == fml.getY()) {
        return true;
      }
      else {
        return false;
      }
    }
}
