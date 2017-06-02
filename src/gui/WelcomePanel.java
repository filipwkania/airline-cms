package gui;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import operations.Query;
import operations.Regex;
import wrappers.Airport;

/**
 * Contains the search flight form.<br>
 * It's named WelcomePanel because it appears when the system starts.
 * @author M&F
 */
class WelcomePanel extends VBox {
	private LinkedList<Airport> airportList;
	private AirportSuggestionPopup airportSP;
	private VBox flightResultsVB;
	private ImageView planeImageView, cloudImageView, earthImageView;
	private HBox animationContainerHB;
	
	public WelcomePanel(final Stage parentStage, final BorderPane root, final Main main) {
		// Creates images and prepares top animation.
		prepareAnimation();
		
		// Prepares the airportList for later use
		populateAirportList();
		
		// Creates the suggestions popup
		airportSP = new AirportSuggestionPopup(parentStage, airportList);
		
		// Creates search form components
		final Button searchB = new Button("Search flights");
		final TextField departureAirportTF = new TextField();
		final TextField arrivalAirportTF = new TextField();
		final TextField departureDateTF = new TextField();
		final TextField seatsNoTF = new TextField();
		
		// Creates choice box for flight class types
		final ChoiceBox classTypeCB = new ChoiceBox();
		classTypeCB.setItems(FXCollections.observableArrayList("First","Coach","Economy"));
		classTypeCB.getSelectionModel().selectLast();
		
		// Sets components propertis
		departureAirportTF.setPrefWidth(106);
		arrivalAirportTF.setPrefWidth(106);
		departureDateTF.setPrefWidth(106);
		departureDateTF.setEditable(false);
		seatsNoTF.setPrefWidth(106);
		classTypeCB.setPrefWidth(94);
		searchB.setPrefWidth(94);
		
		// Adds a tooltip to seatsNoTf
		seatsNoTF.setTooltip(new Tooltip("Maximum is 9999."));

		// Creates top pane that will hold the search form.
		GridPane formGP = new GridPane();
		formGP.setStyle("-fx-padding: 0 0 15 0;");
		formGP.setHgap(5);
		formGP.setVgap(10);

		// Adds components to topGP.
		formGP.add(new Label("Departure airport:"), 0, 0);
		formGP.add(departureAirportTF, 1, 0);
		formGP.add(new Label("Arrival airport:"), 5, 0);
		formGP.add(arrivalAirportTF, 6, 0);
		formGP.add(new Label("Class:"), 10, 0);
		formGP.add(classTypeCB, 11, 0);
		formGP.add(new Label("Departure date:"), 0, 1);
		formGP.add(departureDateTF, 1, 1);
		formGP.add(new Label("Seats no:"), 5, 1);
		formGP.add(seatsNoTF, 6, 1);
		formGP.add(searchB, 11, 1);

		// Creates the flightResults box
		flightResultsVB = new VBox();
		
		// Creates a scroll pane, used for showing many results from flightResultsVB.
		ScrollPane scrollPane = new ScrollPane();
		
		// Sets scroll pane properties
		scrollPane.setStyle("-fx-border-width: 0;" +
							"-fx-padding: 0;");
		scrollPane.setPrefViewportHeight(1000);
		scrollPane.setPrefWidth(430);
		
		// Combines scroll pane and flight result box
		scrollPane.setContent(flightResultsVB);
		
		// Creates a box that will hold the scrollPane
		HBox scrollPaneContainerHB = new HBox();
		scrollPaneContainerHB.setAlignment(Pos.CENTER);
		scrollPaneContainerHB.getChildren().add(scrollPane);
		
		// Creates the title label
		Label titleL = new Label("Find your magic flight");
		titleL.setStyle("-fx-font-size: 15pt;" +
					    "-fx-padding: 0 0 5 0");
		
		// Creates a box for form and results.
		VBox formAndResultsVB = new VBox();
		formAndResultsVB.setAlignment(Pos.CENTER);
		formAndResultsVB.setStyle("-fx-background-color: rgba(255,255,255,0.90);" +
								  "-fx-padding: 5 10 10 10");
		formAndResultsVB.getChildren().addAll(titleL, formGP, scrollPaneContainerHB);
		
		// Creates an empty rectangle used for creating enough between top animation and search form.
		Rectangle spacingR = new Rectangle(580, 46, Color.TRANSPARENT);
		
		// Crates a VBox that will add some distance between top and "formAndResultsVB"
		VBox distanceVBAndformAndResultsVB = new VBox();
		distanceVBAndformAndResultsVB.getChildren().addAll(spacingR, formAndResultsVB);
		
		// Creates a Stack pane for glob image and "distanceVBAndformAndResultsVB"
		StackPane stackP = new StackPane();
		stackP.getChildren().addAll(earthImageView, distanceVBAndformAndResultsVB);
		
		// Adds components to parent pane (this)
		this.getChildren().addAll(animationContainerHB, stackP);
		
		// Shows the airport suggestion popup when "fromTF" is modified
		departureAirportTF.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				// Blocks rendering the popup when "up" and "down" is pressed,
				// in order to allow key navigation through suggestions
				if (e.getCode() != KeyCode.UP && e.getCode() != KeyCode.DOWN &&
					e.getCode() != KeyCode.ENTER && e.getCode() != KeyCode.ESCAPE) {
						airportSP.show(departureAirportTF);
				}
			}
		});
		
		// Shows the airport suggestion popup when "toTF" is modified
		arrivalAirportTF.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				// Blocks rendering the popup when "up" and "down" is pressed,
				// in order to allow key navigation through suggestions
				if (e.getCode() != KeyCode.UP && e.getCode() != KeyCode.DOWN &&
					e.getCode() != KeyCode.ENTER && e.getCode() != KeyCode.ESCAPE) {
						airportSP.show(arrivalAirportTF);
				}
			}
		});
		
		// Shows the calendar popup when "departureDateTF" is clicked
		departureDateTF.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				new FXDateSelectorPopup(parentStage, root, departureDateTF);
				searchB.requestFocus();
			}
		});
		
		// Shows the calendar popup when "departureDateTF" receives a key event
		departureDateTF.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				new FXDateSelectorPopup(parentStage, root, departureDateTF);
				searchB.requestFocus();
			}
		});

		// Searches for flights when the search button is pressed
		searchB.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent me){
				// Checks if search form is not empty
				if (departureAirportTF.getText().isEmpty() || arrivalAirportTF.getText().isEmpty() ||
					departureDateTF.getText().isEmpty() || seatsNoTF.getText().isEmpty()) {
					// Shows an error message.
					new FXMessageDialog(parentStage, root, "Please fill all the necessary fields!");
				} else {
					// Checks if departure and arrival airports are the same.
					if (departureAirportTF.getText().equals(arrivalAirportTF.getText())) {
						new FXMessageDialog(parentStage, root, "Departure and arrival airports can't be the same!");
					
					// Checks if departure airport exists
					} else if (!isAirportValid(departureAirportTF.getText())) {
						new FXMessageDialog(parentStage, root, "Departure airport is not available.");
					
					// Checks if arrival airport exists
					} else if (!isAirportValid(arrivalAirportTF.getText())) {
						new FXMessageDialog(parentStage, root, "Arrival airport is not available.");
					
					// Checks if seats no is valid
					} else if (!Regex.matchClientSeatsNo(seatsNoTF.getText())) {
						new FXMessageDialog(parentStage, root, "The number of seats is not valid.");
					
					// Everything is ok, so let's continue
					} else {
						// Clears old results from flightResultsVB
						flightResultsVB.getChildren().clear();
						
						// Gets from database the flights that match user preferences.
						LinkedList<String[]> foundFlights = Query.getQueryObject().searchFlights(departureDateTF.getText(), 
																 departureAirportTF.getText(), arrivalAirportTF.getText(), 
																 (String) classTypeCB.getSelectionModel().getSelectedItem(), 
																 seatsNoTF.getText());
						
						// Shows a message if there were no flights found
						if (foundFlights.size() == 0) {
							Label noFlightsL = new Label("There are no flights for these preferences.");
							noFlightsL.setStyle("-fx-padding: 0 0 0 100");
							flightResultsVB.getChildren().add(noFlightsL);
						
						// Shows flight results
						} else {							
							// Creates and shows a pane for every found flight.
							// Many parameters are necessary, because every possible flight will
							// be able to redirect to its own reservation maker panel.
							for (String[] s : foundFlights) {
								flightResultsVB.getChildren().add(new SearchResultPanel(seatsNoTF.getText(), 
									(String) classTypeCB.getSelectionModel().getSelectedItem(), 
									departureAirportTF.getText(), arrivalAirportTF.getText(), 
									s[0], s[1], s[2], s[3], s[4], s[5], s[6], 
									parentStage, root, main, WelcomePanel.this));
							}
							
							// Removes top padding from first result
							((SearchResultPanel) flightResultsVB.getChildren().get(0)).setPadding(new Insets(0, 0, 5, 0));
						}
					}
				}
			}
		});
	}
	
	private void prepareAnimation() {
		// Creates images for top animation
		URL planeImgURL = getClass().getResource("resources/airplane_47x30.png");
		planeImageView = new ImageView(new Image(planeImgURL.toString()));
		
		URL cloudImgURL = getClass().getResource("resources/cloud_45x27.png");
		cloudImageView = new ImageView(new Image(cloudImgURL.toString()));
		
		URL earthImgURL = getClass().getResource("resources/earth_330x330.png");
		earthImageView = new ImageView(new Image(earthImgURL.toString()));
		earthImageView.setTranslateY(-7);
		
		// Creates a box that will contain the plane and cloud pictures.
		animationContainerHB = new HBox();
		animationContainerHB.setStyle("-fx-padding: 4 0 0 0");
		animationContainerHB.getChildren().addAll(planeImageView, cloudImageView);
		
		// Creates timeline animation for airplane.
		Timeline airplaneTimeline = new Timeline();
		airplaneTimeline.setCycleCount(Timeline.INDEFINITE);
		airplaneTimeline.setAutoReverse(false);
		
		// Creates starting and ending points for airplane.
		KeyValue airplaneStartPositionKV = new KeyValue(planeImageView.translateXProperty(), 600.0);
		KeyValue airplaneEndPositionKV   = new KeyValue(planeImageView.translateXProperty(), -100.0);
		
		// Creates keyframe values for start and end positions
		KeyFrame airplaneStartKF = new KeyFrame(Duration.ZERO, airplaneEndPositionKV);
		KeyFrame airplaneEndKF   = new KeyFrame(Duration.seconds(120), airplaneStartPositionKV);
		
		// Adds movements to airplane timeline.
		airplaneTimeline.getKeyFrames().addAll(airplaneStartKF, airplaneEndKF);
		
		// Creates timeline animation for the cloud.
		Timeline cloudTimeline = new Timeline();
		cloudTimeline.setAutoReverse(false);
		cloudTimeline.setCycleCount(Timeline.INDEFINITE);
		
		// Creates starting and ending points for cloud.
		KeyValue cloudStartPositionKV = new KeyValue(cloudImageView.translateXProperty(), 555);
		KeyValue cloudEndPositionKV   = new KeyValue(cloudImageView.translateXProperty(), -110);
		
		// Creates keyframes values for start and end positions.
		KeyFrame cloudStartKF = new KeyFrame(Duration.ZERO, cloudStartPositionKV);
		KeyFrame cloudEndKF = new KeyFrame(Duration.seconds(250), cloudEndPositionKV);
		
		// Adds movements to cloud timeline.
		cloudTimeline.getKeyFrames().addAll(cloudStartKF,cloudEndKF);
		
		// Creates timeline animation for earth.
		Timeline earthTimeline = new Timeline();
		earthTimeline.setCycleCount(Timeline.INDEFINITE);
		earthTimeline.setAutoReverse(false);
		
		// Creates animation and key frame for earth.
		KeyValue earthStartKV = new KeyValue(earthImageView.rotateProperty(), 360.0);
		final KeyFrame earthStartKF = new KeyFrame(Duration.seconds(180), earthStartKV);
		
		// Adds movement to earth timeline.
		earthTimeline.getKeyFrames().add(earthStartKF);
		
		//Plays all animations.
		airplaneTimeline.play();
		cloudTimeline.play();
		earthTimeline.play();
	}
	
	/**
	 * Checks if the specified airport id exists in the system.
	 * @param airportId
	 * @return 
	 */
	private boolean isAirportValid(String airportId) {
		for (Airport a : airportList) {
			if (a.getId().equals(airportId)) {
				return true;
			}
		}
		return false;
	}
	
	public void populateResultBox(VBox flightResultsVB, TextField departTF, 
								  TextField fromTF, TextField toTF, ChoiceBox typeCB){
		System.out.println("Button pressed");
	}
	
	private void populateAirportList() {
		// Gets the planes list from the database
		ResultSet rs = Query.getQueryObject().getAirports();
		
		// Creates the LinkedList that will contain the airports
		airportList = new LinkedList<>();
		
		// Fetches the query result data into the observable list.
		// id, name, country and city are added into the Airport object
		if (rs != null) {
			try {
				while (rs.next()) {
					airportList.add(new Airport(rs.getString(1), rs.getString(2), 
											rs.getString(3), rs.getString(4)));
				}
			} catch (SQLException ex) {
				System.err.println("SQL error in WelcomePanel at populateAirportList()");
			}
		}
	}
	
	/**
	 * Hides the airport suggestion popup if exists.
	 */
	public void hideSuggestionPopup() {
		if (airportSP != null) {
			airportSP.hide();
		}
	}
	
	/**
	 * Clears the old results if there are any.
	 */
	public void clearOldResults() {
		flightResultsVB.getChildren().clear();
	}
}
