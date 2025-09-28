/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manager.Model;

import User.User;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author weiha
 */
public class ManagerModel extends User{
    
    public class DatabaseOverflowException extends Exception {
        public DatabaseOverflowException(String message) {
            super(message);
        }
    }
    
    private final Map<String, Path> files = 
            Map.of("users", Path.of("src/database/users.txt"),
                    "appointments",Path.of("src/database/appointments.txt"),
                    "feedbacks",Path.of("src/database/feedbacks.txt"),
                    "comments",Path.of("src/database/comments.txt"),
                    "invoices",Path.of("src/database/invoices.txt"),
                    "invoiceDetails",Path.of("src/database/invoiceDetails.txt"));
    
    private String password, address, contact, dateCreated;
    
    private List<String[]> bufferData;
    
    public ManagerModel()
    {
        
    }
    
    public ManagerModel(int id, String username, String fullname, String email, String password, String address, String contact, String dateCreated)
    {
        super(id, username, fullname, email);
        this.password = password;
        this.address = address;
        this.contact = contact;
        this.dateCreated = dateCreated;
    }
    
    public static String GetCurrentDateTime()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd  hh.mm.ss a");
        String formattedDate = now.format(formatter);
        return formattedDate;
    }
    
    public List<String[]> GetData()
    {
        return bufferData;
    }
    
    public void FlushData()
    {
        bufferData.clear();
    }
    
    public List<String[]> ReadFileAndSaveInBuffer(String key)
    {
        if (bufferData != null)
        {
            bufferData.clear();
        }
        
        bufferData = ReadFile(key);
        
        return bufferData;
    }
    
    public List<String[]> ReadFile(String key)
    {        
        List<String[]> data = new ArrayList<>();
        Path filePath = files.get(key);

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(";");
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    
    public void WriteFile(String key)
    {
        Path filePath = files.get(key);
         try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (String[] row : bufferData) {
                String line = String.join(";", row);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void AddUser(List<String> row) throws DatabaseOverflowException
    {
        int id = Integer.parseInt(bufferData.getLast()[0].substring(1)) + 1;
        if (id > 9999){throw new DatabaseOverflowException("The "+row.getLast().toLowerCase()+"amount is exceeding 9999 limit");}
        id += switch (row.getLast())
        {
            case "Doctor" -> 20000;
            case "Manager" -> 30000;
            case "Staff" -> 40000;
            default -> 10000;
        };
        LocalDate date = LocalDate.now();
        row.addFirst(String.valueOf(id));
        row.add(7, date.toString());
        bufferData.add(row.toArray(String[]::new));
        WriteFile("users");
    }
    
    public void EditUser(int index, String[] row)
    {
        if (index >= 0 && index < bufferData.size()) {
            bufferData.set(index, row);
            WriteFile("users");
        }
    }
    
    public void DeleteUser(int index)
    {
        if (index >= 0 && index < bufferData.size()) {
            bufferData.remove(index);
            WriteFile("users");
        }
    }

    public String GetPassword() 
    {
        return password;
    }

    public void SetPassword(String password) 
    {
        this.password = password;
    }

    public String GetAddress() 
    {
        return address;
    }

    public void SetAddress(String address) 
    {
        this.address = address;
    }

    public String GetContact() 
    {
        return contact;
    }

    public void SetContact(String contact) {
        this.contact = contact;
    }

    public String GetDateCreated() {
        return dateCreated;
    }

    public void SetDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
