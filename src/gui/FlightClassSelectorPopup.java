package gui;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import wrappers.FlightClass;

/**
 * Class used to create a popup for selecting a flight class from a table.
 * @author M&F
 */
public class FlightClassSelectorPopup extends FXTableSelectorPopup {
	public FlightClassSelectorPopup(Stage parentStage, final Pane root, final TableView table, 
							  final TextField textField) {
		super(parentStage, root, table, textField);
	}
	
	@Override
	public void selectTableRowBasedOnTextFieldData(TableView table, TextField textField) {
		// Gets the id inside the text field
		String flightClassId = textField.getText();
		
		// Checks if the text field is empty and selects -1 element if it's true
		if (flightClassId.isEmpty()) {
			table.getSelectionModel().clearSelection();
		} else {
			// Gets all the table objects (Rows)
			ObservableList fcList = table.getItems();

			// Creates an element that will contain the selected flight class
			FlightClass selectedFC = null;

			// Searches the table for the row which contains that id
			for (int i = 0; i < fcList.size(); i++) {
				if (((FlightClass) (fcList.get(i))).getID().equals(flightClassId)) {
					// Add the founded flight class into the "selectedFC" pointer
					selectedFC = (FlightClass) fcList.get(i);
					
					// Breaks the loop
					break;
				}
			}
			
			// Checks if selectedFC is still null.
			// Teoretically, this is impossible, but I still check for security reasons.
			if (selectedFC == null) {
				System.err.println("Problem at selectTableRowBasedOnTextFieldData() in FlightClassSelectorPopup Class");
			} else {
				// Selects the founded airport in the table.
				table.getSelectionModel().select(selectedFC);
			}
		}
	}

	@Override
	public void setTextFieldWithSelectedObject(Object selectedObject, TextField textField) {
		// Sets the id of the selected Airport into the text field
		textField.setText(((FlightClass) selectedObject).getID());
	}
	
}
