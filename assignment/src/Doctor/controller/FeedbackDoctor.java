/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kingston Teoh
 */
public class FeedbackDoctor {
    private DefaultTableModel model;
    private String filePath; // Path to the text file

    public FeedbackDoctor(DefaultTableModel model, String filePath) {
        this.model = model;
        this.filePath = filePath;
    }

    // Method to edit feedback in the feedback column
    public void editFeedback(int selectedRow, String feedback) {
        if (model != null && selectedRow >= 0 && selectedRow < model.getRowCount()) {
            model.setValueAt(feedback, selectedRow, model.getColumnCount() - 1);
            updateTextFile();
        }
    }

    // Method to clear feedback in the feedback column
    public void clearFeedback(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
            model.setValueAt("", selectedRow, model.getColumnCount() - 1);
            updateTextFile();
        }
    }

    // Method to update the text file with current feedback entries
    private void updateTextFile() {
        saveFeedbackToFile(); // Use the proper save method instead
    }

    // Save feedback data to feedbacks.txt in the correct format
    public void saveFeedbackToFile() {
        // Load all existing feedback
        List<String[]> allFeedback = loadAllFeedback();
        
        // Update with current scheduled appointments from the table
        for (int row = 0; row < model.getRowCount(); row++) {
            String currentAppointmentId = model.getValueAt(row, 1).toString();
            String[] newFeedback = {
                model.getValueAt(row, 0).toString(), // Feedback ID
                currentAppointmentId,
                model.getValueAt(row, 2).toString(), // Doctor ID
                model.getValueAt(row, 3).toString(), // Doctor Name
                model.getValueAt(row, 4).toString(), // Customer ID
                model.getValueAt(row, 5).toString(), // Customer Name
                model.getValueAt(row, 6).toString()  // Feedback
            };
            
            // Check if this appointment already exists in feedback
            boolean found = false;
            for (int i = 0; i < allFeedback.size(); i++) {
                String[] existingFeedback = allFeedback.get(i);
                if (existingFeedback.length >= 2 && existingFeedback[1].equals(currentAppointmentId)) {
                    // Update existing feedback
                    allFeedback.set(i, newFeedback);
                    found = true;
                    break;
                }
            }
            
            // If not found, add as new feedback
            if (!found) {
                allFeedback.add(newFeedback);
            }
        }

        // Save all feedback back to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] feedbackData : allFeedback) {
                String line = String.join(";", feedbackData);
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Feedback data saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving feedback data: " + e.getMessage());
        }
    }

    // Load all existing feedback from file to preserve completed appointments
    private List<String[]> loadAllFeedback() {
        List<String[]> allFeedback = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";", -1);
                if (data.length >= 7) {
                    allFeedback.add(data);
                }
            }
        } catch (IOException e) {
            // File might not exist yet, that's okay
            System.out.println("No existing feedback file found, creating new one.");
        }
        
        return allFeedback;
    }

    // Optionally, a method to set a new file path
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
