package gui;

import java.sql.ResultSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import operations.Query;
import operations.Regex;
import wrappers.FlightClass;

/**
 * Contains the entire panel for administrating class flights: a table to list them,
 * and a form for editing them.
 * @author M&F
 */
public class FCManagementPanel extends BorderPane{
    private Stage parentStage;
    private Pane root;
    private TableView table;
    private Label seatsL, fcSeatsL, ecSeatsL, ccSeatsL, fcNameL, modeL, planeTypeL, newTypeL;
    private TextField fcSeatsTF, ecSeatsTF, ccSeatsTF, fcNameTF, newTypeTF;
    private Button saveB, removeB;
    private ChoiceBox modeCB, planeTypeCB;
    
    // Creates preffered width of the components (text fields and choice boxes)
    private final static double COMPONENT_WIDTH = 120;
    
    
    public FCManagementPanel(Stage parentStage, Pane root){
        this.parentStage = parentStage;
        this.root = root;
		
		createTable();
		createEditPanel();
	}
	
	/**
	* Creates and populates the flight class tables table.
	*/
	private void createTable(){
		table = new FCTableView();
		table.setOnMouseClicked(new TableAction());
		
        // Creates a HBox for padding around the table
        HBox tableBox = new HBox();
        tableBox.setPadding(new Insets(10, 0, 10, 10));
        tableBox.getChildren().add(table);
		
		this.setLeft(tableBox);		
	}

	private void createEditPanel() {
		// Creates labels.
		seatsL = new Label("Number of seats:");
        fcSeatsL = new Label("First Class:");
        ecSeatsL = new Label("Economy Class:");
        ccSeatsL = new Label("Coach Class:");
        fcNameL = new Label("Class name:");
        modeL = new Label("Mode:");
        planeTypeL = new Label("Plane type:");
		newTypeL = new Label("New plane type:");
        
        // Creates TextFields.
        fcSeatsTF = new TextField();
        ccSeatsTF = new TextField();
        ecSeatsTF = new TextField();
        fcNameTF = new TextField();
		newTypeTF = new TextField();
		
		// Sets New type components invisible.
		newTypeL.setVisible(false);
		newTypeTF.setVisible(false);
        
        // Creates ChoiceBox
        modeCB = new ChoiceBox(FXCollections.observableArrayList("edit flight class","add flight class"));
        planeTypeCB = new ChoiceBox();
		
		// Adds planeTypes to planeTypeCB
		populateTypeCB();
		
		// Adds ChangeListener to planeTypeCB
		planeTypeCB.getSelectionModel().selectedIndexProperty().addListener(new TypeSelectAction());
        
		// Selects the first option ("Edit" mode is the first one).
        modeCB.getSelectionModel().selectFirst();        
        
        // Adds change listener for modeCB ChoiceBox
        modeCB.getSelectionModel().selectedIndexProperty().addListener(new ModeSelectAction());
        
        // Sets size of components.
		modeCB.setMaxWidth(COMPONENT_WIDTH);
		planeTypeCB.setMaxWidth(COMPONENT_WIDTH);
		newTypeTF.setMaxWidth(COMPONENT_WIDTH);
		fcNameTF.setMaxWidth(COMPONENT_WIDTH);
        fcSeatsTF.setMaxWidth(COMPONENT_WIDTH);
        ccSeatsTF.setMaxWidth(COMPONENT_WIDTH);
        ecSeatsTF.setMaxWidth(COMPONENT_WIDTH);
        
        // Creates Buttons.
        saveB = new Button("Save");
		saveB.setTooltip(new Tooltip("Saves inserted data into the database."));
        removeB = new Button("Remove");
		removeB.setTooltip(new Tooltip("Removes the selected flight class from the database."));
        
        // Adds ActionListeners for button
        saveB.setOnAction(new SaveButtonAction());
        removeB.setOnAction(new RemoveButtonAction());
        
        // Creates hBox for Buttons.
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(saveB,removeB);
        
        // Creates the GridPane that will contain the form components.
        GridPane gp = new GridPane();
        gp.setHgap(2);
        gp.setVgap(8);
        
        // Sets components in the right position in the GridPane.
        gp.add(modeL,1,1); // 1 column 1 row
        gp.add(modeCB,2,1); // 2 columne 1 row
		gp.add(planeTypeL,1,2); //1 column 2 row
		gp.add(planeTypeCB, 2,2); //2 column 2 row
        gp.add(newTypeL,1,3);//1 column 3 row
        gp.add(newTypeTF,2,3);// 2 column 3 row
        gp.add(fcNameL,1,4); // 1 column 4 row
        gp.add(fcNameTF,2,4); // 2 column 4 row
        gp.add(seatsL,1,5); // 1 column 5 row
        gp.add(fcSeatsL,1,6); //1 column 6 row
        gp.add(ccSeatsL,1,7); //1 column 7 row
        gp.add(ecSeatsL,1,8); //1 column 8 row
        gp.add(fcSeatsTF,2,6); //2 column 6 row
        gp.add(ccSeatsTF,2,7); //2 column 7 row
        gp.add(ecSeatsTF,2,8); //2 column 8 row
        
        // Creates Hox that will center the horizontal position of the GridPane gp.
        HBox hBox = new HBox();
        hBox.getChildren().addAll(gp);
        hBox.setAlignment(Pos.CENTER);
        
        // Creates VBox that will center the vertical position og the GridPane gp.
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.getChildren().addAll(hBox, buttonBox);
        vBox.setAlignment(Pos.CENTER);
        
        // Sets VBox in the center part of BorderPane (this).
        this.setCenter(vBox);   
	}
	
