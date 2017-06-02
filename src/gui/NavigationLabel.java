package gui;

import java.net.URL;
import java.util.LinkedList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Contains a special label used for the navigation menu.<br>
 * This class keeps track of all its instances, action necessary when
 * you want to show a special effect on the clicked label.
 * @see #clearLabelList()
 * @author M&F
 */
public class NavigationLabel extends Label {
	private static URL arrowResource;
	private static LinkedList<NavigationLabel> labelList = new LinkedList<>();
	
	/**
	 * Creates a new NavigationLabel instance.
	 * @param text - label name
	 * @param imageNameAndLocation - image location (it's relative to class location) 
	 */
	public NavigationLabel(String text, String imageNameAndLocation) {
		// Calls Label's constructor
		super(text);
		
		// Adds label to the list. It's important for later use.
		// See showMouseClickEffect method.
		labelList.add(this);
		
		// Prepares the label image
		URL resource = getClass().getResource(imageNameAndLocation);
		Image img = new Image(resource.toString());
		
		// Sets the image to label
		setGraphic(new ImageView(img));
		
		// Gets the arrow image
		if (arrowResource == null) {
			arrowResource = getClass().getResource("resources/arrow_7x14.gif");
		}
		
		// Sets label properties
		setPrefWidth(105);
		setStyle("-fx-padding: 5;");
		
		// Sets the mouse effects
		setMouseEnterEffect();
		setMouseExitEffect();
	}
	
	/**
	 * Sets the effect that appears when mouse enters the label.
	 */
	private void setMouseEnterEffect() {
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				setStyle("-fx-background-color: #EEFFFF;" +
						"-fx-border-color: gray;" +
						"-fx-border-width: 0.5 0 0.5 0;");
			}
		});
	}
	
	/**
	 * Sets the effect that appears when mouse exits the label.
	 */
	private void setMouseExitEffect() {
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				setStyle("-fx-background-color: transparent;" +
						 "-fx-border-color: transparent;");
			}
		});
	}
	
	/**
	 * Sets the effect that appears when mouse clicks the label.
	 * @param label - clicked label
	 */
	public static void showMouseClickEffect(NavigationLabel label) {
		// Removes mouse click effect from all labels
		for (NavigationLabel x : labelList) {
			x.setStyle("-fx-background-color: transparent;" +
					   "-fx-border-color: transparent;" +
					   "-fx-background-image: null;");
		}
		
		// Sets the mouse click effect to received label
		label.setMouseEnterEffect();
		label.setStyle("-fx-background-image: url(\"" + arrowResource.toString() + "\");" +
					   "-fx-background-repeat: no-repeat;" +
					   "-fx-background-position: right center;");
	}
	
	/**
	 * Clears the static list of lebels.<br>
	 * This method is used in LeftPanel when is created.<br>
	 * Without using this method, after creating 2 times the LeftPanel, the lableList
	 * will contain the old labels(from first instance) and the new ones(from the new instance).<br>
	 * We don't want this to happen, so we call this method before creating the LeftPanel,
	 * to make sure that all the old labels are erased.
	 * @see LeftPanel
	 */
	public static void clearLabelList() {
		labelList.clear();
	}
}
