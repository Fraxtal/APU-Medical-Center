
package Staff.view;

import Staff.controller.StaffController;
import Staff.service.ManageCustomerAccount;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class StaffAppointments extends javax.swing.JFrame {
    StaffController controller = new StaffController();
    ManageCustomerAccount mca = new ManageCustomerAccount();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StaffAppointments.class.getName());

    public StaffAppointments() {
        initComponents();
        ckbxPast.setSelected(false);
        tblAppointments.setModel(controller.getAppointmentTable());
        tblDoctors.setModel(controller.getDoctorTable());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblAppointments = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtAppointmentId = new javax.swing.JTextField();
        txtDoctorId = new javax.swing.JTextField();
        javax.swing.JButton btnSave = new javax.swing.JButton();
        javax.swing.JButton btnClear = new javax.swing.JButton();
        cmbStatus = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        txtDoctorName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        calendarAppointment = new com.toedter.calendar.JCalendar();
        jLabel5 = new javax.swing.JLabel();
        txtCustomerId = new javax.swing.JTextField();
        javax.swing.JButton btnAdd = new javax.swing.JButton();
        btnCheck = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAppointments = new javax.swing.JTable();
        ckbxPast = new javax.swing.JCheckBox();
        btnReturn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDoctors = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblAppointments.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblAppointments.setText("Appointments");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Appointment ID");
        jLabel3.setName("lbl_ID"); // NOI18N

        jLabel4.setText("Date of Appointment");
        jLabel4.setName("lbl_username"); // NOI18N

        jLabel6.setText("Status");
        jLabel6.setName("lbl_password"); // NOI18N

        jLabel8.setText("Doctor ID");
        jLabel8.setName("lbl_contact"); // NOI18N

        txtAppointmentId.setEditable(false);
        txtAppointmentId.setName("txt_ID"); // NOI18N

        txtDoctorId.setEditable(false);
        txtDoctorId.setName("txt_contact"); // NOI18N

        btnSave.setText("Save");
        btnSave.setName("btn_Save"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnClear.setText("Clear Fields");
        btnClear.setName("btn_Create"); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Scheduled", "Completed", "Cancelled" }));

        jLabel7.setText("Customer Name");
        jLabel7.setName("lbl_ID"); // NOI18N

        txtCustomerName.setName("txt_ID"); // NOI18N

        txtDoctorName.setEditable(false);
        txtDoctorName.setName("txt_contact"); // NOI18N

        jLabel9.setText("Doctor Name");
        jLabel9.setName("lbl_contact"); // NOI18N

        calendarAppointment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setText("Customer ID");
        jLabel5.setName("lbl_ID"); // NOI18N

        txtCustomerId.setName("txt_ID"); // NOI18N

        btnAdd.setText("Add");
        btnAdd.setName("btn_Save"); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnCheck.setText("Check");
        btnCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtAppointmentId)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCustomerName)
                    .addComponent(txtDoctorName)
                    .addComponent(jLabel9)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(195, 195, 195))
                    .addComponent(jLabel6)
                    .addComponent(calendarAppointment, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCustomerId, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addComponent(txtDoctorId))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAppointmentId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCustomerId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCheck))
                .addGap(16, 16, 16)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calendarAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDoctorId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDoctorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(btnSave))
                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32))
        );

        tblAppointments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAppointmentsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAppointments);

        ckbxPast.setText("Past Appointments");
        ckbxPast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbxPastActionPerformed(evt);
            }
        });

        btnReturn.setText("Return");
        btnReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnActionPerformed(evt);
            }
        });

        tblDoctors.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDoctorsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblDoctors);

        jLabel1.setText("Doctors");

        jLabel10.setText("Appointments");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(lblAppointments)))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ckbxPast))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                            .addComponent(jScrollPane2))
                        .addGap(38, 38, 38))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReturn)
                            .addComponent(jLabel1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAppointments)
                    .addComponent(btnReturn))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ckbxPast)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 45, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        String appointmentId = txtAppointmentId.getText();
        Date appointmentDate = calendarAppointment.getDate();
        String status = cmbStatus.getSelectedItem().toString();
        String doctorId = txtDoctorId.getText();
        String doctorName = txtDoctorName.getText();
        String customerId = txtCustomerId.getText();
        String customerName = txtCustomerName.getText();
        
        int option = JOptionPane.showConfirmDialog(this, "Proceed with Changes?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option != JOptionPane.YES_OPTION) {
            return;
        }
        int success = controller.validateAppointmentUpdate(appointmentId, appointmentDate, status, doctorId, doctorName, customerId, customerName);
        switch (success) {
            case 0:
                JOptionPane.showMessageDialog(this, "Appointment sucessfullly updated.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 1:
                JOptionPane.showMessageDialog(this, "Please fill in all the fields.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(this, "Invalid Doctor ID.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 3:
                JOptionPane.showMessageDialog(this, "Invalid Customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 4:
                JOptionPane.showMessageDialog(this, "Invalid Appointment ID.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 5:
                JOptionPane.showMessageDialog(this, "You can't book in the past.", "Error", JOptionPane.ERROR_MESSAGE);
                break;    
            case 6:
                JOptionPane.showMessageDialog(this, "Error occured while updating appointment.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "ERROR", "Error", JOptionPane.ERROR_MESSAGE);
        }
        tblAppointments.setModel(controller.getAppointmentTable());
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearText();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnActionPerformed
        StaffDashboard sd = new StaffDashboard();
        sd.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnReturnActionPerformed

    private void tblAppointmentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAppointmentsMouseClicked
        int index = tblAppointments.getSelectedRow();
        TableModel Appmodel = tblAppointments.getModel();
        if (index != -1){
            txtAppointmentId.setText(Appmodel.getValueAt(index, 0).toString());
            calendarAppointment.setDate(mca.parseStrToDate(Appmodel.getValueAt(index, 1).toString()));
            cmbStatus.setSelectedItem(Appmodel.getValueAt(index, 2).toString());
            txtDoctorId.setText(Appmodel.getValueAt(index, 3).toString());
            txtDoctorName.setText(Appmodel.getValueAt(index, 4).toString());
            txtCustomerId.setText(Appmodel.getValueAt(index, 5).toString());
            txtCustomerName.setText(Appmodel.getValueAt(index, 6).toString());

        }
    }//GEN-LAST:event_tblAppointmentsMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        Date appointmentDate = calendarAppointment.getDate();
        String status = cmbStatus.getSelectedItem().toString();
        String doctorId = txtDoctorId.getText();
        String doctorName = txtDoctorName.getText();
        String customerId = txtCustomerId.getText();
        String customerName = txtCustomerName.getText();
        
        int success = controller.validateAppointmentBooking(appointmentDate, status, doctorId, doctorName, customerId, customerName);
        
        switch (success) {
            case 0:
                JOptionPane.showMessageDialog(this, "Appointment sucessfullly booked.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 1:
                JOptionPane.showMessageDialog(this, "Please fill in all the fields.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(this, "Invalid Doctor ID.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 3:
                JOptionPane.showMessageDialog(this, "Invalid Customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 4:
                JOptionPane.showMessageDialog(this, "Time machine unavailable", "Error", JOptionPane.ERROR_MESSAGE);
                break;    
            case 5:
                JOptionPane.showMessageDialog(this, "Error occured while creating appointment", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "ERROR", "Error", JOptionPane.ERROR_MESSAGE);
        }
        tblAppointments.setModel(controller.getAppointmentTable());
    }//GEN-LAST:event_btnAddActionPerformed

    private void tblDoctorsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDoctorsMouseClicked
        int index = tblDoctors.getSelectedRow();
        TableModel docModel = tblDoctors.getModel();
        if (index != -1){
            txtDoctorId.setText(docModel.getValueAt(index, 0).toString());
            txtDoctorName.setText(docModel.getValueAt(index, 1).toString());
        }
    }//GEN-LAST:event_tblDoctorsMouseClicked

    private void ckbxPastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbxPastActionPerformed
        if (ckbxPast.isSelected()){
            tblAppointments.setModel(controller.getPastAppointmentTable());
        }
        else{
            tblAppointments.setModel(controller.getAppointmentTable());
        }
    }//GEN-LAST:event_ckbxPastActionPerformed

    private void btnCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckActionPerformed
        String customerId = txtCustomerId.getText();
        String customerName = controller.validateCustomerIDtoName(customerId);
        
        if (customerName == null){
            JOptionPane.showMessageDialog(this, "Invalid Customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        txtCustomerName.setText(customerName);
    }//GEN-LAST:event_btnCheckActionPerformed

    public void clearText(){
        txtAppointmentId.setText("");
        txtCustomerName.setText("");
        txtCustomerName.setText("");
        calendarAppointment.setDate(new Date());
        cmbStatus.setSelectedIndex(0);
        txtDoctorId.setText("");
        txtDoctorName.setText("");
    }
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new StaffAppointments().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheck;
    private javax.swing.JButton btnReturn;
    private com.toedter.calendar.JCalendar calendarAppointment;
    private javax.swing.JCheckBox ckbxPast;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAppointments;
    private javax.swing.JTable tblAppointments;
    private javax.swing.JTable tblDoctors;
    private javax.swing.JTextField txtAppointmentId;
    private javax.swing.JTextField txtCustomerId;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDoctorId;
    private javax.swing.JTextField txtDoctorName;
    // End of variables declaration//GEN-END:variables
}
