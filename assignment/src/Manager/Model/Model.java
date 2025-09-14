/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manager.Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.List;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JFileChooser;

/**
 *
 * @author weiha
 */
public class Model{
    private final Map<String, Path> files = 
            Map.of("users", Path.of("src/database/users.txt"),
                    "appointments",Path.of("src/database/appointments.txt"),
                    "feedbacks",Path.of("src/database/feedbacks.txt"),
                    "comments",Path.of("src/database/comments.txt"),
                    "invoices",Path.of("src/database/invoices.txt"),
                    "invoiceDetails",Path.of("src/database/invoiceDetails.txt"));
    
    private List<String[]> bufferData;
    
    public Model()
    {
        
    }
    
    public static String getCurrentDate()
    {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        String formattedDate = now.format(formatter);
        return formattedDate;
    }
    
    public List<String[]> GetData()
    {
        return bufferData;
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
    
    public void AddUser(String[] row)
    {
        bufferData.add(row);
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
    
//    public String getCurrentDate()
//    {
//        LocalDate now = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
//        String formattedDate = now.format(formatter);
//        return formattedDate;
//    }
}
