package Customer;

import User.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Customer extends User {

    private ArrayList<ArrayList<String>> appointmentData;


    public Customer(int id, String username, String email) {
        super(id, username, email);
    }

    public boolean register(String Username, String Email, String Password, String Address, String ContactNo) {
        boolean LoadingStatus = loadUserDB();
        if (LoadingStatus == true){
            LocalDate CurrentDate = LocalDate.now();
            id = data.size();
            String date = CurrentDate.toString();

            try (FileWriter writer = new FileWriter("assignment\\src\\database\\users.txt", true)) {
                writer.write(id + "," + Username + "," + Email + "," + Password + "," + Address + "," + ContactNo + "," + date + ",Customer" + "\n");

                ArrayList<String> newRecord = new ArrayList<>();
                newRecord.add(String.valueOf(id));
                newRecord.add(Username);
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
        else
            return false;
    }

    public void requestAppointment(){
        checkAppointmentID();

    }

    private boolean checkAppointmentID(){
        appointmentData.clear();
        try{
            File file = new File("assignment\\src\\database\\appointments.txt");
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] values = line.split(",");
                        ArrayList<String> record = new ArrayList<>();
                        for (String value : values) {
                            record.add(value.trim());
                        }
                        appointmentData.add(record);
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