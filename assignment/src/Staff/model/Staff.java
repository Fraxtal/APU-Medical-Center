/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    
//    private void 
//    private void loadAppointmentData() {
//        appointmentData.clear();
//        try {
//            File file = new File("assignment\\src\\database\\appointments.txt");
//            try (Scanner reader = new Scanner(file)) {
//                while (reader.hasNextLine()) {
//                    String line = reader.nextLine().trim();
//                    if (!line.isEmpty()) {
//                        String[] values = line.split(";");
//                        ArrayList<String> record = new ArrayList<>();
//                        for (String value : values) {
//                            record.add(value.trim());
//                        }
//                        appointmentData.add(record);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//    
//    private void loadInvoiceData() {
//        invoiceData.clear();
//        try {
//            File file = new File("assignment\\src\\database\\invoices.txt");
//            try (Scanner reader = new Scanner(file)) {
//                while (reader.hasNextLine()) {
//                    String line = reader.nextLine().trim();
//                    if (!line.isEmpty()) {
//                        String[] values = line.split(";");
//                        ArrayList<String> record = new ArrayList<>();
//                        for (String value : values) {
//                            record.add(value.trim());
//                        }
//                        invoiceData.add(record);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
    
    
    public void AddCustomer() {
        try {
            
        }
        catch (Exception ex) {
            
        }
        finally
        {
            
        }
    }
}
