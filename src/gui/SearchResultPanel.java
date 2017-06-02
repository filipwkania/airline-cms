package gui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This class represents the GridPane for search result.
 * @author MF
 */
public class SearchResultPanel extends GridPane {	
	public SearchResultPanel(final String seatsNo, final String seatClass, final String departureAirport, 
						final String arrivalAirport, final String flightScheduleId, String fullPrice, 
						final String departureDate, final String arrivalDate, String length, 
						final String planeManufacturer, final String planeModel, final Stage parentStage, 
						final BorderPane root, final Main main, final WelcomePanel welcomeP) {
		
		// Sets pane properties.
		setPadding(new Insets(10, 0, 5, 0));
		setVgap(5);
		setHgap(7);
		
		// Adds a small line + padding at the buttom of the pane.
		setStyle("-fx-border-color: gray;" +
				 "-fx-border-width: 0 0 0.2 0;");

		// Calculates the price based on selected seatClass.
		if (seatClass.equals("Economy")) {
			fullPrice = "" + (Integer.parseInt(fullPrice) / 100 * 70); // Economy 30% discount.
		} else if (seatClass.equals("Coach")) {
			fullPrice = "" + (Integer.parseInt(fullPrice) / 100 * 85); // Coach 15% discount.
		}
		
		// Calculates the price based on seatsNo
		final String totalPrice = (Integer.parseInt(fullPrice) * Integer.parseInt(seatsNo)) + "";
		
		// Creates labels and book button.
		Label departureTimeTextL = new Label("Departure time:");
		Label arrivalTimeTextL   = new Label("Arrival time:");
		Label flightLengthTextL  = new Label("Flight length:");
		Label planeL			 = new Label("Plane:");
		Label departureTimeL = new Label(departureDate.substring(11, 16));
		Label arrivalTimeL   = new Label(arrivalDate.substring(11, 16));
		Button bookB = new Button("Book tickets");
		
		// Sets action for book button: will show the make reservation panel.
		bookB.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent ae) {
				// Makes sure the airport suggestion popup doesn't remain open
				welcomeP.hideSuggestionPopup();
				
				// Checks if user is authenticated. Guests can't book tickets.
				if (main.getLoggedInUser().getID() == null) {
					main.getTopPanel().startLoginProcedure("Please authenticate before booking.");
				} else {
					// Redirects to reservation maker panel
					root.setCenter(new ReservationMakerPanel(main, parentStage, root, flightScheduleId, 
							seatClass, Integer.parseInt(seatsNo), totalPrice, departureAirport, 
							arrivalAirport, departureDate, arrivalDate, planeManufacturer,
							planeModel, welcomeP));
				}
			}
		});
	
		// Sets components in grid pane.
		add(departureTimeTextL, 0, 0);
		add(departureTimeL, 1, 0);
		add(arrivalTimeTextL, 8, 0);
		add(arrivalTimeL, 9, 0);
		add(new Label("Price:"), 12, 0);
		add(new Label(totalPrice + " dkk"), 13, 0);
		add(flightLengthTextL, 0, 1);
		add(new Label(length + " h"), 1, 1);
		add(new Label(planeManufacturer + " " + planeModel), 8, 1, 2, 1);
		add(bookB, 12, 1, 2, 1);
		
		// Gets grid pane column constraints list
		ObservableList<ColumnConstraints> columnConstraints = getColumnConstraints();
		
		// Adds 9 empty constrants
		for (int i = 0; i < 9; i++) {
			columnConstraints.add(new ColumnConstraints());
		}
		
		// Creates and adds the 10th constraint, the one we need for a nice layout.
		ColumnConstraints colC = new ColumnConstraints();
		colC.setPrefWidth(60);
		columnConstraints.add(colC);
	}
}
