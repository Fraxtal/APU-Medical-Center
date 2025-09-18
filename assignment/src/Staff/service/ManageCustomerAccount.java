package Staff.service;

import Customer.services.CustomerService;
import java.io.*;
import java.lang.System.Logger.Level;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Admin
 */
public class ManageCustomerAccount {
    private static final String usersFile = "src/database/users.txt";
    private static final String appointmentsFile = "src/database/appointments.txt";
    private static final String invoicesFile = "src/database/invoices.txt";
    private static final String invoiceDetailsFile = "src/database/invoiceDetails.txt";
    //private List<String[]> customersList;
    private List<String[]> appointmentsList;
    private List<String[]> invoicesList;
    private List<String[]> invoiceDetailsList;
    
    public List<String[]> loadUsers() {
        List<String[]> userdata = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))){
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 9) {
                    int customerId = Integer.parseInt(data[0]);
//                    String username = data[1];
//                    String fullname = data[2];
//                    String email = data[3];
//                    String password = data[4];
//                    String address = data[5];
//                    String contactNum = data[6];
//                    String dateCreated = data[7];
//                    String role = data[8];
                    
                    userdata.add(data);
                }
                
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return userdata;
    }
    
    public List<String[]> loadCustomers() {
        //return loadUsers().stream().filter(data -> "Customer".equalsIgnoreCase(data[8])).toList();
        return loadUsers().stream().filter(data -> data[8].trim().equalsIgnoreCase("Customer")).toList();
    }
    
    public List<String[]> loadDoctors() {
        return loadUsers().stream().filter(data -> "Doctor".equalsIgnoreCase(data[8])).toList();
    }
    
    public boolean saveUsers(List<String> newData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(usersFile))) {
            for (String user : newData) {
                bw.write(user);
                bw.newLine();
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean updateCustomer(String customerId, String username, String fullname, String email, String pass, String address, String contact) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] values = line.trim().split(";");
                if (values.length >= 9 && values[0].equals(customerId)) {
                    values[1] = username;
                    values[2] = fullname;
                    values[3] = email;
                    values[4] = pass;
                    values[5] = address;
                    values[6] = contact;
                    line = String.join(";", values);
                    found = true;
                }
                lines.add(line);
            }
        }   catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (found) {
            return saveUsers(lines);
        }
        return false;
    }
    
    public boolean deleteCustomer(String customerId) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] values = line.trim().split(";");
                if (values.length >= 9 && values[0].equals(customerId)) {
                    found = true;
                    continue;
                }
                lines.add(line);
            }
        }   catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (found) {
            return saveUsers(lines);
        }
        return false;
    }
    
    public boolean addCustomer(String username, String fullname, String email, String pass, String address, String contact) {
        int newCustomerId = newCustomerId();
        String newAccount = String.join(";", String.valueOf(newCustomerId), username, fullname, email, pass, address, contact, java.time.LocalDate.now().toString(), "Customer");
        try (FileWriter fw = new FileWriter(usersFile, true)) {
            fw.write(newAccount + System.lineSeparator());
        }
        catch (IOException ioE) {
            System.out.println("Encountered an error while writing the file");
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public boolean checkEmailExists(String email) {
        List<String[]> users = loadUsers(); 
        for (String[] user : users) {
            if (user[3].equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkContactExists(String contact) {
        List<String[]> users = loadUsers(); 
        for (String[] user : users) {
            if (user[6].equals(contact)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkUserIdExists(String id) {
        try {
            List<String[]> users = loadUsers();
            for (String[] user : users) {
                if (user[0].equals(id)) {
                    return true;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        
        return false;
    }
    
    public boolean checkEmailLinkedUserId(String userId, String email){
        try {
            List<String[]> users = loadUsers();
            for (String[] user : users) {
                if (user[0].equals(userId) && user[3].equalsIgnoreCase(email)) {
                    return true;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        
        return false;
    }
    
    public boolean checkContactLinkedUserId(String userId, String contact){
        try {
            List<String[]> users = loadUsers();
            for (String[] user : users) {
                if (user[0].equals(userId) && user[6].equalsIgnoreCase(contact)) {
                    return true;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        
        return false;
    }
    
    public int newCustomerId(){
        int currentMaxId = 0;
        try{
            List<String[]> customers = loadCustomers();
            for (String[] customer : customers){
                if (customer.length > 8){
                    int id = Integer.parseInt(customer[0]);
                    currentMaxId = Math.max(currentMaxId, id);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return currentMaxId + 1;
    }
    
    public Date parseStrToDate(String date){
        try {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
}
