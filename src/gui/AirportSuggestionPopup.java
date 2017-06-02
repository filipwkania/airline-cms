package gui;

import java.util.LinkedList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import wrappers.Airport;

/**
 * Class that creates a popup for showing airport suggestions based on what
 * the user inputs into the text field.
 * @author M&F
 */
public class AirportSuggestionPopup {
	private Stage parentStage;
	private Popup popup;
	private ListView airportListView;
	private LinkedList<Airport> airportList;
	private TextField target;
	
	public AirportSuggestionPopup(Stage parentStage, LinkedList<Airport> airportList) {
		this.parentStage = parentStage;
		this.airportList = airportList;

		// Creates the list view used to show suggestions
		airportListView = new ListView();

		airportListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				outputToTextField();
			}
		});
		airportListView.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					outputToTextField();
				} else if (e.getCode() == KeyCode.ESCAPE) {
					popup.hide();
				}
			}
		});
		
		// Creates the suggestion popup
		popup = new Popup();
		popup.getContent().add(airportListView);
	}
	
	/**
	 * Shows the popup under the specified component (target)
	 */
	public void show(TextField target) {
		if (target.getText().length() > 2) {
			this.target = target;

			// Clears the list, because maybe it has some old entries
			airportListView.getItems().clear();
			
			// Finds the matches and adds them into the list view
			findAirportMatches(target.getText());

			// Shows a message if no suggestion is available
			if (airportListView.getItems().size() == 0) {
				airportListView.getItems().add("Airport not found!");
			}		
			
			// Prepares the list view height
			// If it has just 1 element, 26px are used.
			// If it has more than 1, 25px are used.
			double height = 0;
			if (airportListView.getItems().size() == 1) {
				height = 26;
			} else {
				height = 25 * airportListView.getItems().size();
			}
			
			// Sets the list view height based on no. of suggestions
			airportListView.setPrefHeight(height);
			
			// Sets the list view width
			airportListView.setPrefWidth(230);
			
			// Shows the popup
			popup.show(parentStage, getX(target), getY(target));
		} else {
			popup.hide();
		}
	}
	
	/**
	 * Sets into the text field the selected airport suggestion.
	 */
	private void outputToTextField() {
		if (airportListView.getSelectionModel().getSelectedIndex() != -1 &&
			// Checks if an airport suggestion is present,
			// because the "Airport not found!" message doesn't contain a '(';
			(((String) (airportListView.getItems().get(0))).indexOf('(') != -1)) {
			
			// Gets the selected item
			String selectedItem = airportListView.getSelectionModel().getSelectedItem().toString();

			// Output the airport code to text field
			target.setText(selectedItem.substring((selectedItem.indexOf('(') + 1),
						selectedItem.indexOf(')')));
			popup.hide();
		}
	}
	
	/**
	 * Finds the airports that match the input text, and adds them into the list view.
	 * @param input 
	 */
	private void findAirportMatches(String input) {
		// Searches by ID
		for (Airport a : airportList) {
			if (a.getId().toLowerCase().contains(input.toLowerCase())) {
				addAirportToLV(a);
				
				// Stops method execution because the code was founded.
				// It's stopped because if an airport code was founded, it doesn't
				// make sense to search the others also, because airport codes are
				// unique, so the match is 100% accurate.
				return;
			}
		}
		
		// Searches by airport name
		for (Airport a : airportList) {
			if (a.getName().toLowerCase().contains(input.toLowerCase())) {
				addAirportToLV(a);
			}
		}
		
		// Searches by airport city
		for (Airport a : airportList) {
			if (a.getCity().toLowerCase().contains(input.toLowerCase())) {
				addAirportToLV(a);
			}
		}
		
		// Searches by airport country
		for (Airport a : airportList) {
			if (a.getCountry().toLowerCase().contains(input.toLowerCase())) {
				addAirportToLV(a);
			}
		}
	}
	
	/**
	 * Adds the received airport into the airport list view, in a specific format.
	 * @param a 
	 */
	private void addAirportToLV(Airport a) {
		airportListView.getItems().add(a.getName() + "(" + a.getId() + ") " + a.getCity());
	}
	
	/**
	 * @param c
	 * @return the absolute X for positioning the popup.
	 */
	private double getX(Control c) {
		return getComponentAbsoluteX(c) + parentStage.getX() + 2;
	}
	
	/**
	 * @param c
	 * @return the absolute Y for positioning the popup.
	 */
	private double getY(Control c) {
		return getComponentAbsoluteY(c) + parentStage.getY() + 50;
	}
	
	/**
	 * @param c
	 * @return the absolute X position of the specified component.<br>
	 * Absolute position means relative to the master parent container.
	 */
	private double getComponentAbsoluteX(Control c) {
		if (c == null) {
			return 0;
		} else {
			return getComponentAbsoluteXHelper(c);
		}
	}
	
	/**
	 * Helper method for getComponentAbsoluteX.
	 * @param p
	 * @return 
	 */
	private double getComponentAbsoluteXHelper(Parent p) {
		if (p == null) {
			return 0;
		} else {
			return p.getLayoutX() + getComponentAbsoluteXHelper(p.getParent());
		}
	}
	
	/**
	 * @param c
	 * @return the absolute Y position of the specified component.<br>
	 * Absolute position means relative to the master parent container.
	 */
	private double getComponentAbsoluteY(Control c) {
		if (c == null) {
			return 0;
		} else {
			return getComponentAbsoluteYHelper(c);
		}
	}
	
	/**
	 * Helper method for getComponentAbsoluteY.
	 * @param p
	 * @return 
	 */
	private double getComponentAbsoluteYHelper(Parent p) {
		if (p == null) {
			return 0;
		} else {
			return p.getLayoutY() + getComponentAbsoluteYHelper(p.getParent());
		}
	}
	
	/**
	 * Hides the popup.
	 */
	public void hide() {
		popup.hide();
	}
}
