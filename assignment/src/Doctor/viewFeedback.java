/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Kingston Teoh
 */
public class viewFeedback {
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private int feedbackIdCounter;

    public viewFeedback(DefaultTableModel model) {
        this.model = model;
        this.sorter = new TableRowSorter<>(model);
        this.feedbackIdCounter = 1;
    }

    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }

    public void loadFeedbacks(String filePath) {
        model.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",", -1);

            // feedbacks.txt has 7 fields
                if (data.length >= 7) {
                    model.addRow(new Object[]{
                        data[0], // Feedback ID
                        data[1], // Appointment ID
                        data[2], // Doctor ID
                        data[3], // Doctor Name
                        data[4], // Customer ID
                        data[5], // Customer Name
                        data[6]  // Feedback message
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading feedbacks: " + e.getMessage());
        }
    }
}