	/**
	 * Adds types of planes to modeCB ChoiceBox.
	 */
	private void populateTypeCB(){		
		try {
			// Gets ResultSet that is containing all planeTypes
			ResultSet rs = Query.getQueryObject().getFlightClassTypes();
			
			// Creates observableArrayList for the planeTypeCB
			ObservableList<String> types = FXCollections.observableArrayList();
			
			// Populates ObservableArrayList with planeTypes.
			while (rs.next()) {
				types.add(rs.getString(1));
			}
			
			// Adds "New type" option as the last one.
			// It's important to be the last one, because it's checked later.
			types.add("New type");
			
			// Sets types as a data in planeTypesCB.
			planeTypeCB.setItems(types);
		} catch(Exception ex) {
			System.err.println("Problem occured while populating planeTypeCB in FCManagementPanel!");
		}
	}
	
	/**
	 * Clears the form text fields and choice box, and clears the table selection.
	 */
	private void clearFormAndTableSelection() {
		fcNameTF.setText("");
		fcSeatsTF.setText("");
		ccSeatsTF.setText("");
		ecSeatsTF.setText("");
		planeTypeCB.getSelectionModel().clearSelection();
		newTypeTF.setText("");
		
		table.getSelectionModel().clearSelection();
    }
	
	/**
	 * Removes unused flight class types.
	 * This action is neccesary after editing or removing a plane.
	 * Maybe the FCType used by the modified/deleted plane is not used any more,
	 * so we don't want to keep in DB unnecessary data.
	 */
	private void removeUnusedFlightClassTypes() {
		Query.getQueryObject().removeUnusedFlightClassTypes();
	}
	
