package src;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	public ReadJSONFile jsonFile=new ReadJSONFile();;
    @Override
    public void start(Stage stage) throws Exception {
       FXMLLoader loader= new FXMLLoader(getClass().getResource("/src/layout.fxml"));
       AnchorPane root = loader.load(); 
       Scene scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
       MainController controller = loader.getController();
       controller.timeHandle(controller);
    }
}