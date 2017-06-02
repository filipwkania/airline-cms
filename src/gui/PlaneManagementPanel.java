package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import operations.Query;
import operations.Regex;
import wrappers.Airport;
import wrappers.Plane;

/**
 * Contains the entire panel for administrating planes: a table to list planes, and
 * a form for editing them.
 * @author M&F
 */
public class PlaneManagementPanel extends BorderPane {
	private Stage parentStage;
	private Pane root;
	private TableView planeTable, airportTable, flightClassTable;
	private ChoiceBox modeCB, availableCB;
	private TextField flightClassIdTF, airport1TF, airport2TF, flightTimeHoursTF, 
					  flightTimeMinutesTF, priceTF;
	private Button removePlaneB;
	
	// Creates preffered width of the components (text fields and choice boxes)
    private final static double COMPONENT_WIDTH = 120;
	
	public PlaneManagementPanel(Stage parentStage, Pane root) {
		// Stage and root are necessary for FXMessageDialog
		this.parentStage = parentStage;
		this.root = root;
		
		// Creates and populates the airportsTable with all the airports
		// contained into the database.
		createAndPopulateAirportTable();
		
		// Creates and populates the flightClassesTable with all 
		// the flight classes contained into the database.
		flightClassTable = new FCTableView();
		
		// Creates components and adds them to main BorderPane(this) (top and bottom)
		createAndPopulatePlaneTable();
		createEditPanel();
	}
	
