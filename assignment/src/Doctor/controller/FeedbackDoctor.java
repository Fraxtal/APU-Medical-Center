/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kingston Teoh
 */
public class FeedbackDoctor {
    private DefaultTableModel model;
    private String filePath; // Path to the text file

    public FeedbackDoctor(DefaultTableModel model, String filePath) {
        this.model = model; // Initialize the model
        this.filePath = filePath; // Initialize the file path
    }

    // Method to edit feedback in the feedback column
    public void editFeedback(int selectedRow, String feedback) {
     // Check if model is initialized and row is valid
        if (model != null && selectedRow >= 0 && selectedRow < model.getRowCount()) {
            // Update the feedback in the last column (index model.getColumnCount() - 1)
            model.setValueAt(feedback, selectedRow, model.getColumnCount() - 1);
            updateTextFile(); // Update the text file after editing
        }
    }

    // Method to clear feedback in the feedback column
    public void clearFeedback(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
            model.setValueAt("", selectedRow, model.getColumnCount() - 1); // Clear the feedback in the last column
            updateTextFile(); // Update the text file
        }
    }

    // Method to update the text file with current feedback entries
    private void updateTextFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    writer.write(model.getValueAt(row, col).toString());
                    if (col < model.getColumnCount() - 1) {
                        writer.write(","); // Use a delimiter, like a comma
                    }
                }
                writer.newLine(); // New line for the next row
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle any exceptions
        }
    }
    public void saveFeedbackToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int row = 0; row < model.getRowCount(); row++) {
                // Get all column values
                String feedbackId = model.getValueAt(row, 0).toString();
                String appointmentId = model.getValueAt(row, 1).toString();
                String doctorId = model.getValueAt(row, 2).toString();
                String doctorName = model.getValueAt(row, 3).toString();
                String customerId = model.getValueAt(row, 4).toString();
                String customerName = model.getValueAt(row, 5).toString();
                String feedback = model.getValueAt(row, 6).toString();
                
                // Write in the correct format with semicolon delimiter
                String line = feedbackId + ";" + appointmentId + ";" + doctorId + ";" + 
                             doctorName + ";" + customerId + ";" + customerName + ";" + feedback;
                
                writer.write(line);
                writer.newLine(); // New line for the next row
            }
            System.out.println("Feedback data saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace(); // Handle any exceptions
            System.err.println("Error saving feedback data: " + e.getMessage());
        }
    }
    // Optionally, a method to set a new file path
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
