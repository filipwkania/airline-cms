package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
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
import wrappers.FlightSchedule;

/**
 * Contains table and edit panel for managing flight Schedules.
 * @author M&F
 */
public class FSManagementPanel extends BorderPane{
	private Stage parentStage;
	private Pane root;
	private TableView table;
	private PlaneTableView planeTableView;
	private TextField planeIdTF ,dateTF, timeTF;
	private ChoiceBox modeCB;
	private Button saveB, removeB;
	
    // Creates preffered width of the components (text fields and choice boxes)
    private final static double COMPONENT_WIDTH = 120;
	
	public FSManagementPanel(Stage parentStage, Pane root){
		this.parentStage = parentStage;
		this.root = root;
		
		// Prepares planeTableView for later use
		planeTableView = new PlaneTableView(true);
		
		// Creates and populates flight Table, and sets it to top position of the pane.
		createTable();
		
		// Creates Edit panel and sets it to bottom position of the pane.
		createEditPanel();
	}
	
	/**
	 * Creates and populates flight table.
	 */
	private void createTable(){
		// Creates table that will hold the flight schedules
		table = new TableView();
		
		// Creates a list that will contain the flight schedules
		ObservableList<FlightSchedule> tableData = FXCollections.observableArrayList();
		
		// Creates columns for table
		TableColumn planeIDCol = new TableColumn("Plane ID");
		TableColumn fromCol = new TableColumn("Departure from:");
		TableColumn toCol = new TableColumn("Arrival to:");
		TableColumn airport1Col = new TableColumn("Airport");
		TableColumn airport2Col = new TableColumn("Airport");
		TableColumn date1Col = new TableColumn("Date");
		TableColumn date2Col = new TableColumn("Date");
		TableColumn time1Col = new TableColumn("Time");
		TableColumn time2Col = new TableColumn("Time");
		TableColumn idCol = new TableColumn("ID");
		
		// Sets properties.
		idCol.setCellValueFactory(new PropertyValueFactory<FlightSchedule,String>("id"));
		planeIDCol.setCellValueFactory(new PropertyValueFactory<FlightSchedule,String>("planeId"));
		airport1Col.setCellValueFactory(new PropertyValueFactory<FlightSchedule,String>("airport1"));
		airport2Col.setCellValueFactory(new PropertyValueFactory<FlightSchedule,String>("airport2"));
		date1Col.setCellValueFactory(new PropertyValueFactory<FlightSchedule,String>("date1"));
		date2Col.setCellValueFactory(new PropertyValueFactory<FlightSchedule,String>("date2"));
		time1Col.setCellValueFactory(new PropertyValueFactory<FlightSchedule,String>("time1"));
		time2Col.setCellValueFactory(new PropertyValueFactory<FlightSchedule,String>("time2"));
		
		//Setting width of columns.
		idCol.setPrefWidth(35);
		planeIDCol.setPrefWidth(58);
		airport1Col.setPrefWidth(58);
		airport2Col.setPrefWidth(58);
		time1Col.setPrefWidth(53);
		time2Col.setPrefWidth(53);
		date1Col.setPrefWidth(103);
		date2Col.setPrefWidth(103);
		
		// Sets the nested columns
		fromCol.getColumns().addAll(airport1Col, date1Col, time1Col);
		toCol.getColumns().addAll(airport2Col, date2Col, time2Col);
		
		// Adds columns to table
		table.getColumns().addAll(idCol, planeIDCol,fromCol, toCol);
		
		// Sets table height
		table.setPrefHeight(243);
		
		// Gets data from database.
		ResultSet rs = Query.getQueryObject().getFlightSchedules();
		
		if (rs != null) {
			try {
				while (rs.next()) {
					String ID = rs.getString(1);
					String planeID = rs.getString(2);
					String airport1 = rs.getString(3);
					String airport2 = rs.getString(4);
					String departue = rs.getString(5);
					String arrival = rs.getString(6);

					// Adds schedule into table
					tableData.add(new FlightSchedule(ID, planeID, airport1, departue.substring(0, 10), departue.substring(11,16),
						airport2, arrival.substring(0, 10),arrival.substring(11,16)));
				}
			} catch (SQLException e) {
				System.err.println("Problem occured while getting schedules from database in FSManagementPanel.");
			}
		}
		
		// Sets table data.
		table.setItems(tableData);

		// Adds Mouse Click Handler for table
		table.setOnMouseClicked(new TableAction());
		
		// Creates a HBox that will hold the table
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setPadding(new Insets(10,10,10,10));
		
		// Adds table to hBox, in order to add some padding to it.
		hBox.getChildren().add(table);
		
		// Adds table container to parent (this)
		this.setTop(hBox);
	}
	
