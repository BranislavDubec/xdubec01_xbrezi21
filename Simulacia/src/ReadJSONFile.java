/**
* The ReadJSONFile class is created to read information from .json files and make all kinds of classes from them.
*
* @author  Jindrich Brezina, Branislav Dubec
* @version 1.0
* @since   14. 5. 2020
*/

package src;

import org.json.simple.JSONObject;
import java.io.FileReader;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

public class ReadJSONFile {
	private LocalTime previous;
	private List<Print> list;
	public List<Stop> stops;
	public List<Line> lines;
	private List<Print> buses;
	public List<Bus> autobuses;
	private MainController control;
	
	public List<Street> streets;
	public ReadJSONFile() {
		this.list = new ArrayList<>();
		this.streets = new ArrayList<>();
		this.stops = new ArrayList<>();
		this.lines = new ArrayList<>();
		this.buses = new ArrayList<>();
		this.autobuses = new ArrayList<>();
	}
	
	/**
     * This method is used make basic classes from json files.
     * @param control main anchor pane
     * @param box choice box
     */
	public void parseJSON(MainController control,ChoiceBox box) throws Exception {
		parseStreetsAndStops();
		parseLines(box);
		control.printAll(list);
		this.control = control; 
	}
	
	/**
     * This method is used make Streets and Stops from json files.
     */
	private void parseStreetsAndStops() throws Exception {
		
		JSONParser parser = new JSONParser();	
		FileReader reader = new FileReader("data/ulice.json");
		JSONObject obj = (JSONObject) parser.parse(reader);
		JSONArray streets = (JSONArray) obj.get("streets");
		JSONArray stops = (JSONArray) obj.get("stops");
		
		for(int i = 0; i < streets.size(); i++) {
			JSONObject street = (JSONObject) streets.get(i);
			String streetName = (String) street.get("name");
			long startX = (long) street.get("startX");
			long startY = (long) street.get("startY");
			long endX = (long) street.get("endX");
			long endY = (long) street.get("endY");
			
			Street streetL = new Street(streetName, Coordinate.create((int)startX, (int)startY), Coordinate.create((int)endX, (int)endY));
			this.list.add(streetL);
			this.streets.add(streetL);
		}
		
		for(int i = 0; i < stops.size(); i++) {
			JSONObject stop = (JSONObject) stops.get(i);
			String stopName = (String) stop.get("name");
			long x = (long) stop.get("x");
			long y = (long) stop.get("y");
			String stopStreet = (String) stop.get("street");
			String var = (String) stop.get("var");
			Stop stopL = new Stop(stopName, Coordinate.create((int)x, (int)y),var);
				
			Boolean isOnStreet = false;
			for(int j = 0; j < streets.size(); j++) {
				Street current = (Street) this.streets.get(j);
				if (current.getId().equals(stopStreet)) {
					if(!current.addStop(stopL)) {
						System.err.println("Zastavka nelezi na ulici");
						System.exit(1);
					}
					isOnStreet = true;
					break;
				}
			}
			
			if(!isOnStreet) {
				System.err.println("Zastavka nelezi na ulici (ulice neexistuje)");
				System.exit(1);
			}
			else {				
				this.list.add(stopL);
				this.stops.add(stopL);
			}
		}		
	}
	
