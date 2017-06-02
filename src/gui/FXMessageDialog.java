package gui;

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
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Class that behaves exactly as the JOptionPane from Swing.
 * @author M&F
 */
public class FXMessageDialog {
	
	public FXMessageDialog(Stage parentStage, final Pane root, String message) {
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
		
		// Creates a Label with the received message
		Label msgL = new Label(message + "\n ");
		
		// Creates the button that will close the popup
		Button closeB = new Button("Close!");
		closeB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hidePopupAndReEnableComponents(popup, root);
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
	
	private void hidePopupAndReEnableComponents(Popup popup, Pane root) {
		popup.hide();

		// ReEnables the root's children
		for (Node n : root.getChildren()) {
			n.setDisable(false);
		}
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
						hidePopupAndReEnableComponents(popup, root);
					}
				});
			} catch (InterruptedException ex) {
				Logger.getLogger(FXMessageDialog.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
	}
}
