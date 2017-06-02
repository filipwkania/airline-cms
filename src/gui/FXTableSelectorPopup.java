package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Class created for usage as a table selector pupup template.<br>
 * Because tables (an objects contained by them) and text fields can be different from popup
 * to popup, 2 methods must be implemented each time with specific actions:<br>
 * - selectTableRowBasedOnTextFieldData and<br>
 * - setTextFieldWithSelectedObject.
 * @author M&F
 */
public abstract class FXTableSelectorPopup {
	public FXTableSelectorPopup(Stage parentStage, final Pane root, final TableView table, 
							  final TextField textField) {
		// First disables all the root's children
		// They will be enabled only after the user closes the Message Dialog
		for (Node n : root.getChildren()) {
			n.setDisable(true);
		}
		
		// Creates the Popup that will show the message.
		// It must be final in order to use it in an inner class.
		final Popup popup = new Popup();
		
		// Blocks the ability to close the popup when user presses "ESC" key
		popup.hideOnEscapeProperty().setValue(false);
		
		// Creates an error Label for user
		final Label errorL = new Label("");
		errorL.setStyle("-fx-text-fill: red;");
		
		// Creates the select Button, which will take the selected element and put it into the text field
		Button selectB = new Button("Select");
		selectB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Checks if a table row is selected
				if (table.getSelectionModel().getSelectedIndex() == -1) {
					// Shows an error message to the user
					errorL.setText("Please select preferred value from the table.");
				} else {
					// Gets the selected object, processet it, and adds the data into the text field
					setTextFieldWithSelectedObject(table.getSelectionModel().getSelectedItem(), textField);
					
					// Hides the popup and reEnables the components
					hidePopupAndReEnableComponents(popup, root);
				}
			}
		});
		
		// Creates the cancel Button, which will close the popup
		Button cancelB = new Button("Cancel");
		cancelB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hidePopupAndReEnableComponents(popup, root);
			}
		});
		
		// Creates a HBox that will hold the button
		// It's useful for centering the button
		HBox buttonsHBox = new HBox(7);
		buttonsHBox.getChildren().addAll(selectB, cancelB);
		buttonsHBox.setAlignment(Pos.CENTER);
		
		// Creates a VBox that will hold both table and buttons
		VBox vBox = new VBox(10);
		vBox.setPadding(new Insets(7.0, 7.0, 7.0, 7.0));
				
		// Autoselects a table row based on text field data
		selectTableRowBasedOnTextFieldData(table, textField);
		
		// Sets table height
		table.setPrefHeight(195);
		
		// Adds components to the BorderPane
		vBox.getChildren().addAll(errorL, table, buttonsHBox);
		
		// Adds VBox to the popup
		popup.getContent().add(vBox);
		
		// Shows the popup.
		// It has to be showed in order to get its dimensions.
		popup.show(parentStage);
		
		// Gets the size of the popup
		int width  = popup.widthProperty().getValue().intValue();
		int height = popup.heightProperty().getValue().intValue();
		
		// Creates a white rectangle with a dropshadow effect
		Rectangle rect = new Rectangle(width, height, Color.WHITE);
		rect.setEffect(new DropShadow());
		
		// Adds the rectangle in the popup, as THE FIRST NODE, so index 0
		popup.getContent().add(0, rect);
		
		// Centers the popup
		popup.centerOnScreen();
		
		// Moves the focus to the label,
		// because initially the button is focused.
		errorL.requestFocus();
	}
	
	private void hidePopupAndReEnableComponents(Popup popup, Pane root) {
		popup.hide();

		// ReEnables the root's children
		for (Node n : root.getChildren()) {
			n.setDisable(false);
		}
	}
	
	/**
	 * Method used to autoselect an table row based on the information the text field
	 * contains when it is clicked.<br>
	 * It selects nothing when text field is empty.
	 * @param table
	 * @param textField 
	 */
	public abstract void selectTableRowBasedOnTextFieldData(TableView table, TextField textField);
	
	/**
	 * Processes the selected object and sets the value into the text field.<br>
	 * <b>You must cast the object</b> to the type you know it is.
	 * @param selectedObject
	 * @param textField 
	 */
	public abstract void setTextFieldWithSelectedObject(Object selectedObject, TextField textField);
}
