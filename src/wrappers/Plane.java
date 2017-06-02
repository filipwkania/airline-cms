package wrappers;

/**
 * This class is used to wrap every plane when I show the planes list in 
 * the PlanesManagementPanel.
 * @author M&F
 */
public class Plane {
	private String id;
	private String flightClassId;
	private String flightClassType;
	private String flightClassName;
    private String airport1Id;
    private String airport2Id;
	private String flightLength;
	private String available;
	private String fullPrice;
	
	public Plane(String id, String flightClassId, String flightClassType, String flightClassName,
			String airport1Id, String airport2Id, String flightLength, String available, String fullPrice) {
		
		this.id	= id;
		this.flightClassId = flightClassId;
		this.flightClassType = flightClassType;
		this.flightClassName = flightClassName;
		this.airport1Id = airport1Id;
		this.airport2Id = airport2Id;
		this.flightLength = flightLength;
		this.available = available;
		this.fullPrice = fullPrice;
	}
	
	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getFlightClassID() {
		return flightClassId;
	}
	
	public void setFlightClassID(String flightClassId) {
		this.flightClassId = flightClassId;
	}
	
	public String getFlightClassType() {
		return flightClassType;
	}
	
	public void setFlightClassType(String flightClassType) {
		this.flightClassType = flightClassType;
	}
	
	public String getFlightClassName() {
		return flightClassName;
	}
	
	public void setFlightClassName(String flightClassName) {
		this.flightClassName = flightClassName;
	}
	
	public String getAirport1ID() {
		return airport1Id;
	}
	
	public void setAirport1ID(String airport1Id) {
		this.airport1Id = airport1Id;
	}
	
	public String getAirport2ID() {
		return airport2Id;
	}
	
	public void setAirport2ID(String airport2Id) {
		this.airport2Id = airport2Id;
	}
	
	public String getFlightLength() {
		return flightLength;
	}
	
	public void setFlightLength(String flightLength) {
		this.flightLength = flightLength;
	}
	
	public String getAvailability() {
		return available;
	}
	
	/**
	 * @param available - uses "yes" / "no"
	 */
	public void setAvailability(String available) {
		this.available = available;
	}
	
	public String getFullPrice() {
		return fullPrice;
	}
	
	public void setFullPrice(String fullPrice) {
		this.fullPrice = fullPrice;
	}
	
	public Plane duplicate() {
		return new Plane(id, flightClassId, flightClassType, flightClassName, airport1Id, airport2Id, flightLength, available, fullPrice);
	}
}
