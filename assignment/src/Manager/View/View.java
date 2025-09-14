/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Manager.View;

import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author weiha
 */
public interface View {
    public void InitializeTableModel(String[] columnName);
    public void LoadDisplay(List<String[]> content);   
    public void ShowErrorDialog(String message);
    public List<String> GetTableRow(int index);
}
