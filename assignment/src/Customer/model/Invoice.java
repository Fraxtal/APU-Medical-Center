package Customer.model;

import java.io.Serializable;

/**
 * Invoice model representing an invoice entity
 * Demonstrates OOP concepts: Encapsulation, Inheritance, Polymorphism
 * Extends BaseEntity to demonstrate inheritance
 * Implements Serializable for data persistence
 * Format: InvoiceID, Total, PaymentMethod, AppointmentID
 */
public class Invoice extends BaseEntity implements Serializable {
    
    private int invoiceId;
    private double total;
    private String paymentMethod;
    private int appointmentId;
    
    // Default constructor
    public Invoice() {}
    
    // Constructor for creating new invoices
    public Invoice(double total, String paymentMethod, int appointmentId) {
        super(); // Call parent constructor
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.appointmentId = appointmentId;
    }
    
    // Constructor for loading from database
    public Invoice(int invoiceId, double total, String paymentMethod, int appointmentId) {
        super(invoiceId); // Call parent constructor with ID
        this.invoiceId = invoiceId;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.appointmentId = appointmentId;
    }
    
    // Getters and Setters
    public int getInvoiceId() {
        return invoiceId;
    }
    
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public int getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    /**
     * Method to validate invoice data - demonstrates encapsulation
     * Implements abstract method from BaseEntity
     */
    @Override
    public boolean isValid() {
        return invoiceId > 0 && 
               total >= 0 && 
               paymentMethod != null && !paymentMethod.trim().isEmpty() &&
               appointmentId > 0;
    }
    
    /**
     * Method to check if invoice is paid - demonstrates business logic
     */
    public boolean isPaid() {
        return total > 0 && paymentMethod != null && !paymentMethod.trim().isEmpty();
    }
    
    /**
     * Method to get formatted total amount - demonstrates business logic
     */
    public String getFormattedTotal() {
        return String.format("RM %.2f", total);
    }
    
    /**
     * Method to format invoice for display - demonstrates polymorphism
     * Implements abstract method from BaseEntity
     */
    @Override
    public String getSummary() {
        return String.format("Invoice #%d - %s for Appointment #%d", 
                           invoiceId, getFormattedTotal(), appointmentId);
    }
    
    @Override
    public String toString() {
        return String.format("Invoice{id=%d, total=%.2f, paymentMethod='%s', appointmentId=%d}", 
                           invoiceId, total, paymentMethod, appointmentId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Invoice invoice = (Invoice) obj;
        return invoiceId == invoice.invoiceId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(invoiceId);
    }
}
