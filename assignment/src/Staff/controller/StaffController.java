package Staff.controller;

import Staff.service.ManageCustomerAccount;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class StaffController {
    private ManageCustomerAccount serviceMCA;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    
    public StaffController() {
        this.serviceMCA = new ManageCustomerAccount();
    }
    public DefaultTableModel getCustomerTable() {
        List<String[]> customerData = serviceMCA.loadCustomers();
        String[] colName = {"ID", "Username", "Fullname", "Email", "Password", "Address", "Contact Number", "Date Created", "Role"};
        
        DefaultTableModel customerModel = new DefaultTableModel(colName, 0);
        for (String[] customer : customerData){
            customerModel.addRow(customer);
        }

        return customerModel;
    }
    
    public DefaultTableModel getAppointmentTable(){
        List<String[]> appointmentData = serviceMCA.loadAppointments();
        String[] colName = {"Appointment ID", "Appointment Date", "Status", "Doctor ID", "Doctor Name", "Customer ID", "Customer Name"};
        
        DefaultTableModel appointmentModel = new DefaultTableModel(colName, 0);
        for (String[] appointments : appointmentData){
            appointmentModel.addRow(appointments);
        }
        
        return appointmentModel;
    }
    
    public DefaultTableModel getDoctorTable(){
        List<String[]> doctorData = serviceMCA.loadDoctors();
        String[] colName = {"ID", "Username", "Fullname", "Email", "Password", "Address", "Contact Number"};
        
        DefaultTableModel doctorModel = new DefaultTableModel(colName, 0);
        for (String[] doctors : doctorData){
            doctorModel.addRow(doctors);
        }
        
        return doctorModel;
    }
    
    public int validateAccountUpdate(int id, String username, String fullname, String email, String password, String address, String contactNum) {
        if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || contactNum.isEmpty()) {
            return 1;
        }
        
        if (serviceMCA.checkEmailExists(email)){
            return 2;
        }
        
        if (serviceMCA.checkContactExists(contactNum)){
            return 3;
        }
        
        if(!serviceMCA.updateCustomer(id, username, fullname, email, password, address, contactNum)){
            return 4;
        }

        return 0;

    }
    
    public int validateAccountAdd (String username, String fullname, String email, String password, String address, String contactNum){
        if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || contactNum.isEmpty()) {
            return 1;
        }
        
        if (serviceMCA.checkEmailExists(email)){
            return 2;
        }
        
        if (serviceMCA.checkContactExists(contactNum)){
            return 3;
        }
        
        if(!serviceMCA.addCustomer(username, fullname, email, password, address, contactNum)){
            return 4;
        }

        return 0;
    }
    
    public int validateAccountDeletion(String id) {
        int cId = Integer.parseInt(id);
        if (id.isEmpty())
        {
            return 1;
        }
        if (!serviceMCA.checkIdExists(cId)){
            return 1;
        }
        
        if(!serviceMCA.deleteCustomer(cId)){
            return 2;
        }
        return 0;
    }
    
    public int validateAppointmentBooking(Date rawDate, String status, String doctorId, String doctorName, String customerId, String customerName){
        String appointmentDate = sdf.format(rawDate);
        if(doctorId.isEmpty() || doctorName.isEmpty() || customerId.isEmpty() || customerName.isEmpty()){
            return 1;
        }
        
        if(!serviceMCA.checkIdExists(Integer.parseInt(doctorId))){
            return 2;
        }
        
        if(!serviceMCA.checkIdExists(Integer.parseInt(customerId))){
            return 3;
        }
        
        if(rawDate.before(new Date())){
            return 4;
        }
        
        if (!serviceMCA.addAppointment(appointmentDate, status, doctorId, doctorName, customerId, customerName)){
            return 5;
        }
        
        return 0;
    }
    
    public int validateAppointmentUpdate(int appointmentId, Date rawDate, String status, String doctorId, String doctorName, String customerId, String customerName){
        String appointmentDate = sdf.format(rawDate);
        
        if(doctorId.isEmpty() || doctorName.isEmpty() || customerId.isEmpty() || customerName.isEmpty()){
            return 1;
        }
        
        if(!serviceMCA.checkIdExists(Integer.parseInt(doctorId))){
            return 2;
        }
        
        if(!serviceMCA.checkIdExists(Integer.parseInt(customerId))){
            return 3;
        }
        
        if(rawDate.before(new Date())){
            return 4;
        }
        
        if (!serviceMCA.addAppointment(appointmentDate, status, doctorId, doctorName, customerId, customerName)){
            return 5;
        }
        
        if(!serviceMCA.updateAppointment(appointmentId, appointmentDate, status, doctorId, doctorName, customerId, customerName)){
            return 4;
        }
        return 0;
    }
}
