/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manager.Controller;

import Manager.Model.ManagerModel;
import Manager.Model.ReportGenerator;
import Manager.Model.ReportGenerator.DataDoctor;
import Manager.Model.ReportGenerator.DataMonth;
import Manager.Model.ReportGenerator.DoctorReportData;
import Manager.Model.ReportGenerator.MonthlyReportData;
import Manager.View.ManagerAppointment;
import Manager.View.ManagerComment;
import Manager.View.ManagerReports;
import Manager.View.ManagerUserManagement;
import Manager.View.View;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author weiha
 */
public class Controller {
    private final ManagerModel model;
    private View view;
    private final DateTimeFormatter formatter;
    public enum FrameType {UserManagement, Report, Appointment, Comment};
    public enum FileType {TXT, PDF};
    
    public Controller(ManagerModel model)
    {
        this.formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        this.model = model;
    }
    
    public void ShowFrame(FrameType frame)
    {   
        if (view != null) 
        {
        view.Dispose();
        }
        switch (frame)
        {
            case UserManagement -> {
                ManagerUserManagement newPage = new ManagerUserManagement(this);
                view = newPage;
                newPage.setVisible(true);
                UpdateDisplay("users");
            }
            case Report -> {
                ManagerReports newPage = new ManagerReports(this);
                view = newPage;
                newPage.setVisible(true);
            }
            case Appointment -> {
                ManagerAppointment newPage = new ManagerAppointment(this);
                view = newPage;
                newPage.setVisible(true);
                UpdateDisplay("appointments");
            }
            case Comment -> {
                ManagerComment newPage = new ManagerComment(this);
                view = newPage;
                newPage.setVisible(true);
                FirstThreeUpdateDisplay("comments");
            }
            default -> {
            }
        }
        model.FlushData();
    }
    
    public String[] GetYearList(int colIndex, String file)
    {
        DateTimeFormatter tempFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String[]> content = model.ReadFile(file);
        return content.stream()
                .map(row -> String.valueOf(LocalDate.parse(row[colIndex], tempFormatter).getYear()))
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toArray(String[]::new);
    }
    
    public String[] GetFeedback(int row)
    {
        if (row != -1)
        {
            List<String[]> content = model.ReadFile("feedbacks");
            return content.stream()
                    .filter(r -> r[1].equalsIgnoreCase(view.GetTableRow(row).get(0)))
                    .map(r -> r[6])
                    .toArray(String[]::new);
        }
        return new String[]{""};
    }
    
    public String GetComment(int row)
    {
        if (row != -1)
        {
            List<String[]> content = model.GetData();
            return content.stream()
                    .filter(r -> r[0].equalsIgnoreCase(view.GetTableRow(row).get(0)))
                    .findFirst()
                    .map(r -> r[3])
                    .orElse("");
        }
        return null;
    }
    
