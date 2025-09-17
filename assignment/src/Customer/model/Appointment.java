package Customer.model;

import java.time.LocalDate;

public class Appointment extends BaseEntity {
    private String appointmentId; // Changed to String to support A0001 format
    private LocalDate dateOfAppointment; // DOA - Date of appointment
    private String status; // PENDING, CONFIRMED, CANCELLED, COMPLETED
    private int customerId;
    private int doctorId;
    private String customerName;
    private String doctorName;
    
    // Default constructor
    public Appointment() {}
    
    // Constructor for creating new appointments
    public Appointment(int customerId, int doctorId, String customerName, String doctorName, 
                      LocalDate dateOfAppointment) {
        super(); // Call parent constructor
        this.customerId = customerId;
        this.doctorId = doctorId;
        this.customerName = customerName;
        this.doctorName = doctorName;
        this.dateOfAppointment = dateOfAppointment;
        this.status = "PENDING";
    }
    
    // Constructor for loading from database
    public Appointment(String appointmentId, LocalDate dateOfAppointment, String status, 
                      int customerId, int doctorId, String customerName, String doctorName) {
        super(); // Call parent constructor
        this.appointmentId = appointmentId;
        this.dateOfAppointment = dateOfAppointment;
        this.status = status;
        this.customerId = customerId;
        this.doctorId = doctorId;
        this.customerName = customerName;
        this.doctorName = doctorName;
    }
    
    // Getters and Setters
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    
    public LocalDate getDateOfAppointment() { return dateOfAppointment; }
    public void setDateOfAppointment(LocalDate dateOfAppointment) { this.dateOfAppointment = dateOfAppointment; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    /**
     * Method to check if appointment is in the past - demonstrates business logic
     */
    public boolean isPastAppointment() {
        if (dateOfAppointment == null) return false;
        return dateOfAppointment.isBefore(LocalDate.now());
    }
    
    @Override
    public String toString() {
        return String.format("Appointment{id=%s, customer=%s, doctor=%s, date=%s, status=%s}", 
                           appointmentId, customerName, doctorName, dateOfAppointment, status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Appointment that = (Appointment) obj;
        return appointmentId != null && appointmentId.equals(that.appointmentId);
    }
}
