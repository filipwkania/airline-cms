package gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * This contains the navigation menu.<br>
 * Navigation will be different, based on 
 * what rank has the logged in user, or if it is just a guest.
 * @author M&F
 */
public class LeftPanel extends VBox {	
	public LeftPanel(final Main main, String rank) {
		// Sets panel style.
		this.setStyle("-fx-border-color: gray;" +
					  "-fx-border-width: 0 0.2 0 0;" +
					  "-fx-background-color: white;");
		
		// Makes sure the label list is empty when we create the new ones.
		NavigationLabel.clearLabelList();
		
		// Shows the guest navigation by default
		showGuestNavigation(main);
		
		if (rank.equals("client")) {
			showClientNavigation(main);
		}
		
		// Shows the staff nevigation if rank is "staff" or "admin"
		if (rank.equals("staff") || rank.equals("admin")) {
			showStaffNavigation(main);
		}
		
		// Shows the admin navigation if rank is "admin"
		if (rank.equals("admin")) {
			showAdminNavigation(main);
		}
	}
	
	/**
	 * Creates and adds to panel the navigation for "guest" rank.
	 * @param main 
	 */
	private void showGuestNavigation(final Main main) {
		final NavigationLabel homeNL = new NavigationLabel("Search flights", "resources/search_14x14.gif");
		homeNL.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				main.showPanel("WelcomePanel");
				NavigationLabel.showMouseClickEffect(homeNL);
			}
		});
		
		this.getChildren().addAll(homeNL);
	}
	
	/**
	 * Creates and adds to panel the navigation for "client" rank.
	 * @param main 
	 */
	private void showClientNavigation(final Main main) {
		final NavigationLabel clientReservationsNL = new NavigationLabel("My bookings", "resources/ticket_14x12.png");
		clientReservationsNL.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				main.showPanel("ReservationManagementPanel");
				NavigationLabel.showMouseClickEffect(clientReservationsNL);
			}
		});
		
		this.getChildren().add(clientReservationsNL);
	}
	
	/**
	 * Creates and adds to panel the extra navigation for "staff" rank.
	 * @param main 
	 */
	private void showStaffNavigation(final Main main) {
		final NavigationLabel allReservationsNL = new NavigationLabel("Reservations", "resources/ticket_14x12.png");
		allReservationsNL.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				main.showPanel("ReservationManagementPanel");
				NavigationLabel.showMouseClickEffect(allReservationsNL);
			}
		});
		
		final NavigationLabel flightClassesNL = new NavigationLabel("Flight classes", "resources/sprocket_14x14.gif");
		flightClassesNL.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				main.showPanel("FCManagementPanel");
				NavigationLabel.showMouseClickEffect(flightClassesNL);
			}
		});
		
		final NavigationLabel planesNL = new NavigationLabel("Planes", "resources/airplane_14x14.gif");
		planesNL.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				main.showPanel("PlaneManagementPanel");
				NavigationLabel.showMouseClickEffect(planesNL);
			}
		});
		
		final NavigationLabel schedulesNL = new NavigationLabel("Schedules", "resources/calendar_14x14.gif");
		schedulesNL.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				main.showPanel("FSManagementPanel");
				NavigationLabel.showMouseClickEffect(schedulesNL);
			}
		});
				
		this.getChildren().addAll(allReservationsNL, flightClassesNL, planesNL, schedulesNL);
	}
	
	/**
	 * Creates and adds to panel the extra navigation for "admin" rank.
	 * @param main 
	 */
	private void showAdminNavigation(final Main main) {		
		final NavigationLabel usersNL = new NavigationLabel("Users", "resources/user_14x14.gif");
		usersNL.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				main.showPanel("UserManagementPanel");
				NavigationLabel.showMouseClickEffect(usersNL);
			}
		});
		
		final NavigationLabel incomeReportNL = new NavigationLabel("Income report", "resources/money_14x14.gif");
		incomeReportNL.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				main.showPanel("IncomeReportPanel");
				NavigationLabel.showMouseClickEffect(incomeReportNL);
			}
		});
		
		this.getChildren().addAll(usersNL, incomeReportNL);
	}
}
