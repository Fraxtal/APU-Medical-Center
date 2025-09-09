/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kingston Teoh
 */
public class viewDoctorCharges {
    private DefaultTableModel model;
    private List<String[]> appointmentData; // Store appointment data as arrays

    public viewDoctorCharges(DefaultTableModel model) {
        this.model = model;
        this.appointmentData = new ArrayList<>();
        loadAppointmentStatuses();
    }

    /**
     * Load all appointment data from appointments.txt
     */
    private void loadAppointmentStatuses() {
        String appointmentsFile = "src\\database\\appointments.txt";
        
        try (BufferedReader br = new BufferedReader(new FileReader(appointmentsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = line.split(";");
                if (data.length >= 7) {
                    // Trim all values and store the appointment data
                    for (int i = 0; i < data.length; i++) {
                        data[i] = data[i].trim();
                    }
                    appointmentData.add(data);
                }
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading appointment data: " + e.getMessage());
        }
    }

    /**
     * Get status of a specific appointment
     */
    private String getAppointmentStatus(String appointmentId) {
        for (String[] appointment : appointmentData) {
            if (appointment[0].equals(appointmentId)) {
                return appointment[2]; // Status is at index 2
            }
        }
        return null; // Appointment not found
    }

    /**
     * Load invoice details from file, but only show those with scheduled appointments
     */
    public void loadCharges(String filePath) {
        model.setRowCount(0); // Clear existing data

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = line.split(";", -1);
                
                // invoiceDetails.txt has 7 fields: InvoiceDetailsID, Item, Quantity, PricePer, Total, InvoiceID, AppointmentID
                if (data.length >= 7) {
                    // Trim all values
                    for (int i = 0; i < data.length; i++) {
                        data[i] = data[i].trim();
                    }
                    
                    String invoiceAppointmentId = data[6]; // AppointmentID is at index 6
                    String status = getAppointmentStatus(invoiceAppointmentId);
                    
                    // Only show charges for scheduled appointments
                    if (status != null && "Scheduled".equalsIgnoreCase(status)) {
                        model.addRow(new Object[]{
                            data[0], // Invoice Details ID
                            data[1], // Item
                            data[2], // Quantity
                            data[3], // Price Per
                            data[4], // Total
                            data[5], // Invoice ID
                            data[6]  // Appointment ID
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading charges: " + e.getMessage());
        }
    }

    /**
     * Refresh the appointment data (call this if appointments might have changed)
     */
    public void refreshAppointmentData() {
        appointmentData.clear();
        loadAppointmentStatuses();
    }
}

