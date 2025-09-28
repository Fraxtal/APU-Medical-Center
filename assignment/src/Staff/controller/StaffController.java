package Staff.controller;

import Staff.model.ManageAppointments;
import Staff.model.ManageCustomerAccount;
import Staff.model.ManagePayments;
import Staff.model.Staff;
import Staff.view.StaffDashboard;
import User.UserProfile;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;


public class StaffController {
    private ManageCustomerAccount mca;
    private ManageAppointments ma;
    private ManagePayments mp;
    private Staff st;
    
    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static String[] paymentTypes = {"Cash", "Credit"};
    
    public StaffController(){}
    
    public StaffController(Staff st) {
        this.st = st;
        this.mca = new ManageCustomerAccount();
        this.ma = new ManageAppointments();
        this.mp = new ManagePayments();
    }
    
    public DefaultTableModel getCustomerTable() {
        List<String[]> customerData = mca.loadCustomers();
        String[] colName = {"ID", "Username", "Fullname", "Email", "Password", "Address", "Contact Number", "Date Created", "Role"};
        
        DefaultTableModel customerModel = new DefaultTableModel(colName, 0);
        for (String[] customer : customerData){
            customerModel.addRow(customer);
        }

        return customerModel;
    }
    
    public DefaultTableModel getAppointmentTable(){
        List<String[]> appointmentData = ma.loadAppointments();
        String[] colName = {"Appointment ID", "Appointment Date", "Status", "Doctor ID", "Doctor Name", "Customer ID", "Customer Name"};
        
        DefaultTableModel appointmentModel = new DefaultTableModel(colName, 0);
        for (String[] appointments : appointmentData){
            appointmentModel.addRow(appointments);
        }
        
        return appointmentModel;
    }
    
    public DefaultTableModel getPastAppointmentTable(){
        List<String[]> pastAppointmentData = ma.loadPastAppointments();
        String[] colName = {"Appointment ID", "Appointment Date", "Status", "Doctor ID", "Doctor Name", "Customer ID", "Customer Name"};
        
        DefaultTableModel pastAppointmentModel = new DefaultTableModel(colName, 0);
        for (String[] pastAppointments : pastAppointmentData){
            pastAppointmentModel.addRow(pastAppointments);
        }
        
        return pastAppointmentModel;
    }
    
    public DefaultTableModel getDoctorTable(){
        List<String[]> doctorData = mca.loadDoctors();
        String[] colName = {"ID", "Username", "Fullname", "Email", "Password", "Address", "Contact Number"};
        
        DefaultTableModel doctorModel = new DefaultTableModel(colName, 0);
        for (String[] doctors : doctorData){
            doctorModel.addRow(doctors);
        }
        
        return doctorModel;
    }
    
    public DefaultTableModel getInvoiceTable(){
        List<String[]> invoiceData = mp.loadInvoices();
        String[] colName = {"Invoice ID", "Subtotal", "Payment Method", "Appointment ID"};
        
        DefaultTableModel invoiceModel = new DefaultTableModel(colName, 0);
        for (String[] invoice : invoiceData){
            invoiceModel.addRow(invoice);
        }
        
        return invoiceModel;
    }
    
    public DefaultTableModel getInvoiceDetailTable(){
        List<String[]> invoiceDetailData = mp.loadInvoiceDetails();
        String[] colName = {"Invoice Detail ID", "Item Name", "Quantity", "Price Per Item", "Total Price", "Invoice ID", "Appointment ID"};
        
        DefaultTableModel invoiceDetailModel = new DefaultTableModel(colName, 0);
        for (String[] invoiceDetail : invoiceDetailData){
            invoiceDetailModel.addRow(invoiceDetail);
        }
        
        return invoiceDetailModel;
    }
    
    public DefaultTableModel getSpecificInvoiceDetailTable(String invoiceId){
        List<String[]> sInvoiceDetailData = mp.loadSpecificInvoiceDetails(invoiceId);
        String[] colName = {"Invoice Detail ID", "Item Name", "Quantity", "Price Per Item", "Total Price", "Invoice ID", "Appointment ID"};
        
        DefaultTableModel sInvoiceDetailModel = new DefaultTableModel(colName, 0);
        for (String[] sInvoiceDetail : sInvoiceDetailData){
            sInvoiceDetailModel.addRow(sInvoiceDetail);
        }
        
        return sInvoiceDetailModel;
    }
    
    public int validateAccountUpdate(String customerId, String username, String fullname, String email, String password, String address, String contactNum) {
        if (customerId.isEmpty() || username.isEmpty() || fullname.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || contactNum.isEmpty()) {
            return 1;
        }
        
        if (!mca.checkUserIdExists(customerId)){
            return 2;
        }
        
        if (!mca.checkEmailLinkedUserId(customerId, email)){
            if (mca.checkEmailExists(email)){
                return 3;
            }
        }
        
        if (!mca.checkContactLinkedUserId(customerId, contactNum)){
            if (mca.checkContactExists(contactNum)){
                return 4;
            }
        }
        
        
        if(!mca.updateCustomer(customerId, username, fullname, email, password, address, contactNum)){
            return 5;
        }

        return 0;

    }
    
