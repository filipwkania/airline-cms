package wrappers;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class is used to wrap every Flight Class so we can show it in the table.
 * @author M&F
 */
public class FlightClass {
	private final SimpleStringProperty id;
	private final SimpleStringProperty type;
	private final SimpleStringProperty name;
	private final SimpleStringProperty fcSeatsNo;
	private final SimpleStringProperty ccSeatsNo;
	private final SimpleStringProperty ecSeatsNo;

	public FlightClass(String id, String type, String name,  String fcSeatsNo, 
					   String ccSeatsNo,  String ecSeatsNo) {
		this.id = new SimpleStringProperty(id);
		this.type = new SimpleStringProperty(type);
		this.name = new SimpleStringProperty(name);
		this.fcSeatsNo = new SimpleStringProperty(fcSeatsNo);
		this.ccSeatsNo = new SimpleStringProperty(ccSeatsNo);
		this.ecSeatsNo = new SimpleStringProperty(ecSeatsNo);
	}

	public String getID(){
		return id.get();
	}
	
	public void setID(String id){
		this.id.set(id);
	}
	
	public String getName(){
		return name.get();
	}
	
	public void setName(String name){
		this.name.set(name);
	}
	
	public String getType(){
		return type.get();
	}
	
	public void setType(String type){
		this.type.set(type);
	}
	
	public String getFcSeatsNo(){
		return fcSeatsNo.get();
	}
	
	public void setFcSeatsNo(String seatsNo){
		this.fcSeatsNo.set(seatsNo);
	}
	
	public String getCcSeatsNo(){
		return ccSeatsNo.get();
	}
	
	public void setCcSeatsNo(String seatsNo){
		this.ccSeatsNo.set(seatsNo);
	}
	
	public String getEcSeatsNo(){
		return ecSeatsNo.get();
	}
	
	public void setEcSeatsNo(String seatsNo){
		this.ecSeatsNo.set(seatsNo);
	}
	
	public FlightClass duplicate() {
		return new FlightClass(getID(), getType(), getName(), getFcSeatsNo(), getCcSeatsNo(), getEcSeatsNo());
	}
}