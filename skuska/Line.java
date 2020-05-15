import java.util.*;

public class Line {
    private java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street, Stop>> route = new ArrayList<AbstractMap.SimpleImmutableEntry<Street,Stop>>();
    private String id;

    public Line(String s){
        this.id = s;
    }

    public java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street, Stop>> getRoute(){
        return new ArrayList<>(this.route);
    }

    public Boolean addStop(Stop stop){
        if(route.size() == 0 || stop.getStreet().intersects(route.get(route.size() - 1).getKey())){
            route.add(new java.util.AbstractMap.SimpleImmutableEntry<>(stop.getStreet(), stop));
            return true;                
        }
        return false;
    }

    public Boolean addStreet(Street street){
        if(route.size() == 0) return true;
        if(street.intersects(route.get(route.size() - 1).getKey())){
            route.add(new java.util.AbstractMap.SimpleImmutableEntry<>(street, null));
            return true;
        }
        return false;
    }
}
