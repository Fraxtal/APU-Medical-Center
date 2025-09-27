/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor.controller;

import Doctor.model.Doctor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class DoctorCtrl {
    private Doctor info;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private List<String[]> appointmentData;
    private List<String[]> feedbackDataList;
    private List<String[]> commentDataList;

    public DoctorCtrl(DefaultTableModel model) {
        this.model = model;
        this.sorter = new TableRowSorter<>(model);
        this.appointmentData = new ArrayList<>();
        this.feedbackDataList = new ArrayList<>();
        this.commentDataList = new ArrayList<>();
        loadAppointmentStatuses();
    }
    public DoctorCtrl() {
        this.appointmentData = new ArrayList<>();
        this.feedbackDataList = new ArrayList<>();
        this.commentDataList = new ArrayList<>();
        loadAppointmentStatuses();
    }

    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }

    // -------------------- Appointment (Scheduled) --------------------
    public void loadScheduledAppointments(String filePath) {
        model.setRowCount(0); // Clear existing data
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);

                if (data.length >= 7) {
                    String status = data[2].trim();
                    if ("Scheduled".equalsIgnoreCase(status)) {
                        model.addRow(new Object[]{
                            data[0], // Appointment ID
                            data[1], // Date
                            status,
                            data[3], // Doctor ID
                            data[4], // Doctor Name
                            data[5], // Customer ID
                            data[6]  // Customer Name
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading scheduled appointments: " + e.getMessage());
        }
    }

    // -------------------- Appointment History (Completed) --------------------
    public void loadAppointmentHistory(String filePath) {
        model.setRowCount(0);
        loadFeedbackData("src\\database\\feedbacks.txt");
        loadCommentData("src\\database\\comments.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);

                if (data.length >= 7) {
                    String status = data[2].trim();
                    if ("Completed".equalsIgnoreCase(status)) {
                        String appointmentId = data[0];
                        String customerId = data[5];

                        String feedback = findFeedbackByAppointmentId(appointmentId);
                        String[] commentInfo = findCommentByCustomerId(customerId);

                        model.addRow(new Object[]{
                            appointmentId,
                            data[1], // Date
                            status,
                            data[3], // Doctor ID
                            data[4], // Doctor Name
                            customerId,
                            data[6], // Customer Name
                            feedback,
                            commentInfo[0], // Subject
                            commentInfo[1]  // Context
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading appointment history: " + e.getMessage());
        }
    }

    // -------------------- Doctor Charges (Invoices for Scheduled Appointments) --------------------
    public void loadCharges(String filePath) {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);

                if (data.length >= 7) {
                    for (int i = 0; i < data.length; i++) data[i] = data[i].trim();

                    String appointmentId = data[6];
                    String status = getAppointmentStatus(appointmentId);

                    if (status != null && "Scheduled".equalsIgnoreCase(status)) {
                        model.addRow(new Object[]{
                            data[0], // Invoice Details ID
                            data[1], // Item
                            data[2], // Quantity
                            data[3], // Price Per
                            data[4], // Total
                            data[5], // Invoice ID
                            data[6]  // Appointment ID
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading charges: " + e.getMessage());
        }
    }

    // -------------------- Feedback (For Scheduled Appointments) --------------------
    public void loadFeedbacks(String filePath) {
        model.setRowCount(0);
        int feedbackId = getNextFeedbackId(); // for new feedbacks

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);

                if (data.length >= 7) {
                    String status = data[2].trim();
                    if ("Scheduled".equalsIgnoreCase(status)) {
                        String appointmentId = data[0];

                        // Get feedback record if exists
                        String[] feedbackRecord = findExistingFeedbackRecord(appointmentId);

                        String feedbackIdStr;
                        String feedbackText;

                        if (feedbackRecord != null) {
                            feedbackIdStr = feedbackRecord[0];   // Feedback ID
                            feedbackText = feedbackRecord[6];    // Feedback text
                        } else {
                            feedbackIdStr = String.valueOf(feedbackId++);
                            feedbackText = "No feedback yet.";
                        }

                    // Add correct mapping to JTable
                        model.addRow(new Object[]{
                            feedbackIdStr,    // Feedback ID
                            data[0],          // Appointment ID
                            data[3],          // Doctor ID
                            data[4],          // Doctor Name
                            data[5],          // Customer ID
                            data[6],          // Customer Name
                            feedbackText      // Feedback
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading feedbacks: " + e.getMessage());
        }
    }
    private String[] findExistingFeedbackRecord(String appointmentId) {
        try (BufferedReader br = new BufferedReader(new FileReader("src\\database\\feedbacks.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
                if (data.length >= 7 && data[1].equals(appointmentId)) {
                    return data; // full record (includes Feedback ID + Feedback text)
                }
            }
        } catch (IOException ignored) {}
        return null;
    }
    // -------------------- Helper Methods --------------------
    private void loadAppointmentStatuses() {
        appointmentData.clear();
        String appointmentsFile = "src\\database\\appointments.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(appointmentsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 7) {
                    for (int i = 0; i < data.length; i++) data[i] = data[i].trim();
                    appointmentData.add(data);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading appointments: " + e.getMessage());
        }
    }

    private String getAppointmentStatus(String appointmentId) {
        for (String[] appt : appointmentData) {
            if (appt[0].equals(appointmentId)) return appt[2];
        }
        return null;
    }

    private int getNextFeedbackId() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("src\\database\\feedbacks.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
                try {
                    int id = Integer.parseInt(data[0]);
                    if (id > maxId) maxId = id;
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException ignored) {}
        return maxId + 1;
    }

    private String findExistingFeedback(String appointmentId) {
        try (BufferedReader br = new BufferedReader(new FileReader("src\\database\\feedbacks.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
                if (data.length >= 7 && data[1].equals(appointmentId)) {
                    return data[6];
                }
            }
        } catch (IOException ignored) {}
        return "No feedback yet";
    }

    private void loadFeedbackData(String filePath) {
        feedbackDataList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
                if (data.length >= 7) feedbackDataList.add(data);
            }
        } catch (IOException ignored) {}
    }

    private void loadCommentData(String filePath) {
        commentDataList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
                if (data.length >= 4) {
                    commentDataList.add(data);
                } else if (data.length == 3) {
                    commentDataList.add(new String[]{"", data[0], data[1], data[2]});
                }
            }
        } catch (IOException ignored) {}
    }

    private String findFeedbackByAppointmentId(String appointmentId) {
        for (String[] fb : feedbackDataList) {
            if (fb[1].equals(appointmentId)) return fb[6];
        }
        return "No feedback available";
    }

    private String[] findCommentByCustomerId(String customerId) {
        for (String[] comment : commentDataList) {
            if (comment[1].equals(customerId)) return new String[]{comment[2], comment[3]};
        }
        return new String[]{"", ""};
    }

    public void refreshAppointmentData() {
        loadAppointmentStatuses();
    }
    
    public void setCurrentDoctor(Doctor user){
        this.info = user;
    }
    
    public Doctor getCurrentDoctor(){
        return info;
    }
    
}

