package src;


import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
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

public class MainController extends Main{
		@FXML
		private Pane content;
		@FXML
		private AnchorPane plusButton;
		@FXML
		private AnchorPane minusButton;
		@FXML
		private Label showTime = new Label();
		@FXML
		private Label showSpeed = new Label();
		@FXML
		private ChoiceBox<String> timeChoice = new ChoiceBox<>();
		@FXML
		private ChoiceBox<String> speedChoice = new ChoiceBox<>();
		@FXML
		private Button setValue = new Button();
		
		
		private List<Print> toPrint;
		private long period = (long) Math.pow(10, 9);
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
			newScale.setX(1.1 * content.getScaleX());
			newScale.setY(1.1 * content.getScaleY());
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
			newScale.setX( content.getScaleX() / 1.1);
			newScale.setY( content.getScaleY() / 1.1);
			content.getTransforms().add(newScale);
			content.layout();
		}
		
		public void timeHandle(MainController control) throws Exception {
			
			showSpeed.setText("1.0");
			LocalTime temp = LocalTime.now();
			jsonFile.parseJSON(control);
			jsonFile.generateOnStart(time);
			int i = 0;
			temp = temp.minusMinutes(temp.getMinute());
			while(i <48) {
				i++;
				timeChoice.getItems().add(temp.toString().substring(0,5)+":00");
				if(i == 1) {
					timeChoice.setValue(temp.toString().substring(0,5)+":00");
				}
				temp = temp.plusMinutes(30);
			}
			double j = 0;
			while(j <= 8) {
				j = Math.round(j * 100.0) / 100.0;
				speedChoice.getItems().add(Double.toString(j));
				if(j >1.0) {
					speedChoice.setValue("1.0");
					
				}
				if(j<1) {
					j += 0.1;
					
				}
				else {
					j+=1;
				}
			}
			
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								showTime.setText(time.toString().substring(0, 8));
							}
							catch(Exception e){
								showTime.setText(time.toString().substring(0, 5) + ":00");
							}
							jsonFile.initGenerate(time);
							jsonFile.update(time, content, period);
							time = time.plusNanos(period/100);
						}
					});
				  }
				},0,period/100000000l) ;
			
		}
		
		@FXML
		void setValues(MouseEvent event) {
			event.consume();
			showSpeed.setText(speedChoice.getValue());
			double tmp = Double.parseDouble(showSpeed.getText())*1000000000;
			period = (long) tmp;
			showTime.setText(timeChoice.getValue());
			String temp = showTime.getText() ;
			time = LocalTime.parse(temp);
			jsonFile.deleteObjects(content);
			jsonFile.generateOnStart(time);
			
		}
		
		public void printAll(List<Print> toPrint) {
			this.toPrint = toPrint;
			
			for(Print print:toPrint) {
				content.getChildren().addAll(print.printShapes());
			}
		}
}	
