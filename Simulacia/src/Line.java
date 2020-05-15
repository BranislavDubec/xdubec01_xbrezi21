package src;

import java.time.LocalTime;
import java.util.*;

public class Line {
    private java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street, Stop>> route = new ArrayList<AbstractMap.SimpleImmutableEntry<Street,Stop>>();
    private String id;
    private long delay;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Coordinate> path;

    public Line(String s, LocalTime startTime, LocalTime endTime, long delay){
        this.id = s;
        this.startTime = startTime;
        this.endTime = endTime;
        this.delay = delay;
        this.path = new ArrayList<>();
    }
    
    public LocalTime getStartTime() {
    	return this.startTime;
    }
    
    public LocalTime getEndTime() {
    	return this.endTime;
    }
    
    public long getDelay() {
    	return this.delay;
    }
    
    public String getID() {
    	return this.id;
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
    
    public void setPath() {
    	this.path.clear();
    	this.path.add(getRoute().get(0).getValue().getCoordinate());
    	for(int i = 0; i < this.route.size() - 1; i++) {
    		this.path.add(getRoute().get(i).getKey().pointOfIntersection(getRoute().get(i).getKey().begin(), getRoute().get(i).getKey().end(), getRoute().get(i+1).getKey().begin(), getRoute().get(i+1).getKey().end()));
    	}
    	this.path.add(getRoute().get(this.route.size()-1).getValue().getCoordinate());
    }
    
    public List<Coordinate> getPath() {
    	return this.path;
    }
}
