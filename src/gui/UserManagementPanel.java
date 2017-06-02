package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import wrappers.LoggedInUser;
import wrappers.User;

/**
 * Contains the entire panel for administrating users: a table to list users, and
 * a form for editing them.
 * @author M&F
 */
public class UserManagementPanel extends BorderPane {
	private Stage parentStage;
	private Pane root;
	private LoggedInUser loggedInUser;
	private TableView table;
	private ChoiceBox modeCB;
	private TextField fNameTF, lNameTF, passTF;
	private ChoiceBox rankCB;
	private Button removeUserB;
	
	// Creates preffered width of the components (text fields and choice boxes)
    private final static double COMPONENT_WIDTH = 120;
	
	public UserManagementPanel(Stage parentStage, Pane root, LoggedInUser loggedInUser) {
		// Stage and root are necessary for FXMessageDialog
		this.parentStage = parentStage;
		this.root = root;
		this.loggedInUser = loggedInUser;
		
		// Creates components and adds them to main BorderPane(this)
		createAndPopulateTable();
		createEditPanel();		
	}
	
	/**
	 * Creates the table and adds users into from database.
	 */
	private void createAndPopulateTable() {
		// Gets the user list from the database
		ResultSet rs = Query.getQueryObject().getUsers();
		
		// Creates an ObservableList which will contain all the users.
		// It's necessary for outputting to table.
		ObservableList<User> tableData = FXCollections.observableArrayList();
		
		// Fetches the query result data into the observable list.
		// id, fName, lName, rank and password are added into the User object
		// id is not shown. It's needed just for database usage.
		if (rs != null) {
			try {
				while (rs.next()) {
					tableData.add(new User(rs.getString(1), rs.getString(2), 
										   rs.getString(3), rs.getString(4), rs.getString(5)));
				}
			} catch (SQLException ex) {
				System.err.println("SQL error in UserManagementPanel");
			}
		}
		
		// Creates the table columns
		TableColumn firstNameColumn = new TableColumn("First name");
		TableColumn lastNameColumn = new TableColumn("Last name");
		TableColumn rankColumn = new TableColumn("System rank");
		
		// Specifies which fields from the inserted object should be printed in each cell
		// It works both with <User, String> or without
		// Maybe it's more specific with <User, String>
		// Sets which is responsible for calling the User bean’s property. 
		// So when the list of users is put into the TableView, it will know
		// how to pull the properties to be placed in each cell in the table
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory("lastName"));
		rankColumn.setCellValueFactory(new PropertyValueFactory("rank"));
		
		// Sets columns width
		firstNameColumn.setPrefWidth(100);
		lastNameColumn.setPrefWidth(100);
		rankColumn.setPrefWidth(100);
		
		// Creates table and adds columns and data to it
		table = new TableView();
		table.getColumns().addAll(firstNameColumn, lastNameColumn, rankColumn);
		table.setItems(tableData);
		
		// Adds action Event handler to table
		table.setOnMouseClicked(new TableAction());
		
		// Creates HBox for adding padding to the table.
		HBox tableBox = new HBox();
		tableBox.setPadding(new Insets(10, 0, 10, 10));
		tableBox.getChildren().add(table);

