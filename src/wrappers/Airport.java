package wrappers;

/**
 * This class is used to wrap airports for showing them in a table.
 * @author M&F
 */
public class Airport {
	private String id;
	private String name;
	private String country;
	private String city;
	
	public Airport(String id, String name, String country, String city) {
		this.id = id;
		this.name = name;
		this.country = country;
		this.city = city;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
}
