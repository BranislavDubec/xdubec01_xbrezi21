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
	
	public ReadJSONFile() {
		this.list = new ArrayList<>();
		this.streets = new ArrayList<>();
	}
	
	public void parseJSON(FXMLLoader loader) throws Exception {
		parseStreetsAndStops(loader);
		parseLines();
	}
	
	private void parseStreetsAndStops(FXMLLoader loader) throws Exception {
		
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
			}
		}		
		
		MainController control = loader.getController();
		control.printAll(list);
	}
	
	private void parseLines() throws Exception {
		
		JSONParser parser = new JSONParser();	
		FileReader reader = new FileReader("data/b.json");
		JSONObject obj = (JSONObject) parser.parse(reader);
		
	}
}
