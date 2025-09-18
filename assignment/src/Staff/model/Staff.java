
package Staff.model;

import User.User;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Staff extends User{
    
    private String password;
    private String address;
    private String contactNum;
    private String dateCreated;
    private String role;
   
    public Staff(){}
   
    public Staff(int id, String username, String email, String fullname, String password, String address, String contactNum, String dateCreated, String role) {
        super(id, username, email, fullname);
        this.password = password;
        this.address = address;
        this.contactNum = contactNum;
        this.dateCreated = dateCreated;
        this.role = role;
    }
    
    //Getter Setter
    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNum() {
        return contactNum;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getRole() {
        return role;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
