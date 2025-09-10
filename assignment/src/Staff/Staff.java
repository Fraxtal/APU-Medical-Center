/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Staff;

import User.User;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Staff extends User{
    
   private static ArrayList<ArrayList<String>> appointmentData;
   private static ArrayList<ArrayList<String>> invoiceData;
//   private static ArrayList<ArrayList<String>> itemdata;
   
   
    public Staff(int id, String username, String email) {
        super(id, username, email);
    }
    
    //Getter Setter
    //...
    
    private void loadAppointmentData() {
        appointmentData.clear();
        try {
            File file = new File("assignment\\src\\database\\appointments.txt");
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] values = line.split(";");
                        ArrayList<String> record = new ArrayList<>();
                        for (String value : values) {
                            record.add(value.trim());
                        }
                        appointmentData.add(record);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void loadInvoiceData() {
        invoiceData.clear();
        try {
            File file = new File("assignment\\src\\database\\invoices.txt");
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] values = line.split(";");
                        ArrayList<String> record = new ArrayList<>();
                        for (String value : values) {
                            record.add(value.trim());
                        }
                        invoiceData.add(record);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
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