	/**
	 * Creates and populates the airportsTable with all the airports
	 * contained into the database.
	 */
	private void createAndPopulateAirportTable() {
		// Gets the planes list from the database
		ResultSet rs = Query.getQueryObject().getAirports();
		
		// Creates an ObservableList which will contain all the airports.
		// It's necessary for output to table.
		ObservableList<Airport> airportsTableData = FXCollections.observableArrayList();
		
		// Fetches the query result data into the observable list.
		// id, name, country and city are added into the Airport object
		if (rs != null) {
			try {
				while (rs.next()) {
					airportsTableData.add(new Airport(rs.getString(1), rs.getString(2), 
											rs.getString(3), rs.getString(4)));
				}
			} catch (SQLException ex) {
				System.err.println("SQL error in PlaneManagementPanel at initializeAirportTable()");
			}
		}
		
		// Creates the airport table columns
		TableColumn idColumn = new TableColumn("ID");
		TableColumn nameColumn = new TableColumn("Name");
		TableColumn countryColumn = new TableColumn("Country");
		TableColumn cityColumn = new TableColumn("City");
		
		// Specifies which elements from the inserted object should be printed in each cell
		idColumn.setCellValueFactory(new PropertyValueFactory("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
		countryColumn.setCellValueFactory(new PropertyValueFactory("country"));
		cityColumn.setCellValueFactory(new PropertyValueFactory("city"));
		
		// Sets the preferred width of the columns
		idColumn.setPrefWidth(45);
		nameColumn.setPrefWidth(125);
		countryColumn.setPrefWidth(95);
		cityColumn.setPrefWidth(95);
		
		// Adds columns and data to table
		airportTable = new TableView();
		airportTable.getColumns().addAll(idColumn, nameColumn, countryColumn, cityColumn);
		airportTable.setItems(airportsTableData);
	}
	
	/**
	 * Creates and populates the planeTable with all the planes
	 * contained into the database.
	 */
	private void createAndPopulatePlaneTable() {
		// Creates and populates the planes table with all planes.
		planeTable = new PlaneTableView(false);
		
		// Adds action Event handler to table
		planeTable.setOnMouseClicked(new PlaneManagementPanel.PlanesTableAction());
		
		//Creates HBox for adding padding to the table.
		HBox tableBox = new HBox();
		tableBox.setPadding(new Insets(10,10,10,10));
		tableBox.getChildren().add(planeTable);

		// Adds table to BorderPane (this object)
		this.setTop(tableBox);
	}
	
	/**
	 * Creates the bottom panel that will allow to add, edit and remove planes.
	 */
	private void createEditPanel() {
		// Creates a VBox that will hold the entire editing form.
		// It's showed in the bottom part of the main panel.
		VBox bottomVB = new VBox();
		
		// Creates necessary labels
		Label modeL = new Label("Mode:");
		Label flightClassIdL = new Label("Flight class ID:");
		Label airport1L = new Label("Airport 1:");
		Label airport2L = new Label("Airport 2:");
		Label flightTimeHoursL = new Label("Flight time hours:");
		Label flightTimeMinutesL = new Label("Flight time minutes:");
		Label availableL = new Label("Available:");
		Label priceL = new Label("Full price (kr):");
		
		// Creates the ChoiceBox that will change between "edit mode" and "add mode"
		// Edit mode MUST BE on index 0, so the first option, because the system checks later
		// which index is selected and it considers index 0 as edit mode.
		modeCB = new ChoiceBox(FXCollections.observableArrayList("edit planes", 
																 "add new planes"));
		modeCB.getSelectionModel().selectFirst();
		modeCB.getSelectionModel().selectedIndexProperty().addListener(new ModeSelectAction());
		
		// Creates text boxes 
		flightClassIdTF = new TextField();
		airport1TF = new TextField();
		airport2TF = new TextField();
		flightTimeHoursTF = new TextField();
		flightTimeMinutesTF = new TextField();
		availableCB = new ChoiceBox(FXCollections.observableArrayList("no", "yes"));
		priceTF = new TextField();		
		
		// Creates tooltip for components
		flightTimeHoursTF.setTooltip(new Tooltip("Repect 2 digits format: 02 or 15!"));
		flightTimeMinutesTF.setTooltip(new Tooltip("Respect 2 digits format: 02 or 15!\n" +
												   "Possible values: 00-59."));
		priceTF.setTooltip(new Tooltip("Full price means the price for first class, the 100% price.\n" +
									  "Even if plane doesn't have seats for first class, you must specify the price for it."));
		
		// Sets components width
		modeCB.setPrefWidth(COMPONENT_WIDTH);
		flightClassIdTF.setPrefWidth(COMPONENT_WIDTH);
		airport1TF.setPrefWidth(COMPONENT_WIDTH);
		airport2TF.setPrefWidth(COMPONENT_WIDTH);
		flightTimeHoursTF.setPrefWidth(COMPONENT_WIDTH);
		flightTimeMinutesTF.setPrefWidth(COMPONENT_WIDTH);
		availableCB.setPrefWidth(COMPONENT_WIDTH);
		priceTF.setPrefWidth(COMPONENT_WIDTH);
		
		// Set components properties and action listeners
		flightClassIdTF.setEditable(false);
		flightClassIdTF.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				new FlightClassSelectorPopup(parentStage, root, flightClassTable, flightClassIdTF);
			}
		});
		airport1TF.setEditable(false);
		airport1TF.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				new AirportSelectorPopup(parentStage, root, airportTable, airport1TF);
			}
		});
		airport2TF.setEditable(false);
		airport2TF.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				new AirportSelectorPopup(parentStage, root, airportTable, airport2TF);
			}
		});
		
		// Creates a GridPane to hold the left components
		GridPane leftFormGP = new GridPane();
		leftFormGP.setHgap(5);
		leftFormGP.setVgap(5);
		
		// Adds left components to container
		leftFormGP.add(modeL, 0, 0);
		leftFormGP.add(modeCB, 1, 0);
		leftFormGP.add(flightClassIdL, 0, 1);
		leftFormGP.add(flightClassIdTF, 1, 1);
		leftFormGP.add(airport1L, 0, 2);
		leftFormGP.add(airport1TF, 1, 2);
		leftFormGP.add(airport2L, 0, 3);
		leftFormGP.add(airport2TF, 1, 3);
		
		// Creates a GridPane to hold the right components
		GridPane rightFormGP = new GridPane();
		rightFormGP.setHgap(5);
		rightFormGP.setVgap(5);
		
		// Adds right components to container
		rightFormGP.add(flightTimeHoursL, 4, 0);
		rightFormGP.add(flightTimeHoursTF, 5, 0);
		rightFormGP.add(flightTimeMinutesL, 4, 1);
		rightFormGP.add(flightTimeMinutesTF, 5, 1);
		rightFormGP.add(availableL, 4, 2);
		rightFormGP.add(availableCB, 5, 2);
		rightFormGP.add(priceL, 4, 3);
		rightFormGP.add(priceTF, 5, 3);
		
		// Creates a HBox that will contain left and right form
		HBox formHB = new HBox(50);
		formHB.setAlignment(Pos.CENTER);
		formHB.getChildren().addAll(leftFormGP, rightFormGP);
		
		// Creates the necessary buttons for the edit panel
		Button savePlaneB = new Button("Save plane");
		savePlaneB.setTooltip(new Tooltip("Saves inserted data into the database."));
		removePlaneB = new Button("Remove plane");
		removePlaneB.setTooltip(new Tooltip("Removes the selected plane from the database."));
		
		// Adds actions to buttons
		savePlaneB.setOnAction(new SaveButtonAction());
		removePlaneB.setOnAction(new RemoveButtonAction());
		
		// Creates a HBox that will hold the buttons
		HBox buttonsHB = new HBox(10);
		buttonsHB.setAlignment(Pos.CENTER);
		buttonsHB.getChildren().addAll(savePlaneB, removePlaneB);
		
		// Adds some space between form and buttons
		buttonsHB.setPadding(new Insets(20, 0, 10, 0));
		
		// Adds form box and buttons box to main container
		bottomVB.getChildren().addAll(formHB, buttonsHB);
		
		// Adds the etire form into the main panel
		setBottom(bottomVB);
	}
	
	/**
	 * Clears the form text fields and choice box, and clears the table selection.
	 */
	private void clearFormAndTableSelection() {
		flightClassIdTF.setText("");
		airport1TF.setText("");
		airport2TF.setText("");
		flightTimeHoursTF.setText("");
		flightTimeMinutesTF.setText("");
		availableCB.getSelectionModel().clearSelection();
		priceTF.setText("");
		planeTable.getSelectionModel().clearSelection();
	}
	
	/**
	 * Contains the action that happens when user selects a row in the table.<br>
	 * Practically, populates the edit form with the selected plane's data.
	 */
	private class PlanesTableAction implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent me) {
			// Checks if selected row is not -1
			if (planeTable.getSelectionModel().getSelectedIndex() != -1) {
				// Gets selected item.
				Plane selectedPlane = (Plane) planeTable.getSelectionModel().getSelectedItem();
				
				// Populates the edit form with the plane's data
				flightClassIdTF.setText(selectedPlane.getFlightClassID());
				airport1TF.setText(selectedPlane.getAirport1ID());
				airport2TF.setText(selectedPlane.getAirport2ID());
				flightTimeHoursTF.setText(selectedPlane.getFlightLength().substring(0,2));
				flightTimeMinutesTF.setText(selectedPlane.getFlightLength().substring(3, 5));
				availableCB.getSelectionModel().select(selectedPlane.getAvailability());
				priceTF.setText(selectedPlane.getFullPrice());
			}
		}
	}
	
	/**
	 * Contains the action that happens when user clicks on "Remove" button.<br>
	 * Practically, removes the plane from database and table,
	 * and the clears the form and the table selection.
	 */
	private class RemoveButtonAction implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent ae) {
			// If no plane is selected in the table, shows an error.
			if (planeTable.getSelectionModel().getSelectedIndex() == -1) {
				new FXMessageDialog(parentStage, root, "Please select the plane you want to remove.");
			} else {
				// Gets selected Plane
				Plane selectedPlane = (Plane) planeTable.getSelectionModel().getSelectedItem();
				
				// Checks if selected plane has schedules as children.
				// If it has, deny removal.
				if (Query.getQueryObject().checkIfPlaneHasSchedules(selectedPlane.getID())) {
					// Shows an error message.
					new FXMessageDialog(parentStage, root, "System can't delete this plane,\n" +
										"because there are flight schedules for it!");
				// Performs removal
				} else {
					// Removes plane form database
					Query.getQueryObject().removePlane(selectedPlane.getID());

					// Removes user from table
					planeTable.getItems().remove(selectedPlane);

					// Shows a confirmation message
					new FXMessageDialog(parentStage, root, "Plane " + selectedPlane.getID() +
														" has been succesfully deleted!");

					// Clears the form and the table selection
					clearFormAndTableSelection();
				}
			}
		}
	}
	
	/**
	 * Contains the action that happens when user clicks on "Save" button.<br>
	 * It has different functionality, depending on which mode is selected: edit or add.<br>
	 * When edit mode, the data is saved for the selected plane.<br>
	 * When add mode, the data is added as a new plane.
	 */
	private class SaveButtonAction implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent ae) {
			// Checks if edit mode is active, but no plane is selected from table
			// When modeCB.getSelectionModel().getSelectedIndex() is 0 it means edit mode
			if ((modeCB.getSelectionModel().getSelectedIndex() == 0) &&
				(planeTable.getSelectionModel().getSelectedIndex() == -1)) {
				new FXMessageDialog(parentStage, root, "Please select the plane  you want to edit!");
			
			// Checks if a flight class is selected
			} else if (flightClassIdTF.getText().isEmpty()) {
				new FXMessageDialog(parentStage, root, "Please select a flight class!");
			
			// Checks if airport 1 is selected
			} else if (airport1TF.getText().isEmpty()) {
				new FXMessageDialog(parentStage, root, "Please select airport 1!");
			
			// Checks if airport 2 is selected
			} else if (airport2TF.getText().isEmpty()) {
				new FXMessageDialog(parentStage, root, "Please select airport 2!");
			
			// Checks if airport 1 == airport 2
			} else if (airport1TF.getText().equals(airport2TF.getText())) {
				new FXMessageDialog(parentStage, root, "Airport 1 and 2 can't be the same!");
				
			// Checks if flightTimeHoursTF is contains just 2 digits
			} else if (!Regex.matchDoubleDigit(flightTimeHoursTF.getText())) {
				new FXMessageDialog(parentStage, root, "Please write valid flight length hours!\n" +
													   "You must respect this format: hh. (ex: 02 or 14)");
				
			// Checks if flightTimeMinutesTF is contains just 2 digits
			} else if (!Regex.matchDoubleDigit(flightTimeMinutesTF.getText())) {
				new FXMessageDialog(parentStage, root, "Please write valid flight length minutes!\n" +
													   "You must respect this format: mm. (ex: 02 or 14)");
			
			// Checks if flightTimeMinutesTF is less than 60.
			// There's no need to check for less than 0, because the Regex above checked that.
			} else if ((Integer.parseInt(flightTimeMinutesTF.getText()) > 59)) {
				new FXMessageDialog(parentStage, root, "Please write a valid number of minutes (0-59)!");
				
			// Checks if availability status is selected
			} else if (availableCB.getSelectionModel().getSelectedIndex() == -1) {
				new FXMessageDialog(parentStage, root, "Please select availability status!");
			
			// Checks if full price is specified and valid
			} else if (!Regex.matchFullPrice(priceTF.getText())) {
				new FXMessageDialog(parentStage, root, "Please write a valid full price! (1-99999)");
			
			// If you are here, it means that input data is ok.
			// Now the data will be saved into database, based on which mode is selected.
			// Checks which mode is selected (0 = edit, 1 = add) and performs the query.
			} else if (modeCB.getSelectionModel().getSelectedIndex() == 0) {
				// Edit mode is selected, so we need the id of the selected plane
				// in order to perform the query
				Plane selectedPlane = (Plane) planeTable.getSelectionModel().getSelectedItem();
				
				// Prepares flightTime and hour in this format hh:mm
				String formattedFlightLength = flightTimeHoursTF.getText() + ":" +
									flightTimeMinutesTF.getText();
				
				// If plane is marked as unavailable, but there are schedules for it,
				// deny the modification. Schedules must be removed firstly.
				// Checks if there are schedules for this plane.
				if (availableCB.getSelectionModel().getSelectedIndex() == 0 && 
					Query.getQueryObject().checkIfPlaneHasSchedules(selectedPlane.getID())) {
					new FXMessageDialog(parentStage, root, "You can't make this plane unavailable,\n" +
										"because there are flight schedules for it.");	
				} else {
					// Performs the plane update query
					Query.getQueryObject().updatePlane(selectedPlane.getID(), flightClassIdTF.getText(),
							airport1TF.getText(), airport2TF.getText(), formattedFlightLength,
							(String) (availableCB.getSelectionModel().getSelectedItem()),
							priceTF.getText());

					// Checks if updated plane has a different flightClassId now.
					// If it has, get the data of the new flightClassId and add it into plane object,
					// in order to be shown in table
					if (!flightClassIdTF.getText().equals(selectedPlane.getFlightClassID())) {
						try {
							// Gets the new flight class data
							ResultSet rs = Query.getQueryObject().getFlightClass(flightClassIdTF.getText());

							// Adds data into plane object if rs is valid
							if (rs != null && rs.next()) {
								selectedPlane.setFlightClassType(rs.getString(1));
								selectedPlane.setFlightClassName(rs.getString(2));
							}
						} catch (SQLException ex) {
							System.err.println("Error on getting flight class data, on update plane.");
						}

					}

					// Updates the Plane object to show the new data in the table
					selectedPlane.setFlightClassID(flightClassIdTF.getText());
					selectedPlane.setAirport1ID(airport1TF.getText());
					selectedPlane.setAirport2ID(airport2TF.getText());
					selectedPlane.setFlightLength(formattedFlightLength);
					selectedPlane.setAvailability((String) (availableCB.getSelectionModel().getSelectedItem()));
					selectedPlane.setFullPrice(priceTF.getText());

					// Updates table data.
					planeTable.getItems().set(planeTable.getSelectionModel().getSelectedIndex(),
											selectedPlane.duplicate());

					// Shows a confirmation message
					new FXMessageDialog(parentStage, root, "Plane has been successfully updated.");

					// Clears form and table selection in order to protect from multiple saves
					clearFormAndTableSelection();
				}
			
			// If you're here, it means that "add" mode is selected
			// So we have to insert a new plane with the specified details
			} else {
				// Prepares flightTime and hour in this format hh:mm
				String formattedFlightLength = flightTimeHoursTF.getText() + ":" +
									flightTimeMinutesTF.getText();
				
				// Performs the insert query
				Query.getQueryObject().insertPlane(flightClassIdTF.getText(), airport1TF.getText(), 
						airport2TF.getText(), formattedFlightLength,
						(String) (availableCB.getSelectionModel().getSelectedItem()), priceTF.getText());
				
				// Gets id of the recently added plane, necessary for creating the Plane object
				String planeId = Query.getQueryObject().getLastAddedPlaneId();
				
				// Gets details of the selected flight class
				ResultSet rs = Query.getQueryObject().getFlightClass(flightClassIdTF.getText());

				String flightClassType = null;
				String flightClassName = null;
				
				// Puts details into Strings
				try {
					if (rs != null && rs.next()) {
						flightClassType = rs.getString(1);
						flightClassName = rs.getString(2);
					}
				} catch (SQLException ex) {
					Logger.getLogger(PlaneManagementPanel.class.getName()).log(Level.SEVERE, null, ex);
				}
				
				// Checks if flight class details are not null
				if (flightClassName == null || flightClassType == null) {
					System.err.println("Error on getting fc details, on add plane in P.Man.P!");
					
					// Stops method execution
					return;
				}
				
				// Everything is ok. Adds all data into a new Plane object
				Plane newPlane = new Plane(planeId, flightClassIdTF.getText(), flightClassType, 
					flightClassName, airport1TF.getText(), airport2TF.getText(), formattedFlightLength,
					(String) (availableCB.getSelectionModel().getSelectedItem()), priceTF.getText());
				
				// Adds object into table
				planeTable.getItems().add(newPlane);
				
				// Shows a confirmation message
				new FXMessageDialog(parentStage, root, "Plane has been successfully added.");
				
				// Clears form and table selection in order to protect from multiple saves
				clearFormAndTableSelection();
			}
		}
	}
	
	/**
	 * Contains the action that happens when mode (edit/add) is changed.
	 */
	private class ModeSelectAction implements ChangeListener<Object> {
		@Override
		public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
			if (modeCB.getSelectionModel().getSelectedIndex() == 0) {
				// Clears the form if user has some data in it.
				clearFormAndTableSelection();
				
				// Enables remove button and table
				removePlaneB.setDisable(false);
				planeTable.setDisable(false);
			} else if (modeCB.getSelectionModel().getSelectedIndex() == 1) {
				// Clears the form if user has some data in it
				clearFormAndTableSelection();
				
				// Disables remove button and table
				removePlaneB.setDisable(true);
				planeTable.setDisable(true);
			}
		}
	}
}
