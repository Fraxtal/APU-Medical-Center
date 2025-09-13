
package Staff.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ManageCustomerAccount {
    private String usersFile = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\APU-Medical-Center\\assignment\\src\\database\\users.txt";
    
    public List<String[]> loadUsers() {
        List<String[]> userdata = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))){
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 9) {
                    int id = Integer.parseInt(data[0]);
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
        return loadUsers().stream().filter(data -> "Customer".equalsIgnoreCase(data[8])).toList();
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
    
}
