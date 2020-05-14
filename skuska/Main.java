import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
       controller.timeHandle();
       ReadJSONFile test = new ReadJSONFile();
       test.parseJSON(loader);
    }
}