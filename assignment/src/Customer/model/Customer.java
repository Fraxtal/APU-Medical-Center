package Customer.model;

import User.User;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Customer model representing a customer entity
 * Demonstrates OOP concepts: Inheritance, Encapsulation, Polymorphism
 * This class focuses on data representation and basic business rules
 */
public class Customer extends User implements Serializable {
    
    private String password;
    private String address;
    private String contactNumber;
    private String dateCreated;
    private String role;
    private List<Appointment> appointments;
    
    /**
     * Constructor for creating a new Customer
     */
    public Customer(){}
    
    
    public Customer(int id, String username, String fullname, String email, 
                   String password, String address, String contactNumber, String dateCreated) {
        super(id, username, fullname, email);
        this.password = password;
        this.address = address;
        this.contactNumber = contactNumber;
        this.dateCreated = dateCreated;
        this.role = "Customer";
        this.appointments = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public String getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public List<Appointment> getAppointments() {
        return appointments;
    }
    
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
    
    /**
     * Add an appointment to the customer's appointment list
     */
    public void addAppointment(Appointment appointment) {
        if (appointment != null) {
            this.appointments.add(appointment);
        }
    }
    
    /**
     * Remove an appointment from the customer's appointment list
     */
    public boolean removeAppointment(Appointment appointment) {
        return this.appointments.remove(appointment);
    }
    
    /**
     * Get appointments by status
     */
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointments.stream()
                .filter(appointment -> appointment.getStatus().equalsIgnoreCase(status))
                .toList();
    }
    
    /**
     * Check if customer has any pending appointments
     */
    public boolean hasPendingAppointments() {
        return appointments.stream()
                .anyMatch(appointment -> "PENDING".equalsIgnoreCase(appointment.getStatus()));
    }
    
    /**
     * Get total number of appointments - demonstrates encapsulation
     */
    public int getTotalAppointments() {
        return appointments.size();
    }
    
    /**
     * Get completed appointments count - demonstrates business logic
     */
    public int getCompletedAppointmentsCount() {
        return (int) appointments.stream()
                .filter(appointment -> "COMPLETED".equalsIgnoreCase(appointment.getStatus()))
                .count();
    }
    
    /**
     * Validate customer data - demonstrates encapsulation
     */
    public boolean isValidCustomer() {
        return id > 0 && 
               username != null && !username.trim().isEmpty() &&
               fullname != null && !fullname.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               email.contains("@") && email.contains(".") &&
               password != null && !password.trim().isEmpty() &&
               role != null && !role.trim().isEmpty();
    }
    
    /**
     * Get customer summary - demonstrates polymorphism
     */
    public String getCustomerSummary() {
        return String.format("Customer: %s (%s) - %d appointments", 
                           fullname, email, getTotalAppointments());
    }
    
    /**
     * Check if customer can book appointment - demonstrates business rules
     */
    public boolean canBookAppointment() {
        // Business rule: Customer can book if they have less than 5 pending appointments
        long pendingCount = appointments.stream()
                .filter(appointment -> "PENDING".equalsIgnoreCase(appointment.getStatus()))
                .count();
        return pendingCount < 5;
    }
    
    @Override
    public String toString() {
        return String.format("Customer{id=%d, username='%s', fullname='%s', email='%s', address='%s', contactNumber='%s', role='%s'}", 
                           id, username, fullname, email, address, contactNumber, role);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return id == customer.id;
    }
    

    public String getFullname() {
        return this.fullname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContactNo(String contactNo) {
        this.contactNumber = contactNo;
    }
}
