/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor.model;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * @author Kingston Teoh
 */
public class viewAppointment {
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    public viewAppointment(DefaultTableModel model) {
        this.model = model;
        this.sorter = new TableRowSorter<>(model);
    }
    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }
public void loadFromTxt(String filePath) {
    model.setRowCount(0); // Clear existing data

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] data = line.split(";", -1);
            
            // Ensure there are enough data fields
            if (data.length >= 7) { 
                String status = data[2]; 

                // Only add the row if the status is "Scheduled"
                if ("Scheduled".equalsIgnoreCase(status)) {
                    model.addRow(new Object[]{
                        data[0], // Appointment ID
                        data[1], // Date of Appointment
                        status,   // Status
                        data[3],  // Doctor ID
                        data[4],  // Doctor Name
                        data[5],  // Customer ID
                        data[6]   // Customer Name
                    });
                }
            }
        }
    } catch (IOException e) 
        {
        JOptionPane.showMessageDialog(null, "Error loading file: " + e.getMessage());
        }
    }
    
    
}