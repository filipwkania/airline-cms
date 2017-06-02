package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
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
import operations.Query;
import operations.Regex;

/**
 * Deals with login. Creates a login popup used for authentication.
 * @author M&F
 */
public class LoginPopup {
    private Label nameL, passwordL, errorMessageL, surnameL;
    private TextField nameTF, surnameTF;
    private Button logInB, registerB, cancelB;
    private PasswordField passwordPF;
	
	public LoginPopup(final Stage parentStage, final Pane root, final Main main, String userMessage) {
                
		// First disables all the root's children.
		// They will be enabled only after the login popup is closed.
		for (Node n : root.getChildren()) {
			n.setDisable(true);
		}
		
		// Gets top panel from the Main class.
		final TopPanel topPanel = main.getTopPanel();
		
		// Creates the Popup that will hold the login form.
		// It must be final in order to use it in an inner class.
		final Popup popup = new Popup();
		popup.hideOnEscapeProperty().setValue(Boolean.FALSE);
		
        // Creates necessary form Labels
		surnameL = new Label("Surname: ");
		nameL = new Label("Name: ");
		passwordL = new Label("Password: ");
		
		// Creates the label that will show the errors if any.
		errorMessageL = new Label(userMessage);
		
		// Creates necessary TextFields
		nameTF = new TextField();
		surnameTF = new TextField();
		passwordPF = new PasswordField();                             
                
		// Creates the buttons for log in and closing popup
		logInB = new Button("Log in");
		registerB = new Button("Register");
		cancelB = new Button("Cancel");
		
		// Creates an explanatory tool tip for register button
		registerB.setTooltip(new Tooltip("Registration is available just for clients!\n" +
										 "Other users can be added just by admins."));
		
		// Checks fName, lName and pass text, and calls 
		// the login method if everything is ok (inputted text is valid).
		logInB.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){				
				// Checks if fName, lName and password contain correct characters
				if (!Regex.matchName(nameTF.getText()) ||
					!Regex.matchName(surnameTF.getText()) ||
					!Regex.matchPassword(passwordPF.getText())) {
					errorMessageL.setText("Incorrect name or password!");
				
				// Tries to perform the login action
				} else {
					String loggedUserRank = Query.getQueryObject().verifyUserLogin(nameTF.getText(),
									surnameTF.getText(), passwordPF.getText());

					// If the result from the DB is not null it means, that query
					// found user where name, surname and password match each other
					// then we can continue to change rank and welcome panel
					// also log in into log out
					if (loggedUserRank != null) {
						// Gets the id of the logged in user
						int userID = Query.getQueryObject().getUserId(nameTF.getText(), surnameTF.getText());
						
						// Sets the details of the logged user in the main,
						// because this class keeps track of it.
						main.setLoggedInUser(userID + "", nameTF.getText(), loggedUserRank);
						
						// Sets the welcome message with user's name
						// welcomeUser() uses the main object to get the user's name
						topPanel.welcomeUser(main);
						
						// Changes the "Log in" label into "Log out"
						topPanel.changeLogInLabelToLogOut();
						
						// Closes the login popup
						hidePopupAndReEnableComponents(popup, root);
						
						// Shows the left panel (which contains the menu) based on user's rank
						main.showLeftPanel(loggedUserRank);
						
						// Shows a confirmation message for successful login
						new FXMessageDialog(parentStage, root, "Welcome back, " + nameTF.getText() + "!");

					//in this case inputed data was not correct/does not exist in database.
					} else {
						errorMessageL.setText("Incorrect name or password!");
					}
				}
			}
		});
		
		registerB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Checks if fName and lName contain valid data.
				if (!Regex.matchName(nameTF.getText()) ||
					!Regex.matchName(surnameTF.getText())) {
					errorMessageL.setText("Please use valid names!");
					
				// Checks if password is strong enough.
				} else if (!Regex.matchPassword(passwordPF.getText())) {
					errorMessageL.setText("Minim 6 chars for password: a-z,A-Z,0-9");
				
				// Checks if used fName and lName already exist into database.
				} else if (Query.getQueryObject().checkIfUserExists(nameTF.getText(),
																	surnameTF.getText())) {
					errorMessageL.setText("User already exists!");
				
				// Inserts the new user in database
				} else {
					// Performs the insert query
					Query.getQueryObject().insertUser(nameTF.getText(), surnameTF.getText(), 
						"client", passwordPF.getText());
					
					// Gets the id of the logged in user
					int userID = Query.getQueryObject().getUserId(nameTF.getText(), surnameTF.getText());

					// Sets the details of the logged user in the main,
					// because this class keeps track of it.
					main.setLoggedInUser(userID + "", nameTF.getText(), "client");

					// Sets the welcome message with user's name
					// welcomeUser() uses the main object to get the user's name
					topPanel.welcomeUser(main);

					// Changes the "Log in" label into "Log out"
					topPanel.changeLogInLabelToLogOut();

					// Closes the login popup
					hidePopupAndReEnableComponents(popup, root);

					// Shows the left panel (which contains the menu) based on user's rank
					main.showLeftPanel("client");

					// Shows a confirmation message for successful login
					new FXMessageDialog(parentStage, root, "Welcome, " + nameTF.getText() + "! Enjoy your new account!");
				}
				
				// Closes the popup
