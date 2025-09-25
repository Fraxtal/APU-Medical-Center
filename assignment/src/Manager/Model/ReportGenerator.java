/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manager.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 *
 * @author weiha
 */
public class ReportGenerator extends ManagerModel {
    
    public record DataDoctor(
        String name,
        int appointment,
        double income) {}
    
    public record DataMonth(
        int appointment,
        double income) {}
    
        public record DoctorReportData(
        Map<String, DataDoctor> doctorData,
        IntSummaryStatistics appointmentSummary,
        DoubleSummaryStatistics incomeSummary
    ) {}
        
    public record MonthlyReportData(
        Map<String, DataMonth> monthlyData,
        IntSummaryStatistics appointmentSummary,
        DoubleSummaryStatistics incomeSummary
    ) {}
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    private final DateTimeFormatter readFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    protected final String textTemplates = 
            """
            =======================================================================
                                        %s
            =======================================================================
            
            -----------------------------------------------------------------------
            Staff Structures
            -----------------------------------------------------------------------
            - Number of Managers : %d
            - Number of Staffs : %d
            - Number of Doctors : %d
            - Total Workforce : %d
            
            -----------------------------------------------------------------------
            Appointment
            -----------------------------------------------------------------------
            - Appointment Completed : %d
            - Appointment Cancelled : %d
            
            """;
    
    public DoctorReportData GetDoctorDetail(List<String[]> user, List<String[]> appointment, List<String[]> invoice, int year)
    {
        Map<String, DataDoctor> doctorData = new HashMap<>();

        Map<String, List<String>> doctorAppointments = appointment.stream()
            .filter(row -> row.length == 7)
            .filter(row -> row[2].trim().equalsIgnoreCase("completed"))
            .filter(row -> LocalDate.parse(row[1].trim(), readFormatter).getYear() == year)
            .collect(Collectors.groupingBy(
                    row -> row[3].trim(),
                    Collectors.mapping(row -> row[0].trim(),
                           Collectors.toList())
            ));
        
        Map<String, Double> appointmentIncome = invoice.stream()
            .filter(row -> row.length == 4)
            .collect(Collectors.toMap(
                row -> row[3].trim(),                      
                row -> Double.valueOf(row[1].trim()),      
                Double::sum));
        
        for (String[] row : user) {
            if (row.length == 9 && row[row.length - 1].toLowerCase().contains("doctor")) {
                String id = row[0].trim();
                String name = row[2].trim();

                int apptCount = doctorAppointments.getOrDefault(id, List.of()).size();

                double income = doctorAppointments.getOrDefault(id, List.of()).stream()
                        .mapToDouble(apptID -> appointmentIncome.getOrDefault(apptID.trim(), 0.0))
                        .sum();

                doctorData.put(id, new DataDoctor(name, apptCount, income));
            }
        }
        
        IntSummaryStatistics doctorApptSummary = doctorData.values().stream()
                .collect(Collectors.summarizingInt(DataDoctor::appointment));

        DoubleSummaryStatistics doctorIncomeSummary = doctorData.values().stream()
                .collect(Collectors.summarizingDouble(DataDoctor::income));

        return new DoctorReportData(doctorData, doctorApptSummary, doctorIncomeSummary);
        }
    
    public MonthlyReportData GetMonthlyDetail(List<String[]> appointment, List<String[]> invoice, int year)
    {
        Map<String, String> appointmentDate = appointment.stream()
                .filter(row -> row.length == 7)
                .filter(row -> LocalDate.parse(row[1].trim(), readFormatter).getYear() == year)
                .collect(Collectors.toMap(
                    row -> row[0],
                    row -> LocalDate.parse(row[1].trim(), readFormatter).format(formatter)
                ));
        
        Map<String, DataMonth> monthlyData = invoice.stream()
                .filter(row -> row.length == 4)
                .collect(Collectors.groupingBy(
                    row -> {
                        String apptId = row[3].trim();
                        String date = appointmentDate.get(apptId);
                        return date;
                    },
                    Collectors.teeing(
                        Collectors.counting(),
                        Collectors.summingDouble(row -> Double.valueOf(row[1].trim())),
                        (count, sum) -> new DataMonth(count.intValue(), sum)
                    )
                ));
        
        IntSummaryStatistics monthlyApptSummary = monthlyData.values().stream()
                .collect(Collectors.summarizingInt(DataMonth::appointment));

        DoubleSummaryStatistics monthlyIncomeSummary = monthlyData.values().stream()
                .collect(Collectors.summarizingDouble(DataMonth::income));
        
        return new MonthlyReportData(monthlyData, monthlyApptSummary, monthlyIncomeSummary);
    }
    
    public Map<String, Integer> GetAppointmentCount(int year, List<String[]> appointment) 
    {
        Map<String, Integer> apptCount = appointment.stream()
                .filter(row -> LocalDate.parse(row[1].trim(), readFormatter).getYear() == year)
                .filter(row -> row[2].trim().equalsIgnoreCase("completed") || row[2].trim().equalsIgnoreCase("cancelled"))
                .collect(Collectors.groupingBy(
                        row -> row[2].trim().toLowerCase(),
                        Collectors.summingInt(counts -> 1)
                ));
        
        return apptCount;
    }
    
    public int[] GetWorkforce(List<String[]> user)
    {
        int[] output = new int[4];
        for (String[] row : user)
        {
            switch (row[8].toLowerCase())
            {
                case "manager" -> output[0]++;
                case "staff" -> output[1]++;
                case "doctor" -> output[2]++;
            }
        }
        output[3] = output[0] + output[1] + output[2];
        
        return output;
    }
    
