package Customer;

import User.User;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Customer extends User {

    private ArrayList<ArrayList<String>> data;

    public Customer(int id, String username, String email) {
        super(id, username, email);
    }

    public boolean register(String Username, String Email, String Password, String Address, String ContactNo) {

        loadDatabase();
        LocalDate CurrentDate = LocalDate.now();
        id = data.size();
        String date = CurrentDate.toString();

        try (FileWriter writer = new FileWriter("assignment\\src\\database\\users.txt", true)) {
            writer.write(id + "," + Username + "," + Email + "," + Password + "," + Address + "," + ContactNo + "," + date + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void loadDatabase() {
        
    }

}