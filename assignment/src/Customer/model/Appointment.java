package Customer.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.io.Serializable;

/**
 * Appointment model representing an appointment entity
 * Demonstrates OOP concepts: Encapsulation, Inheritance, Polymorphism
 * Extends BaseEntity to demonstrate inheritance
 * Implements Serializable for data persistence
 */
public class Appointment extends BaseEntity {
    private int appointmentId;
    private LocalDate dateOfAppointment; // DOA - Date of appointment
    private String status; // PENDING, CONFIRMED, CANCELLED, COMPLETED
    private String feedback; // Feedback from doctor
    private String comment; // Comment (to be decided)
    private int customerId;
    private int doctorId;
    private String customerName;
    private String doctorName;
    
    // Default constructor
    public Appointment() {}
    
    // Constructor for creating new appointments
    public Appointment(int customerId, int doctorId, String customerName, String doctorName, 
                      LocalDate dateOfAppointment, String comment) {
        super(); // Call parent constructor
        this.customerId = customerId;
        this.doctorId = doctorId;
        this.customerName = customerName;
        this.doctorName = doctorName;
        this.dateOfAppointment = dateOfAppointment;
        this.status = "PENDING";
        this.comment = comment;
        this.feedback = ""; // Empty initially
    }
    
    // Constructor for loading from database
    public Appointment(int appointmentId, LocalDate dateOfAppointment, String status, 
                      String feedback, String comment, int customerId, int doctorId, 
                      String customerName, String doctorName) {
        super(appointmentId); // Call parent constructor with ID
        this.appointmentId = appointmentId;
        this.dateOfAppointment = dateOfAppointment;
        this.status = status;
        this.feedback = feedback;
        this.comment = comment;
        this.customerId = customerId;
        this.doctorId = doctorId;
        this.customerName = customerName;
        this.doctorName = doctorName;
    }
    
    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    
    public LocalDate getDateOfAppointment() { return dateOfAppointment; }
    public void setDateOfAppointment(LocalDate dateOfAppointment) { this.dateOfAppointment = dateOfAppointment; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    /**
     * Method to validate appointment data - demonstrates encapsulation
     * Implements abstract method from BaseEntity
     */
    @Override
    public boolean isValid() {
        return appointmentId > 0 && 
               customerId > 0 && 
               doctorId > 0 &&
               customerName != null && !customerName.trim().isEmpty() &&
               doctorName != null && !doctorName.trim().isEmpty() &&
               dateOfAppointment != null && 
               status != null && !status.trim().isEmpty();
    }
    
    /**
     * Method to check if appointment is in the past - demonstrates business logic
     */
    public boolean isPastAppointment() {
        if (dateOfAppointment == null) return false;
        return dateOfAppointment.isBefore(LocalDate.now());
    }
    
    /**
     * Method to check if appointment can be cancelled - demonstrates business rules
     */
    public boolean canBeCancelled() {
        return "PENDING".equalsIgnoreCase(status) || "CONFIRMED".equalsIgnoreCase(status);
    }
    
    /**
     * Method to format appointment for display - demonstrates polymorphism
     * Implements abstract method from BaseEntity
     */
    @Override
    public String getSummary() {
        return String.format("Appointment #%d - %s with %s on %s (%s)", 
                           appointmentId, customerName, doctorName, 
                           dateOfAppointment, status);
    }
    
    /**
     * Method to format appointment for display - demonstrates polymorphism
     */
    public String getDisplayString() {
        return getSummary(); // Reuse the summary method
    }
    
    @Override
    public String toString() {
        return String.format("Appointment{id=%d, customer=%s, doctor=%s, date=%s, status=%s}", 
                           appointmentId, customerName, doctorName, dateOfAppointment, status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Appointment that = (Appointment) obj;
        return appointmentId == that.appointmentId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(appointmentId);
    }
}