		/**
     * This method is used make Lines from json files.
     */
	private void parseLines(ChoiceBox box) throws Exception {
		
		JSONParser parser = new JSONParser();	
		FileReader reader = new FileReader("data/linky.json");
		JSONObject obj = (JSONObject) parser.parse(reader);
		JSONArray lines = (JSONArray) obj.get("lines");
		//lines
		for(int i = 0; i < lines.size(); i++) {
			JSONObject stop = (JSONObject) lines.get(i);
			String lineName = (String) stop.get("name");
			String startStop = (String) stop.get("startStop");
			String endStop = (String) stop.get("endStop");
			long delay = (long) stop.get("delay");
			String startTimeS = (String) stop.get("startTime");
			String endTimeS = (String) stop.get("endTime");
			JSONArray streets = (JSONArray) stop.get("streets");
			JSONArray JSONstops = (JSONArray) stop.get("stops");
			LocalTime startTime = LocalTime.parse(startTimeS);
			LocalTime endTime = LocalTime.parse(endTimeS);
			
			Line line = new Line(lineName, startTime, endTime, delay);
			
			boolean found = false;
			for(int j = 0; j < this.stops.size(); j++) {
				if(this.stops.get(j).getId().equals(startStop)) {
					if(line.addStop(this.stops.get(j))) {
						
						found = true;
						break;
					}
					else {
						System.err.println("startStop not following line");
						System.exit(1);
					}
				}
			}
			if(!found) {
				System.err.println("startStop not found");
				System.exit(1);
			}
			
			//streets on line
			for(int j = 0; j < streets.size(); j++) {
				String street = (String) streets.get(j);
				JSONArray someStop = (JSONArray) JSONstops.get(j);
				String otherStop = (String) someStop.get(0);
				
				found = false;
				if(!otherStop.equals("none")) {
					//for all stops in JSON on one street
					for(int k = 0; k < someStop.size();k++) {
						//comparing with existing stops
						for(int l = 0; l < this.stops.size(); l++) {
							otherStop = (String) (someStop.get(k));
							if(this.stops.get(l).getId().equals(otherStop)) {							
								if(line.addStop(this.stops.get(l))) {
									
									found = true;
									break;
								}
								else {
									System.err.println("Streets on line dont follow each other");
									System.exit(1);
								}
							}							
						}
						if(!found) {
							System.err.println("Stop not found");
							System.exit(1);
						}
					}
				}
				else {
					//find if streets exist and follow each other
					for(int k = 0; k < this.streets.size(); k++) {
						
						if(this.streets.get(k).getId().equals(street)) {
							if(line.addStreet(this.streets.get(k))) {
								found = true;
								break;
							}
							else {
								System.err.println("Streets on line dont follow each other");
								System.exit(1);
							}
						}
					}
					if(!found) {
						System.err.println("Street not found");
						System.exit(1);
					}
				}
			}
			
			found = false;
			for(int j = 0; j < this.stops.size(); j++) {
				if(this.stops.get(j).getId().equals(endStop)) {
					if(line.addStop(this.stops.get(j))) {
						found = true;
						break;
					}
					else {
						System.err.println("endStop not following line");
						System.exit(1);
					}
				}
			}
			if(!found) {
				System.err.println("endStop not found");
				System.exit(1);
			}
			this.lines.add(line);
			line.setPath();
			
		}
		for	(int i =0; i < this.lines.size();i++) {
			box.getItems().add(this.lines.get(i).getID());
		}
	}
	
	//buses start
	public void initGenerate(LocalTime time) {
		
		if (this.previous == null) this.previous = time;
		//for all lines
		for(Line line : this.lines) {
			LocalTime start = line.getStartTime();
			int startS = start.toSecondOfDay();
			LocalTime end = line.getEndTime();
			int endS = end.toSecondOfDay();
			long delay = line.getDelay();
			int timeS = time.toSecondOfDay();
			int previousTimeS = this.previous.toSecondOfDay();
			for(int n = 0; n*delay+startS <= endS; n++) {
				if(timeS == startS + (delay*n*60) && timeS != previousTimeS ) {
					Path path = new Path(line.getPath());
					Bus bus = new Bus(line.getRoute().get(0).getValue().getCoordinate(), 1, path, 0, line.stops, time,line.getID(),line);
					//int temp = (bus.getTimeSchedule().getTimes().size());
					/*for(int i = 0; i < temp; i++) {
						System.out.println("TIME : "+bus.getTimeSchedule().getTimes().get(i).getKey().toString()+" STOP : "+bus.getTimeSchedule().getTimes().get(i).getValue());
					}*/
					this.buses.add(bus);	
					this.autobuses.add(bus);
					this.control.printAll(buses);
					this.buses.clear();
				}
			}
		}
		this.previous = time;
	}
	
	//move buses
	public void update(LocalTime time, Pane content, long period) {
		Bus bus;
		for(int i = 0; i < this.autobuses.size(); i++) {
			bus = this.autobuses.get(i);
			for(int j = 0; j < this.stops.size(); j++) {				
				if(this.stops.get(j).getCoordinate().equals(bus.getPosition())) {
					for(int k = 2; k < bus.getPath().getPath().size(); k++) {
						if(bus.getPosition().equals(bus.getPath().getPath().get(k))) {
							if(bus.wait == true) {
								if(bus.timeWait != 0) {
									bus.timeWait--;
								}
								else {
									//bus.getPath().getPath().remove(bus.getPath().getPath().get(k));
									bus.wait = false;
									bus.timeWait = 500/ (((double)period/1000000000));
								}
							}
							else {
								bus.wait = true;
								bus.timeWait = 500 / (((double)period/1000000000));
								for (Pair <LocalTime,String> pair : bus.getTimeSchedule().getTimes()) {
									if(pair.getValue().equals(this.stops.get(j).var)) {
										bus.delay = time.toSecondOfDay() - pair.getKey().toSecondOfDay();
										bus.lastStop=pair.getValue();
									}
								}
							}
						}
					}
				}
			}
			if(!bus.wait) {
				if(!bus.update(period,this.streets)) {
					this.autobuses.remove(i);
					content.getChildren().remove(bus.printable.get(0));
				}	
			}
		}
	}
	
