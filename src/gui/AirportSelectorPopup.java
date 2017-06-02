package gui;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import wrappers.Airport;

/**
 * Class used to create a popup for selecting an airport from a table.
 * @author M&F
 */
public class AirportSelectorPopup extends FXTableSelectorPopup {
	public AirportSelectorPopup(Stage parentStage, final Pane root, final TableView table, 
							  final TextField textField) {
		super(parentStage, root, table, textField);
	}

	@Override
	public void selectTableRowBasedOnTextFieldData(TableView table, TextField textField) {
		// Gets the id inside the text field
		String airportId = textField.getText();
		
		// Checks if the text field is empty and selects -1 element if it's true
		if (airportId.isEmpty()) {
			table.getSelectionModel().clearSelection();
		} else {
			// Gets all the table objects (Rows)
			ObservableList airportsList = table.getItems();

			// Creates an element that will contain the selected airport
			Airport selectedAirport = null;

			// Searches the table for the row which contains that id
			for (int i = 0; i < airportsList.size(); i++) {
				if (((Airport) (airportsList.get(i))).getId().equals(airportId)) {
					// Add the founded airport into the "selectedAirport" pointer
					selectedAirport = (Airport) airportsList.get(i);
					
					// Breaks the loop
					break;
				}
			}
			
			// Checks if selectedAirport is still null.
			// Teoretically, this is impossible, but I still check for security reasons.
			if (selectedAirport == null) {
				System.err.println("Problem at selectTableRowBasedOnTextFieldData() in AirportSelectorPopup Class");
			} else {
				// Selects the founded airport in the table.
				table.getSelectionModel().select(selectedAirport);
			}
		}
	}

	@Override
	public void setTextFieldWithSelectedObject(Object selectedObject, TextField textField) {
		// Sets the id of the selected Airport into the text field
		textField.setText(((Airport) selectedObject).getId());
	}
	
}
