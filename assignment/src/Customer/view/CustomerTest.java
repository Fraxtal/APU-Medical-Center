/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Customer.view;

import Customer.controller.CustomerController;
import Customer.model.Customer;

public class CustomerTest {
    public static void main(String[] args) {
        // Create dummy customer
        Customer dummyCustomer = new Customer(
            1, "john_doe", "John Doe", "john.doe@email.com",
            "password123", "123 Main St", "0123456789", "2024-01-15"
        );

        // Create controller and set customer
        CustomerController controller = new CustomerController();
        controller.setCurrentCustomer(dummyCustomer);

        // Create and show dashboard
        CustomerDashboard dashboard = new CustomerDashboard();
        dashboard.setController(controller);
        dashboard.setVisible(true);
    }
}