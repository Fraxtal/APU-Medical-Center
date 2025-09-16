package Customer.services;

import Customer.model.Appointment;
import Customer.model.Customer;
import Customer.model.Invoice;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.logging.*;

public class CustomerService implements FileService {
    
    private static final Logger logger = Logger.getLogger(CustomerService.class.getName());
    private static final String APPOINTMENTS_FILE = "src\\database\\appointments.txt";
    private static final String USERS_FILE = "src\\database\\users.txt";
    private static final String INVOICES_FILE = "src\\database\\invoices.txt";
    private static final String COMMENTS_FILE = "src\\database\\comments.txt";
    private static final DateTimeFormatter DTFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
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
            tableData[i][1] = appointment.getDateOfAppointment(); // DOA
            tableData[i][2] = appointment.getStatus(); // Status
            tableData[i][3] = appointment.getDoctorName(); // Doctor Name
        }
        
        return tableData;
    }
    
        public List<Invoice> getInvoicesForAppointment(String appointmentId) {
        List<Invoice> invoices = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(INVOICES_FILE))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] v = line.split("[;,]");
                if (v.length >= 4) {
                    String invApptId = v[3].trim();    // now A0001 format
                    if (appointmentId.equalsIgnoreCase(invApptId)) {
                        invoices.add(parseInvoiceFromLine(v));
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading invoices", e);
        }
        return invoices;
    }


    public Invoice getInvoiceByAppointmentId(String appointmentId) {
        List<Invoice> list = getInvoicesForAppointment(appointmentId);
        return list.isEmpty() ? null : list.get(0);
    }

    public Object[][] getInvoiceDetailsForDisplay(String invoiceId) {
        List<Object[]> details = new ArrayList<>();
        
        try {
            File file = new File("src\\database\\invoiceDetails.txt");
            if (!file.exists()) {
                return new Object[0][4];
            }
            
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) continue;
                    String[] values = line.split("[;,]");
                    if (values.length >= 7) {
                        String detailInvoiceId = values[5].trim();
                        if (detailInvoiceId.equalsIgnoreCase(invoiceId)) {
                            Object[] row = new Object[] {
                                values[1],
                                Integer.parseInt(values[2]),
                                String.format("RM %.2f", Double.parseDouble(values[3])),
                                String.format("RM %.2f", Double.parseDouble(values[4]))
                            };
                            details.add(row);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading invoice details", e);
        }
        return details.toArray(new Object[0][4]);
    }

    public String getDoctorFeedbackForAppointment(String appointmentId) {
        File file = new File("src\\database\\feedbacks.txt");
        if (!file.exists()) return null;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] p = line.split("[;,]");
                if (p.length >= 7) {
                    String rawApptId = p[1].trim();
                    String normalizedFileAppt = normalizeAppointmentId(rawApptId);
                    String normalizedInputAppt = normalizeAppointmentId(appointmentId);
                    if (normalizedFileAppt.equalsIgnoreCase(normalizedInputAppt)) {
                        return p[6].trim();
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading doctor feedback", e);
        }
        return null;
    }
    
    private String normalizeAppointmentId(String id) {
        if (id == null || id.isBlank()) return "";
        String trimmed = id.trim();
        if (trimmed.matches("A0*\\d+")) {
            try {
                int n = Integer.parseInt(trimmed.substring(1));
                return String.valueOf(10000 + n);
            } catch (NumberFormatException ignore) { /* fall through */ }
        }
        if (trimmed.matches("\\d+")) {
            try {
                int n = Integer.parseInt(trimmed);
                if (n >= 10000) {
                    return String.valueOf(n);
                }
            } catch (NumberFormatException ignore) { /* fall through */ }
        }
        return trimmed;
    }
    
    public List<String> getDoctors() {
        List<String> doctorNames = new ArrayList<>();
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return doctorNames;
            }
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) continue;
                    String[] values = line.split(";");
                    // Expected format:
                    // 0:id;1:username;2:fullname;3:email;4:password;5:address;6:contact;7:dateCreated;8:role
                    if (values.length >= 9) {
                        String role = values[8];
                        if ("Doctor".equalsIgnoreCase(role)) {
                            String fullName = values[2];
                            doctorNames.add(fullName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading doctors list", e);
        }
        return doctorNames;
    }

    /**
     * Get a doctor's ID by their full name from users database.
     * Returns -1 if not found.
     */
    public int getDoctorIdByName(String doctorFullName) {
        if (doctorFullName == null) return -1;
        String target = doctorFullName.trim();
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return -1;
            }
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) continue;
                    String[] values = line.split(";");
                    if (values.length >= 9) {
                        String role = values[8];
                        if ("Doctor".equalsIgnoreCase(role)) {
                            String fullName = values[2];
                            if (fullName != null && fullName.trim().equalsIgnoreCase(target)) {
                                try {
                                    return Integer.parseInt(values[0]);
                                } catch (NumberFormatException ignore) {
                                    return -1;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error resolving doctor ID by name", e);
        }
        return -1;
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
    
    private String formatAppointmentForFile(Appointment appointment) {
        return String.format("%s;%s;%s;%d;%s;%d;%s",
                           appointment.getAppointmentId(),
                           appointment.getDateOfAppointment().format(DTFORMATTER), // force date only
                           appointment.getStatus(),
                           appointment.getDoctorId(),
                           appointment.getDoctorName(),
                           appointment.getCustomerId(),
                           appointment.getCustomerName());
    }

    
    private Invoice parseInvoiceFromLine(String[] values) {
        int invoiceId = Integer.parseInt(values[0].replaceAll("\\D", "")); // supports INV001
        double total = Double.parseDouble(values[1]);
        String paymentMethod = values[2]; // may be empty
        String appointmentId = values[3].trim(); 
        return new Invoice(invoiceId, total, paymentMethod, appointmentId);
    }
    public Appointment getAppointmentById(String appointmentId) {
        try {
            File file = new File(APPOINTMENTS_FILE);
            if (!file.exists()) {
                return null;
            }

            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] values = line.split(";");
                        if (values.length >= 7) {
                            String currentAppointmentId = values[0];
                            if (currentAppointmentId.equals(appointmentId)) {
                                return parseAppointmentFromLine(values);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading appointment by ID", e);
        }
        return null;
    }
    
    public Appointment bookAppointment(Customer customer, String doctorFullName, LocalDate dateOfAppointment) {
        try {
            if (customer == null || customer.getId() <= 0 || doctorFullName == null || doctorFullName.trim().isEmpty() || dateOfAppointment == null) {
                logger.warning("Invalid input for booking appointment");
                return null;
            }
            if (dateOfAppointment.isBefore(LocalDate.now())) {
                logger.warning("Cannot book appointment in the past");
                return null;
            }

            int doctorId = getDoctorIdByName(doctorFullName.trim());
            if (doctorId <= 0) {
                logger.warning("Doctor not found: " + doctorFullName);
                return null;
            }

            String newAppointmentId = generateAppointmentId();
            Appointment appt = new Appointment(newAppointmentId,
                                               dateOfAppointment,
                                               "PENDING",
                                               customer.getId(),
                                               doctorId,
                                               customer.getFullname() != null ? customer.getFullname() : customer.getUsername(),
                                               doctorFullName.trim());

            // Persist by appending to appointments file
            File file = new File(APPOINTMENTS_FILE);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file, true)) {
                if (file.length() > 0 && !endsWithNewline(file)) {
                    writer.write("\n");
                }
                writer.write(formatAppointmentForFile(appt) + "\n");
            }
            logger.info("Appointment booked: " + appt.getAppointmentId());
            return appt;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error booking appointment", e);
            return null;
        }
    }

    private boolean endsWithNewline(File file) {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            if (raf.length() == 0) return true;
            raf.seek(raf.length() - 1);
            int last = raf.read();
            return last == '\n' || last == '\r';
        } catch (IOException e) {
            return true;
        }
    }

    public boolean submitCustomerComment(int customerId, String subject, String context) {
        if (customerId <= 0) {
            logger.warning("Invalid customerId for comment");
            return false;
        }
        if (subject == null || subject.trim().isEmpty()) {
            logger.warning("Subject is required");
            return false;
        }
        if (context == null || context.trim().isEmpty()) {
            logger.warning("Context is required");
            return false;
        }

        int nextId = 1;
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(COMMENTS_FILE);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (line.isEmpty()) continue;
                        lines.add(line);
                        String[] parts = line.split("[;,]");
                        if (parts.length >= 1) {
                            try {
                                int id = Integer.parseInt(parts[0].trim());
                                nextId = Math.max(nextId, id + 1);
                            } catch (NumberFormatException ignore) {}
                        }
                    }
                }
            }

            // Write back existing lines (unchanged), then append the new line (semicolon-separated)
            try (FileWriter writer = new FileWriter(COMMENTS_FILE)) {
                for (String l : lines) {
                    writer.write(l + "\n");
                }
                // Format: CommentId;CustomerID;Subject;Context
                String safeSubject = subject.replace("\n", " ").trim();
                String safeContext = context.replace("\n", " ").trim();
                writer.write(String.format("%d;%d;%s;%s%n", nextId, customerId, safeSubject, safeContext));
            }
            logger.info("Comment submitted: id=" + nextId + ", customerId=" + customerId);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error submitting customer comment", e);
            return false;
        }
    }
    
    private LocalDate parseDate(String input) {
    if (input == null || input.isBlank()) {
        return null;
    }
    try {
        // Try strict date format first (yyyy-MM-dd)
        return LocalDate.parse(input.trim(), DTFORMATTER);
    } catch (DateTimeParseException e1) {
        try {
            // Try parsing as LocalDateTime (yyyy-MM-dd HH:mm:ss)
            DateTimeFormatter dtTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HH:mm[:ss]]");
            LocalDateTime ldt = LocalDateTime.parse(input.trim(), dtTimeFormatter);
            return ldt.toLocalDate();
        } catch (DateTimeParseException e2) {
            logger.log(Level.WARNING, "Failed to parse date: " + input, e2);
            return null;
        }
    }
}


}
