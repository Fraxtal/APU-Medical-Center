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

    protected User(int id, String username, String fullname, String email) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
    }
    
    public boolean register(String Username,String Fullname, String Email, String Password, String Address, String ContactNo) {
        loadUserDB();
        id = data.size();
        String date = LocalDate.now().toString();

        try (FileWriter writer = new FileWriter("assignment\\src\\database\\users.txt", true)) {
            writer.write(id + ";" + Username + ";" + Fullname + ";" + Email + ";" + Password + ";" + Address + ";" + ContactNo + ";" + date + ";Customer" + "\n");

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

            return true;
        } catch (IOException e) {
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
                    case "customer" -> new Customer(userId, userUsername, userFullname, userEmail);
                    case "staff" -> new Staff(userId, userUsername, userFullname, userEmail);
                    case "doctor" -> new Doctor(userId, userUsername, userFullname, userEmail);
                    case "manager" -> new Manager(userId, userUsername, userFullname, userEmail);
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
   
    
    public void updateUserInformation() {

    }
    
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}

