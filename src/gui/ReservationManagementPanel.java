package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import operations.Query;
import wrappers.LoggedInUser;
import wrappers.Reservation;

/**
 * Contains the panel that shows all reservations of the logged in user.<br>
 * If user is staff or admin, panel will show all reservations.
 * @author M&F
 */
public class ReservationManagementPanel extends BorderPane {
	private Stage parentStage;
	private Pane root;
	private LoggedInUser loggedInUser;
	private TableView table;
	private Button payB, cancelB;
	
	public ReservationManagementPanel(Stage parentStage, Pane root, LoggedInUser loggedInUser) {
		this.parentStage = parentStage;
		this.root = root;
		this.loggedInUser = loggedInUser;
		
		// Adds padding to panel (this)
		this.setStyle("-fx-padding: 10;");
		
		// Creates the welcome label with an unique message, based on user rank.
		Label welcomeL = new Label();
		if (loggedInUser.getRank().equals("client")) {
			welcomeL.setText("Here there are all you bookings, " + loggedInUser.getFName() + ".");
		} else {
			welcomeL.setText("Here there are all the reservations registered by this system.");
		}
		
		// Adds label to panel (this)
		this.setTop(welcomeL);
		
		createTable();
		createButtons();
	}
	
	/**
	 * Creates table that shows reservations.
	 */
	private void createTable() {
		ResultSet rs = null;
		
		// Gets reservations.
		// If rank is client, gets just reservations made by him.
		// If not, gets all reservations
		if (loggedInUser.getRank().equals("client")) {
			rs = Query.getQueryObject().getReservations(loggedInUser.getID());
		} else {
			rs = Query.getQueryObject().getReservations(null);
		}
		
		// Creates an ObservableList which will contain all the reservations.
		// It's necessary for outputting to table.
		ObservableList<Reservation> tableData = FXCollections.observableArrayList();
		
		// Fetches the query result data into the observable list.
		// See getReservations() in Query for result elements or see table column names.
		if (rs != null) {
			try {
				while (rs.next()) {
					tableData.add(new Reservation(rs.getString(1), rs.getString(2).substring(0, 10),
						rs.getString(3).substring(0, 16), rs.getString(4), rs.getString(5), 
						rs.getString(6), rs.getString(7), rs.getString(8) + " " + rs.getString(9)));
				}
			} catch (SQLException ex) {
				System.err.println("SQL error in ReservationManagementPanel");
			}
		}
		
		// Creates the table columns
		TableColumn idC = new TableColumn("ID");
		TableColumn creationDateC = new TableColumn("Created");
		TableColumn departureDateC = new TableColumn("Departure date");
		TableColumn arrivalAirportC = new TableColumn("Arrival airport");
		TableColumn seatClassC = new TableColumn("Class");
		TableColumn priceC = new TableColumn("Price");
		TableColumn statusC = new TableColumn("Status");
		TableColumn userNameC = new TableColumn("User name");
		
		// Sets columns preferred width
		idC.setPrefWidth(35);
		arrivalAirportC.setPrefWidth(139);
		
		// Specifies which fields from the inserted object should be printed in each cell
		idC.setCellValueFactory(new PropertyValueFactory<Reservation, String>("id"));
		creationDateC.setCellValueFactory(new PropertyValueFactory<Reservation, String>("creationDate"));
		departureDateC.setCellValueFactory(new PropertyValueFactory<Reservation, String>("departureDate"));
		arrivalAirportC.setCellValueFactory(new PropertyValueFactory<Reservation, String>("arrivalAirport"));
		seatClassC.setCellValueFactory(new PropertyValueFactory<Reservation, String>("seatClass"));
		priceC.setCellValueFactory(new PropertyValueFactory<Reservation, String>("price"));
		statusC.setCellValueFactory(new PropertyValueFactory<Reservation, String>("status"));
		userNameC.setCellValueFactory(new PropertyValueFactory<Reservation, String>("userName"));
		
		// Creates table and adds data to it
		table = new TableView();
		table.setItems(tableData);
		
		// Adds columns to table based on which rank has the logged in user.
		// If rank is "client", the name is not shown.
		// If not, name is shown.
		if (loggedInUser.getRank().equals("client")) {
			table.getColumns().addAll(idC, creationDateC, departureDateC, arrivalAirportC,
									  seatClassC, priceC, statusC);
		} else {
			table.getColumns().addAll(idC, creationDateC, departureDateC, arrivalAirportC,
									  seatClassC, priceC, statusC, userNameC);
		}
		
		// Creates a box for adding padding to table
		HBox tableHB = new HBox();
		tableHB.setPadding(new Insets(10, 0, 10, 0));
		tableHB.getChildren().add(table);
		
		// Adds table box to parent's center
		this.setCenter(tableHB);
		
		// Creates table selection action, which sets buttons availability based on
		// reservation status.
		table.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				// Makes sure the selection is valid (it's not -1)
				if (table.getSelectionModel().getSelectedIndex() != -1) {
					// Gets selected reservation
					Reservation selectedR = (Reservation) table.getSelectionModel().getSelectedItem();
					
					if (selectedR.getStatus().equals("confirmed")) {
						payB.setDisable(true);
						cancelB.setDisable(false);
					} else if (selectedR.getStatus().equals("unconfirmed")) {
						payB.setDisable(false);
						cancelB.setDisable(false);
					} else {
						payB.setDisable(true);
						cancelB.setDisable(true);
					}
				}
			}
		});
	}
	
	/**
	 * Creates action buttons.
	 */
	private void createButtons() {
		payB = new Button("Pay reservation");
		cancelB = new Button("Cancel reservation");
		
		// Disables the buttons. They will be enabled based on which reservation is clicked.
		payB.setDisable(true);
		cancelB.setDisable(true);
		
		// Creates action for payB
		payB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Gets selected reservation.
				Reservation selectedR = (Reservation) table.getSelectionModel().getSelectedItem();
				
				// Checks if reserved flight was completed.
				// If it did, client won't be allowed to pay for a flight that already happened.
				if (Query.getQueryObject().isReservedFlightCompleted(selectedR.getId())) {
					new FXMessageDialog(parentStage, root, "The contained flight is finished.\n" +
										"You can't pay for an already completed flight.");
				} else {
					// Sets the reservation as confirmed
					Query.getQueryObject().payReservation(selectedR.getId());
					
					// Updates the object, to show the change in the table.
					selectedR.setStatus("confirmed");
					
					// Updates table with new data.
					table.getItems().set(table.getSelectionModel().getSelectedIndex(), 
										 selectedR.duplicate());
					
					// Clears table selection and disables buttons.
					clearTableDisableButtons();
					
					// Shows a confirmation message.
					new FXMessageDialog(parentStage, root, "Reservation " + selectedR.getId() + 
										" has been successfully paid.");
				}
			}
		});
		
		// Creates action for cancelB
		cancelB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Gets selected reservation.
				Reservation selectedR = (Reservation) table.getSelectionModel().getSelectedItem();
				
				// Checks if reserved flight was completed.
				// If it did, client won't be allowed to cancel the reservation.
				if (Query.getQueryObject().isReservedFlightCompleted(selectedR.getId())) {
					new FXMessageDialog(parentStage, root, "The contained flight is finished.\n" +
													"You can't cancel the reservation anymore.");
				} else {
					// Sets the reservation as canceled.
					Query.getQueryObject().cancelReservation(selectedR.getId());
					
					// Saves which status had the selected reservation.
					boolean flightWasUnconfirmed = selectedR.getStatus().equals("unconfirmed");
					
					// Updates the object, to show the change in the table.
					selectedR.setStatus("canceled");
					
					// Updates table with new data.
					table.getItems().set(table.getSelectionModel().getSelectedIndex(), 
										 selectedR.duplicate());
					
					// Clears table selection and disables buttons.
					clearTableDisableButtons();
					
					// If initial status was unconfirmed, it just shows a confirmation message
					if (flightWasUnconfirmed) {
						new FXMessageDialog(parentStage, root, "Reservation " + selectedR.getId() + 
											" has been successfully canceled.");
					
					// The reservation was confirmed, so it also shows the amout of money received back.
					} else {
						// Shows a confirmation message and the amout of money received back.
						if (!selectedR.getSeatClass().equals("Economy")) {
							new FXMessageDialog(parentStage, root, "Reservation " + selectedR.getId() + 
												" has been successfully canceled.\n" +
												"You received " + selectedR.getPrice() + " dkk back.");
						} else {
							// Checks if there are at least 2 weeks until the departure date.
							if (Query.getQueryObject().areMoreThan14DaysUntilDeparture(selectedR.getId())) {
								new FXMessageDialog(parentStage, root, "Reservation " + selectedR.getId() + 
												" has been successfully canceled.\n" +
												"You received " + selectedR.getPrice() + " dkk back.");
							} else {
								new FXMessageDialog(parentStage, root, "Reservation " + selectedR.getId() + 
												" has been successfully canceled.\n" +
												"You canceled to late, so you won't get the money back.");
							}
						}
					}
				}
			}
		});
		
		// Creates a box that will hold the buttons
		HBox buttonsHB = new HBox(15);
		buttonsHB.setAlignment(Pos.CENTER);
		buttonsHB.getChildren().addAll(payB, cancelB);
		
		// Adds box the to bottom of the parent pane (this)
		this.setBottom(buttonsHB);
	}
	
	/**
	 * Clears table selection and disables buttons.
	 */
	private void clearTableDisableButtons() {
		table.getSelectionModel().clearSelection();
		payB.setDisable(true);
		cancelB.setDisable(true);
	}
}
