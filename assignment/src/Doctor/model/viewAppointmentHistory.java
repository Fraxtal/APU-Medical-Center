/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Kingston Teoh
 */
public class viewAppointmentHistory {
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private List<String[]> feedbackDataList; // [appointmentId, feedback]
    private List<String[]> commentDataList;  // [customerId, subject, context]

    public viewAppointmentHistory(DefaultTableModel model) {
        this.model = model;
        this.sorter = new TableRowSorter<>(model);
        this.feedbackDataList = new ArrayList<>();
        this.commentDataList = new ArrayList<>();
    }

    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }

    public void loadFromTxt(String filePath) {
        model.setRowCount(0); // Clear existing data
        
        // Load feedback data
        loadFeedbackData("src\\database\\feedbacks.txt");
        
        // Load comment data
        loadCommentData("src\\database\\comments.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
            
                // Ensure there are enough data fields
                if (data.length >= 7) {
                    String status = data[2]; // Fetching the status
                    String appointmentId = data[0];
                    String customerId = data[5];

                    // Only add the row if the status is "Completed"
                    if ("Completed".equalsIgnoreCase(status)) {
                        // Get feedback data for this appointment
                        String feedback = findFeedbackByAppointmentId(appointmentId);
                        
                        // Get comment data for this customer
                        String[] commentInfo = findCommentByCustomerId(customerId);
                        
                        model.addRow(new Object[]{
                            appointmentId, // Appointment ID
                            data[1],       // Date of Appointment
                            status,        // Status
                            data[3],       // Doctor ID
                            data[4],       // Doctor Name
                            customerId,    // Customer ID
                            data[6],       // Customer Name
                            feedback,      // Feedback
                            commentInfo[0], // Subject
                            commentInfo[1]  // Context
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading file: " + e.getMessage());
        }
    }

    private String findFeedbackByAppointmentId(String appointmentId) {
        for (String[] feedback : feedbackDataList) {
            if (feedback[1].equals(appointmentId)) { // feedback[1] is appointment ID
                return feedback[6]; // feedback[6] is the feedback text
            }
        }
        return "No feedback available";
    }

    private String[] findCommentByCustomerId(String customerId) {
        for (String[] comment : commentDataList) {
            // Based on your example: "2;10001;Clinic Experience;Appreciated the thorough check-up..."
            // comment[0] = comment ID, comment[1] = customer ID, comment[2] = subject, comment[3] = context
            if (comment[1].equals(customerId)) {
                return new String[]{comment[2], comment[3]};
            }
        }
        return new String[]{"", ""};
    }

    private void loadFeedbackData(String filePath) {
        feedbackDataList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
                if (data.length >= 7) {
                    feedbackDataList.add(data);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading feedback file: " + e.getMessage());
        }
    }

    private void loadCommentData(String filePath) {
        commentDataList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
                // Expecting format: CommentID;CustomerID;Subject;Context
                if (data.length >= 4) {
                    commentDataList.add(data);
                } else if (data.length == 3) {
                    // Handle case where there might be missing CommentID
                    commentDataList.add(new String[]{"", data[0], data[1], data[2]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading comment file: " + e.getMessage());
        }
    }
}