//				hidePopupAndReEnableComponents(popup, root);
			}
		});
		
		// Creates the action that will close the popup.
		cancelB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hidePopupAndReEnableComponents(popup, root);
			}
		});
		
		// Creates a GridPane that will hold both text and button
		GridPane formContainer = new GridPane();
		formContainer.setHgap(5);
		formContainer.setVgap(5);
		formContainer.setPadding(new Insets(0, 0, 0, 2));
                
		// Sets all components into correct places in Grid Pane
		formContainer.add(nameL, 1, 1); // 1 column 1 row
		formContainer.add(nameTF, 2, 1); // 2 column 1 row
		formContainer.add(surnameL, 1, 2); // 1 column 2 row
		formContainer.add(surnameTF, 2, 2); // 2 column 2 row
		formContainer.add(passwordL, 1, 3); // 1 column 3 row
		formContainer.add(passwordPF, 2, 3);// 2 column 3 row              
		
		// Creates a HorizontalBox that will hold the error message
		HBox errorMContainer = new HBox();
		errorMContainer.getChildren().addAll(new Label("  "), errorMessageL);
		
		// Creates a HorizontalBox that will hold the buttons (save/cancel)
		HBox buttonsContainer = new HBox(5);
		buttonsContainer.getChildren().addAll(logInB, registerB, cancelB);
		buttonsContainer.setAlignment(Pos.CENTER);
		
		// Creates a VerticalBox that will hold the components
		VBox vb = new VBox(15);
		vb.setPadding(new Insets(15, 0, 0, 32));
		vb.getChildren().addAll(errorMContainer, formContainer, buttonsContainer);
		
		// Adds VerticalBox to popup
		popup.getContent().add(vb);
		
		// Shows the popup
		popup.show(parentStage);
		
		// Sets the size of the popup
		int width  = 290;
		int height = 190;
		
		// Creates a rectangle with a dropshadow effect.
        // Also we made here a fancy Gradient background.
		Rectangle rect = new Rectangle(width, height,
							new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
								new Stop(0, Color.web("#DDF1FF")),
								new Stop(0.5, Color.WHITE),    
								new Stop(1, Color.web("#DDF1FF")),}));
		rect.setEffect(new DropShadow());
		
		// Adds the rectangle in the popup, as THE FIRST NODE, so index 0
		popup.getContent().add(0, rect);
		
		// Centers the popup
		popup.centerOnScreen();
	}

	private void hidePopupAndReEnableComponents(Popup popup, Pane root) {
		popup.hide();

		// ReEnables root's children.
		for (Node n : root.getChildren()) {
			n.setDisable(false);
		}
	}

}