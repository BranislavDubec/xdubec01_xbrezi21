package src;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.util.Pair;

public class MainController extends Main{
		@FXML
		private Pane content;
		@FXML
		private Pane schedule;
		@FXML
		private Pane forHover;
		@FXML
		private Pane lineAnchor;
		@FXML
		private Pane streetAnchor;
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
		private ChoiceBox<String> Linka = new ChoiceBox<>();
		@FXML
		private Label showLine = new Label();
		@FXML
		private Button setValueSpeed = new Button();
		@FXML
		private Button setLine = new Button();
		@FXML
		private ChoiceBox<String> streetChoice = new ChoiceBox<>();
		@FXML
		private ChoiceBox<String> streetSpeedChoice = new ChoiceBox<>();
		
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
			jsonFile.parseJSON(control,Linka);
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
			while(j <= 5) {
				j = Math.round(j * 100.0) / 100.0;
				if(j == 3) {
					j++;
					continue;
				}
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
		void setValueSpeed(MouseEvent event) {
			event.consume();
			showTime.setText(timeChoice.getValue());
			String temp = showTime.getText() ;
			time = LocalTime.parse(temp);
			jsonFile.deleteObjects(content);
			showSpeed.setText(speedChoice.getValue());
			double tmp = Double.parseDouble(showSpeed.getText())*1000000000;
			period = (long) tmp;
			jsonFile.deleteObjects(content);
			jsonFile.generateOnStart(time);
			
		}
		@FXML
		public void setValueLine(MouseEvent event) {
			event.consume();
			if(lineAnchor.isVisible()) {
				lineAnchor.getChildren().clear();
			}
			Button exit = new Button();
			
			showLine.setText(Linka.getValue());
			lineAnchor.setVisible(true);
			//lineAnchor.setMinWidth(500);
			Label write  = new Label();
			src.Line tmp =jsonFile.autobuses.get(0).trine; 
			int jk = 0;
			String finalBus = "";
			for (Bus bus : jsonFile.autobuses) {
				if(bus.line == Linka.getValue()) {
					tmp = bus.trine;
					finalBus += "Delay: " + bus.delay + " NextStop:" + bus.getNextStop() + "\n";
					jk ++;
				}
			}
			String finalValue = "";
			int i = -1;
			for (Coordinate cord : tmp.getPath()) {
				
				for(Stop stop : tmp.stops) {
					if(cord.getX() == stop.getCoordinate().getX() && cord.getY() == stop.getCoordinate().getY()) {
						i++;
						if(i == 0) continue;
						finalValue += Integer.toString(i) + ". " + stop.var + "\n";
						//write.setText("number: " + Integer.toString(i) + ". name: "+ stop.var + " ");
						//lineAnchor.getChildren().add(write);					
						}
				}
			}
			write.setText("Line number : " + Linka.getValue() + " starts everyday at : " + tmp.getStartTime().toString() + 
					"\n with delay : " +  tmp.getDelay() + " minutes and last bus starts at " + tmp.getEndTime().toString() + 
					".\n It route is : " + finalValue + "\nThere are currently :" + jk + " buses of this line: \n" + finalBus
					);
			write.setLayoutX(0);
			write.setLayoutY(0);
			write.setStyle("-fx-font-weight: bold");
			write.setStyle("-fx-stroke: black");
			write.setOpacity(1);
			lineAnchor.getChildren().add(write);
			exit.setLayoutX(lineAnchor.getWidth() );
			exit.setLayoutY(lineAnchor.getHeight());
			exit.setText("X");
			exit.setOnMouseClicked((u) -> {
				lineAnchor.getChildren().clear();
				lineAnchor.setVisible(false);
			});
			lineAnchor.getChildren().add(exit);
			}
		@FXML
		
		public void printAll(List<Print> toPrint) {
			this.toPrint = toPrint;
			for(Print print:toPrint) {
				
				content.getChildren().addAll(print.printShapes());
				if(print instanceof Bus) {
					Node node = content.getChildren().get(content.getChildren().size()-1);
					node.setOnMouseClicked((e) -> {
									e.consume();
									Button del = new Button();
									del.setText("X");
									if(schedule.isVisible()) {
										schedule.getChildren().clear();
										List<Node> NodetoRemove = new ArrayList<Node>();
										for(Node nodes : content.getChildren()) {
											if(nodes instanceof Line) {
												
												if(((Line)nodes).getStroke() == Color.ORANGE && ((Line)nodes).getStrokeWidth() == 2.0) {
													
													NodetoRemove.add(nodes);
												}
											}
										}
										content.getChildren().removeAll(NodetoRemove);
									}
									schedule.setVisible(true);
									schedule.setOpacity(1);
									del.setLayoutX(230);
									del.setLayoutY(10);
									schedule.getChildren().add(del);
									double y = 10;
									schedule.setPrefSize(250, 200);
									Label writepar = new Label();
									String tmp = ((Bus) print).line;
									writepar.setText("Line number: "+tmp );
									writepar.setLayoutX(10);
									writepar.setLayoutY(y);
									schedule.getChildren().add(writepar);
									y+=50;
									List<Pair<LocalTime, String>> temp = ((Bus) print).getTimeSchedule().getTimes();
									for (Pair<LocalTime,String> a : temp) {
										Label writeTime = new Label();
										try {
											writeTime.setText(a.getKey().toString().substring(0,8));
										}
										catch(Exception t){
											writeTime.setText(a.getKey().toString().substring(0,5) + ":00");
										}
										Label writeName = new Label();
										writeName.setText(a.getValue());
										writeTime.setLayoutX(10);
										writeTime.setLayoutY(y);
										writeName.setLayoutX(130);
										writeName.setLayoutY(y);
										y += 40;
										schedule.getChildren().add(writeTime);
										schedule.getChildren().add(writeName);
									}
									for (int i = 1; i < ((Bus)print).getPath().getPath().size()-1; i++) {
										Line a =new Line(((Bus)print).getPath().getPath().get(i).getX() , ((Bus)print).getPath().getPath().get(i).getY() , ((Bus)print).getPath().getPath().get(i+1).getX(),((Bus)print).getPath().getPath().get(i+1).getY());
										a.setStroke(Color.ORANGE);
										a.setStrokeWidth(2.0);
										content.getChildren().add(a);
									}
									del.setOnMouseClicked((f) ->{
										f.consume();
										List<Node> NodetoRemove = new ArrayList<Node>();
										for(Node nodes : content.getChildren()) {
											if(nodes instanceof Line) {
												
												if(((Line)nodes).getStroke() == Color.ORANGE && ((Line)nodes).getStrokeWidth() == 2.0) {
													
													NodetoRemove.add(nodes);
												}
											}
										}
										content.getChildren().removeAll(NodetoRemove);
										schedule.getChildren().clear();
										schedule.setPrefSize(0, 0);
										schedule.setVisible(false);
									});
								});
					node.setOnMouseEntered((e) -> {
						e.consume();
						node.setScaleX(2);
						node.setScaleY(2);
						Label writeVar = new Label();
						writeVar.setText( "Information about current\nbus.\nLine number : \n" + ((Bus) print).line + "\n");
						Label writeTime = new Label();
						int times = time.toSecondOfDay();
						writeTime.setText("Current delay is : \n"+ ((Bus) print).delay + "[s] \n"+
									"Next Stop is: \n" +((Bus) print).getNextStop()
								);
						writeVar.setLayoutX(10);
						writeVar.setLayoutY(150);
						writeTime.setLayoutX(10);
						writeTime.setLayoutY(250);
						forHover.getChildren().add(writeVar);
						forHover.getChildren().add(writeTime);

						forHover.setPrefSize(250, 300);
						forHover.setVisible(true);
						//print.line
					});
					node.setOnMouseExited((e) -> {
						e.consume();
						node.setScaleX(1);
						node.setScaleY(1);
						forHover.getChildren().clear();
					});
								
				}
				if(print instanceof Street) {
					Node node = content.getChildren().get(content.getChildren().size()-1);
					node.setOnMouseClicked((e) ->{
						e.consume();
						Button del  = new Button();
						if(streetAnchor.isVisible() == true) {
							//streetAnchor.setVisible(false);
							streetAnchor.getChildren().clear();
						}
						streetAnchor.setVisible(true);
						Label write = new Label();
						write.setText("Chosen street: " + ((Street)print).ID + "\n current traffic: " + ((Street)print).speed);
						streetAnchor.setOpacity(1);
						streetAnchor.getChildren().add(write);
						ChoiceBox choicebox = new ChoiceBox();
						for(int i = 1; i <= 10; i++) {
							choicebox.getItems().add(i);
						}
						choicebox.setValue(((Street)print).speed);
						choicebox.setLayoutY(40);
						streetAnchor.getChildren().add(choicebox);
						
						Button traffic = new Button();
						traffic.setLayoutX(60);
						traffic.setLayoutY(40);
						traffic.setText("OK");
						
						del.setLayoutX(180);
						del.setLayoutY(5);
						del.setText("X");
						streetAnchor.getChildren().add(del);
						del.setOnMouseClicked((p) ->{
							streetAnchor.getChildren().clear();
							streetAnchor.setVisible(false);
						});
						streetAnchor.getChildren().add(traffic);
						traffic.setOnMouseClicked((h)->{
							//TODO
							try {
							((Street)print).speed = Double.parseDouble(choicebox.getValue().toString()) ;
							streetAnchor.getChildren().remove(0);
							write.setText("Chosen street: " + ((Street)print).ID + "\n current traffic: " + ((Street)print).speed);
							streetAnchor.getChildren().add(write);
							}
							catch (Exception l) {
								System.err.println("Error");
							}
						});
						
					});
				}
				}
			}
		}

