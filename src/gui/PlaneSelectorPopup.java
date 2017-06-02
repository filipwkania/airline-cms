package gui;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import wrappers.FlightClass;
import wrappers.Plane;

/**
 * This class shows the popup with the table for choosing plane ID in FSManagementPanel.
 * @author M&F
 */
public class PlaneSelectorPopup extends FXTableSelectorPopup {
	
	public PlaneSelectorPopup(Stage parentStage, final Pane root, final TableView table, 
							  final TextField textField){
		
		super(parentStage, root, table, textField);		
	}

	@Override
	public void selectTableRowBasedOnTextFieldData(TableView table, TextField textField) {
		
	// Gets the id inside the text field
		String planeId = textField.getText();
		
		// Checks if the text field is empty and selects -1 element if it's true
		if (planeId.isEmpty()) {
			table.getSelectionModel().clearSelection();
		} else {
			// Gets all the table objects (Rows)
			ObservableList planeList = table.getItems();

			// Creates an element that will contain the selected plane
			Plane selectedPlane = null;

			// Searches the table for the row which contains that id
			for (int i = 0; i < planeList.size(); i++) {
				if (((Plane) (planeList.get(i))).getID().equals(planeId)) {
					// Add the founded plane into the "selectedPlane" pointer
					selectedPlane = (Plane) planeList.get(i);
					
					// Breaks the loop
					break;
				}
			}
			
			// Checks if selectedPlane is still null.
			// Teoretically, this is impossible, but I still check for security reasons.
			if (selectedPlane == null) {
				System.err.println("Problem at selectTableRowBasedOnTextFieldData() in PlaneSelectorPopup Class");
			} else {
				// Selects the founded plane in the table.
				table.getSelectionModel().select(selectedPlane);
			}
		}
	}

	@Override
	public void setTextFieldWithSelectedObject(Object selectedObject, TextField textField) {
		textField.setText(((Plane)selectedObject).getID());
	}
}
