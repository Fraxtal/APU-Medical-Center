/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Kingston Teoh
 */
public class viewFeedback {
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    public viewFeedback(DefaultTableModel model) {
        this.model = model;
        this.sorter = new TableRowSorter<>(model);
    }

    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }

    public void loadScheduledAppointments(String filePath) {
        model.setRowCount(0); // Clear existing data

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int feedbackId = 1; // Start with ID 1
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
            
                // Ensure there are enough data fields (7 columns from appointments.txt)
                if (data.length >= 7) {
                    String status = data[2].trim();
                    
                    // Only add appointments with "Scheduled" status
                    if ("Scheduled".equalsIgnoreCase(status)) {
                        model.addRow(new Object[]{
                            String.valueOf(feedbackId++), // Auto-generated Feedback ID
                            data[0], // Appointment ID
                            data[3], // Doctor ID
                            data[4], // Doctor Name
                            data[5], // Customer ID
                            data[6], // Customer Name
                            "No feedback yet" // Default feedback text
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading scheduled appointments: " + e.getMessage());
        }
    }
}