	/**
	 * Contains the action that happens when user selects a row in the table.<br>
	 * Practically, populates the edit form with data of the selected flight class.
	 */
    private class TableAction implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent me) {
            // Checks if a valid row was selected (not -1)
            if (table.getSelectionModel().getSelectedIndex() != -1) {
				// Gets the selected object
				FlightClass fc = (FlightClass) table.getSelectionModel().getSelectedItem();
				
                // Populates the form with selected object's data.
                fcNameTF.setText(fc.getName());
                fcSeatsTF.setText(fc.getFcSeatsNo());
                ccSeatsTF.setText(fc.getCcSeatsNo());
                ecSeatsTF.setText(fc.getEcSeatsNo());
                planeTypeCB.getSelectionModel().select(fc.getType());
				
            }
        }
    }
	
    //Class for handling Remove button events.
    private class RemoveButtonAction implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent ae) {
			// If no flight class is selected in the table, shows an error.
            if (table.getSelectionModel().getSelectedIndex() == -1) {
				new FXMessageDialog(parentStage, root, "Please select the flight class you want to remove.");
			} else {
				// Gets the selected Flight Class
                FlightClass fc = (FlightClass) table.getSelectionModel().getSelectedItem();
				
				// Checks if selected flight class has planes as children.
				// If it has, deny removal.
				if (Query.getQueryObject().checkIfFlightClassHasPlanes(fc.getID())) {
					// Shows an error message.
					new FXMessageDialog(parentStage, root, "System can't delete this flight class,\n" +
										"because there are planes using it!");
				// Performs removal
				} else {
					// Removes the flight class from the database
					Query.getQueryObject().removeFlightClass(fc.getID());

					// Removes the flight class from the table
					table.getItems().remove(fc);

					// Makes sure we don't have an unused flight class type.
					removeUnusedFlightClassTypes();
					
					// Repopulates the typeCB, because maybe a type doesn't exist anymore.
					populateTypeCB();

					// Shows a confirmation message
					new FXMessageDialog(parentStage, root, "Flight class " + fc.getType() + " " + 
												fc.getName() + " has been succesfully deleted!");
					
					// Clears form and table selection
					clearFormAndTableSelection();
				}
            }
        }
    }
	
    /**
	 * Contains the action that happens when save button is pressed.
	 */
    private class SaveButtonAction implements EventHandler<ActionEvent>{        
        @Override
        public void handle(ActionEvent ae) {
			// Checks if edit mode is active, but no flight class is selected from table.
			// When modeCB.getSelectionModel().getSelectedIndex() is 0 it means edit mode.
			if ((modeCB.getSelectionModel().getSelectedIndex() == 0) &&
				(table.getSelectionModel().getSelectedIndex() == -1)) {
				new FXMessageDialog(parentStage, root, "Please select the flight class you want to edit!");
				
				// Stops method execution
				return;
			}
			
            //Checks if a plane type is selected.
            if (planeTypeCB.getSelectionModel().getSelectedIndex() == -1) {
                new FXMessageDialog(parentStage, root, "Please select a plane type!");
				
			//Checks if the type name is inputted correctly.
			} else if (planeTypeCB.getSelectionModel().getSelectedIndex() == planeTypeCB.getItems().size()-1
					&& !Regex.matchFlightClass(newTypeTF.getText())) {
				new FXMessageDialog(parentStage, root, "Please input the type name!\n" + 
														 "Type can contain only A-Z, a-z, 0-9 and '-' characters!");
							
			//Checks if the class name is inputted correctly.
			} else if (!Regex.matchFlightClass(fcNameTF.getText())) {
				new FXMessageDialog(parentStage, root, "Please input the class name!\n" + 
														 "Class can contain only A-Z, a-z, 0-9 and '-' characters!");
			
			//Checks if the numbers of seats was inputted correctly.
			} else if (!Regex.matchSeatsNo(fcSeatsTF.getText()) || !Regex.matchSeatsNo(ecSeatsTF.getText()) ||
					   !Regex.matchSeatsNo(ccSeatsTF.getText())) {
				new FXMessageDialog(parentStage, root, "Please input correct number of seats! (range: 0-9999)");
			
			// Performs the saving process
			} else {
				// String for the type name.
				String classTypeName = null;
				
				//Checks if the New type is chosen, if yes then type name will be taken from newTypeTF
				if (planeTypeCB.getSelectionModel().getSelectedIndex() == planeTypeCB.getItems().size()-1){
					classTypeName = newTypeTF.getText();
				
				//Otherwise it takes type name from ChoiceBox.
				} else {
					classTypeName = planeTypeCB.getSelectionModel().getSelectedItem().toString();
				}
				
				// Gets the id of the selected flight class type,
				// or adds it into DB if it doesn't exist.
				String classTypeId = Query.getQueryObject().getFlightClassTypeId(classTypeName);
				
				if (classTypeId == null) {
					// It's null, so we have to add it into DB
					Query.getQueryObject().addFlightClassType(classTypeName);
				}
				
				// Gets flight class type id from DB, because now we know for sure that it's there.
				classTypeId = Query.getQueryObject().getFlightClassTypeId(classTypeName);
				
				// Checks which option edit or add is chosen.
				// This one is for Add option.
				if (modeCB.getSelectionModel().getSelectedIndex() == 1) {
					// Checks if new entry contains duplicate data
					// Checks if inputted data already exists into another entry.
					if (Query.getQueryObject().getFlightClassId(classTypeId, fcNameTF.getText()) != null) {
						// Shows an error message
						new FXMessageDialog(parentStage, root, "You can't insert duplicate data!");
					} else {
						// Executes addFlightClass query.
						Query.getQueryObject().addFlightClass(classTypeId, fcNameTF.getText(), fcSeatsTF.getText(),
															ccSeatsTF.getText(), ecSeatsTF.getText());

						// Gets the ID of the inserted flight class
						String lastID = Query.getQueryObject().getLastAddedFlightClassId();

						// Adds FlightClass to the table.
						table.getItems().add(new FlightClass(lastID, classTypeName, fcNameTF.getText(),
								fcSeatsTF.getText(), ccSeatsTF.getText(), ecSeatsTF.getText()));

						//Shows info about done action.
						new FXMessageDialog(parentStage, root, "Flight class has been successfully added!");
					
						// Repopulates the typeCB, because maybe a type doesn't exist anymore
						// Or maybe a new one appeared because of updating / adding.
						populateTypeCB();

						// Clears form and table selection
						clearFormAndTableSelection();
					}
					
				// Here there is Edit mode.
				} else {
					// Gets selected element
					FlightClass selectedFC = (FlightClass) table.getSelectionModel().getSelectedItem();
					
					// Checks if inserted data match other entry, because we don't want that.
					String checkingResult = Query.getQueryObject().getFlightClassId(classTypeId, fcNameTF.getText());
					
					// Checks if the checkingResult is null or it has the same ID as the element being edited.
					// If it doesn't, it means that inputted data is the same with other entry's data.
					if ((checkingResult != null) && (!checkingResult.equals(selectedFC.getID()))) {
						new FXMessageDialog(parentStage, root, "You can't use this data, because it already exists into database!");
					} else {
						//Executes updateFlightClass query
						Query.getQueryObject().updateFlightClass(selectedFC.getID(), classTypeId, fcNameTF.getText(), 
								fcSeatsTF.getText(), ccSeatsTF.getText(), ecSeatsTF.getText());

						// Updates the FlightClass object.
						selectedFC.setName(fcNameTF.getText());
						selectedFC.setType(classTypeName);
						selectedFC.setFcSeatsNo(fcSeatsTF.getText());
						selectedFC.setCcSeatsNo(ccSeatsTF.getText());
						selectedFC.setEcSeatsNo(ecSeatsTF.getText());

						// Updates table with the updated FC object.
						table.getItems().set(table.getSelectionModel().getSelectedIndex(),
											 selectedFC.duplicate());

						// Makes sure we don't have an unused flight class type.
						removeUnusedFlightClassTypes();

						//Shows confirmation message for successful update.
						new FXMessageDialog(parentStage, root, "Flight class has been successfully updated!");
					
						// Repopulates the typeCB, because maybe a type doesn't exist anymore
						// Or maybe a new one appeared because of updating / adding.
						populateTypeCB();

						// Clears form and table selection
						clearFormAndTableSelection();
					}
				}
			}
        }
    }
	
	/**
	 *Contains the action that happens when type of plane is changed to a new type.
	 */
	private class TypeSelectAction implements ChangeListener<Object>{
		@Override
		public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
			if (planeTypeCB.getSelectionModel().getSelectedIndex() == (planeTypeCB.getItems().size() - 1)) {
				newTypeL.setVisible(true);
				newTypeTF.setVisible(true);
			} else {
				newTypeL.setVisible(false);
				newTypeTF.setVisible(false);
			}
		}
	}
	
    /**
	 * Contains the action that happens when mode (edit/add) is changed.
	 */
    private class ModeSelectAction implements ChangeListener<Object>{
        @Override
        public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
            if(modeCB.getSelectionModel().getSelectedIndex() == 0){
                //Clears the form on the right, and removes selection from table.
                clearFormAndTableSelection();
				
                // Enables remove button and table
				removeB.setDisable(false);
				table.setDisable(false);                
			} else if (modeCB.getSelectionModel().getSelectedIndex() == 1) {
				// Clears the form if user has some data in it, and removes selection from table.
				clearFormAndTableSelection();
				
				//Requests focus on FCnameTF.
				planeTypeCB.requestFocus();
				
				// Disables remove button and table
				removeB.setDisable(true);
				table.setDisable(true);
			}
        }
    }
}
