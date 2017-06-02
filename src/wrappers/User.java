package wrappers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class is used to wrap every user when I show the user list in the UserManagementPanel.
 * @author M&F
 */
public class User {
	private final SimpleStringProperty id;
	private SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty rank;
	private final SimpleStringProperty password;
 
    public User(String id, String fName, String lName, String rank, String password) {
		this.id = new SimpleStringProperty(id);
        this.firstName = new SimpleStringProperty(fName);
        this.lastName = new SimpleStringProperty(lName);
        this.rank = new SimpleStringProperty(rank);
		this.password = new SimpleStringProperty(password);
    }
	
	public String getID() {
		return id.get();
	}
	
	public void setID(String id) {
		this.id.set(id);
	}
 
    public String getFirstName() {
        return firstName.get();
    }
    public void setFirstName(String fName) {
//        firstName.set(fName);
		firstName = new SimpleStringProperty(fName);
    }
        
    public String getLastName() {
        return lastName.get();
    }
    public void setLastName(String lName) {
        lastName.set(lName);
    }
    
    public String getRank() {
        return rank.get();
    }
    public void setRank(String rank) {
        this.rank.set(rank);
    }
	
	public String getPassword() {
		return password.get();
	}
	
	public void setPassword(String password) {
		this.password.set(password);
	}
	
	public User duplicate() {
		return new User(getID(), getFirstName(), getLastName(), getRank(), getPassword());
	}
}