		// Adds table to BorderPane (this object)
		this.setLeft(tableBox);
	}
	
	/**
	 * Creates the right panel that will allow to add, edit and remove users
	 */
	private void createEditPanel() {
		// Creates the panel that holds the form: labels and text fields (fName : ..., lName... etc) 
		GridPane formContainer = new GridPane();
		formContainer.setAlignment(Pos.CENTER);
		formContainer.setHgap(5);
		formContainer.setVgap(5);
		
		// Creates the ChoiceBox that will change between "edit mode" and "add mode"
		// Edit mode MUST BE on index 0, so the first option, because the system checks later
		// which index is selected and it considers index 0 as edit mode.
		// So, keep "edit users" first, then "add new users".
		modeCB = new ChoiceBox(FXCollections.observableArrayList("edit users", "add new users"));
		modeCB.getSelectionModel().selectFirst();
		modeCB.getSelectionModel().selectedIndexProperty().addListener(new ModeSelectAction());
		
		// Creates text fields and choice box that will show the information
		fNameTF = new TextField();
		lNameTF = new TextField();
		rankCB  = new ChoiceBox();
		passTF  = new TextField();
		
		// Populates rankCB with database rank levels
		populateRankCB();

		// Sets TF and CB width
		modeCB.setMaxWidth(COMPONENT_WIDTH);
		fNameTF.setMaxWidth(COMPONENT_WIDTH);
		lNameTF.setMaxWidth(COMPONENT_WIDTH);
		rankCB.setMaxWidth(COMPONENT_WIDTH);
		passTF.setMaxWidth(COMPONENT_WIDTH);
		
		// Add components to grid pane
		formContainer.add(new Label("Mode:"), 0, 0);
		formContainer.add(modeCB, 1, 0);
		formContainer.add(new Label("First name:"), 0, 1);
		formContainer.add(fNameTF, 1, 1);
		formContainer.add(new Label("Last name:"), 0, 2);
		formContainer.add(lNameTF, 1, 2);
		formContainer.add(new Label("Rank:"), 0, 3);
		formContainer.add(rankCB, 1, 3);
		formContainer.add(new Label("Password:"), 0, 4);
		formContainer.add(passTF, 1, 4);
		
		// Creates the necessary buttons for the edit panel
		Button saveUserB = new Button("Save");
		saveUserB.setTooltip(new Tooltip("Saves inserted data into the database."));
		removeUserB = new Button("Remove");
		removeUserB.setTooltip(new Tooltip("Removes the selected user from the database."));
		
		// Add Event Handlers to buttons
		saveUserB.setOnAction(new SaveButtonAction());
		removeUserB.setOnAction(new RemoveButtonAction());
		
		// Creates a HBox that holds the buttons
		HBox buttonsContainer = new HBox(5);
		buttonsContainer.setAlignment(Pos.CENTER);
		buttonsContainer.getChildren().addAll(saveUserB, removeUserB);
		
		// Creates a VBox that holds the form and the buttons
		VBox vBox = new VBox(15);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(formContainer, buttonsContainer);
		
		this.setCenter(vBox);
	}
	
	/**
	 * Adds rank types to rankCB ChoiceBox.
	 */
	private void populateRankCB() {
		try {
			// Gets ResultSet that is containing all planeTypes
			ResultSet rs = Query.getQueryObject().getUserRanks();
			
			// Creates observableArrayList for the planeTypeCB
			ObservableList<String> types = FXCollections.observableArrayList();
			
			// Populates ObservableArrayList with planeTypes.
			while (rs.next()) {
				types.add(rs.getString(1));
			}
			
			// Sets types as a data in planeTypesCB.
			rankCB.setItems(types);
		} catch(Exception ex) {
			System.err.println("Problem occured while populating rankCB in UserManagementPanel!");
		}
	}
	
	/**
	 * Clears the form text fields and choice box, and clears the table selection.
	 */
	private void clearFormAndTableSelection() {
		fNameTF.setText("");
		lNameTF.setText("");
		rankCB.getSelectionModel().clearSelection();
		passTF.setText("");
		
		table.getSelectionModel().clearSelection();
	}
	
	/**
	 * Contains the action that happens when user selects a row in the table.<br>
	 * Practically, populates the edit form with the selected user's data.
	 */
	private class TableAction implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent me) {
			// Checks if selected row is not -1
			if (table.getSelectionModel().getSelectedIndex() != -1) {
				User selectedUser = (User) table.getSelectionModel().getSelectedItem();
				
				// Populates the edit form with the user's data
				fNameTF.setText(selectedUser.getFirstName());
				lNameTF.setText(selectedUser.getLastName());
				rankCB.getSelectionModel().select(selectedUser.getRank());
				passTF.setText(selectedUser.getPassword());
			}
		}
	}
	
	/**
	 * Contains the action that happens when user clicks on "Remove" button.<br>
	 * Practically, removes the user from database and table,
	 * and the clears the form and the table selection.
	 */
	private class RemoveButtonAction implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent ae) {
			// If no user is selected in the table, show an error
			if (table.getSelectionModel().getSelectedIndex() == -1) {
				new FXMessageDialog(parentStage, root, "Please select the user you want to remove.");
			} else {
				// Gets selected user
				User selectedUser = (User) table.getSelectionModel().getSelectedItem();
				
				// Checks if the selected user is the same as the loggedInUser.
				// In this case, denies the remove. The user can't delete himself.
				if (loggedInUser.getID().equals(selectedUser.getID())) {
					// Shows an error message
					new FXMessageDialog(parentStage, root, "You can't delete yourself from the user list.");
				} else {
					// Removes user form database
					Query.getQueryObject().removeUser(selectedUser.getID());

					// Removes user from table
					table.getItems().remove(selectedUser);

					// Shows a confirmation message
					new FXMessageDialog(parentStage, root, "User " + selectedUser.getFirstName() + " " +
														"has been succesfully deleted!");

					// Clears the form and the table selection
					clearFormAndTableSelection();
				}
			}
		}
	}

	/**
	 * Contains the action that happens when user clicks on "Save" button.<br>
	 * It has different functionality, depending on which mode is selected: edit or add.<br>
	 * When edit mode, the data is saved for the selected user.<br>
	 * When add mode, the data is added as a new user.
	 */
	private class SaveButtonAction implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent ae) {
			// Checks if edit mode is active, but no person is selected from table
			// When modeCB.getSelectionModel().getSelectedIndex() is 0 it means edit mode
			if ((modeCB.getSelectionModel().getSelectedIndex() == 0) &&
				(table.getSelectionModel().getSelectedIndex() == -1)) {
				new FXMessageDialog(parentStage, root, "Please select the user you want to edit!");
				
				// Stops method execution
				return;
			}
			
			// Checks if fNameTF has less least 2 chars and contains only letters
			if (!Regex.matchName(fNameTF.getText())) {
				new FXMessageDialog(parentStage, root, "Invalid first name. Valid example: Dean.\n" +
													   "Max length is 25 chars.");
				// Stops method execution
				return;
			}
			
			// Checks if lNameTF has less least 2 chars and contains only letters
			if (!Regex.matchName(lNameTF.getText())) {
				new FXMessageDialog(parentStage, root, "Invalid last name. Valid example: Johnson.\n" +
													   "Max length is 25 chars.");
				// Stops method execution
				return;
			}
			
			// Checks if a rank option is selected
			if (rankCB.getSelectionModel().getSelectedIndex() == -1) {
				new FXMessageDialog(parentStage, root, "Please select a rank option.");
				
				// Stops method execution
				return;
			}
			
			// Checks if the password has at least 6 chars and contains only letters and digits
			if (!Regex.matchPassword(passTF.getText())) {
				new FXMessageDialog(parentStage, root, "Please insert a valid password with at least 6 chars.\n" +
									"It must contain at least 1 small letter, 1 capital letter and 1 decimal.\n" +
									"Only letters and numbers are permitted.");
				
				// Stops method execution
				return;
			}
			
			// If you are here, it means that input data is ok.
			// Now the data will be saved into database, based on which mode is selected.
			// Checks which mode is selected (0 = edit, 1 = add) and performs the query.
			if (modeCB.getSelectionModel().getSelectedIndex() == 0) {
				// Edit mode is selected, so we need the id of the selected person
				// in order to perform the query. Gets the selected user.
				User selectedUser = (User) table.getSelectionModel().getSelectedItem();
				
				// Checks if the checkingResult is -1 or it has the same ID as the element being edited.
				// If it doesn't, it means that inputted data is the same with other entry's data.
				int checkingResult = Query.getQueryObject().getUserId(fNameTF.getText(), lNameTF.getText());
				
				if ((checkingResult != -1) && (checkingResult != Integer.parseInt(selectedUser.getID()))) {
					new FXMessageDialog(parentStage, root, "You can't use this data, because it already exists into database!");
				} else {
					// Performs the query
					Query.getQueryObject().updateUser(selectedUser.getID(), fNameTF.getText(), lNameTF.getText(), 
						(String) (rankCB.getSelectionModel().getSelectedItem()), passTF.getText());
					
					// Updates the User object to show the new data in the table
					selectedUser.setFirstName(fNameTF.getText());
					selectedUser.setLastName(lNameTF.getText());
					selectedUser.setRank((String) (rankCB.getSelectionModel().getSelectedItem()));
					selectedUser.setPassword(passTF.getText());

					// Updates table data.
					// First gets the user Object index in the items list,
					// then removes the user object from the table list,
					// then adds the updated user object at the same index it was before.
//					int userObjectIndexInItemsList = table.getItems().indexOf(selectedUser);
//					table.getItems().remove(table.getItems().indexOf(selectedUser));
//					table.getItems().add(userObjectIndexInItemsList, selectedUser);					
					
//					User x = new User("ss", "ss", "ss", "ss", "ss");
					
					table.getItems().set(table.getItems().indexOf(selectedUser), selectedUser.duplicate());
					
//					table.setItems(null);
//					table.getItems().add(null);
//					((ObservableListWrapper) (tableData)).a
//					ListListenerHelper.fireValueChangedEvent(tableData , ae);
//					new NonIterableChange.SimpleUpdateChange(index, index + 1, tableData);
					
//					tableData.get(index).setFirstName("Johhhhhh");
//					UserManagementPanel.this.fNameTF == this.val$e;
					
					
//					table.setItems(tableData);
					
					
					// Recreates the table.
					// Necessary because of Java FX 2.1 or later bug.
//					createAndPopulateTable();
					
					// Shows a confirmation message
					new FXMessageDialog(parentStage, root, "User has been successfully updated.");

					// Clears form and table selection in order to protect from multiple saves
					clearFormAndTableSelection();
				}
			
			// Now is add mode
			} else if (modeCB.getSelectionModel().getSelectedIndex() == 1) {
				// Checks if the "new" user already exists
				if (Query.getQueryObject().checkIfUserExists(fNameTF.getText(), lNameTF.getText())) {
					// Shows an error message
					new FXMessageDialog(parentStage, root, "This user already exists into the database!");
				} else {
					// Performs the insert query
					Query.getQueryObject().insertUser(fNameTF.getText(), lNameTF.getText(), 
						(String) (rankCB.getSelectionModel().getSelectedItem()), passTF.getText());
					
					// Gets the id of the new user
					int userID = Query.getQueryObject().getUserId(fNameTF.getText(), lNameTF.getText());
					
					// Checks if the id is valid.
					// If it's not valid (-1), it means that the insert method didn't work.
					if (userID == -1) {
						// Shows an error message
						new FXMessageDialog(parentStage, root, "Insert sql failed. Please try again.");
					} else {
						// Creates an User object and adds it into the table
						table.getItems().add(new User(userID + "", fNameTF.getText(), lNameTF.getText(), 
							(String) (rankCB.getSelectionModel().getSelectedItem()), passTF.getText()));
						
						// Clears the form
						clearFormAndTableSelection();
						
						// Shows a confirmation message
						new FXMessageDialog(parentStage, root, "User has been successfully added.");
					}
				}
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
				// Clears the form if user has some data in it
				clearFormAndTableSelection();
				
				// Enables remove button and table
				removeUserB.setDisable(false);
				table.setDisable(false);
			} else if (modeCB.getSelectionModel().getSelectedIndex() == 1) {
				// Clears the form if user has some data in it
				clearFormAndTableSelection();
				
				// Disables remove button and table
				removeUserB.setDisable(true);
				table.setDisable(true);
			}
		}
	}
}
