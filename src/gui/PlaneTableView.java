package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import operations.Query;
import wrappers.Plane;

/**
 * Creates and populates a TableView with all the planes
 * contained into the database.
 * @author M&F
 */
public class PlaneTableView extends TableView<Plane> {
	/**
	 * Creates a table view with all planes, or just with the available one, if parameter is true.
	 * @param justAvailablePlanes 
	 */
	public PlaneTableView(boolean justAvailablePlanes) {
		// Gets the planes list from the database
		ResultSet rs = Query.getQueryObject().getPlanes(justAvailablePlanes);
		
		// Creates an ObservableList which will contain all the users.
		// It's necessary for outputting to table.
		ObservableList<Plane> planesTableData = FXCollections.observableArrayList();
		
		// Fetches the query result data into the observable list.
		// id, flightClassId, flightClassType, flightClassName, airport1Id, 
		// airport2Id, flightLength, available and fullPrice are added into the Plane object
		// id and flightClassId are not shown. They're needed just for database usage.
		if (rs != null) {
			try {
				while (rs.next()) {
					planesTableData.add(new Plane(rs.getString(1), rs.getString(2), rs.getString(3), 
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), 
						rs.getString(8), rs.getString(9)));
				}
			} catch (SQLException ex) {
				System.err.println("SQL error in PlaneManagementPanel");
			}
		}
		
		// Creates the table columns
		TableColumn planeIDColumn = new TableColumn("ID");
		TableColumn flightClassTypeColumn = new TableColumn("Type");
		TableColumn flightClassNameColumn = new TableColumn("Name");
		TableColumn airport1IdColumn = new TableColumn("Airport 1");
		TableColumn airport2IdColumn = new TableColumn("Airport 2");
		TableColumn flightLengthColumn = new TableColumn("F.Length");
		TableColumn availableColumn = new TableColumn("Available");
		TableColumn fullPriceColumn = new TableColumn("Price");
		
		// Specifies which elements from the inserted object should be printed in each cell
		planeIDColumn.setCellValueFactory(new PropertyValueFactory("ID"));
		flightClassTypeColumn.setCellValueFactory(new PropertyValueFactory("flightClassType"));
		flightClassNameColumn.setCellValueFactory(new PropertyValueFactory("flightClassName"));
		airport1IdColumn.setCellValueFactory(new PropertyValueFactory("airport1ID"));
		airport2IdColumn.setCellValueFactory(new PropertyValueFactory("airport2ID"));
		flightLengthColumn.setCellValueFactory(new PropertyValueFactory("flightLength"));
		availableColumn.setCellValueFactory(new PropertyValueFactory("availability"));
		fullPriceColumn.setCellValueFactory(new PropertyValueFactory("fullPrice"));
		
		// Sets columns size
		planeIDColumn.setPrefWidth(35);
		flightClassTypeColumn.setPrefWidth(76);
		flightClassNameColumn.setPrefWidth(120);
		airport1IdColumn.setPrefWidth(70);
		airport2IdColumn.setPrefWidth(70);
		flightLengthColumn.setPrefWidth(70);
		availableColumn.setPrefWidth(70);
		fullPriceColumn.setPrefWidth(50);
		
		// Adds columns and data to it
		this.getColumns().addAll(planeIDColumn, flightClassTypeColumn, flightClassNameColumn,
				airport1IdColumn, airport2IdColumn, flightLengthColumn, availableColumn, fullPriceColumn);
		this.setItems(planesTableData);
		this.setPrefHeight(195);
	}
}
