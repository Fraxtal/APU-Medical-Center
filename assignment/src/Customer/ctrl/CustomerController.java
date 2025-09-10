package Customer.ctrl;

import Customer.model.Appointment;
import Customer.model.Customer;
import Customer.model.Invoice;
import Customer.services.CustomerService;
import Customer.view.AppointmentBooking;
import Customer.view.AppointmentHistory;
import Customer.view.CustomerDashboard;
import User.UserProfile;
import User.Homepage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for Customer module
 * Demonstrates OOP concepts: Encapsulation, Abstraction, Polymorphism
 * Handles communication between Customer views and CustomerService
 * Implements proper MVC separation of concerns
 * Provides input validation and business logic coordination
 */
public class CustomerController {
    
    private static final Logger logger = Logger.getLogger(CustomerController.class.getName());
    
    private Customer currentCustomer;
    private CustomerService customerService;
    
    /**
     * Constructor
     */
    public CustomerController() {
        this.customerService = new CustomerService();
    }
    
    /**
     * Set the current logged-in customer
     */
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        // Load customer's appointments
        if (customer != null) {
            List<Appointment> appointments = customerService.getCustomerAppointments(customer.getId());
            customer.setAppointments(appointments);
        }
    }
    
    /**
     * Get the current customer
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
    
    /**
     * Navigate to Customer Dashboard
     */
    public void showCustomerDashboard() {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return;
        }
        
        CustomerDashboard dashboard = new CustomerDashboard();
        dashboard.setController(this);
        dashboard.setVisible(true);
    }
    
    /**
     * Navigate to Appointment Booking
     */
    public void showAppointmentBooking() {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return;
        }
        
        AppointmentBooking booking = new AppointmentBooking();
        booking.setController(this);
        booking.setVisible(true);
    }
    
    /**
     * Navigate to Appointment History
     */
    public void showAppointmentHistory() {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return;
        }
        
        AppointmentHistory history = new AppointmentHistory();
        history.setController(this);
        history.setVisible(true);
    }
    
    /**
     * Navigate to User Profile
     */
    public void showUserProfile() {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return;
        }
        
        UserProfile profile = new UserProfile();
        profile.setController(this);
        profile.setVisible(true);
    }
    
    /**
     * Logout and return to homepage
     */
    public void logout() {
        currentCustomer = null;
        Homepage homepage = new Homepage();
        homepage.setVisible(true);
    }
    
    /**
     * Book a new appointment - demonstrates input validation and business logic coordination
     */
    public boolean bookAppointment(int doctorId, String doctorName, LocalDate date, String comment) {
        // Authentication check
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return false;
        }
        
        // Business rule: Check if customer can book more appointments
        if (!currentCustomer.canBookAppointment()) {
            logger.warning("Customer has reached maximum pending appointments limit");
            return false;
        }
        
        // Input validation - demonstrates encapsulation
        if (!validateAppointmentInput(doctorId, doctorName, date, comment)) {
            return false;
        }
        
        // Check if time slot is available
        if (!customerService.isTimeSlotAvailable(date, null, doctorName)) {
            logger.warning("Time slot is not available");
            return false;
        }
        
        // Create appointment object - demonstrates object creation
        Appointment appointment = new Appointment(
            currentCustomer.getId(),
            doctorId,
            currentCustomer.getFullname(),
            doctorName,
            date,
            comment
        );
        
        // Book appointment through service layer
        boolean success = customerService.bookAppointment(appointment);
        
        if (success) {
            // Update customer's appointment list - demonstrates data consistency
            currentCustomer.addAppointment(appointment);
            logger.info("Appointment booked successfully for customer: " + currentCustomer.getId());
        }
        
        return success;
    }
    
    /**
     * Validate appointment input - demonstrates encapsulation and input validation
     */
    private boolean validateAppointmentInput(int doctorId, String doctorName, LocalDate date, String comment) {
        if (doctorId <= 0) {
            logger.warning("Valid doctor ID is required");
            return false;
        }
        
        if (doctorName == null || doctorName.trim().isEmpty()) {
            logger.warning("Doctor name is required");
            return false;
        }
        
        if (date == null || date.isBefore(LocalDate.now())) {
            logger.warning("Invalid appointment date - cannot book in the past");
            return false;
        }
        
        // Comment is optional, so no validation needed
        
        return true;
    }
    
    /**
     * Cancel an appointment
     */
    public boolean cancelAppointment(int appointmentId) {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return false;
        }
        
        // Find the appointment in customer's list
        Appointment appointmentToCancel = currentCustomer.getAppointments().stream()
            .filter(appointment -> appointment.getAppointmentId() == appointmentId)
            .findFirst()
            .orElse(null);
        
        if (appointmentToCancel == null) {
            logger.warning("Appointment not found for customer");
            return false;
        }
        
        // Cancel through service
        boolean success = customerService.cancelAppointment(appointmentId);
        
        if (success) {
            // Update appointment status in customer's list
            appointmentToCancel.setStatus("CANCELLED");
            logger.info("Appointment cancelled successfully: " + appointmentId);
        }
        
        return success;
    }
    
    /**
     * Get customer's appointments
     */
    public List<Appointment> getCustomerAppointments() {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return List.of();
        }
        
        return currentCustomer.getAppointments();
    }
    
    /**
     * Get customer's appointments by status
     */
    public List<Appointment> getCustomerAppointmentsByStatus(String status) {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return List.of();
        }
        
        return currentCustomer.getAppointmentsByStatus(status);
    }
    
    /**
     * Update customer information - demonstrates input validation and data consistency
     */
    public boolean updateCustomerInfo(String username, String fullname, String email, 
                                    String address, String contactNo) {
        // Authentication check
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return false;
        }
        
        // Input validation - demonstrates encapsulation
        if (!validateCustomerInput(username, fullname, email)) {
            return false;
        }
        
        // Update customer object - demonstrates object state management
        currentCustomer.setUsername(username);
        currentCustomer.setFullname(fullname);
        currentCustomer.setEmail(email);
        currentCustomer.setAddress(address);
        currentCustomer.setContactNo(contactNo);
        
        // Update through service layer
        boolean success = customerService.updateCustomerInfo(currentCustomer);
        
        if (success) {
            logger.info("Customer information updated successfully: " + currentCustomer.getId());
        }
        
        return success;
    }
    
    /**
     * Validate customer input - demonstrates encapsulation and input validation
     */
    private boolean validateCustomerInput(String username, String fullname, String email) {
        if (username == null || username.trim().isEmpty()) {
            logger.warning("Username is required");
            return false;
        }
        
        if (username.length() < 3) {
            logger.warning("Username must be at least 3 characters long");
            return false;
        }
        
        if (fullname == null || fullname.trim().isEmpty()) {
            logger.warning("Full name is required");
            return false;
        }
        
        if (email == null || email.trim().isEmpty()) {
            logger.warning("Email is required");
            return false;
        }
        
        // Email format validation
        if (!email.contains("@") || !email.contains(".")) {
            logger.warning("Invalid email format");
            return false;
        }
        
        return true;
    }
    
    /**
     * Create an invoice for an appointment
     */
    public boolean createInvoice(int appointmentId, double total, String paymentMethod) {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return false;
        }
        
        // Input validation
        if (appointmentId <= 0) {
            logger.warning("Valid appointment ID is required");
            return false;
        }
        
        if (total <= 0) {
            logger.warning("Total amount must be greater than 0");
            return false;
        }
        
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            logger.warning("Payment method is required");
            return false;
        }
        
        // Create invoice object
        Invoice invoice = new Invoice(total, paymentMethod, appointmentId);
        
        // Create invoice through service
        boolean success = customerService.createInvoice(invoice);
        
        if (success) {
            logger.info("Invoice created successfully for appointment: " + appointmentId);
        }
        
        return success;
    }
    
    /**
     * Get invoices for an appointment
     */
    public List<Invoice> getInvoicesForAppointment(int appointmentId) {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return List.of();
        }
        
        return customerService.getInvoicesForAppointment(appointmentId);
    }
    
    /**
     * Get available doctors
     */
    public List<String> getAvailableDoctors() {
        return customerService.getAvailableDoctors();
    }
    
    /**
     * Get available appointment types
     */
    public List<String> getAppointmentTypes() {
        return customerService.getAppointmentTypes();
    }
    
    /**
     * Check if time slot is available
     */
    public boolean isTimeSlotAvailable(LocalDate date, LocalTime time, String doctorName) {
        return customerService.isTimeSlotAvailable(date, time, doctorName);
    }
    
    /**
     * Refresh customer data from database
     */
    public void refreshCustomerData() {
        if (currentCustomer != null) {
            List<Appointment> appointments = customerService.getCustomerAppointments(currentCustomer.getId());
            currentCustomer.setAppointments(appointments);
        }
    }
}
