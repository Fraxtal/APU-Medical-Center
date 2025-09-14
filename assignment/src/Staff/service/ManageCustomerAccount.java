package Staff.service;

import java.io.*;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Arrays;
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
    public List<String[]> customersList;
    
    public List<String[]> loadUsers() {
        List<String[]> userdata = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))){
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 9) {
                    int customerId = Integer.parseInt(data[0]);
                    String username = data[1];
                    String fullname = data[2];
                    String email = data[3];
                    String password = data[4];
                    String address = data[5];
                    String contactNum = data[6];
                    String dateCreated = data[7];
                    String role = data[8];
                    
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
        return this.customersList = loadUsers().stream().filter(data -> data[8].trim().equalsIgnoreCase("Customer")).toList();
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
    
    public boolean updateCustomer(int id, String username, String fullname, String email, String pass, String address, String contact) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] values = line.trim().split(";");
                if (values.length >= 9 && Integer.parseInt(values[0]) == id) {
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
    
    public boolean deleteCustomer(int id) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] values = line.trim().split(";");
                if (values.length >= 9 && Integer.parseInt(values[0]) == id) {
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
    
    private int generateCustomerId() {
        int maxId = 0;
        try {
            File file = new File(usersFile);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (!line.isEmpty()) {
                            String[] values = line.split(";");
                            if (values.length > 0) {
                                try {
                                    int id = Integer.parseInt(values[0]);
                                    maxId = Math.max(maxId, id);
                                } catch (NumberFormatException e) {
                                    // Skip invalid lines
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxId + 1;
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
    
    public boolean checkIdExists(int id) {
        List<String[]> users = loadUsers(); 
        for (String[] user : users) {
            if (user[0].equals(String.valueOf(id))) {
                return true;
            }
        }
        return false;
    }
    
    public int newCustomerId(){
        List<String[]> customers = loadCustomers();
        int currentMaxId = 0;
        for (String[] customer : customers){
            if (customer.length > 8){
                int id = Integer.parseInt(customer[0]);
                currentMaxId = Math.max(currentMaxId, id);
            }
        }
        return currentMaxId + 1;
    }
    
    public List<String[]> loadAppointments() {
        List<String[]> appointmentData = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))){
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 9) {
                    int appointmentId = Integer.parseInt(data[0]);
                    String appointmentDate = data[1];
                    String status = data[2];
                    String doctorId = data[3];
                    String doctorName = data[4];
                    String customerId = data[5];
                    String customerName = data[6];
                    
                    appointmentData.add(data);
                }
                
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return appointmentData;
    }
}
