package Customer.ctrl;

import Customer.model.Appointment;
import Customer.model.Customer;
import Customer.model.Invoice;
import Customer.services.CustomerService;
import Customer.view.*;
import User.UserProfile;
import User.Homepage;
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
        profile.setCurrentUser(currentCustomer);
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
