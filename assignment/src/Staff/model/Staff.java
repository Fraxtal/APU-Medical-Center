
package Staff.model;

import User.User;

public class Staff extends User{
    private String password;
    private String address;
    private String contactNumber;
    private String dateCreated;
    private String role;
    
    public Staff(){}
    
    
    public Staff(int id, String username, String fullname, String email, String password, String address, String contactNumber, String dateCreated) {
        super(id, username, fullname, email);
        this.password = password;
        this.address = address;
        this.contactNumber = contactNumber;
        this.dateCreated = dateCreated;
        this.role = "Staff";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
