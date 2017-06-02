package gui;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import operations.Regex;

/**
 * Contains a form for inserting details for a ticket.<br>
 * It's automaticaly generated in "ReservationMakerPanel".
 * @author M&F
 */
public class TicketDetailsPanel extends VBox {
	private ChoiceBox genderCB;
	private TextField nameTF, surnameTF;
	
	public TicketDetailsPanel(int ticketNo) {
		Label titleL = new Label("Details for ticket no. " + ticketNo);
		titleL.setStyle("-fx-padding: 0 0 2 0");
		
		// Create components
		genderCB = new ChoiceBox(FXCollections.observableArrayList("", "M", "F"));
		genderCB.setPrefWidth(60);
		nameTF = new TextField();
		nameTF.setPrefWidth(165);
		surnameTF = new TextField();
		surnameTF.setPrefWidth(115);
		
		// Creates a grid pane for buttons
		GridPane gp = new GridPane();
		gp.setHgap(6);
		
		// Adds components to grid pane
		gp.addColumn(0, new Label("Gender:"));
		gp.addColumn(1, genderCB);
		gp.addColumn(6, new Label("Name:"));
		gp.addColumn(7, nameTF);
		gp.addColumn(12, new Label("Surname:"));
		gp.addColumn(13, surnameTF);
		
		// Adds title and buttons to parent (this)
		this.getChildren().addAll(titleL, gp);
		
		// Shows a small gray line at buttom and adds some padding
		// Adds top and buttom padding, but skip top padding for first ticket
		if (ticketNo == 1) {
			this.setStyle("-fx-padding: 0 0 4 0;" +
						  "-fx-border-width: 0 0 0.2 0;" + 
						  "-fx-border-color: gray;");
		} else {
			this.setStyle("-fx-padding: 7 0 4 0;" +
						  "-fx-border-width: 0 0 0.2 0;" + 
						  "-fx-border-color: gray;");
		}
	}
	
	/**
	 * Checks if ticket inserted data is valid.
	 * @return checking result
	 */
	public boolean isDataValid() {
		return genderCB.getSelectionModel().getSelectedIndex() != -1 &&
			   genderCB.getSelectionModel().getSelectedIndex() != 0 &&
			   Regex.matchFreeName(nameTF.getText()) &&
			   Regex.matchFreeName(surnameTF.getText());
	}
	
	/**
	 * @return selected gender
	 */
	public String getGender() {
		if (genderCB.getSelectionModel().getSelectedIndex() != 0) {
			return (String) genderCB.getSelectionModel().getSelectedItem(); 
		} else {
			return null;
		}
	}
	
	public String getName() {
		return nameTF.getText();
	}
	
	public String getSurname() {
		return surnameTF.getText();
	}
}
