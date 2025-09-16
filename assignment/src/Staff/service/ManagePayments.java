
package Staff.service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class ManagePayments {
    private static final String invoicesFile = "src/database/invoices.txt";
    private static final String invoiceDetailsFile = "src/database/invoiceDetails.txt";
    private PDDocument receipt;
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
    // moved ManageAppointments class
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
    
    
    
//    public PDDocument generateReceipt(){
//        
//    }
    
//    public void generateReceipt(String customerId, String customerName, String invoiceId ){
//        List<String[]> invoiceDetails = loadSpecificInvoiceDetails(invoiceId);
//        String Title = "APU Medical Centre";
//        String today = (new Date()).toString();
//        
//        float x = 50;
//        float y = 70;   
//        
//        PDDocument doc = new PDDocument();
//        PDPage page = new PDPage(PDRectangle.A4);
//        doc.addPage(page);
//        
//        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)){
//            
//            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
//            contentStream.beginText();
//            contentStream.newLineAtOffset(50, 700);
//            
//            
//            contentStream.showText("Hello, Welcome to PDF box11");
//            contentStream.endText();
//            doc.save("src/testingground/sample.pdf");
//            doc.close();
//        }
//        catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//        
//        
//        
//        
//        
//        
//        
//        
//        System.out.println("success");
//   }
    
    public boolean generateReceipt(String customerId, String customerName, String invoiceId) {
        List<String[]> invoiceDetails = loadSpecificInvoiceDetails(invoiceId);

        if (invoiceDetails.isEmpty()) {
            System.out.println("No invoice details found for invoice ID: " + invoiceId);
            return false;
        }

        String title = "APU Medical Centre";
        String separator = "-------------------------------------------------------------";
        String today = new Date().toString();

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);

        float xMargin = 50;
        float yStart = page.getMediaBox().getHeight() - xMargin;
        float yMargin = yStart;
        float lineHeight = 18;

        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
            contentStream.newLineAtOffset(xMargin, yMargin);
            contentStream.showText(title);
            contentStream.endText();

            yMargin -= lineHeight * 2;

            //date
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.newLineAtOffset(xMargin, yMargin);
            contentStream.showText("Date: " + today);
            contentStream.endText();

            yMargin -= lineHeight;

            //customer info
            contentStream.beginText();
            contentStream.newLineAtOffset(xMargin, yMargin);
            contentStream.showText("Customer ID: " + customerId + "   Name: " + customerName);
            contentStream.endText();

            yMargin -= lineHeight;

            //invoice id
            contentStream.beginText();
            contentStream.newLineAtOffset(xMargin, yMargin);
            contentStream.showText("Invoice ID: " + invoiceId);
            contentStream.endText();

            yMargin -= lineHeight * 1.5;

            //separator
            contentStream.beginText();
            contentStream.newLineAtOffset(xMargin, yMargin);
            contentStream.showText(separator);
            contentStream.endText();

            yMargin -= lineHeight;

            // Table headers
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(xMargin, yMargin);
            contentStream.showText(String.format("%-20s %-10s %-10s %-10s", "Item", "Qty", "Price", "Total"));
            contentStream.endText();

            yMargin -= lineHeight;

            //separators
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.newLineAtOffset(xMargin, yMargin);
            contentStream.showText(separator);
            contentStream.endText();

            yMargin -= lineHeight;

            //itemized invoiced details
            double subtotal = 0;
            for (String[] item : invoiceDetails) {
                String itemName = item[1];
                String quantity = item[2];
                String pricePer = item[3];
                String totalPrice = item[4];

                subtotal += Double.parseDouble(totalPrice);

                contentStream.beginText();
                contentStream.newLineAtOffset(xMargin, yMargin);
                contentStream.showText(String.format("%-20s %-10s %-10s %-10s", itemName, quantity, pricePer, totalPrice));
                contentStream.endText();

                yMargin -= lineHeight;

                if (yMargin < xMargin + lineHeight * 4) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    doc.addPage(page);
                    yMargin = page.getMediaBox().getHeight() - xMargin;
                }
            }

            yMargin -= lineHeight;

            //separator
            contentStream.beginText();
            contentStream.newLineAtOffset(xMargin, yMargin);
            contentStream.showText(separator);
            contentStream.endText();

            yMargin -= lineHeight;

            //subtotal
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(xMargin, yMargin);
            contentStream.showText(String.format("Subtotal: RM %.2f", subtotal));
            contentStream.endText();
        } 
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            doc.save("src/receipts/Invoice_" + invoiceId + ".pdf");
            doc.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
            return false; 
        }

    return true; 
}

}
