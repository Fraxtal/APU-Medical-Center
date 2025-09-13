/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Staff.controller;

import Staff.service.ManageCustomerAccount;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class StaffController {
    private ManageCustomerAccount serviceMCA;
    
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
    
}
