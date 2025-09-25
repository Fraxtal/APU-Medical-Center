/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Customer.view;

import Customer.ctrl.CustomerController;
import Customer.model.Customer;
import User.User;

public class CustomerTest {
    
//This is a class to test the functions of Customer
    public static void main(String[] args) {
            // Login as John Doe using the actual database
            User loggedInUser = User.login("john_doe", "password123");
            
            if (loggedInUser instanceof Customer) {
                Customer johnDoe = (Customer) loggedInUser;

                System.out.println("Successfully logged in as: " + johnDoe.getFullname());
                System.out.println("Customer ID: " + johnDoe.getId());
                System.out.println("Email: " + johnDoe.getEmail());

                // Create controller and set customer
                CustomerController controller = new CustomerController();
                controller.setCurrentCustomer(johnDoe);

                // Create and show dashboard
                CustomerDashboard dashboard = new CustomerDashboard();
                dashboard.setController(controller);
                dashboard.setVisible(true);


            } else {
                System.err.println("Login failed or user is not a Customer");
            }
            
    }
}