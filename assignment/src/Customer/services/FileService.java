package Customer.services;

import Customer.model.Appointment;
import Customer.model.Customer;
import java.util.List;


public interface FileService {
    
    List<Appointment> getCustomerAppointments(int customerId);

    boolean bookAppointment(Appointment appointment);
    
    boolean updateCustomerInfo(Customer customer);
    
}
