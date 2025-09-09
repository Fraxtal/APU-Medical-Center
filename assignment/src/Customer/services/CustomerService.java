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

/**
 * Service layer for Customer business logic and data access
 * Demonstrates OOP concepts: Encapsulation, Abstraction, Polymorphism
 * Implements IService interface to demonstrate interface implementation
 * Handles all customer-related operations including appointment management
 * Uses text files for data persistence as per coursework requirements
 */
public class CustomerService implements FileService {
    
    private static final Logger logger = Logger.getLogger(CustomerService.class.getName());
    private static final String APPOINTMENTS_FILE = "assignment\\src\\database\\appointments.txt";
    private static final String USERS_FILE = "assignment\\src\\database\\users.txt";
    private static final String INVOICES_FILE = "assignment\\src\\database\\invoices.txt";
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
                        if (values.length >= 9) {
                            int appointmentCustomerId = Integer.parseInt(values[5]); // CustomerID is at index 5
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
            int appointmentId = generateAppointmentId();
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
     * Cancel an appointment - demonstrates business logic and file operations
     */
    public boolean cancelAppointment(int appointmentId) {
        // Input validation
        if (appointmentId <= 0) {
            logger.warning("Invalid appointment ID provided");
            return false;
        }
        
        try {
            List<String> lines = new ArrayList<>();
            boolean found = false;
            
            // Read all lines and update the specific appointment
            File file = new File(APPOINTMENTS_FILE);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (!line.isEmpty()) {
                        String[] values = line.split(";");
                        if (values.length >= 9) {
                            int currentAppointmentId = Integer.parseInt(values[0]);
                            if (currentAppointmentId == appointmentId) {
                                // Business rule: Check if appointment can be cancelled
                                String currentStatus = values[2]; // status is at index 2
                                if ("CANCELLED".equalsIgnoreCase(currentStatus)) {
                                    logger.warning("Appointment already cancelled: " + appointmentId);
                                    return false;
                                }
                                if ("COMPLETED".equalsIgnoreCase(currentStatus)) {
                                    logger.warning("Cannot cancel completed appointment: " + appointmentId);
                                    return false;
                                }
                                
                                // Update status to CANCELLED
                                values[2] = "CANCELLED";
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
                try (FileWriter writer = new FileWriter(APPOINTMENTS_FILE)) {
                    for (String line : lines) {
                        writer.write(line + "\n");
                    }
                }
                logger.info("Appointment cancelled successfully: " + appointmentId);
                return true;
            } else {
                logger.warning("Appointment not found: " + appointmentId);
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cancelling appointment", e);
        }
        
        return false;
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
    
    /**
     * Get available doctors (placeholder - would typically come from a doctor service)
     */
    public List<String> getAvailableDoctors() {
        List<String> doctors = new ArrayList<>();
        doctors.add("Dr. Smith");
        doctors.add("Dr. Johnson");
        doctors.add("Dr. Williams");
        doctors.add("Dr. Brown");
        doctors.add("Dr. Davis");
        return doctors;
    }
    
    /**
     * Get available appointment types
     */
    public List<String> getAppointmentTypes() {
        List<String> types = new ArrayList<>();
        types.add("General Consultation");
        types.add("Follow-up");
        types.add("Emergency");
        types.add("Specialist Consultation");
        types.add("Health Check-up");
        return types;
    }
    
    /**
     * Create an invoice for an appointment
     */
    public boolean createInvoice(Invoice invoice) {
        // Input validation
        if (invoice == null || !invoice.isValid()) {
            logger.warning("Invalid invoice data provided");
            return false;
        }
        
        try {
            // Generate invoice ID
            int invoiceId = generateInvoiceId();
            invoice.setInvoiceId(invoiceId);
            
            // Write to text file - coursework requirement
            try (FileWriter writer = new FileWriter(INVOICES_FILE, true)) {
                String line = formatInvoiceForFile(invoice);
                writer.write(line + "\n");
                writer.flush();
            }
            
            logger.info("Invoice created successfully: " + invoiceId);
            return true;
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error creating invoice", e);
            return false;
        }
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
    
    /**
     * Check if appointment time slot is available
     */
    public boolean isTimeSlotAvailable(LocalDate date, LocalTime time, String doctorName) {
        try {
            File file = new File(APPOINTMENTS_FILE);
            if (!file.exists()) {
                return true; // No appointments exist, so slot is available
            }
            
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] values = line.split(";");
                        if (values.length >= 9) {
                            LocalDate appointmentDate = LocalDate.parse(values[1], DATE_FORMATTER); // DOA at index 1
                            String appointmentDoctor = values[8]; // Doctor Name at index 8
                            String status = values[2]; // status at index 2
                            
                            if (appointmentDate.equals(date) && 
                                appointmentDoctor.equals(doctorName) &&
                                !"CANCELLED".equalsIgnoreCase(status)) {
                                return false; // Slot is taken
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error checking time slot availability", e);
        }
        
        return true; // Slot is available
    }
    
    // Private helper methods
    
    private int generateAppointmentId() {
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
            logger.log(Level.WARNING, "Error generating appointment ID", e);
        }
        return maxId + 1;
    }
    
    private Appointment parseAppointmentFromLine(String[] values) {
        int appointmentId = Integer.parseInt(values[0]); // AppointmentID
        LocalDate dateOfAppointment = LocalDate.parse(values[1], DATE_FORMATTER); // DOA
        String status = values[2]; // status
        String feedback = values[3]; // feedback
        String comment = values[4]; // comment
        int customerId = Integer.parseInt(values[5]); // CustomerID
        int doctorId = Integer.parseInt(values[6]); // DoctorID
        String customerName = values[7]; // Customer Name
        String doctorName = values[8]; // Doctor Name
        
        return new Appointment(appointmentId, dateOfAppointment, status, feedback, comment,
                             customerId, doctorId, customerName, doctorName);
    }
    
    private String formatAppointmentForFile(Appointment appointment) {
        return String.format("%d;%s;%s;%s;%s;%d;%d;%s;%s",
                           appointment.getAppointmentId(),
                           appointment.getDateOfAppointment().format(DATE_FORMATTER),
                           appointment.getStatus(),
                           appointment.getFeedback(),
                           appointment.getComment(),
                           appointment.getCustomerId(),
                           appointment.getDoctorId(),
                           appointment.getCustomerName(),
                           appointment.getDoctorName());
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
