package Customer.ctrl;

import Customer.model.Appointment;
import Customer.model.Customer;
import Customer.model.Invoice;
import Customer.services.CustomerService;
import Customer.view.*;
import User.UserProfile;
import User.Homepage;
import java.time.LocalDate;
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
    private CustomerService customerService = new CustomerService(); 
    
    /**
     * Constructor
     */
    public CustomerController(){}
    
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
     * Navigation
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
    
    public void showCustomerComment() {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return;
        }
        
        CustomerComment cc = new CustomerComment();
        cc.setController(this);
        cc.setVisible(true);
    }
    
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
        history.setCurrentCustomer(currentCustomer);
        history.setVisible(true);
    }
    
    public void showAppointmentDetails(String appointmentId) {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return;
        }
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            logger.warning("No appointmentId provided for details view");
            return;
        }
        AppointmentDetails details = new AppointmentDetails();
        details.setController(this);
        details.setCurrentCustomer(currentCustomer);
        details.loadAppointmentDetailsById(appointmentId.trim());
        details.setVisible(true);
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
        profile.setCurrentUser(currentCustomer, new String[]{
            currentCustomer.getUsername(),
            currentCustomer.getFullname(),
            currentCustomer.getEmail(),
            currentCustomer.getAddress(),
            currentCustomer.getContactNumber(),
            currentCustomer.getPassword(),
            "Manager",
            currentCustomer.getDateCreated()});
        profile.setNavigationCallback(() -> showCustomerDashboard());
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
    
    private boolean validateAppointmentInput(int doctorId, String doctorName, LocalDate date) {
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
        return true;
    }
   
    public List<Appointment> getCustomerAppointments() {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return List.of();
        }
        
        return currentCustomer.getAppointments();
    }
    
    public List<Appointment> getCustomerAppointmentsByStatus(String status) {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return List.of();
        }
        
        return currentCustomer.getAppointmentsByStatus(status);
    }
    
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
            logger.info(() -> "Customer information updated successfully: " + currentCustomer.getId());
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
    
    public List<Invoice> getInvoicesForAppointment(String appointmentId) {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return List.of();
        }
        
        return customerService.getInvoicesForAppointment(appointmentId);
    }
    
    public void refreshCustomerData() {
        if (currentCustomer != null) {
            List<Appointment> appointments = customerService.getCustomerAppointments(currentCustomer.getId());
            currentCustomer.setAppointments(appointments);
        }
    }
    
    public boolean submitComment(String subject, String context) {
        if (currentCustomer == null) {
            logger.warning("No customer logged in");
            return false;
        }
        return customerService.submitCustomerComment(currentCustomer.getId(), subject, context);
    }

}
