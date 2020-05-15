import org.json.simple.JSONObject;
import java.io.FileReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javafx.scene.layout.Pane;

public class ReadJSONFile {
	private LocalTime previous;
	private List<Print> list;
	private List<Street> streets;
	private List<Stop> stops;
	private List<Line> lines;
	private List<Print> buses;
	private List<Bus> autobuses;
	private MainController control;
	
	public ReadJSONFile() {
		this.list = new ArrayList<>();
		this.streets = new ArrayList<>();
		this.stops = new ArrayList<>();
		this.lines = new ArrayList<>();
		this.buses = new ArrayList<>();
		this.autobuses = new ArrayList<>();
	}
	
	public void parseJSON(MainController control) throws Exception {
		parseStreetsAndStops();
		parseLines();
		control.printAll(list);
		this.control = control;
		 
	}
	
	private void parseStreetsAndStops() throws Exception {
		
		JSONParser parser = new JSONParser();	
		FileReader reader = new FileReader("data/a.json");
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

			Stop stopL = new Stop(stopName, Coordinate.create((int)x, (int)y));
			
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
	
	private void parseLines() throws Exception {
		
		JSONParser parser = new JSONParser();	
		FileReader reader = new FileReader("data/b.json");
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
				
				found = false;
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
				if(timeS >= startS + (delay*n*60) && previousTimeS < startS + (delay*n*60)) {
					Path path = new Path(line.getPath());
					Bus bus = new Bus(line.getRoute().get(0).getValue().getCoordinate(), 1, path, 0);
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
			if(!bus.update(period)) {
				this.autobuses.remove(i);
				content.getChildren().remove(bus.printable.get(0));
			}
		}
	}
	
	public void generateOnStart(LocalTime time) {
		//for all lines
		for(Line line : this.lines) {
			Path path = new Path(line.getPath());
			double distance = path.getDistance() / 10;
			LocalTime start = line.getStartTime();
			int startS = start.toSecondOfDay();
			LocalTime end = line.getEndTime();
			int endS = end.toSecondOfDay();
			long delay = line.getDelay();
			int timeS = time.toSecondOfDay();

			if(time.toSecondOfDay() >= startS && time.toSecondOfDay() <= endS+distance) {
				for(int n = 0; n*delay*60+startS <= endS; n++) {
					if(timeS >= startS + (delay*n*60) && timeS < startS+(delay*n*60)+distance) {
						Bus bus = new Bus(line.getRoute().get(0).getValue().getCoordinate(), 1, path, (timeS - startS+(delay*n*60))*10);
						this.buses.add(bus);
						this.autobuses.add(bus);
						this.control.printAll(buses);
						this.buses.clear();
					}
				}
			}
			
		}
	}
	
}
