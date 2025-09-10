package User;

import Customer.model.Customer;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class User {

    protected int id;
    protected String username;
    protected String email;
    protected String fullname;
    protected ArrayList<ArrayList<String>> data;

    public User(){}
    
    public User(int id, String username, String fullname, String email) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
    }
    
    public boolean register(String Username, String Fullname, String Email, String Password, String Address, String ContactNo) {
        // Load existing user data
        loadUserDB();
        
        // Check if username or email already exists
        if (isUserExists(Username, Email)) {
            System.out.println("Username or email already exists!");
            return false;
        }
        
        // Get next available customer ID starting from 10000
        id = getNextCustomerId();
        String date = LocalDate.now().toString();

        try (FileWriter writer = new FileWriter("assignment\\src\\database\\users.txt", true)) {
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

    public static User login(String input, String password) {

        ArrayList<ArrayList<String>> data = loadUserDB(); // Load data

        for (ArrayList<String> userRecord : data) {
            boolean credentialMatches = userRecord.get(1).equals(input.trim()) // username
                    || userRecord.get(3).equals(input.trim()); // email (assuming index 3)

            if (credentialMatches && userRecord.get(4).equals(password)) { // assuming password at index 4
                String role = userRecord.get(8); // Assuming role is at index 8

                int userId = Integer.parseInt(userRecord.get(0));
                String userUsername = userRecord.get(1);
                String userFullName = userRecord.get(2);
                String userEmail = userRecord.get(3);

                // 3. Return a specific User subclass based on the role.
                // This switch statement is the ONLY place where the protected
                // constructors of the subclasses are called.
                return switch (role.toLowerCase()) {
                    case "customer" -> new Customer(userId, userUsername, userFullName, userEmail);
//                    case "staff" -> new Staff(userId, userUsername, userFullname, userEmail);
//                    case "doctor" -> new Doctor(userId, userUsername, userFullname, userEmail);
//                    case "manager" -> new Manager(userId, userUsername, userFullname, userEmail);
                    default -> throw new IllegalArgumentException("Unknown role: " + role);
                };
            }
        }
        throw new SecurityException("Invalid credentials");
    }

    private static ArrayList<ArrayList<String>> loadUserDB() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        try {
            File file = new File("assignment\\src\\database\\users.txt");
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
    private boolean isUserExists(String username, String email) {
        for (ArrayList<String> record : data) {
            if (record.size() >= 9) { // Ensure record has enough fields
                String existingUsername = record.get(1);
                String existingEmail = record.get(3);
                
                if (existingUsername.equalsIgnoreCase(username) || existingEmail.equalsIgnoreCase(email)) {
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
   
    
    public void updateUserInformation() {

    }
    
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}

