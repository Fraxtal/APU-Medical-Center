package Customer.services;

import Customer.model.Appointment;
import Customer.model.Customer;
import Customer.model.Invoice;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerService implements FileService {
    
    private static final Logger logger = Logger.getLogger(CustomerService.class.getName());
    private static final String APPOINTMENTS_FILE = "src\\database\\appointments.txt";
    private static final String USERS_FILE = "src\\database\\users.txt";
    private static final String INVOICES_FILE = "src\\database\\invoices.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Load all appointments for a specific customer
     */
    public List<Appointment> getCustomerAppointments(int customerId) {
        List<Appointment> appointments = new ArrayList<>();
        
        try {
            File file = new File(APPOINTMENTS_FILE);
            if (!file.exists()) {
                return appointments; // Return empty list if file doesn't exist
            }
            
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] values = line.split(";");
                        if (values.length >= 7) {
                            // Current DB order: id;date;status;doctorId;doctorName;customerId;customerName
                            int appointmentCustomerId = Integer.parseInt(values[5]); // CustomerID at index 5
                            if (appointmentCustomerId == customerId) {
                                Appointment appointment = parseAppointmentFromLine(values);
                                appointments.add(appointment);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading customer appointments", e);
        }
        
        return appointments;
    }
    
    /**
     * Book a new appointment - demonstrates business logic and validation
     */
    public boolean bookAppointment(Appointment appointment) {
        // Input validation - demonstrates encapsulation
        if (appointment == null || !appointment.isValid()) {
            logger.warning("Invalid appointment data provided");
            return false;
        }
        
        // Business rule validation
        if (appointment.isPastAppointment()) {
            logger.warning("Cannot book appointment in the past");
            return false;
        }
        
        try {
            // Generate appointment ID
            String appointmentId = generateAppointmentId();
            appointment.setAppointmentId(appointmentId);
            
            // Write to text file - coursework requirement
            try (FileWriter writer = new FileWriter(APPOINTMENTS_FILE, true)) {
                String line = formatAppointmentForFile(appointment);
                writer.write(line + "\n");
                writer.flush();
            }
            
            logger.info("Appointment booked successfully: " + appointmentId);
            return true;
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error booking appointment", e);
            return false;
        }
    }
    /**
     * Update customer information - demonstrates validation and file operations
     */
    public boolean updateCustomerInfo(Customer customer) {
        // Input validation - demonstrates encapsulation
        if (customer == null || !customer.isValidCustomer()) {
            logger.warning("Invalid customer data provided");
            return false;
        }
        
        try {
            List<String> lines = new ArrayList<>();
            boolean found = false;
            
            // Read all lines and update the specific customer
            File file = new File(USERS_FILE);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (!line.isEmpty()) {
                            String[] values = line.split(";");
                            if (values.length >= 9) {
                                int currentCustomerId = Integer.parseInt(values[0]);
                                if (currentCustomerId == customer.getId()) {
                                    // Update customer information
                                    values[1] = customer.getUsername(); // Username
                                    values[2] = customer.getFullname(); // Fullname
                                    values[3] = customer.getEmail(); // Email
                                    values[4] = customer.getPassword(); // Password
                                    values[5] = customer.getAddress() != null ? customer.getAddress() : ""; // Address
                                    values[6] = customer.getContactNumber() != null ? customer.getContactNumber() : ""; // ContactNumber
                                    // values[7] = dateCreated (keep original)
                                    // values[8] = role (keep original)
                                    line = String.join(";", values);
                                    found = true;
                                }
                            }
                        }
                        lines.add(line);
                    }
                }
            }
            
            if (found) {
                // Write back to text file - coursework requirement
                try (FileWriter writer = new FileWriter(USERS_FILE)) {
                    for (String line : lines) {
                        writer.write(line + "\n");
                    }
                }
                logger.info("Customer information updated successfully: " + customer.getId());
                return true;
            } else {
                logger.warning("Customer not found: " + customer.getId());
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating customer information", e);
        }
        
        return false;
    }

    public Object[][] getAppointmentsForTable(int customerId) {
        List<Appointment> appointments = getCustomerAppointments(customerId);
        Object[][] tableData = new Object[appointments.size()][4];
        
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            tableData[i][0] = appointment.getAppointmentId(); // ID
            tableData[i][1] = appointment.getDateOfAppointment().format(DATE_FORMATTER); // DOA
            tableData[i][2] = appointment.getStatus(); // Status
            tableData[i][3] = appointment.getDoctorName(); // Doctor Name
        }
        
        return tableData;
    }
    
    /**
     * Get invoices for a specific appointment
     */
    public List<Invoice> getInvoicesForAppointment(int appointmentId) {
        List<Invoice> invoices = new ArrayList<>();
        
        try {
            File file = new File(INVOICES_FILE);
            if (!file.exists()) {
                return invoices; // Return empty list if file doesn't exist
            }
            
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] values = line.split(";");
                        if (values.length >= 4) {
                            int invoiceAppointmentId = Integer.parseInt(values[3]); // AppointmentID is at index 3
                            if (invoiceAppointmentId == appointmentId) {
                                Invoice invoice = parseInvoiceFromLine(values);
                                invoices.add(invoice);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading invoices for appointment", e);
        }
        
        return invoices;
    }
    
    // Private helper methods
    
    private String generateAppointmentId() {
        int maxId = 0;
        try {
            File file = new File(APPOINTMENTS_FILE);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (!line.isEmpty()) {
                            String[] values = line.split(";");
                            if (values.length > 0) {
                                String idStr = values[0];
                                if (idStr.startsWith("A")) {
                                    try {
                                        int id = Integer.parseInt(idStr.substring(1));
                                        maxId = Math.max(maxId, id);
                                    } catch (NumberFormatException e) {
                                        // Skip invalid lines
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error generating appointment ID", e);
        }
        return String.format("A%04d", maxId + 1);
    }
    
    private Appointment parseAppointmentFromLine(String[] values) {
        String appointmentId = values[0]; // AppointmentID (A0001)
        LocalDate dateOfAppointment = parseDate(values[1]); // DOA - handle both date and datetime formats
        String status = values[2]; // status
        int doctorId = Integer.parseInt(values[3]); // DoctorID
        String doctorName = values[4]; // Doctor Name
        int customerId = Integer.parseInt(values[5]); // CustomerID
        String customerName = values[6]; // Customer Name
        
        return new Appointment(appointmentId, dateOfAppointment, status, customerId, doctorId, customerName, doctorName);
    }
    
    private LocalDate parseDate(String raw) {
        String trimmed = raw.trim();
        try {
            // Try pure date first
            return LocalDate.parse(trimmed, DATE_FORMATTER);
        } catch (Exception ignore) {
            // Then try date-time and return just the date portion
            try {
                return java.time.LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toLocalDate();
            } catch (Exception ex) {
                // As a fallback, if there's a space, take the date portion before space
                int idx = trimmed.indexOf(' ');
                if (idx > 0) {
                    return LocalDate.parse(trimmed.substring(0, idx), DATE_FORMATTER);
                }
                // Re-throw the last exception if all parsing attempts fail
                throw ex;
            }
        }
    }
    
    private String formatAppointmentForFile(Appointment appointment) {
        // Match current DB order: id;date;status;doctorId;doctorName;customerId;customerName
        return String.format("%s;%s;%s;%d;%s;%d;%s",
                           appointment.getAppointmentId(),
                           appointment.getDateOfAppointment().format(DATE_FORMATTER),
                           appointment.getStatus(),
                           appointment.getDoctorId(),
                           appointment.getDoctorName(),
                           appointment.getCustomerId(),
                           appointment.getCustomerName());
    }
    
    private int generateInvoiceId() {
        int maxId = 0;
        try {
            File file = new File(INVOICES_FILE);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (!line.isEmpty()) {
                            String[] values = line.split(";");
                            if (values.length > 0) {
                                try {
                                    int id = Integer.parseInt(values[0]);
                                    maxId = Math.max(maxId, id);
                                } catch (NumberFormatException e) {
                                    // Skip invalid lines
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error generating invoice ID", e);
        }
        return maxId + 1;
    }
    
    private Invoice parseInvoiceFromLine(String[] values) {
        int invoiceId = Integer.parseInt(values[0]); // InvoiceID
        double total = Double.parseDouble(values[1]); // Total
        String paymentMethod = values[2]; // PaymentMethod
        int appointmentId = Integer.parseInt(values[3]); // AppointmentID
        
        return new Invoice(invoiceId, total, paymentMethod, appointmentId);
    }
    
    private String formatInvoiceForFile(Invoice invoice) {
        return String.format("%d;%.2f;%s;%d",
                           invoice.getInvoiceId(),
                           invoice.getTotal(),
                           invoice.getPaymentMethod(),
                           invoice.getAppointmentId());
    }
}
