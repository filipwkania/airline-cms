package wrappers;

/**
 * Class used to wrap reservations contained by the system.
 * @author M&F
 */
public class Reservation {
	private String id;
	private String creationDate;
	private String departureDate;
	private String arrivalAirport;
	private String seatClass;
	private String price;
	private String status;
	private String userName;
	
	public Reservation(String id, String creationDate, String departureDate, String arrivalAirport,
					   String seatClass, String price, String status, String userName) {
		
		this.id = id;
		this.creationDate = creationDate;
		this.departureDate = departureDate;
		this.arrivalAirport = arrivalAirport;
		this.seatClass = seatClass;
		this.price = price;
		this.status = status;
		this.userName = userName;
	}
	
	public String getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(String arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSeatClass() {
		return seatClass;
	}

	public void setSeatClass(String seatClass) {
		this.seatClass = seatClass;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String newStatus) {
		status = newStatus;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userFName) {
		this.userName = userFName;
	}
	
	public Reservation duplicate() {
		return new Reservation(id, creationDate, departureDate, arrivalAirport, 
							   seatClass, price, status, userName);
	}
}
