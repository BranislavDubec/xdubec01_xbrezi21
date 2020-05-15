import org.json.simple.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import javafx.fxml.FXMLLoader;
import org.json.simple.parser.JSONParser;

public class ReadJSONFile {
	private List<Print> list;
	private List<Street> streets;
	private List<Stop> stops;
	private List<Line> lines;
	
	public ReadJSONFile() {
		this.list = new ArrayList<>();
		this.streets = new ArrayList<>();
		this.stops = new ArrayList<>();
		this.lines = new ArrayList<>();
	}
	
	public void parseJSON(MainController control) throws Exception {
		parseStreetsAndStops();
		parseLines();
		control.printAll(list);
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
			long startTime = (long) stop.get("startTime");
			long endTime = (long) stop.get("endTime");
			JSONArray streets = (JSONArray) stop.get("streets");
			
			Line line = new Line(lineName);
			
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
			Coordinate lel = this.streets.get(19).pointOfIntersection(this.streets.get(19).begin(),this.streets.get(19).end(),this.streets.get(18).begin(),this.streets.get(18).end());
			System.out.println("X "+lel.getX()+" Y "+lel.getY());
		}
	}
}
