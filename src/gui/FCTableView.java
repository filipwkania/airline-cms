package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import operations.Query;
import wrappers.FlightClass;

/**
 * Creates and populates a table which shows flight classes in "FCManagementPanel".<br>
 * It's also used in popup selector for PlaneManagementPanel
 * @author M&F
 */
public class FCTableView extends TableView<FlightClass> {
	public FCTableView() {
		// Creates an ObservableList which will contain all the flightclasses.
		// It's necessary for outputting to table.
        ObservableList<FlightClass> tableData = FXCollections.observableArrayList();

        // Creates columns.
        TableColumn flightTypeName = new TableColumn("Type name");
        TableColumn flightClassName = new TableColumn("Class name");
        TableColumn FCseats = new TableColumn("F.C");
        TableColumn CCseats = new TableColumn("C.C");
        TableColumn ECseats = new TableColumn("E.C");
        
        // Sets columns unresizeable.
        FCseats.setResizable(false);
        CCseats.setResizable(false);
        ECseats.setResizable(false);
        flightClassName.setResizable(false);
        flightTypeName.setResizable(false);
        
        // Sets propertyValueFactory for the Columns.
        flightClassName.setCellValueFactory(new PropertyValueFactory<FlightClass, String>("name"));
        FCseats.setCellValueFactory(new PropertyValueFactory<FlightClass,String>("fcSeatsNo"));
        CCseats.setCellValueFactory(new PropertyValueFactory<FlightClass,String>("ccSeatsNo"));
        ECseats.setCellValueFactory(new PropertyValueFactory<FlightClass,String>("ecSeatsNo"));
        flightTypeName.setCellValueFactory(new PropertyValueFactory<FlightClass,String>("type"));
        
        // Sets preffered witdh of table's columns.
		flightTypeName.setMinWidth(80);
        flightClassName.setPrefWidth(95);
        FCseats.setPrefWidth(45);
        CCseats.setPrefWidth(45);
        ECseats.setPrefWidth(45);
        
        // Adds columns to table.
        this.getColumns().addAll(flightTypeName,flightClassName,FCseats, CCseats,ECseats);
        
        // Part of populating table with data.
        // Fills ResultSet with data from database.
        ResultSet rs = Query.getQueryObject().getFlightClasses();
        
        // Checks if ResultSet is not empty.
        if (rs != null) {
            try {
                // Adds elements from ResultSet into data of the table until ResultSet has no next element.
                while(rs.next())
                    tableData.add(new FlightClass(rs.getString(1),rs.getString(2),rs.getString(3),
							rs.getString(4),rs.getString(5),rs.getString(6)));
            } catch (SQLException ex) {
                System.err.println("SQL error in FCManagementPanel fetch from query result!");
            }
        }
		
        // Adds items to the table.
        this.setItems(tableData);
	}
}
	
