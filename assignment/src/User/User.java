package User;

import Customer.Customer;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class User {
    protected int id;
    protected String username;
    protected String email;
    protected final ArrayList<ArrayList<String>> data;

    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.data = new ArrayList<>();
    }

    public Object login(String input, String password, boolean isEmailLogin) {
        loadUserDB(); // Load user data
    
        for (ArrayList<String> userRecord : data) {
            // Verify record has all required fields (including role)
            if (userRecord.size() < 8) continue; // Skip incomplete records

            boolean credentialMatches = isEmailLogin
                ? userRecord.get(2).equals(input.trim()) // Email check
                : userRecord.get(1).equals(input.trim()); // Username check

            if (credentialMatches && userRecord.get(3).equals(password)) {
                String role = userRecord.get(7); // Assuming role is at index 7
                
                // Return appropriate class instance based on role
                switch(role.toLowerCase()) {
                    case "customer" -> {
                        return new Customer(
                                Integer.parseInt(userRecord.get(0)),
                                userRecord.get(1),
                                userRecord.get(2)
                        );
                    }
                    case "staff" -> {
                        return new Staff(
                                Integer.parseInt(userRecord.get(0)),
                                userRecord.get(1),
                                userRecord.get(2)
                        );
                    }
                    case "doctor" -> {
                        return new Doctor(
                                Integer.parseInt(userRecord.get(0)),
                                userRecord.get(1),
                                userRecord.get(2)
                        );
                    }
                    case "manager" -> {
                        return new Manager(
                                Integer.parseInt(userRecord.get(0)),
                                userRecord.get(1),
                                userRecord.get(2)
                        );
                    }
                    default -> throw new IllegalArgumentException("Unknown role: " + role);
                }
            }
        }
        throw new SecurityException("Invalid credentials");
    }

    public boolean loadUserDB() {
        data.clear();
        try{
            File file = new File("assignment\\src\\database\\users.txt");
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] values = line.split(",");
                        ArrayList<String> record = new ArrayList<>();
                        for (String value : values) {
                            record.add(value.trim());
                        }
                        data.add(record);
                    }
                }
            }
            return true;
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }

}