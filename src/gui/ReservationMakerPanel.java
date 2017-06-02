package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import operations.Query;

/**
 *
 * @author M&F
 */
public class ReservationMakerPanel extends BorderPane {
	private Main main;
	private WelcomePanel welcomeP;
	private Stage parentStage;
	private Pane root;
	private TicketDetailsPanel[] ticketDetailsPanels;
	
	public ReservationMakerPanel(Main main, Stage parentStage, Pane root, String flightScheduleId, 
								 String flightClass, int seatsNo, String totalPrice,
								 String airport1Id, String airport2Id, String departureTimeStamp,
								 String arrivalTimeStamp, String planeManufacturer, String planeModel,
								 WelcomePanel welcomeP) {
		this.main = main;
		this.welcomeP = welcomeP;
		
		// Stage and root are necessary for FXMessageDialog
		this.parentStage = parentStage;
		this.root = root;
		
		// Sets the padding for the entire container.
		this.setPadding(new Insets(10));
		
		showFlightDetails(flightClass, seatsNo, totalPrice, airport1Id, airport2Id, 
						  departureTimeStamp, arrivalTimeStamp, planeManufacturer, planeModel);
		showTicketDetailsInputPanes(seatsNo);
		createActionButtons(flightScheduleId, totalPrice, flightClass);
	}
	
	private void showFlightDetails(String flightClass, int seatsNo, String totalPrice, String airport1Id,
								   String airport2Id, String departureTimeStamp, String arrivalTimeStamp,
								   String planeManufacturer, String planeModel) {
		// Gets flight details
		Query.getQueryObject().getFlightSchedules();
		
		// Creates a grid pane that will show all flight details
		GridPane detailsGP = new GridPane();
		detailsGP.setVgap(5);
		detailsGP.setHgap(5);
		
		// Adds components to grid pane
		detailsGP.add(new Label("Flight details:"), 0, 0);
		detailsGP.add(new Label("Departure: " + airport1Id), 0, 1);
		detailsGP.add(new Label("Date: " + departureTimeStamp.substring(5, 16)), 4, 1);
		detailsGP.add(new Label("Arrival: " + airport2Id), 8, 1);
		detailsGP.add(new Label("Date: " + arrivalTimeStamp.substring(5, 16)), 12, 1);
		detailsGP.add(new Label("Class: " + flightClass), 0, 2);
		detailsGP.add(new Label("Total price: " + totalPrice + " dkk"), 4, 2);
		detailsGP.add(new Label("Plane: " + planeManufacturer + " " + planeModel), 8, 2, 8, 1);
		
		this.setTop(detailsGP);
	}

	private void showTicketDetailsInputPanes(int seatsNo) {
		// Creates a VBox that will contain all the ticket panes
		VBox vBox = new VBox(5);
		
		// Creates an array that will hold all the ticket panels
		ticketDetailsPanels = new TicketDetailsPanel[seatsNo];
		
		// Creates panes for tickets, based on required seatsNo
		for (int i = 0; i < seatsNo; i++) {
			// Creates the ticket pane with the specified ticket no
			ticketDetailsPanels[i] = new TicketDetailsPanel(i + 1);
			
			// Adds pane to container
			vBox.getChildren().add(ticketDetailsPanels[i]);
		}
		
		// Creates a scroll pane, because maybe we'll have to many ticket panes
		// and we want to show them all.
		ScrollPane sp = new ScrollPane();
		sp.setStyle("-fx-padding: 10 0 0 0;");
		
		// Adds tickets container to scroll pane
		sp.setContent(vBox);
		
		// Adds scroll pane to main parent (this)
		this.setCenter(sp);
	}

	private void createActionButtons(final String flightScheduleId, final String price,
									 final String flightClass) {
		// Creates action buttons
		Button reserveB = new Button("Reserve");
		Button reserveAndPayB = new Button("Reserve and pay");
		Button cancelB = new Button("Cancel");
		
		// Creates tooltips for buttons
		reserveB.setTooltip(new Tooltip("Reserves tickets for at least 2 hours!\n" +
										"If it's not paid in 2 hours, it can expire!"));
		
		// Create actions for buttons
		reserveB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				bookButtonAction(flightScheduleId, flightClass, price, "unconfirmed");
			}
		});
		
		reserveAndPayB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				bookButtonAction(flightScheduleId, flightClass, price, "confirmed");
			}
		});
		
		cancelB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Shows back the welcome panel (search flight form).
				main.showPanel("WelcomePanel");
			}
		});
		
		// Creates a HBox container for buttons
		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.CENTER);
		hBox.setStyle("-fx-padding: 10 0 0 0;");
		
		// Adds buttons to container
		hBox.getChildren().addAll(reserveB, reserveAndPayB, cancelB);
		
		// Adds buttons container to parent (this)
		this.setBottom(hBox);
	}
	
	private void bookButtonAction(String flightScheduleId, String flightClass, String totalPrice, 
								  String status) {
		// Shows an error message if ticket data is not valid or completed
		if (!isTicketDataValid()) {
			new FXMessageDialog(parentStage, root, "Please write valid data for every ticket!");
		} else {
			// Creates the reservation and saves its ID
			String reservationId = Query.getQueryObject().createReservation(flightScheduleId, 
					totalPrice, flightClass, status, main.getLoggedInUser().getID());
			
			// Save tickets into database
			for (TicketDetailsPanel t : ticketDetailsPanels) {
				Query.getQueryObject().createTicket(t.getGender(), t.getName(), t.getSurname(), reservationId);
			}
			
			// Clears old flight results if there any.
			welcomeP.clearOldResults();
			
			// Redirects to welcome panel
			main.showPanel("WelcomePanel");
			
			// Shows a confirmation message, different for payed or not.
			if (status.equals("confirmed")) {
				new FXMessageDialog(parentStage, root, "Reservation has been successfully made!");
			} else {
				new FXMessageDialog(parentStage, root, "Reservation has been successfully made!\n" +
									"You have 2 hours to pay it, otherwise it can expire!");
			}
		}
	}
	
	private boolean isTicketDataValid() {
		// Checks every ticket panel
		for (int i = 0; i < ticketDetailsPanels.length; i++) {
			if (!ticketDetailsPanels[i].isDataValid()) {
				return false;
			}
		}
		
		// If you're here, it means that ticket data is ok
		return true;
	}
}
