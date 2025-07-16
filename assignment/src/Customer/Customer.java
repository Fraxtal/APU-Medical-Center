import User.User;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Customer extends User {

    private ArrayList<Integer> ids;

    public Customer(int id, String username, String email) {
        super(id, username, email);
    }

    public static void register(String Username, String Email, String Password, String Address, String ContactNo) {

        loadDatabase();

        try (FileWriter writer = new FileWriter("assignment\\src\\database\\users.txt", true)) {
            writer.write("\n");
            System.out.println("Registration successful!");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void loadDatabase() {

    }

}