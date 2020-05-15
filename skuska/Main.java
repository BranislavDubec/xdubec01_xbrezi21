import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
       FXMLLoader loader= new FXMLLoader(getClass().getResource("/layout.fxml"));
       AnchorPane root = loader.load(); 
       Scene scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
       MainController controller = loader.getController();
       ReadJSONFile test = new ReadJSONFile();
       MainController control = loader.getController();
       test.parseJSON(control);
       controller.timeHandle(test);
    }
}