    public int validateAccountAdd (String username, String fullname, String email, String password, String address, String contactNum){
        if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || contactNum.isEmpty()) {
            return 1;
        }
        
        if (mca.checkEmailExists(email)){
            return 2;
        }
        
        if (mca.checkContactExists(contactNum)){
            return 3;
        }
        
        if(!mca.addCustomer(username, fullname, email, password, address, contactNum)){
            return 4;
        }

        return 0;
    }
    
    public int validateAccountDeletion(String customerId) {
        if (customerId.isEmpty())
        {
            return 1;
        }
        if (!mca.checkUserIdExists(customerId)){
            return 1;
        }
        
        if(!mca.deleteCustomer(customerId)){
            return 2;
        }
        return 0;
    }
    
    public int validateAppointmentBooking(Date rawDate, String status, String doctorId, String doctorName, String customerId, String customerName){
        String appointmentDate = sdf.format(rawDate);
        if(doctorId.isEmpty() || doctorName.isEmpty() || customerId.isEmpty() || customerName.isEmpty()){
            return 1;
        }
        
        if(!mca.checkUserIdExists(doctorId)){
            return 2;
        }
        
        if(!mca.checkUserIdExists(customerId)){
            return 3;
        }
        
        if(rawDate.before(new Date())){
            return 4;
        }
        
        if (!ma.addAppointment(appointmentDate, status, doctorId, doctorName, customerId, customerName)){
            return 5;
        }
        
        return 0;
    }
    
    public int validateAppointmentUpdate(String appointmentId, Date rawDate, String status, String doctorId, String doctorName, String customerId, String customerName){
        String appointmentDate = sdf.format(rawDate);
        
        if(appointmentId.isEmpty() || doctorId.isEmpty() || doctorName.isEmpty() || customerId.isEmpty() || customerName.isEmpty()){
            return 1;
        }
        
        if(!mca.checkUserIdExists(doctorId)){
            return 2;
        }
        
        if(!mca.checkUserIdExists(customerId)){
            return 3;
        }
        
        if(!ma.checkAppointmentIdExists(appointmentId)){
            return 4;
        }
        
        if(rawDate.before(new Date())){
            return 5;
        }
        
        if(!ma.updateAppointment(appointmentId, appointmentDate, status, doctorId, doctorName, customerId, customerName)){
            return 6;
        }
        return 0;
    }
    
    public int validateInvoiceUpdate(String invoiceId, String paymentMethod){
        if (invoiceId.isEmpty() || paymentMethod.isEmpty()){
            return 1;
        }
         
        if (!Arrays.asList(paymentTypes).contains(paymentMethod)){
             return 2;
        }
        
        if (!mp.updateInvoicePayment(invoiceId, paymentMethod)){
            return 3;
        }
        return 0;
    }
    
    public String validateCustomerIDtoName(String customerId){
        if(!mca.checkUserIdExists(customerId)){
            return null;
        }
        
        String customerName = mp.returnCustomerNamefromId(customerId);
        if(customerName != null){
            return customerName;
        }
        
        return null;
    }
    
    //implemented in StaffPayments
    public String validateAppIdtoCustomerName(String appointmentId){
        if(!ma.checkAppointmentIdExists(appointmentId)){
            return null;
        }
        
        String customerId = ma.returnCustomerIDfromAppId(appointmentId);
        if(customerId != null){
            String customerName = mp.returnCustomerNamefromId(customerId);
            if (customerName != null){
                return customerName;
            }
        }
        
        return null;
    }
    
    public int validateGenerateReceipt(String appointmentId, String invoiceId, String paymentMethod){
        if (!Arrays.asList(paymentTypes).contains(paymentMethod)){
             return 1;
        }
        
        String customerId = ma.returnCustomerIDfromAppId(appointmentId);
        if(customerId == null){
            return 2;
        }
        
        String customerName = mp.returnCustomerNamefromId(customerId);
        if (customerName == null){
            return 2;
        }
        
        if(!mp.generateReceipt(customerId, customerName, invoiceId)){
            return 3;
        }
        
        return 0;
    }
    
        public void showUserProfile() {
        if (st == null) {
            Logger.getLogger(StaffController.class.getName()).warning("No customer logged in");
            return;
        }
        
        UserProfile profile = new UserProfile();
        profile.setCurrentUser(st);
        profile.setNavigationCallback(() -> {
        StaffDashboard frame = new StaffDashboard(this);
        frame.setVisible(true);
        });
        profile.setVisible(true);
    }
}
