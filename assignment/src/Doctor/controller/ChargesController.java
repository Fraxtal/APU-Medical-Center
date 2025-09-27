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
    private String invoicesFilePath;
    private String invoiceDetailsFilePath;

    public ChargesController(DefaultTableModel model) {
        this.model = model;
        this.invoicesFilePath = "src\\database\\invoices.txt";
        this.invoiceDetailsFilePath = "src\\database\\InvoiceDetails.txt";
        loadItemPrices();
    }

    public void loadItemPrices() {
        List<String> itemList = new ArrayList<>();
        List<Double> priceList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(invoiceDetailsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 4) {
                    String item = data[1].trim();
                    double price = Double.parseDouble(data[3].trim());
                    
                    // Add to lists if not already present
                    if (!itemList.contains(item)) {
                        itemList.add(item);
                        priceList.add(price);
                    }
                }
            }
            
            // Convert lists to arrays
            items = itemList.toArray(new String[0]);
            prices = new double[priceList.size()];
            for (int i = 0; i < priceList.size(); i++) {
                prices[i] = priceList.get(i);
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading item prices: " + e.getMessage());
        }
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
    
    // Add to table
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
            
            model.removeRow(selectedRow);
                    
            removeFromInvoiceDetailsFile(detailId);
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
        saveInvoiceDetailsToTxt();
        return totalAmount;
    }

    private double calculateTotalForInvoice(String invoiceId) {
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            String rowInvoiceId = model.getValueAt(i, 5).toString();
            if (rowInvoiceId.equals(invoiceId)) {
                total += Double.parseDouble(model.getValueAt(i, 4).toString());
            }
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

    private void saveInvoiceDetailsToTxt() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invoiceDetailsFilePath))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String line = model.getValueAt(i, 0) + ";" +
                             model.getValueAt(i, 1) + ";" +
                             model.getValueAt(i, 2) + ";" +
                             model.getValueAt(i, 3) + ";" +
                             model.getValueAt(i, 4) + ";" +
                             model.getValueAt(i, 5) + ";" +
                             model.getValueAt(i, 6);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving to InvoiceDetails.txt: " + ex.getMessage());
        }
    }

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
        return "001";
    }
    
}
