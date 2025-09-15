
package Staff.service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManagePayments {
    private static final String invoicesFile = "src/database/invoices.txt";
    private static final String invoiceDetailsFile = "src/database/invoiceDetails.txt";
    ManageCustomerAccount mca = new ManageCustomerAccount();
    
    public List<String[]> loadInvoices() {
        List<String[]> invoicesData = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(invoicesFile))){
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 4) {
                    String invoicesId = data[0];
                    String subtotal = data[1];
                    String paymentMethod = data[2];
                    String appointmentId = data[3];
                    
                    invoicesData.add(data);
                }
                
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return invoicesData;
    }
    
    public boolean saveInvoices(List<String> newData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invoicesFile))) {
            for (String invoice : newData) {
                bw.write(invoice);
                bw.newLine();
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public List<String[]> loadInvoiceDetails() {
        List<String[]> invoiceDetailsData = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(invoiceDetailsFile))){
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 7) {
                    String invoiceDetailId = data[0];
                    String itemName = data[1];
                    String quantity = data[2];
                    String pricePer = data[3];
                    String priceTotal = data[4];
                    String invoiceId = data[5];
                    String appointmentId = data[6];
                    
                    invoiceDetailsData.add(data);
                }
                
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return invoiceDetailsData;
    }
    
    public List<String[]> loadSpecificInvoiceDetails(String invoiceId) {
        return loadInvoiceDetails().stream().filter(data -> invoiceId.equalsIgnoreCase(data[5])).toList();
    }
    
    public boolean updateInvoicePayment(String invoiceId, String paymentMethod){
        List<String> invoiceLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(invoicesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] values = line.trim().split(";");
                if (values.length >= 4 && values[0].equalsIgnoreCase(invoiceId)) {
                    values[2] = paymentMethod;
                    line = String.join(";", values);
                    found = true;
                }
                invoiceLines.add(line);
            }
        }   catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (found) {
            return saveInvoices(invoiceLines);
        }
        return false;
    }
    
    //function that returns a customer's details(Id, fullname) based on the selected invoice.txt - AppointmentId
    public String returnCustomerNamefromId(String customerId){
        List<String[]> customers = mca.loadCustomers(); 
        for (String[] customer : customers) {
            if (customer[0].equalsIgnoreCase(customerId)) {
                return customer[2];
            }
        }
        return null;
    }
    
//    public String returnCustomerIDfromAppId(String appointmentId){
//        List<String[]> appointments = mca.loadAppointments(); 
//        for (String[] appointment : appointments) {
//            if (appointment[0].equalsIgnoreCase(appointmentId)) {
//                return appointment[5];
//            }
//        }
//        return null;
//        
//    }
    
    
    public Date parseStrToDate(String date){
        try {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
