package wrappers;

/**
 * Class used in Main for keeping track about the logged user.
 * @author M&F
 */
public class LoggedInUser {
	private String id;
	private String fName;
	private String rank;
	
	public LoggedInUser(String rank) {
		this.rank = rank;
	}
	
	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getFName() {
		return fName;
	}
	
	public void setFName(String fName) {
		this.fName = fName;
	}
	
	public String getRank() {
		return rank;
	}
	
	public void setRank(String rank) {
		this.rank = rank;
	}
}
