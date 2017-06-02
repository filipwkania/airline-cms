package gui;

import java.net.URL;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import operations.Query;
import wrappers.LoggedInUser;

/**
 * The main class for this system. It starts the application and instantiates the gui components.
 * @author M&F
 */
public class Main extends Application {
	private Stage primaryStage;
	private BorderPane root;
	private TopPanel topPanel;
	private LeftPanel leftPanel;
	private LoggedInUser loggedInUser;
	
	// We made the WelcomePanel as a field, because it will be shown many times,
	// so it doesn't make sense to create a new one each time.
	// Welcome panel contains the search flight panel. It's named "Welcome" because it's the one 
	// that appears when the program starts.
	private WelcomePanel welcomePanel;
	
	public static void main(String[] args) {
		// Creates the database connection for faster use later.
		Query.getQueryObject();
		
		// Starts the application.
		launch(args);
	}
	
	// Neccesary method for starting a JavaFX2 application.
	@Override
	public void start(Stage primaryStage) {
		// Adds the primaryStage as a field
		this.primaryStage = primaryStage;
		
		// Sets application's title
		primaryStage.setTitle("Magic Fly");
		
		// Creates the current system user as a guest
		loggedInUser = new LoggedInUser("guest");
		
		// Creates the root panel that will hold all components
		root = new BorderPane();
		
		// Creates the three main components: top, left and welcome panels.
		topPanel = new TopPanel(primaryStage, root, this);
		LeftPanel leftPanel = new LeftPanel(this, loggedInUser.getRank());
		welcomePanel = new WelcomePanel(primaryStage, root, this);
		
		// Sets the components into the main container
		root.setCenter(welcomePanel);
		root.setTop(topPanel);
		root.setLeft(leftPanel);

		// Creates the scene
		Scene scene = new Scene(root, 687, 400);
		
		// Adds stylesheet to scene
		URL cssResource = getClass().getResource("resources/application.css");
		scene.getStylesheets().add(cssResource.toString());
		
		// Sets the scene to primaryStage(can be considered as a JFrame in Swing)
		primaryStage.setScene(scene);
		
		// Sets stage properties
		primaryStage.setResizable(false);
		
		// Sets icon to application
		URL appIcon = getClass().getResource("resources/airplane_40x40.png");
		primaryStage.getIcons().add(new Image(appIcon.toString()));

		// Shows stage
		primaryStage.show();
	}
	
	/**
	 * Tries to lose the focus by moving it on a label or something.
	 * Labels don't do anything on focus, so the focus can be considered as lost / dissapeared.
	 */
	private void loseFocus() {
		// Moves the focus on the left panel, because it contains only labels.
		root.getLeft().requestFocus();
	}
	
	/**
	 * Displays the specified panel in the center part of the root (application).<br>
	 * LeftPanel has a special method, because needs more details.
	 * @param panelName - exactly the class name.
	 */
	public void showPanel(String panelName) {
		switch (panelName) {
			case "WelcomePanel" : root.setCenter(welcomePanel);
								  
								  // Removes and puts back the left and top panels, in order to be on top
								  // of welcomePanel animation, to avoid showing it in bad places.
								  root.setLeft(null);
								  root.setTop(null);
								  root.setLeft(leftPanel);
								  root.setTop(topPanel);
								  break;
			case "ReservationManagementPanel" : root.setCenter(new ReservationManagementPanel(primaryStage, 
																root, loggedInUser));
											    break;
			case "FCManagementPanel" : root.setCenter(new FCManagementPanel(primaryStage, root));
									   break;
			case "UserManagementPanel" : root.setCenter(new UserManagementPanel(primaryStage, 
											root, loggedInUser));
										 break;
			case "PlaneManagementPanel" : root.setCenter(new PlaneManagementPanel(primaryStage, root));
										  break;
			case "FSManagementPanel" : root.setCenter(new FSManagementPanel(primaryStage, root));
									   break;
			case "IncomeReportPanel" : root.setCenter(new IncomeReportPanel());
									   break;
			default : System.err.println("Error on showPanel()! " + panelName + " doesn't exist.");
		}
		
		// Makes sure the airport suggestion popup doesn't remain open.
		welcomePanel.hideSuggestionPopup();
		
		// Moves away the focus
		loseFocus();
	}
	
	/**
	 * @return the TopPanel object (the panel which contains welcome and log in/out labels)
	 */
	public TopPanel getTopPanel(){
		return topPanel;
	}
	
	/**
	 * Shows the left panel (which contains the navigation) based on user's rank.
	 * @param rank - the rank of the current logged user (guest, staff or admin)
	 */
	public void showLeftPanel(String rank) {
		// Creates the left panel based on the new requested rank.
		leftPanel = new LeftPanel(this, rank);
		
		// Shows it.
		root.setLeft(leftPanel);
	}
	
	/**
	 * Sets the details of the current logged user.<br>
	 * When you want to set a guest, use (null, null, "guest").
	 * @param userID
	 * @param fName
	 * @param rank 
	 */
	public void setLoggedInUser(String userID, String fName, String rank){
		loggedInUser.setID(userID);
		loggedInUser.setFName(fName);
		loggedInUser.setRank(rank);
	}
	
	/**
	 * @return the object which holds info about the current logged user
	 */
	public LoggedInUser getLoggedInUser(){
		return loggedInUser;
	}
}
