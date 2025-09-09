/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Kingston Teoh
 */
public class FeedbackWritter {
        public static void main(String[] args) {
        String appointmentsFile = "src\\database\\appointments.txt";
        String feedbacksFile = "src\\database\\feedbacks.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(appointmentsFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(feedbacksFile, true))) { // append mode

            // Find last feedbackId from feedbacks.txt
            int lastId = getLastFeedbackId(feedbacksFile);

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length >= 7) {
                    String appointmentId = parts[0].trim();
                    String status = parts[2].trim();
                    String doctorId = parts[3].trim();
                    String doctorName = parts[4].trim();
                    String customerId = parts[5].trim();
                    String customerName = parts[6].trim();

                    if (status.equalsIgnoreCase("Completed")) {
                        // Increment feedbackId
                        lastId++;
                        // Placeholder feedback message (can be replaced by user input)
                        String feedbackMessage = "No feedback yet.";

                        String feedbackEntry = lastId + ";" +
                                               appointmentId + ";" +
                                               doctorId + ";" +
                                               doctorName + ";" +
                                               customerId + ";" +
                                               customerName + ";" +
                                               feedbackMessage;

                        bw.write(feedbackEntry);
                        bw.newLine();
                    }
                }
            }
            System.out.println(" Feedback entries added to " + feedbacksFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Utility to get the last feedbackId from feedbacks.txt
    private static int getLastFeedbackId(String feedbacksFile) {
        int lastId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(feedbacksFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 0) {
                    try {
                        lastId = Integer.parseInt(parts[0].trim());
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException ignored) {}
        return lastId;
    }
}