	public void generateOnStart(LocalTime time) {
		//for all lines
		for(Line line : this.lines) {
			Path path = new Path(line.getPath());
			double distance = path.getDistance(this.stops) / 10;
			LocalTime start = line.getStartTime();
			int startS = start.toSecondOfDay();
			LocalTime end = line.getEndTime();
			int endS = end.toSecondOfDay();
			long delay = line.getDelay();
			int timeS = time.toSecondOfDay();
			if(time.toSecondOfDay() >= startS && time.toSecondOfDay() <= endS+distance) {
				for(int n = 0; n*delay*60+startS <= endS; n++) {
					if(timeS >= startS + (delay*n*60) && timeS < startS+(delay*n*60)+distance) {
						int tmp = 0;
						Bus buss = new Bus(line.getRoute().get(0).getValue().getCoordinate(), 1, path, (((timeS - (startS+(delay*n*60)))*10)-50*tmp), line.stops, time.minusSeconds((timeS - (startS+(delay*n*60)))),line.getID(),line);
						for(Pair<LocalTime, String> pair : buss.getTimeSchedule().getTimes()) {
							if(pair.getKey().toSecondOfDay() < timeS) {
								tmp++;
							}
						}
						Bus bus = new Bus(line.getRoute().get(0).getValue().getCoordinate(), 1, path, (((timeS - (startS+(delay*n*60)))*10)-50*(tmp-1)), line.stops, time.minusSeconds((timeS - (startS+(delay*n*60)))),line.getID(),line);
						this.buses.add(bus);
						this.autobuses.add(bus);
						this.control.printAll(buses);
						this.buses.clear();
					}
				}
			}
		}
	}
	public boolean updateLine(String num, String street, String delete) {
		src.Street str1 = this.streets.get(0);
		for( src.Street str : this.streets) {
			if(str.getId().equals(street)) {
				str1 = str;
				break;
			}
		}
		src.Street str2 = this.streets.get(0);
		for( src.Street str : this.streets) {
			if(str.getId().equals(delete)) {
				str2 = str;
				break;
			}
		}
		//str1 
		int numInt = Integer.parseInt(num);
		src.Line l = this.lines.get(numInt-1);
		src.Line modified = new src.Line(l.getID(), l.getStartTime(), l.getEndTime(), l.getDelay());
		
		java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street, Stop>> temp =this.lines.get(numInt-1).getRoute();
		
		for (int i = 0; i < l.getRoute().size(); i++) {
			if(temp.get(i).getValue()== null) {
				if(temp.get(i).getKey().equals(str2)) {
				modified.addStreet(str1);
				while(temp.get(i).getKey().equals(str2)) {
					i++;
				}
				if(!modified.addStreet(temp.get(i).getKey())) {
					return false;
				}
				}
				else {
				modified.addStreet(l.getRoute().get(i).getKey());
				
				}
			}
			else {
				if(temp.get(i).getKey().equals(str2)) {
					modified.addStreet(str1);
					while(temp.get(i).getKey().equals(str2)) {
						i++;
					}
					if(!modified.addStreet(temp.get(i).getKey())) {
						return false;
					}
					}
					else {
						modified.addStop(temp.get(i).getValue());
					}
				
			}
			
		}
		modified.setPath();
		this.lines.remove(numInt-1);
		this.lines.add(numInt-1,modified);
		
		return true;
		
	}
	public void deleteObjects(Pane content) {
		List<Bus> toRemove = new ArrayList<>();
		for(Bus bus : this.autobuses) {
			toRemove.add(bus);
			content.getChildren().remove(bus.printable.get(0));
		}
		this.autobuses.removeAll(toRemove);

	}
	
}