	/**
	 * Creates Edit panel and sets it to bottom position of the pane.
	 */
	private void createEditPanel(){
		// Creates a Grid Pane that will hold the left components.
		GridPane leftGP = new GridPane();
		leftGP.setHgap(5);
		leftGP.setVgap(5);
		
		// Creates a Grid Pane that will hold the right components.
		GridPane rightGP = new GridPane();
		rightGP.setHgap(5);
		rightGP.setVgap(5);
		
		// Creates TextFields
		planeIdTF = new TextField();
		dateTF = new TextField();
		timeTF = new TextField();
		
		// Adds a Tooltip for timeTF
		timeTF.setTooltip(new Tooltip("Ex: 14:35 or 01:17"));
		
		// Sets the mouse clicked event handler that will show the plane selector popup.
		planeIdTF.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e) {
				new PlaneSelectorPopup(parentStage, root, planeTableView, planeIdTF);
			}
		});

		// Sets the mouse clicked event handler that will show the date selector popup.
		dateTF.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e) {
				new FXDateSelectorPopup(parentStage, root, dateTF);
			}
		});
		
		// Block editing possibility, because the only popups will be used for setting them.
		dateTF.setEditable(false);
		planeIdTF.setEditable(false);
		
		// Creates Labels
		Label planeIDL = new Label("Plane ID:");
		Label dateL = new Label("Departure date:");
		Label modeL = new Label("Mode:");
		Label timeL = new Label("Departure time:");
		
		// Creates the mode ChoiceBox and selects the first element
		modeCB = new ChoiceBox(FXCollections.observableArrayList("edit flight","add flight"));
		modeCB.getSelectionModel().selectFirst();
		
		// Add action listener for modeCB
		modeCB.getSelectionModel().selectedIndexProperty().addListener(new ModeSelectionAction());
		
		// Sets components width.
		planeIdTF.setPrefWidth(COMPONENT_WIDTH);
		dateTF.setPrefWidth(COMPONENT_WIDTH);
		modeCB.setPrefWidth(COMPONENT_WIDTH);
		timeTF.setPrefWidth(COMPONENT_WIDTH);
		
		// Adds components in leftGP.
		leftGP.add(modeL, 0, 0);	
		leftGP.add(modeCB, 1, 0);
		leftGP.add(planeIDL, 0, 1);
		leftGP.add(planeIdTF, 1,1);
		
		//Setting components in rigthGP.		
		rightGP.add(dateL, 0,0);
		rightGP.add(dateTF, 1, 0);
		rightGP.add(timeL, 0,1);
		rightGP.add(timeTF,1,1);
		
		// Creates a HBox that will hold the edit form (leftGP and rightGP)
		HBox paneBox = new HBox(60);
		paneBox.getChildren().addAll(leftGP, rightGP);
		paneBox.setAlignment(Pos.CENTER);
		
		// Creates buttons.
		saveB = new Button("Save flight");
		removeB = new Button("Remove flight");
		
		// Creates Tooltips for buttons.
		saveB.setTooltip(new Tooltip("Saves the flight schedule into the database."));
		removeB.setTooltip(new Tooltip("Removes selected flight from the database."));
		
		// Creates action listener for save Button.
		saveB.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				// Checks if components are filled properly.
				//Checks if the planeIDTF is filled.
				if (planeIdTF.getText().isEmpty()) {
					new FXMessageDialog(parentStage, root, "Please input plane to Plane ID text field.");
				
				//Checks if the dateTF is filled.
				} else if (dateTF.getText().isEmpty()) {
					new FXMessageDialog(parentStage, root, "Please input departure date.");
				
				//Checks if the timeTF is filled.
				} else if (timeTF.getText().isEmpty()) {
					new FXMessageDialog(parentStage, root, "Please input departure time.");
				
				//Checks if the timeTF input is correct.
				} else if (timeCheck() != true) {
					new FXMessageDialog(parentStage, root, "Please input a valid departure time!\n"
							+ "Correct example: 01:46 or 17:33");
				} else if (checkToday() != true) {
					new FXMessageDialog(parentStage, root, "Departure time must be later than current time!");
				
				// Data is valid, so we continue the action
				} else {
					// Checks which mode is selected. (0 = edit, 1 = add)
					if (modeCB.getSelectionModel().getSelectedIndex() == 0) {
						// Checks if there is a schedule selected
						if (table.getSelectionModel().getSelectedIndex() == -1) {	
							// Shows an error message
							new FXMessageDialog(parentStage, root, "Please select the flight you want to modify.");
						} else {
							// Gets selected schedule
							FlightSchedule fs = (FlightSchedule) table.getSelectionModel().getSelectedItem();

							// Updates flight in the DB.
							Query.getQueryObject().updateFlightSchedule(fs.getId(), planeIdTF.getText(),
																		dateTF.getText() + " " + timeTF.getText());

							// Clears components.
							clearFormAndTableSelection();

							// Updates table by recreating it.
							createTable();

							// Shows a confirmation message.
							new FXMessageDialog(parentStage, root, "Flight has been successfuly changed!");
						}
					} else {
						// Adds flight to database.
						Query.getQueryObject().addFlightSchedule(planeIdTF.getText(),
																(dateTF.getText()+ " " + timeTF.getText()));

						// Clears form and table selection in order to avoid duplicate data.
						clearFormAndTableSelection();
						
						// Updates table by recreating it.
						createTable();
						
						// By recreating, table becomes enable, but we don't want that.
						table.setDisable(true);
						
						// Shows a confirmation message.
						new FXMessageDialog(parentStage, root, "Flight has been successfully added!");
					}
				}
			}
		});
		
		// Creates action listener for Remove button.
		removeB.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				// Checks if a flight schedule is selected for removal
				if (table.getSelectionModel().getSelectedIndex() == -1) {
					// Shows an error message.
					new FXMessageDialog(parentStage, root, "Please select the flight that should be removed.");
				} else {
					// Gets the selected flight schedule
					FlightSchedule selectedFS = (FlightSchedule) table.getSelectionModel().getSelectedItem();
					
					// Checks if there are reservations for this flight schedule.
					// If there are, deny removal.
					if (Query.getQueryObject().checkIfFlightScheduleHasReservations(selectedFS.getId())) {
						// Shows an error message.
						new FXMessageDialog(parentStage, root, "System can't delete this flight schedule,\n" +
											"because there are reservations for it!");
					} else {
						//Removes flight from DB.
						Query.getQueryObject().removeFlightSchedule(selectedFS.getId());

						// Removes flight schedule from table
						table.getItems().remove(selectedFS);

						// Clears form and table selection.
						clearFormAndTableSelection();

						// Shows a confirmation message.
						new FXMessageDialog(parentStage, root, "Flight schedule " + selectedFS.getId() + " was successfully deleted!");
					}
				}
			}
		});
		
		// Creates a box that will contain the buttons.
		HBox buttonContainerHB = new HBox();
		buttonContainerHB.setSpacing(10);
		buttonContainerHB.setAlignment(Pos.CENTER);
		buttonContainerHB.setPadding(new Insets(20, 0, 10, 0));		
		
		// Adds buttons to box
		buttonContainerHB.getChildren().addAll(saveB, removeB);
		
		// Creates a VBox that will hold the entire edit form (text fields, labels and buttons).
		VBox formContainerVB = new VBox();	
		formContainerVB.setAlignment(Pos.CENTER);
		formContainerVB.getChildren().addAll(paneBox,buttonContainerHB);	
		
		// Sets form container to bottom part of parent (this).
		this.setBottom(formContainerVB);
	}

	/**
	 * Checks if the inputted time has correct format.
	 * @return checking result
	 */
	public boolean timeCheck(){
		// Gets inputted time
		String time = timeTF.getText();
		
		// Checks if the time text has 5 characters.
		if (time.length() != 5) {
			return false;
		
		// Checks the time.
		} else if (Integer.parseInt(time.substring(0, 2)) > 23 //Checks if hour is bigger than 23
				|| Integer.parseInt(time.substring(0, 2)) < 0 // Checks if hours is lower than 0
				|| Integer.parseInt(time.substring(3, 5)) > 59 // Checks if minutes are bigger than 59
				|| Integer.parseInt(time.substring(3, 5)) < 0) { //Checks if minutes are lower than 0
			return false;
		
		// Checks if the character between minutes and hours is a colon.
		} else if (time.charAt(2)!=':') {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Checks if the given day is the current day.
	 * @return checking result
	 */
	public boolean checkToday(){
		Calendar cal = Calendar.getInstance();

		// Gets data from text fields
		String date = dateTF.getText();
		String hour = timeTF.getText().substring(0, 2);
		
		// Compares the given and current date.
		if (Integer.parseInt(date.substring(0, 4)) == (cal.get(Calendar.YEAR))
				&& Integer.parseInt(date.substring(5, 7)) == (cal.get(Calendar.MONTH)+1)
				&& Integer.parseInt(date.substring(8, 10)) == (cal.get(Calendar.DAY_OF_MONTH))
				&& Integer.parseInt(hour) < cal.get(Calendar.HOUR_OF_DAY)){
			
			// Everything is ok, so false is returned.
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Clears components and table selection.
	 */
	public void clearFormAndTableSelection(){
		planeIdTF.setText("");
		dateTF.setText("");
		timeTF.setText("");
		
		table.getSelectionModel().clearSelection();
	}
	
	/**
	 * Contains the action that happens when a mode is selected from modeCB.
	 */
	private class ModeSelectionAction implements ChangeListener<Object>{
		@Override
		public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
			// Checks if the new value is 1. 1 means add mode
			if (modeCB.getSelectionModel().getSelectedIndex() == 1) {
				// Clears components and table selection.
				clearFormAndTableSelection();
				
				// Disables the table and remove button.
				table.setDisable(true);
				removeB.setDisable(true);
				
			// Edit mode was selected
			} else {
				// Clears components and table selection.
				clearFormAndTableSelection();
				
				// Enables table and remove button
				table.setDisable(false);
				removeB.setDisable(false);
			}
		}
	}
	
	/**
	 * Contains the action that happens when a table item is selected.
	 */
	private class TableAction implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent e) {
			// Checks if selected row is not -1
			if (table.getSelectionModel().getSelectedIndex() != -1) {
				// Gets selected item
				FlightSchedule s = (FlightSchedule) table.getSelectionModel().getSelectedItem();
				
				// Fills components with data from selected table item.
				planeIdTF.setText(s.getPlaneId());
				dateTF.setText(s.getDate1());
				timeTF.setText(s.getTime1());
			}
		}
	}
}
