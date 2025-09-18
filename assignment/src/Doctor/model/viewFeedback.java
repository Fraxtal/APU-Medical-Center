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
            int feedbackId = getNextFeedbackId(); // Get the next available ID
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
            
                if (data.length >= 7) {
                    String status = data[2].trim();
                    String appointmentId = data[0];
                    
                    // Only add appointments with "Scheduled" status
                    if ("Scheduled".equalsIgnoreCase(status)) {
                        // Check if feedback already exists for this appointment
                        String existingFeedback = findExistingFeedback(appointmentId);
                        
                        model.addRow(new Object[]{
                            String.valueOf(feedbackId++), // Auto-generated Feedback ID
                            appointmentId, // Appointment ID
                            data[3], // Doctor ID
                            data[4], // Doctor Name
                            data[5], // Customer ID
                            data[6], // Customer Name
                            existingFeedback // Use existing feedback if available
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading scheduled appointments: " + e.getMessage());
        }
    }

    private int getNextFeedbackId() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("src\\database\\feedbacks.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
                if (data.length >= 1) {
                    try {
                        int id = Integer.parseInt(data[0]);
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException e) {
                        // Ignore invalid IDs
                    }
                }
            }
        } catch (IOException e) {
            // File might not exist, start from 1
        }
        return maxId + 1;
    }

    private String findExistingFeedback(String appointmentId) {
        try (BufferedReader br = new BufferedReader(new FileReader("src\\database\\feedbacks.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
                if (data.length >= 7 && data[1].equals(appointmentId)) {
                    return data[6]; // Return existing feedback
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        }
        return "No feedback yet";
    }//  create new feedback entries from scheduled appointments
}
