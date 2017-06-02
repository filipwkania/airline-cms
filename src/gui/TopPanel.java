package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This panel contains the welcome message showed to the user, messages status 
 * and log in / out possibility.
 * @author M&F
 */
public class TopPanel extends BorderPane {
	private static final String USER_MESSAGE = "Welcome, dear ";
	private final Main main;
	private final Stage parentStage;
	private final Pane root;
	private Label welcomeL, authenticateL;
        
	public TopPanel(final Stage parentStage, final Pane root, final Main main) {
		this.main = main;
		
		// Stage and root are necessary for LoginWindow
		this.parentStage = parentStage;
		this.root = root;
        
		// Sets panel style
		this.setStyle("-fx-border-color: gray;" +
					  "-fx-border-width: 0 0 0.2 0;" +
					  "-fx-padding: 3 5 3 5;");
		
		// Creates the welcomeLabel that will hold the welcome message for logged in user.
		// Writes only guest now, because there is no one logged in yet.
		welcomeL = new Label(USER_MESSAGE + "guest!");
		
		// Creates label used for "Log in" or "Log out"
		// When a user loggs in, this label will change in "Log out" and vice versa.
		authenticateL = new Label("Log in");
		
		// Prepares the authentication label image
		URL authenticateImgURL = getClass().getResource("resources/lock_14x14.gif");
		Image authenticateImg = new Image(authenticateImgURL.toString());
		
		// Sets image to authentication label
		authenticateL.setGraphic(new ImageView(authenticateImg));
		
		// Sets distance between image and text on authenticationL
		authenticateL.setStyle("-fx-graphic-text-gap: 2;" +
							   "-fx-content-display: right;");
        
		// Sets authenticationL action on mouse click
		authenticateL.setOnMouseClicked(new EventHandler(){
			@Override
			public void handle(Event arg0) {
				startLoginProcedure(null);
			}
		});
		
		// Sets underline effect on mouse over
		authenticateL.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				authenticateL.setStyle("-fx-underline: true;");
			}
		});
		
		// Removes underline effect on mouse out
		authenticateL.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				authenticateL.setStyle("-fx-underline: false;");
			}
		});
		
		// Creates help label
		final Label helpL = new Label("Help");
		
		// Prepares the help label image
		URL helpImgURL = getClass().getResource("resources/questionmark_11x14.gif");
		Image helpImg = new Image(helpImgURL.toString());
		
		// Sets image to authentication label
		helpL.setGraphic(new ImageView(helpImg));
		
		// Sets distance between image and text on authenticationL
		helpL.setStyle("-fx-graphic-text-gap: 2;" +
					   "-fx-content-display: right;");
		
		// Sets underline effect on mouse over
		helpL.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				helpL.setStyle("-fx-underline: true;");
			}
		});
		
		// Removes underline effect on mouse out
		helpL.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				helpL.setStyle("-fx-underline: false;");
			}
		});
		
		// Creates action that opens manual pdf on click
		helpL.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				// Checks whether the API is supported by this VM
		        if (Desktop.isDesktopSupported()) {
		            Desktop desktop = Desktop.getDesktop();
		            
		            File file = new File("User Manual.pdf");
		            
		            if (file.exists()) {
		            	try {
		            		// Tries to open the manual file
							desktop.open(file);
						} catch (IOException e1) {}
		            } else {
		            	// Throws an error message if file doesn't exist
		            	new FXMessageDialog(parentStage, root, "Help files are under construction.");
		            }
				}
			}
		});
		
		// Creates a box as container for help and authenticate labels
		HBox rightBox = new HBox(10);
		rightBox.getChildren().addAll(authenticateL, helpL);
		
		// Adds components to Pane
		this.setLeft(welcomeL);
		this.setRight(rightBox);
	}
	
	public void startLoginProcedure(String userMessage) {
		// If no one is logged in, then the label contains "Log in" text.
		// So, the LoginWindow will be showed on mouse click.
		if (authenticateL.getText().equals("Log in")) {
			new LoginPopup(parentStage, root, main, userMessage);

		// In this case sameone is logged in and label contains "Log out" text.
		// So, now we will log out the user
		} else {
			// Now no one is logged anymore, so we'll empty the logged in user details
			// and set it's rank to guest
			main.setLoggedInUser(null, null, "guest");

			// Changes the welcome message (removes the logged user's name and use guest now).
			welcomeUser(main);

			// Changes the "Log out" label into "Log in".
			authenticateL.setText("Log in");

			// Shows the left panel (navigation) for guests.
			main.showLeftPanel("guest");

			// Shows the welcome panel on the center.
			// By doing this we avoid showing an admin panel to a guest after the admin loggs out.
			main.showPanel("WelcomePanel");

			// Shows a confirmation message for successful log out.
			new FXMessageDialog(parentStage, root, "See you next time!");
		}
		
		// Works also:
//		authenticationL.fireEvent(new Event(MouseEvent.MOUSE_CLICKED));
//		Event.fireEvent(authenticationL, new Event(MouseEvent.MOUSE_CLICKED));
	}
	
	/**
	 * Welcomes the current authenticated user.
	 * @param main - a referrence to Main class object
	 * @param name - the of the current user or null if it's a guest
	 */
	public void welcomeUser(Main main){
		// if name is not null then the message welcomes logged user and shows his rank
		if (main.getLoggedInUser().getFName() != null) {
			welcomeL.setText(USER_MESSAGE + main.getLoggedInUser().getFName() +
							 "! Your rank: "+main.getLoggedInUser().getRank()+".");	
		
		//when the name is null it means the guest is logged, so no name will be displayed.
		} else {	
			welcomeL.setText(USER_MESSAGE + "guest !");
		}
	}
	
	/**
	 * Changes the authentication label from "Log in" to "Log out"
	 */
	public void changeLogInLabelToLogOut(){
		authenticateL.setText("Log out");
	}
}
