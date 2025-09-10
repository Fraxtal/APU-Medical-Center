package Customer.services;

import Customer.model.Appointment;
import Customer.model.Customer;
import java.util.List;

/**
 * Service interface demonstrating OOP concepts: Abstraction and Polymorphism
 * Defines the contract for service operations
 * Allows for different implementations and promotes loose coupling
 */
public interface FileService {
    
    /**
     * Get appointments for a specific customer
     * @param customerId The ID of the customer
     * @return List of appointments for the customer
     */
    List<Appointment> getCustomerAppointments(int customerId);
    
    /**
     * Book a new appointment
     * @param appointment The appointment to book
     * @return true if successful, false otherwise
     */
    boolean bookAppointment(Appointment appointment);
    
    /**
     * Cancel an appointment
     * @param appointmentId The ID of the appointment to cancel
     * @return true if successful, false otherwise
     */
    boolean cancelAppointment(int appointmentId);
    
    /**
     * Update customer information
     * @param customer The customer with updated information
     * @return true if successful, false otherwise
     */
    boolean updateCustomerInfo(Customer customer);
    
    /**
     * Validate if a time slot is available
     * @param date The date to check
     * @param time The time to check
     * @param doctorName The doctor name
     * @return true if available, false otherwise
     */
    boolean isTimeSlotAvailable(java.time.LocalDate date, java.time.LocalTime time, String doctorName);
}
