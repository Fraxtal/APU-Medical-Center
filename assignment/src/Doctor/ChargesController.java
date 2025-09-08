/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor;

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
        this.invoicesFilePath = "C:\\Users\\Kingston Teoh\\Documents\\NetBeansProjects\\APU-Medical-Center\\assignment\\src\\database\\invoices.txt";
        this.invoiceDetailsFilePath = "C:\\Users\\Kingston Teoh\\Documents\\NetBeansProjects\\APU-Medical-Center\\assignment\\src\\database\\InvoiceDetails.txt";
        loadItemPrices();
    }

    public void loadItemPrices() {
        List<String> itemList = new ArrayList<>();
        List<Double> priceList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(invoiceDetailsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
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

    public void addCharge(String invoiceId, String item, int quantity, double pricePer, double total) {
        String detailId = generateInvoiceDetailId();
        String appointmentId = getAppointmentIdForInvoice(invoiceId);
        
        model.addRow(new Object[]{
            detailId, item, quantity, pricePer, total, invoiceId, appointmentId
        });
    }

    public void deleteCharge(int selectedRow) {
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        }
    }

    public void editCharge(int selectedRow, String invoiceId, String item, int quantity, double pricePer, double total) {
        if (selectedRow != -1) {
            String detailId = model.getValueAt(selectedRow, 0).toString();
            String appointmentId = model.getValueAt(selectedRow, 6).toString();
            
            model.setValueAt(detailId, selectedRow, 0);
            model.setValueAt(item, selectedRow, 1);
            model.setValueAt(quantity, selectedRow, 2);
            model.setValueAt(pricePer, selectedRow, 3);
            model.setValueAt(total, selectedRow, 4);
            model.setValueAt(invoiceId, selectedRow, 5);
            model.setValueAt(appointmentId, selectedRow, 6);
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
            String line = invoiceId + "," + totalAmount + "," + paymentMethod + "," + getAppointmentIdForInvoice(invoiceId);
            bw.write(line);
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving to invoices.txt: " + ex.getMessage());
        }
    }

    private void saveInvoiceDetailsToTxt() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invoiceDetailsFilePath))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String line = model.getValueAt(i, 0) + "," +
                             model.getValueAt(i, 1) + "," +
                             model.getValueAt(i, 2) + "," +
                             model.getValueAt(i, 3) + "," +
                             model.getValueAt(i, 4) + "," +
                             model.getValueAt(i, 5) + "," +
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
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                int id = Integer.parseInt(model.getValueAt(i, 0).toString());
                if (id > maxId) maxId = id;
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }
        return String.format("%03d", maxId + 1);
    }

    private String getAppointmentIdForInvoice(String invoiceId) {
        // Implement your logic to get appointment ID from invoice ID
        // This is a placeholder - replace with your actual logic
        return "001";
    }
    
}
