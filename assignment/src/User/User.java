package User;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

class InvalidProfileEditException extends Exception {
    public InvalidProfileEditException(String m) {
        super(m);
    }
}

public class User {

    protected int id;
    protected String username;
    protected String email;
    protected String fullname;
    protected ArrayList<ArrayList<String>> data;

    public User(){
        this.data = loadUserDB();
    }
    
    public User(int id, String username, String fullname, String email) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.data = loadUserDB();
    }
    
    public boolean register(String Username, String Fullname, String Email, String Password, String Address, String ContactNo) {
        // Load existing user data
        this.data = loadUserDB();
        
        // Get next available customer ID starting from 10000
        id = getNextCustomerId();
        String date = LocalDate.now().toString();

        try (FileWriter writer = new FileWriter("src\\database\\users.txt", true)) {
            // Write new customer record to database
            writer.write(id + ";" + Username + ";" + Fullname + ";" + Email + ";" + Password + ";" + Address + ";" + ContactNo + ";" + date + ";Customer" + "\n");

            // Add to in-memory data structure
            ArrayList<String> newRecord = new ArrayList<>();
            newRecord.add(String.valueOf(id));
            newRecord.add(Username);
            newRecord.add(Fullname);
            newRecord.add(Email);
            newRecord.add(Password);
            newRecord.add(Address);
            newRecord.add(ContactNo);
            newRecord.add(date);
            newRecord.add("Customer");
            data.add(newRecord);

            System.out.println("Customer registered successfully with ID: " + id);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to database: " + e.getMessage());
            return false;
        }
    }

    public static ArrayList<String> login(String input, String password) {

        ArrayList<ArrayList<String>> data = loadUserDB(); // Load data

        for (ArrayList<String> userRecord : data) {
            boolean credentialMatches = userRecord.get(1).equals(input.trim()) // username
                    || userRecord.get(3).equals(input.trim()); // email (assuming index 3)

            if (credentialMatches && userRecord.get(4).equals(password)) { // assuming password at index 4
               return userRecord;
            }
        }
        throw new SecurityException("Invalid credentials");
    }

    private static ArrayList<ArrayList<String>> loadUserDB() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        try {
            File file = new File("src\\database\\users.txt");
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] values = line.split(";");
                        ArrayList<String> record = new ArrayList<>();
                        for (String value : values) {
                            record.add(value.trim());
                        }
                        data.add(record);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return data;
    }
    
    /**
     * Check if username or email already exists in the database
     */
    public boolean isUserExists(String username,String fullname, String email) {
        for (ArrayList<String> record : data) {
            if (record.size() >= 9) {
                String existingUsername = record.get(1);
                String existingFullname = record.get(2);
                String existingEmail = record.get(3);
                
                if (existingUsername.equalsIgnoreCase(username) || existingEmail.equalsIgnoreCase(email) || existingFullname.equalsIgnoreCase(fullname)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Get the next available customer ID starting from 10000
     */
    private int getNextCustomerId() {
        int maxId = 9999; // Start from 10000, so max is 9999 initially
        
        for (ArrayList<String> record : data) {
            if (record.size() >= 9) { // Ensure record has enough fields
                try {
                    int recordId = Integer.parseInt(record.get(0));
                    String recordRole = record.get(8);
                    
                    // Check if this record is a customer and has ID >= 10000
                    if (recordRole.equalsIgnoreCase("Customer") && recordId >= 10000) {
                        maxId = Math.max(maxId, recordId);
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid ID records
                    continue;
                }
            }
        }
        
        return maxId + 1;
    }
   
    
    public boolean updateProfile(String username, String fullname, String email, String address, String contact, String password) throws InvalidProfileEditException {
        try {
            ArrayList<ArrayList<String>> allData = loadUserDB();
            
        List<String> conflicts = allData.stream()
            .filter(record -> Integer.parseInt(record.get(0)) != this.id)
            .flatMap(record -> {
                List<String> issues = new ArrayList<>();
                if (record.get(1).equals(username)) issues.add("username");
                if (record.get(3).equals(email)) issues.add("email");
                return issues.stream();
            })
            .toList();

        if (!conflicts.isEmpty()) {
            String message = "Repeated " + String.join(" and ", conflicts);
            throw new InvalidProfileEditException(message);
        }
            for (ArrayList<String> record : allData) {
                if (record.size() >= 9 && Integer.parseInt(record.get(0)) == this.id) {
                    record.set(1, username);
                    record.set(2, fullname);
                    record.set(3, email);
                    record.set(4, password);
                    record.set(5, address);
                    record.set(6, contact);
                    break;
                }
            }
            
            try (FileWriter writer = new FileWriter("src\\database\\users.txt")) {
                for (ArrayList<String> record : allData) {
                    writer.write(String.join(";", record) + "\n");
                }
            }
            
            this.username = username;
            this.fullname = fullname;
            this.email = email;
            return true;
        } catch (InvalidProfileEditException e) { 
            throw e;
        }catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
            return false;
        }
    }
    
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullname() { return fullname; }
}

