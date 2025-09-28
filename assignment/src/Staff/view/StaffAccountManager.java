package Staff.view;

import Doctor.controller.TableSearchHandler;
import Staff.controller.StaffController;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;


public class StaffAccountManager extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StaffAccountManager.class.getName());
    StaffController controller;
    TableSearchHandler tbs;

    public StaffAccountManager(StaffController sc) {
        initComponents();
        this.controller = sc;
        tblAccounts.setModel(controller.getCustomerTable());
        tbs = new TableSearchHandler(tblAccounts);
        txtUsername.requestFocusInWindow();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtContact = new javax.swing.JTextField();
        javax.swing.JButton btnSave = new javax.swing.JButton();
        javax.swing.JButton btnDelete = new javax.swing.JButton();
        javax.swing.JButton btnclear = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtFullname = new javax.swing.JTextField();
        javax.swing.JButton btnAdd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAccounts = new javax.swing.JTable();
        btnReturn = new javax.swing.JButton();
        txtCustomerSearch = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setText("Customer Account Manager");
        jLabel1.setName("lbl_title"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("ID");
        jLabel2.setName("lbl_ID"); // NOI18N

        jLabel3.setText("Username:");
        jLabel3.setName("lbl_username"); // NOI18N

        jLabel4.setText("Email:");
        jLabel4.setName("lbl_email"); // NOI18N

        jLabel5.setText("Password:");
        jLabel5.setName("lbl_password"); // NOI18N

        jLabel6.setText("Address:");
        jLabel6.setName("lbl_address"); // NOI18N

        jLabel7.setText("Contact Number:");
        jLabel7.setName("lbl_contact"); // NOI18N

        txtID.setEditable(false);
        txtID.setName("txt_ID"); // NOI18N

        txtUsername.setName("txt_username"); // NOI18N

        txtAddress.setName("txt_address"); // NOI18N

        txtPassword.setName("txt_password"); // NOI18N

        txtEmail.setName("txt_email"); // NOI18N

        txtContact.setName("txt_contact"); // NOI18N

        btnSave.setText("Save");
        btnSave.setName("btn_Save"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.setName("btn_Delete"); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnclear.setText("Clear Fields");
        btnclear.setName("btn_Create"); // NOI18N
        btnclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnclearActionPerformed(evt);
            }
        });

        jLabel8.setText("Full Name");
        jLabel8.setName("lbl_username"); // NOI18N

        txtFullname.setName("txt_username"); // NOI18N

        btnAdd.setText("Add");
        btnAdd.setName("btn_Save"); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtID, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAddress, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtContact, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnclear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFullname, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFullname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnclear, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        tblAccounts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAccountsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAccounts);
        if (tblAccounts.getColumnModel().getColumnCount() > 0) {
            tblAccounts.getColumnModel().getColumn(0).setMinWidth(30);
            tblAccounts.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        btnReturn.setText("Return");
        btnReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnActionPerformed(evt);
            }
        });

        txtCustomerSearch.setForeground(new java.awt.Color(204, 204, 204));
        txtCustomerSearch.setText("Search...");
        txtCustomerSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCustomerSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerSearchFocusLost(evt);
            }
        });
        txtCustomerSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCustomerSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnReturn)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCustomerSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(txtCustomerSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReturn)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnclearActionPerformed
        clearText();
    }//GEN-LAST:event_btnclearActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String customerID = txtID.getText();
        String username = txtUsername.getText();
        String fullname = txtFullname.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        
        int success = controller.validateAccountUpdate(customerID, username, fullname, email, pass, address, contact);
        int currentRow = tblAccounts.getSelectedRow();
        
        if (currentRow == -1){
            JOptionPane.showMessageDialog(this, "Please select a record to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (success == 4) {
            int option = JOptionPane.showConfirmDialog(this,"Contact Number: " + contact + "\nThis number is already tied to an account.\nProceed?",
                    "Confirmation",JOptionPane.YES_NO_OPTION);

            if (option != JOptionPane.YES_OPTION) {
                return;
            }
        }
        

        switch (success) {
            case 0:
                JOptionPane.showMessageDialog(this, "Account details updated.", "Info", JOptionPane.INFORMATION_MESSAGE);tblAccounts.setModel(controller.getCustomerTable());
                break;
            case 1:
                JOptionPane.showMessageDialog(this, "Please fill in all the fields.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(this, "Invalid ID", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 3:
                JOptionPane.showMessageDialog(this, "Email already taken.", "Info", JOptionPane.INFORMATION_MESSAGE);

                break;
            case 4:
                JOptionPane.showMessageDialog(this, "Error occurred while updating account.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "ERROR.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }

        tblAccounts.setModel(controller.getCustomerTable());
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnActionPerformed
        StaffDashboard sd = new StaffDashboard(controller);
        sd.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnReturnActionPerformed

    private void tblAccountsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAccountsMouseClicked
        int baseIndex = tblAccounts.getSelectedRow(); 
        if (baseIndex != -1) {
            int displayIndex = tblAccounts.convertRowIndexToModel(baseIndex); 

            TableModel model = tblAccounts.getModel();
            txtID.setText(model.getValueAt(displayIndex, 0).toString());
            txtUsername.setText(model.getValueAt(displayIndex, 1).toString());
            txtFullname.setText(model.getValueAt(displayIndex, 2).toString());
            txtEmail.setText(model.getValueAt(displayIndex, 3).toString());
            txtPassword.setText(model.getValueAt(displayIndex, 4).toString());
            txtAddress.setText(model.getValueAt(displayIndex, 5).toString());
            txtContact.setText(model.getValueAt(displayIndex, 6).toString());
}

         
    }//GEN-LAST:event_tblAccountsMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRow = tblAccounts.getSelectedRow();
    
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String idToDelete = tblAccounts.getValueAt(selectedRow, 0).toString();
        String usernameToDelete = tblAccounts.getValueAt(selectedRow, 1).toString();
        int success = controller.validateAccountDeletion(idToDelete);
        
        int option = JOptionPane.showConfirmDialog(this,"Delete this Account?\nID: " + idToDelete + "\nUsername:" + usernameToDelete,
                    "Confirmation",JOptionPane.YES_NO_OPTION);

        if (option != JOptionPane.YES_OPTION) {
            return;
        }
        
        switch (success) {
            case 0:
                JOptionPane.showMessageDialog(this, "Account deleted.", "Info", JOptionPane.INFORMATION_MESSAGE);
                clearText();
                break;
            case 1:
                JOptionPane.showMessageDialog(this, "Invalid Customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(this, "Error occurred while deleting account.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "ERROR.", "Error", JOptionPane.ERROR_MESSAGE);  
                break;
        }
        tblAccounts.setModel(controller.getCustomerTable());
  
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String username = txtUsername.getText();
        String fullname = txtFullname.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        tblAccounts.setModel(controller.getCustomerTable());
        int success = controller.validateAccountAdd(username, fullname, email, pass, address, contact);
        
        if (success == 3) {
            int option = JOptionPane.showConfirmDialog(this,"Contact Number: " + contact + "\nThis number is already tied to an account.\nProceed?",
                    "Confirmation",JOptionPane.YES_NO_OPTION);

            if (option != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        switch (success) {
            case 0:
                JOptionPane.showMessageDialog(this, "New account added.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 1:
                JOptionPane.showMessageDialog(this, "Please fill in all the fields.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(this, "Email already taken.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 4:
                JOptionPane.showMessageDialog(this, "Error occured during account creation.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "ERROR", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        tblAccounts.setModel(controller.getCustomerTable());
    }//GEN-LAST:event_btnAddActionPerformed

    private void txtCustomerSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerSearchFocusGained
        if(txtCustomerSearch.getText().equals("Search...")){
            txtCustomerSearch.setText("");
            txtCustomerSearch.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtCustomerSearchFocusGained

    private void txtCustomerSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerSearchFocusLost
        if(txtCustomerSearch.getText().equals("")){
            txtCustomerSearch.setText("Search...");
            txtCustomerSearch.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_txtCustomerSearchFocusLost

    private void txtCustomerSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerSearchKeyReleased
        String query = txtCustomerSearch.getText();
        tbs.filterTable(query);
    }//GEN-LAST:event_txtCustomerSearchKeyReleased

    public void clearText(){
        txtID.setText("");
            txtUsername.setText("");
            txtFullname.setText("");
            txtEmail.setText("");
            txtPassword.setText("");
            txtAddress.setText("");
            txtContact.setText("");
    }

    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(() -> new StaffAccountManager().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReturn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAccounts;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtCustomerSearch;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFullname;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
