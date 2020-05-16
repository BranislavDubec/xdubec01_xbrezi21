package src;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class TimeSchedule {
	List<Stop> stops;
	List<Pair<LocalTime, String>> stopsTime;
	List<Coordinate> path;
	LocalTime start;
	
	public TimeSchedule(List<Stop> stops, Path path, LocalTime start) {
		this.stops = stops;
		this.stopsTime = new ArrayList<>();
		this.path = path.getPath();
		this.start = start;
	}
	
	public List<Pair<LocalTime, String>> getTimes() {
		return this.stopsTime;
	}
	
	public void setTimes() {
		double A = 0;
		for(int i = 1; i < path.size(); i++) {
			for(Stop stop : stops) {
				if(path.get(i).equals(stop.getCoordinate())) {
					this.stopsTime.add(new Pair<LocalTime, String>(this.start.plusSeconds((long)A/10), stop.getId()));
					A += 50;
					if(i == 1) A -= 50;
					break;
				}
			}
			if(i != path.size()-1) {				
				A += coordDistance(path.get(i), path.get(i+1));
			}
		}
	}
	
	private double coordDistance(Coordinate first, Coordinate second) {
		return Math.sqrt(Math.pow(first.getX() - second.getX(), 2) + Math.pow(first.getY() - second.getY(), 2));
	}
}
