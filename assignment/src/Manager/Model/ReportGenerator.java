/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manager.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;

/**
 *
 * @author weiha
 */
public class ReportGenerator extends Model {
    
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
    
    protected final String textTemplates = 
            """
            ================================================
                           %s
            ================================================
            
            ------------------------------------------------
            Staff Structures
            ------------------------------------------------
            - Number of Managers : %d
            - Number of Staffs : %d
            - Number of Doctors : %d
            - Total Workforce : %d
            
            ------------------------------------------------
            Appointment
            ------------------------------------------------
            - Appointment Completed : %d
            - Appointment Cancelled : %d
            
            """;
    
    public DoctorReportData GetDoctorDetail(List<String[]> user, List<String[]> appointment, List<String[]> invoice, int year)
    {
        Map<String, DataDoctor> doctorData = new HashMap<>();

        Map<String, List<String>> doctorAppointments = appointment.stream()
            .filter(row -> row.length == 7)
            .filter(row -> row[2].equalsIgnoreCase("completed"))
            .filter(row -> LocalDate.parse(row[1]).getYear() == year)
            .collect(Collectors.groupingBy(
                    row -> row[3],
                    Collectors.mapping(row -> row[0],
                           Collectors.toList())
            ));
        
        Map<String, Double> appointmentIncome = invoice.stream()
            .filter(row -> row.length == 4)
            .collect(Collectors.toMap(
                row -> row[3],                      
                row -> Double.valueOf(row[1]),      
                Double::sum));
        
        for (String[] row : user) {
            if (row.length == 9 && row[row.length - 1].toLowerCase().contains("doctor")) {
                String id = row[0];
                String name = row[2];

                int apptCount = doctorAppointments.getOrDefault(id, List.of()).size();

                double income = doctorAppointments.getOrDefault(id, List.of()).stream()
                        .mapToDouble(apptID -> appointmentIncome.getOrDefault(apptID, 0.0))
                        .sum();

                doctorData.put(id, new DataDoctor(name, apptCount, income));
            }
        }
        
        IntSummaryStatistics doctorApptSummary = appointment.stream()
                    .filter(apptRow -> apptRow.length == 7)
                    .filter(apptRow -> doctorAppointments.get(apptRow[3]).contains(apptRow[0]))
                    .collect(Collectors.collectingAndThen(
                    groupingBy(apptRow -> apptRow[3],
                               Collectors.counting()),
                    map -> map.values().stream()
                              .collect(Collectors.summarizingInt(Long::intValue))
                    ));
                
        DoubleSummaryStatistics doctorIncomeSummary = doctorAppointments.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().stream()
                        .mapToDouble(appointmentId -> appointmentIncome.getOrDefault(appointmentId, 0.0))
                        .sum()
                ))
                .values().stream()
                .collect(Collectors.summarizingDouble(Double::doubleValue));
        
        return new DoctorReportData(doctorData, doctorApptSummary, doctorIncomeSummary);
        }
    
    public MonthlyReportData GetMonthlyDetail(List<String[]> appointment, List<String[]> invoice, int year)
    {
        
        Map<String, String> appointmentDate = appointment.stream()
                .filter(row -> row.length == 7)
                .filter(row -> LocalDate.parse(row[1]).getYear() == year)
                .collect(Collectors.toMap(
                    row -> row[0],
                    row -> LocalDate.parse(row[1]).format(formatter)
                ));
        
        IntSummaryStatistics monthlyApptSummary = appointment.stream()
                .filter(row -> row.length == 7)
                .filter(row -> LocalDate.parse(row[1]).getYear() == year)
                .collect(Collectors.collectingAndThen(
                groupingBy(row -> LocalDate.parse(row[1]).format(formatter),
                           Collectors.counting()),
                map -> map.values().stream()
                          .collect(Collectors.summarizingInt(Long::intValue))
                ));
        
        DoubleSummaryStatistics monthlyIncomeSummary = invoice.stream()
                .filter(invRow -> invRow.length == 4)
                .collect(Collectors.collectingAndThen(
                groupingBy(
                    invRow -> appointmentDate.get(invRow[3]),
                    Collectors.summingDouble(invRow -> Double.valueOf(invRow[1]))),
                map -> map.values().stream()
                    .collect(Collectors.summarizingDouble(Double::doubleValue))
                ));
        
        Map<String, DataMonth> monthlyData = invoice.stream()
                .filter(row -> row.length == 4)
                .collect(Collectors.groupingBy(
                    row -> {
                        String apptId = row[3];
                        String date = appointmentDate.get(apptId);
                        return date;
                    },
                    Collectors.teeing(
                        Collectors.counting(),
                        Collectors.summingDouble(row -> Double.valueOf(row[1])),
                        (count, sum) -> new DataMonth(count.intValue(), sum)
                    )
                ));
        
        return new MonthlyReportData(monthlyData, monthlyApptSummary, monthlyIncomeSummary);
    }
    
    public Map<String, Integer> GetAppointmentCount(int year, List<String[]> appointment) 
    {
        Map<String, Integer> apptCount = appointment.stream()
                .filter(row -> LocalDate.parse(row[1]).getYear() == year)
                .filter(row -> row[2].equalsIgnoreCase("completed") || row[2].equalsIgnoreCase("cancelled"))
                .collect(Collectors.groupingBy(
                        row -> row[2].toLowerCase(),
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
        Map<String, DataDoctor> data = dataset.doctorData;
        IntSummaryStatistics apptSummary = dataset.appointmentSummary();
        DoubleSummaryStatistics incomeSummary = dataset.incomeSummary();
        
        StringBuilder temp = new StringBuilder();
        temp.append(
                """
                ------------------------------------------------
                Doctors
                ------------------------------------------------
                """); 
        
        temp.append("Total Appointment Completed: ").append(apptSummary.getSum()).append("\n");
        temp.append("Lowest Appointment Completed: ").append(apptSummary.getMin()).append("\n");
        temp.append("Average Appointment Completed: ").append(String.format("%.2f", incomeSummary.getAverage())).append("\n");
        temp.append("Highest Appointment Completed: ").append(apptSummary.getMax()).append("\n");
        
        temp.append("Total Income: ").append(incomeSummary.getSum()).append("\n");
        temp.append("Lowest Income Made: ").append(incomeSummary.getMin()).append("\n");
        temp.append("Average Income Made: ").append(String.format("%.2f", incomeSummary.getAverage())).append("\n");
        temp.append("Highest Income Made: ").append(incomeSummary.getMax()).append("\n");
        
        temp.append("|---------------------------------------------------------------------|\n");
        temp.append(String.format("| %-9s | %-15s | %-15s | %-15s |\n", "ID", "Name", "Appointments", "Income Made"));
        temp.append("|---------------------------------------------------------------------|\n");
        
        for (Map.Entry<String, DataDoctor> entry : data.entrySet())
        {
            String id = entry.getKey();
            String name = entry.getValue().name();
            int appointments = entry.getValue().appointment();
            double income = entry.getValue().income();
            
            String row = String.format("| %-9s | %-15s | %-15d | %-15.2f |\n", id, name, appointments, income);
            temp.append("|---------------------------------------------------------------------|\n");
            temp.append(row);
        }
        
        return temp.toString();
    }
    private String GenerateMonthlySection(List<String[]> appointment, List<String[]> invoice, int year)
    {
        MonthlyReportData dataset = GetMonthlyDetail(appointment, invoice, year);
        Map<String, DataMonth> data = dataset.monthlyData;
        IntSummaryStatistics apptSummary = dataset.appointmentSummary;
        DoubleSummaryStatistics incomeSummary = dataset.incomeSummary;
        
        StringBuilder temp = new StringBuilder();
        temp.append(
                """
                ------------------------------------------------
                Month
                ------------------------------------------------
                """); 
        
        temp.append("Total Appointment Count: ").append(apptSummary.getSum()).append("\n");
        temp.append("Lowest Appointment Completed: ").append(apptSummary.getMin()).append("\n");
        temp.append("Average Appointment Completed: ").append(String.format("%.2f", incomeSummary.getAverage())).append("\n");
        temp.append("Highest Appointment Completed: ").append(apptSummary.getMax()).append("\n");
        
        temp.append("Total Income: ").append(incomeSummary.getSum()).append("\n");
        temp.append("Lowest Income: ").append(incomeSummary.getMin()).append("\n");
        temp.append("Average Income: ").append(String.format("%.2f", incomeSummary.getAverage())).append("\n");
        temp.append("Highest Income: ").append(incomeSummary.getMax()).append("\n");
        
        temp.append("|---------------------------------------------------------|\n");
        temp.append(String.format("| %-15s | %-15s | %-15s |\n", "Month", "Appointments", "Income Made"));
        temp.append("|---------------------------------------------------------|\n");
        
        for (Map.Entry<String, DataMonth> entry : data.entrySet())
        {
            String month = entry.getKey();
            int appointments = entry.getValue().appointment();
            double income = entry.getValue().income();
            
            temp.append(String.format("| %-15s | %-15d | %-15.2f |\n", month, appointments, income));
            temp.append("|---------------------------------------------------------|\n");
        }
        
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
                workforce[1], 
                workforce[2], 
                workforce[3], 
                workforce[4],
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