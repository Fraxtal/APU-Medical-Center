/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kingston Teoh
 */
public class ChargesController {
    private DefaultTableModel model;
    private String[] items;
    private double[] prices;
    private String invoicesFilePath = "src\\database\\invoices.txt";
    private String invoiceDetailsFilePath = "src\\database\\InvoiceDetails.txt";

    public ChargesController(DefaultTableModel model) {
        this.model = model;
        loadItemPrices();
    }

    public void loadItemPrices() {
        // Use predefined items and prices instead of reading from file
        items = new String[]{"Paracetamol", "Vitamin C", "Blood Test", "Consultation", "X-ray", "Antibiotics", "Blood Pressure Test"};
        prices = new double[]{5.00, 10.00, 50.00, 100.00, 200.00, 15.00, 25.00};
    }

    public double getItemPrice(String item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(item)) {
                return prices[i];
            }
        }
        return 0.0;
    }

    public String[] getItems() {
        return items;
    }

    public double calculateTotal(int quantity, double pricePer) {
        return quantity * pricePer;
    }


    public void addCharge(String invoiceId, String item, int quantity, double pricePer, double total, String appointmentId) {
        String detailId = generateInvoiceDetailId();
        
        // Add to table model first
        model.addRow(new Object[]{
            detailId, item, quantity, 
            String.format("%.2f", pricePer), 
            String.format("%.2f", total), 
            invoiceId, appointmentId
        });
        
        // Then save to file
        saveToInvoiceDetailsFile(detailId, item, quantity, pricePer, total, invoiceId, appointmentId);
    }

    private void saveToInvoiceDetailsFile(String detailId, String item, int quantity, double pricePer, 
                                            double total, String invoiceId, String appointmentId) {
        String filePath = "src\\database\\InvoiceDetails.txt";
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = detailId + ";" +
                        item + ";" +
                        quantity + ";" +
                        String.format("%.2f", pricePer) + ";" +
                         String.format("%.2f", total) + ";" +
                        invoiceId + ";" +
                        appointmentId;
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving to InvoiceDetails.txt: " + e.getMessage());
        }
}

    public void deleteCharge(int selectedRow) {
        if (selectedRow != -1) {
            String detailId = model.getValueAt(selectedRow, 0).toString();
            
            // Remove from file first
            removeFromInvoiceDetailsFile(detailId);
            
            // Then remove from table
            model.removeRow(selectedRow);
        }
    }

    private void removeFromInvoiceDetailsFile(String detailId) {

        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(invoiceDetailsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length > 0 && !data[0].equals(detailId)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading InvoiceDetails.txt: " + e.getMessage());
            return;
        }
        
  
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoiceDetailsFilePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving to InvoiceDetails.txt: " + e.getMessage());
        }
    }


    public double sendInvoice(String invoiceId) {
        double totalAmount = calculateTotalForInvoice(invoiceId);
        saveToInvoicesTxt(invoiceId, totalAmount);
        return totalAmount;
    }

    private double calculateTotalForInvoice(String invoiceId) {
        double total = 0;
        // Read from file instead of just table model
        try (BufferedReader br = new BufferedReader(new FileReader(invoiceDetailsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 7 && data[5].trim().equals(invoiceId)) {
                    total += Double.parseDouble(data[4].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading invoice details: " + e.getMessage());
        }
        return total;
    }

    private void saveToInvoicesTxt(String invoiceId, double totalAmount) {
        String paymentMethod = "Cash";
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invoicesFilePath, true))) {
            String line = invoiceId + ";" + totalAmount + ";" + paymentMethod + ";" + getAppointmentIdForInvoice(invoiceId);
            bw.write(line);
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving to invoices.txt: " + ex.getMessage());
        }
    }

    // Remove this method - it overwrites the entire file with just table data
    // This is dangerous as it loses data not currently in the table

    private String generateInvoiceDetailId() {
        int maxId = 0;
    
        // Read from the actual file to get all existing IDs
        try (BufferedReader br = new BufferedReader(new FileReader(invoiceDetailsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length > 0) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException e) {
                        // Skip invalid IDs
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return String.format("%03d", maxId + 1);
    }

    private String getAppointmentIdForInvoice(String invoiceId) {
        // Get actual appointment ID from invoice details
        try (BufferedReader br = new BufferedReader(new FileReader(invoiceDetailsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 7 && data[5].trim().equals(invoiceId)) {
                    return data[6].trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading appointment ID: " + e.getMessage());
        }
        return "";
    }
    
}
