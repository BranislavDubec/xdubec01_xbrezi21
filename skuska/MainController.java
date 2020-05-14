
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

public class MainController {
		@FXML
		private Pane content;
		@FXML
		private AnchorPane plusButton;
		@FXML
		private AnchorPane minusButton;
		
		private Timer timer = new Timer(false);
		private LocalTime time	 = LocalTime.now();
		@FXML
		void buttonHoverPlus(MouseEvent event) {
			event.consume();
			plusButton.setOpacity(0.8);
		}
		@FXML
		void buttonUnHoverPlus(MouseEvent event) {
			event.consume();
			plusButton.setOpacity(1);
		}
		@FXML
		void buttonPressedPlus(MouseEvent event) {
			event.consume();
			ObservableList<Node> group = content.getChildrenUnmodifiable();
			//content.translateXProperty().set(content.getTranslateX() + 25 + 1.1*content.getTranslateX() );
			//content.translateYProperty().set(content.getTranslateY() + 50);
		//	content.setScaleX(1.05 * content.getScaleX());
			//content.setScaleY(1.05 * content.getScaleY());
			Scale newScale = new Scale();
			newScale.setPivotX((content.getMaxWidth() - content.getMinWidth()) / 2);
			newScale.setPivotY((content.getMaxHeight() - content.getMinHeight()) / 2);
			newScale.setX(1.02 * content.getScaleX());
			newScale.setY(1.02 * content.getScaleY());
			content.getTransforms().add(newScale);
			content.layout();
			
		}
		@FXML
		void buttonHoverMinus(MouseEvent event) {

			event.consume();
			minusButton.setOpacity(0.8);

		}
		@FXML
		void buttonUnHoverMinus(MouseEvent event) {
			event.consume();
			minusButton.setOpacity(1);
		}
		@FXML
		void buttonPressedMinus(MouseEvent event) {
			event.consume();
			ObservableList<Node> group = content.getChildrenUnmodifiable();
			//content.translateXProperty().set(content.getTranslateX() + 25 + 1.1*content.getTranslateX() );
			//content.translateYProperty().set(content.getTranslateY() + 50);
			//content.setScaleX(1.05 * content.getScaleX());
			//content.setScaleY(1.05 * content.getScaleY());
			Scale newScale = new Scale();
			newScale.setPivotX((content.getMaxWidth() - content.getMinWidth()) / 2);
			newScale.setPivotY((content.getMaxHeight() - content.getMinHeight()) / 2);
			newScale.setX( content.getScaleX() / 1.02);
			newScale.setY( content.getScaleY() / 1.02);
			content.getTransforms().add(newScale);
			content.layout();
		}
		TimerTask task = new TimerTask()
		{
		        public void run()
		        {
		            timer.schedule(task, 5000l);      
		        }

		};
}