    private String GenerateDoctorSection(List<String[]> user, List<String[]> appointment, List<String[]> invoice, int year)
    {
        DoctorReportData dataset = GetDoctorDetail(user, appointment, invoice, year);
        Map<String, DataDoctor> data = dataset.doctorData();
        IntSummaryStatistics apptSummary = dataset.appointmentSummary();
        DoubleSummaryStatistics incomeSummary = dataset.incomeSummary();
        
        StringBuilder temp = new StringBuilder();
        temp.append(
                """
                -----------------------------------------------------------------------
                Doctors
                -----------------------------------------------------------------------
                """); 
        
        temp.append("Total Appointment Completed: ").append(apptSummary.getSum()).append("\n");
        temp.append("Lowest Appointment Completed: ").append(apptSummary.getMin()).append("\n");
        temp.append("Average Appointment Completed: ").append(String.format("%.2f", apptSummary.getAverage())).append("\n");
        temp.append("Highest Appointment Completed: ").append(apptSummary.getMax()).append("\n");
        
        temp.append("Total Income: RM").append(String.format("%.2f", incomeSummary.getSum())).append("\n");
        temp.append("Lowest Income Made: RM").append(String.format("%.2f", incomeSummary.getMin())).append("\n");
        temp.append("Average Income Made: RM").append(String.format("%.2f", incomeSummary.getAverage())).append("\n");
        temp.append("Highest Income Made: RM").append(String.format("%.2f", incomeSummary.getMax())).append("\n");
        
        temp.append("|---------------------------------------------------------------------|\n");
        temp.append(String.format("| %-9s | %-19s | %-15s | %-15s |\n", "ID", "Name", "Appointments", "Income Made"));
        temp.append("|---------------------------------------------------------------------|\n");
        
        data.entrySet().stream().sorted(Comparator.comparing(entry -> entry.getKey())).forEach(entry -> {
            String id = entry.getKey();
            String name = entry.getValue().name();
            int appointments = entry.getValue().appointment();
            double income = entry.getValue().income();

            String row = String.format("| %-9s | %-19s | %-15d | %-15.2f |\n", id, name, appointments, income);
            temp.append(row);
            temp.append("|---------------------------------------------------------------------|\n");
        });
        
        return temp.toString();
    }
    private String GenerateMonthlySection(List<String[]> appointment, List<String[]> invoice, int year)
    {
        MonthlyReportData dataset = GetMonthlyDetail(appointment, invoice, year);
        Map<String, DataMonth> data = dataset.monthlyData();
        IntSummaryStatistics apptSummary = dataset.appointmentSummary();
        DoubleSummaryStatistics incomeSummary = dataset.incomeSummary();
        
        StringBuilder temp = new StringBuilder();
        temp.append(
                """
                -----------------------------------------------------------------------
                Month
                -----------------------------------------------------------------------
                """); 
        
        temp.append("Total Appointment Count: ").append(apptSummary.getSum()).append("\n");
        temp.append("Lowest Appointment Completed: ").append(apptSummary.getMin()).append("\n");
        temp.append("Average Appointment Completed: ").append(String.format("%.2f", apptSummary.getAverage())).append("\n");
        temp.append("Highest Appointment Completed: ").append(apptSummary.getMax()).append("\n");
        
        temp.append("Total Income: RM").append(String.format("%.2f", incomeSummary.getSum())).append("\n");
        temp.append("Lowest Income: RM").append(String.format("%.2f", incomeSummary.getMin())).append("\n");
        temp.append("Average Income: RM").append(String.format("%.2f", incomeSummary.getAverage())).append("\n");
        temp.append("Highest Income: RM").append(String.format("%.2f", incomeSummary.getMax())).append("\n");
        
        temp.append("|-----------------------------------------------------|\n");
        temp.append(String.format("| %-15s | %-15s | %-15s |\n", "Month", "Appointments", "Income Made"));
        temp.append("|-----------------------------------------------------|\n");
        
        data.entrySet().stream().sorted(Comparator.comparing((Map.Entry<String, DataMonth> entry) -> YearMonth.parse(entry.getKey(), formatter)).reversed())
                .forEach(entry -> {
                    String month = entry.getKey();
                    int appointments = entry.getValue().appointment();
                    double income = entry.getValue().income();

                    temp.append(String.format("| %-15s | %-15d | %-15.2f |\n", month, appointments, income));
                    temp.append("|-----------------------------------------------------|\n");
        });
        
        return temp.toString();
    }
   
    private String GetReportText(int year)
    {
        List<String[]> user = ReadFile("users");
        List<String[]> appointment = ReadFile("appointments");
        List<String[]> invoice = ReadFile("invoices");
        
        int[] workforce = GetWorkforce(user);
        Map<String, Integer> counts = GetAppointmentCount(year, appointment);
        
        return String.format(textTemplates, 
                "Analysed Report", 
                workforce[0], 
                workforce[1], 
                workforce[2], 
                workforce[3],
                counts.getOrDefault("completed", 0),
                counts.getOrDefault("cancelled", 0))+
                GenerateDoctorSection(user, appointment, invoice, year)+
                GenerateMonthlySection(appointment, invoice, year);
    }
    
    public void GenerateTXTReport(File file, int year) throws IOException
    {
        String filePath = file.getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".txt")) {
            file = new File(filePath + ".txt");
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(GetReportText(year));
        }
    }
}