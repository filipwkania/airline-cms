package wrappers;

import javafx.beans.property.SimpleStringProperty;

/**
 * Wrapper for flight schedules.
 * @author M&F
 */
public class FlightSchedule {
	private SimpleStringProperty planeId;
	private SimpleStringProperty airport1;
	private SimpleStringProperty date1;
	private SimpleStringProperty time1;
	private SimpleStringProperty airport2;
	private SimpleStringProperty date2;
	private SimpleStringProperty time2;
	private SimpleStringProperty id;

	
	public FlightSchedule(String id, String planeId, String airport1, String date1, String time1, 
			String airport2, String date2, String time2){
		
		this.id = new SimpleStringProperty(id);
		this.planeId = new SimpleStringProperty(planeId);
		this.airport1 = new SimpleStringProperty(airport1);
		this.airport2 = new SimpleStringProperty(airport2);
		this.date1 = new SimpleStringProperty(date1);
		this.date2 = new SimpleStringProperty(date2);
		this.time1 = new SimpleStringProperty(time1);
		this.time2 = new SimpleStringProperty(time2);
	}
	
	public String getId(){
		return id.get();
	}
	
	public String getPlaneId(){
		return planeId.get();
	}
	
	public String getAirport1(){
		return airport1.get();
	}
	
	public String getAirport2(){
		return airport2.get();
	}
	
	public String getDate1(){
		return date1.get();
	}
	
	public String getDate2(){
		return date2.get();
	}
	
	public String getTime1(){
		return time1.get();
	}
	
	public String getTime2(){
		return time2.get();
	}
	
	public void setId(String id){
		this.planeId.set(id);
	}
	
	public void setPlaneId(String planeId){
		this.planeId.set(planeId);
	}
	
	public void setAirport1(String airport1){
		this.airport1.set(airport1);
	}
	
	public void setAirport2(String airport2){
		this.airport2.set(airport2);
	}
	
	public void setDate1(String date1){
		this.date1.set(date1);
	}
	
	public void setDate2(String date2){
		this.date2.set(date2);
	}
}
