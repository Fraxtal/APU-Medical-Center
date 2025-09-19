/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Doctor.controller;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Kingston Teoh
 */
public class TableSearchHandler {
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;

    public TableSearchHandler(JTable table) {
        this.table = table;
        // Assumes your table already has a DefaultTableModel
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
    }

    // Apply filter
    public void filterTable(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            sorter.setRowFilter(null); // Show all
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm)); 
        }
    }

    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }
}