    public String[] GetUserDetail(int row)
    {
        if (row != -1)
        {
            List<String[]> content = model.ReadFile("users");
            return content.stream()
                    .filter(r -> r[0].equalsIgnoreCase(view.GetTableRow(row).get(1)))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
    
    public String[] FillDetail(int row)
    {
        if (row != -1)
        {
            return view.GetTableRow(row).toArray(String[]::new);
        }
        return new String[]{};
    }
    
    public void DisplayReport(String type, int year, String key)
    {
        switch (type)
        {
            case "month" ->
            {
                GetMonthlyReport(year, key);
            }
            
            case "doctor" ->
            {
                GetDoctorReport(year, key);
            }
            
            default -> throw new IllegalArgumentException("Invalid sort key: " + key);
        }
    }
    
    public List<String[]> SearchRow(String keyword)
    {
        List<String[]> filtered = model.GetData().stream()
            .filter(row -> {
                return Arrays.stream(row).anyMatch(item -> item.toLowerCase().contains(keyword.toLowerCase()));
            })
            .toList();
        return filtered;
    }
    
    public void AddUser(String[] data)
    {
        model.AddUser(data);
    }
    
    public void EditUser(int index, String[] data)
    {
        if (index > -1)
        {
            model.EditUser(index, data);
        }
        else
        {
            view.ShowErrorDialog("Null selection! Please select a row before editing");
        }
    }
    
    public void DeleteUser(int index)
    {
        if (index > -1)
        {
            model.DeleteUser(index);
        }
        else
        {
            view.ShowErrorDialog("Null selection! Please select a row before deleting");
        }
    }
    
    public void UpdateDisplay(String file)
    {
        view.LoadDisplay(model.ReadFileAndSaveInBuffer(file));
    }
    
    public void RoleFilteredUpdateDisplay(String file, String role)
    {
        List<String[]> filtered = model.ReadFileAndSaveInBuffer(file).stream()
                .filter(row -> role.equalsIgnoreCase(row[6]))
                .map(row -> new String[]{row[0], row [1], row[2]})
                .toList();
        view.LoadDisplay(filtered);
    }
    
    public void FirstThreeUpdateDisplay(String file)
    {
        List<String[]> firstThree = model.ReadFileAndSaveInBuffer(file).stream()
                .map(row -> new String[]{row[0], row [1], row[2]})
                .toList();
        view.LoadDisplay(firstThree);
    }
    
    public void SaveReport(ManagerReports MR, int year, FileType type)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as ...");
        fileChooser.setSelectedFile(new File("report-"+ManagerModel.GetCurrentDateTime()+"."+type.toString().toLowerCase()));

        if (fileChooser.showSaveDialog(MR) == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = fileChooser.getSelectedFile();
            
            try
            {
                ReportGenerator r = new ReportGenerator();
                r.GenerateTXTReport(fileToSave, year);
                JOptionPane.showMessageDialog(MR, "File saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MR, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void GetMonthlyReport(int year, String key)
    {
        List<String[]> appointment = model.ReadFile("appointments");
        List<String[]> invoice = model.ReadFile("invoices");
        ReportGenerator rg = new ReportGenerator();
        List<String[]> content = new ArrayList<>();
        
        MonthlyReportData dataset = rg.GetMonthlyDetail(appointment, invoice, year);
        IntSummaryStatistics apptSummary = dataset.appointmentSummary();
        DoubleSummaryStatistics incomeSummary = dataset.incomeSummary();
        
        content.add(new String[]{"Total", String.valueOf(apptSummary.getSum()), String.valueOf(incomeSummary.getSum())});
        content.add(new String[]{"Lowest", String.valueOf(apptSummary.getMin()), String.valueOf(incomeSummary.getMin())});
        content.add(new String[]{"Average", String.valueOf(apptSummary.getAverage()), String.valueOf(incomeSummary.getAverage())});
        content.add(new String[]{"Highest", String.valueOf(apptSummary.getMax()), String.valueOf(incomeSummary.getMax())});
        content.addAll(SortMonthlyReportBy(key, dataset));
        
        view.InitializeTableModel(new String[]{"", "Appointments", "Income"});
        view.LoadDisplay(content);
    }
    
    private void GetDoctorReport(int year, String key)
    {
        List<String[]> user = model.ReadFile("users");
        List<String[]> appointment = model.ReadFile("appointments");
        List<String[]> invoice = model.ReadFile("invoices");
        ReportGenerator rg = new ReportGenerator();
        List<String[]> content = new ArrayList<>();
        
        DoctorReportData dataset = rg.GetDoctorDetail(user, appointment, invoice, year);
        IntSummaryStatistics apptSummary = dataset.appointmentSummary();
        DoubleSummaryStatistics incomeSummary = dataset.incomeSummary();
        
        content.add(new String[]{"Total", String.valueOf(apptSummary.getSum()), String.valueOf(incomeSummary.getSum())});
        content.add(new String[]{"Lowest", String.valueOf(apptSummary.getMin()), String.valueOf(incomeSummary.getMin())});
        content.add(new String[]{"Average", String.valueOf(apptSummary.getAverage()), String.valueOf(incomeSummary.getAverage())});
        content.add(new String[]{"Highest", String.valueOf(apptSummary.getMax()), String.valueOf(incomeSummary.getMax())});
        
        content.addAll(SortDoctorReportBy(key, dataset));
        
        view.InitializeTableModel(new String[]{"", "Appointments", "Income"});
        view.LoadDisplay(content);
    }
    
    private List<String[]> SortMonthlyReportBy(String key, MonthlyReportData dataset)
    {
        Comparator<Map.Entry<String, DataMonth>> comparator;
        switch (key)
        {
            case "month" -> comparator = Comparator.comparing(entry -> YearMonth.parse(entry.getKey(), formatter));   
            case "appointment" -> comparator = Comparator.comparingInt(entry -> entry.getValue().appointment());
            case "income" -> comparator = Comparator.comparingDouble(entry -> entry.getValue().income());
            default -> comparator = Comparator.comparing(entry -> YearMonth.parse(entry.getKey(), formatter));
        }
        return dataset.monthlyData().entrySet().stream()
                        .sorted(comparator.reversed()) 
                        .map(kv -> new String[]{
                            kv.getKey(),
                            String.valueOf(kv.getValue().appointment()),
                            String.valueOf(kv.getValue().income())})
                        .toList();
    }
    
    private List<String[]> SortDoctorReportBy(String key, DoctorReportData dataset)
    {
        Comparator<Map.Entry<String, DataDoctor>> comparator;
        switch (key)
        {
            case "id" -> comparator = Comparator.comparing(entry -> entry.getKey());   
            case "name" -> comparator = Comparator.comparing(entry -> entry.getValue().name());   
            case "appointment" -> comparator = Comparator.comparingInt(entry -> entry.getValue().appointment());
            case "income" -> comparator = Comparator.comparingDouble(entry -> entry.getValue().income());
            default -> comparator = Comparator.comparing(entry -> YearMonth.parse(entry.getKey(), formatter));
        }
         
        return dataset.doctorData().entrySet().stream()
                        .sorted(key.equals("id") ? comparator : comparator.reversed()) 
                        .map(kv -> new String[]{
                            kv.getKey(),
                            String.valueOf(kv.getValue().name()),
                            String.valueOf(kv.getValue().appointment()),
                            String.valueOf(kv.getValue().income())})
                        .toList();
    }
}

