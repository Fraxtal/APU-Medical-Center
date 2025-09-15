package Customer.model;

import java.io.Serializable;

public class Invoice extends BaseEntity implements Serializable {
    private int invoiceId;
    private double total;
    private String paymentMethod;
    private String appointmentId;

    public Invoice(int invoiceId, double total, String paymentMethod, String appointmentId) {
        super(invoiceId);
        this.invoiceId = invoiceId;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.appointmentId = appointmentId;
    }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    
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
    
    
    public String getFormattedTotal() {
        return String.format("RM %.2f", total);
    }
    
    @Override
    public String toString() {
        return String.format("Invoice{id=%d, total=%.2f, paymentMethod='%s', appointmentId=%s}", 
                           invoiceId, total, paymentMethod, appointmentId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Invoice invoice = (Invoice) obj;
        return invoiceId == invoice.invoiceId;
    }
    
}
