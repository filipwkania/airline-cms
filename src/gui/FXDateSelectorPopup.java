package gui;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * This class we use to choose date in FSManagementPanel
 * @author M&F
 */
public class FXDateSelectorPopup{
	
	private Button selectB, cancelB;
	private Popup p;
	private final DateChooser dc = new DateChooser();
	
	public FXDateSelectorPopup(final Stage parentStage, final Pane root, final TextField tf){

		p = new Popup();
		//Disabling the root Pane components.
		for (Node n : root.getChildren()) {
			n.setDisable(true);
		}		
		
		// Blocks the ability to close the popup when user presses "ESC" key
		p.hideOnEscapeProperty().setValue(false);
		
		// Creates buttons.
		cancelB = new Button("Cancel");
		selectB = new Button("Select");
		
		// Adds action for Cancel button
		cancelB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hidePopupAndReEnableComponents(p, root, true);
			}
		});
		
		// Adds action for Select button.
		selectB.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				// Checks if the date is chosen correctly (not the date from the past).
				if (checkDateIfNotInPast()) {
					tf.setText(dc.getDateInNiceFormat());
					hidePopupAndReEnableComponents(p, root, true);
				
				// If a date from past is chosen, shows an error message.
				} else {
					showMessageDialog(parentStage, root, "Unfortunately you can't fly to the past.");					
				}
			}
		});
		
		// Creates box for buttons.
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10);
		hBox.getChildren().addAll(selectB,cancelB);
		
		// Creates box for components.
		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.setSpacing(15);
		box.setPadding(new Insets(15, 10, 10, 10));
		box.getChildren().addAll(dc,hBox);
		
		// Creates a rectangle with a dropshadow effect.
        // Also we made here a fancy Gradient background.
		Rectangle rect = new Rectangle(273, 230,
							new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
								new Stop(0, Color.web("#DDF1FF")),
								new Stop(0.5, Color.WHITE),    
								new Stop(1, Color.web("#DDF1FF")),}));
		rect.setEffect(new DropShadow());
		
		// Adds the rectangle in the popup, as THE FIRST NODE, so index 0
		p.getContent().add(0, rect);	
		
		// Centers the popup
		p.centerOnScreen();		
		
		// Adds box to popup and shows it.
		p.getContent().addAll(box);
		p.show(parentStage);
	}
	
	/**
	 * Checks if chosen date is not a date from the past.
	 * @return 
	 */
	private boolean checkDateIfNotInPast(){
		Calendar cal = Calendar.getInstance();
		String date = dc.getDateInNiceFormat();
			//If the year is lower.
		if(Integer.parseInt(date.substring(0, 4)) < (cal.get(Calendar.YEAR)) 
				//If the year is same but month is lower.
				|| Integer.parseInt(date.substring(0, 4)) == (cal.get(Calendar.YEAR)) 
				&& Integer.parseInt(date.substring(5, 7)) < (cal.get(Calendar.MONTH)+1)
				//If the year and month are ok but the day is lower.
				|| Integer.parseInt(date.substring(0, 4)) == (cal.get(Calendar.YEAR)) 
				&& Integer.parseInt(date.substring(5, 7)) == (cal.get(Calendar.MONTH)+1)				
				&& Integer.parseInt(date.substring(8, 10)) < (cal.get(Calendar.DAY_OF_MONTH))){
			return false;
		}
		else
			return true;
	}
	
	private void hidePopupAndReEnableComponents(Popup popup, Pane root, Boolean check) {
		popup.hide();
		if(check)
			// ReEnables root's children.
			for (Node n : root.getChildren()) {
				n.setDisable(false);
			}
	}
	
	/**
	 * We had to add this modified Messenger because the FXMessageDialog is enabling the
	 * components after its disposed, and here we don't want this option, the same in AutoCloser class.
	 * @param parentStage
	 * @param root
	 * @param message 
	 */
	public void showMessageDialog(Stage parentStage, final Pane root, String message) {
		// First disables all the root's children
		// They will be enabled only after the user closes the showMessageDialog Dialog
		for (Node n : root.getChildren()) {
			n.setDisable(true);
		}
		
		// Creates the Popup that will show the message.
		// It must be final in order to use it in an inner class.
		final Popup popup = new Popup();
		
		// Blocks the ability to close the popup when user presses "ESC" key
		popup.hideOnEscapeProperty().setValue(false);
		
		// Creates a Label with the received message
		Label msgL = new Label(message + "\n ");
		
		// Creates the button that will close the popup
		Button closeB = new Button("Close!");
		closeB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hidePopupAndReEnableComponents(popup, root, false);
			}
		});
		
		// Creates a HBox that will hold the button
		// It's useful for centering the button
		HBox hBox = new HBox();
		hBox.getChildren().add(closeB);
		hBox.setAlignment(Pos.CENTER);
		
		// Creates a BorderPane that will hold both text and button
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(7.0, 7.0, 7.0, 7.0));
				
		// Adds components to the BorderPane
		borderPane.setCenter(msgL);
		borderPane.setBottom(hBox);
		
		// Adds VBox to the popup
		popup.getContent().add(borderPane);
		
		// Shows the popup
		// It has to be showed in order to get its dimensions
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
		
		// Moves the focus to the label, because initially the button is focused.
		msgL.requestFocus();
		
		// Creates the thread that will auto close this popup
		new AutoCloser(popup, root).start();
	}
	
	/**
	 * Class that auto closes the popup after a specified time.
	 */
	private class AutoCloser extends Thread {
		private Popup popup;
		private Pane root;
		
		public AutoCloser(Popup popup, Pane root) {
			this.popup = popup;
			this.root  = root;
		}
		
		@Override
		public void run() {
			super.run();
			
			try {
				sleep(3500);
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						hidePopupAndReEnableComponents(popup, root, false);						
					}
				});
			} catch (InterruptedException ex) {
				Logger.getLogger(FXMessageDialog.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
