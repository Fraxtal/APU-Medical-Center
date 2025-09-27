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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
/**
 *
 * @author weiha
 */
public class ReportGenerator extends ManagerModel {
    
    public interface HasAppointmentAndIncome {
        double income();
        int appointment();
        String label(String key);
        String group();
    }
    
    public record DataDoctor(
        String name,
        int appointment,
        double income) implements HasAppointmentAndIncome
    {
        @Override
        public String label(String key){
            return name;
        }

        @Override
        public String group() {
            return "Doctor";
        }
    }
    
    public record DataMonth(
        int appointment,
        double income) implements HasAppointmentAndIncome
    {
        @Override
        public String label(String key){
            return key;
        }
        
        @Override
        public String group() {
            return "Month";
        }
    }
    
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
    
    private enum Alignment{Left, Right, Center};
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    private final DateTimeFormatter readFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    //--Getting Data--
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
                .collect(Collectors.groupingBy(
                        row -> row[2].trim().toLowerCase(),
                        Collectors.summingInt(counts -> 1)
                ));
        
        return apptCount;
    }
    
    public int[] GetWorkForce(List<String[]> user)
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
    
    //--TXT Report Helper--
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
        
        temp.append("Total Appointment Completed: ").append(apptSummary.getSum()).append("\n");
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
   
    //--TXT Report Generator--
    private String GetReportText(int year)
    {
        String textTemplates = 
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
            - Appointment Scheduled : %d
            - Appointment Pending : %d
            
            """;

        List<String[]> user = ReadFile("users");
        List<String[]> appointment = ReadFile("appointments");
        List<String[]> invoice = ReadFile("invoices");
        
        int[] workforce = GetWorkForce(user);
        Map<String, Integer> counts = GetAppointmentCount(year, appointment);
        
        return String.format(textTemplates, 
                "Analysed Report", 
                workforce[0], 
                workforce[1], 
                workforce[2], 
                workforce[3],
                counts.getOrDefault("completed", 0),
                counts.getOrDefault("cancelled", 0),
                counts.getOrDefault("scheduled", 0),
                counts.getOrDefault("pending", 0))+
                GenerateDoctorSection(user, appointment, invoice, year)+
                GenerateMonthlySection(appointment, invoice, year);
    }
    
    //--PDF Helpers--
    
    private float Separator(PDPageContentStream cs, float margin, float y) throws Exception
    {
        y -= 20;
        cs.moveTo(margin, y); 
        cs.lineTo(550, y); 
        cs.stroke();
        return y - 30;
    }
    
    private float PrintLine(PDPageContentStream cs, PDFont font, float size,
                           float x, float y, String text) throws Exception {
        cs.beginText();
        cs.setFont(font, size);
        cs.newLineAtOffset(x, y);
        cs.showText(text == null ? "" : text);
        cs.endText();
        return y;
    }
    
    private float PrintLine(PDPageContentStream cs, PDFont font, float size,
                           float pageWidth, float margin, float y, String text, Alignment align) throws Exception
    {
        float lineStart = margin + switch (align) 
        {
            case Alignment.Left -> 0;
            case Alignment.Right -> pageWidth - (font.getStringWidth(text) / 1000 * size);
            case Alignment.Center -> (pageWidth - (font.getStringWidth(text) / 1000 * size))/2;
        };
        cs.beginText();
        cs.setFont(font, size);  
        cs.newLineAtOffset(lineStart, y);
        cs.showText(text == null ? "" : text);
        cs.endText();
        return y;
    }
    
    private float PrintPairs(PDPageContentStream cs, PDFont bold, PDFont normal, float size,
                           float margin, float y, String key, String value) throws Exception
    {
        PrintLine(cs, bold, size, margin, y, key);
        PrintLine(cs, normal, size, margin + 200, y, value);
        return y;
    }
    
    private float PrintWrapped(PDPageContentStream cs, PDFont font, float size,
                               float x, float y, String text,
                               float maxWidth, float lineHeight) throws Exception 
    {
        if (text == null || text.isEmpty()) return y;
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            String testLine = line + (line.isEmpty() ? "" : " ") + word;
            float w = font.getStringWidth(testLine) / 1000 * size;
            if (w > maxWidth) {
                PrintLine(cs, font, size, x, y, line.toString());
                y -= lineHeight;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(testLine);
            }
        }
        if (!line.isEmpty()) {
            PrintLine(cs, font, size, x, y, line.toString());
            y -= lineHeight;
        }
        return y;
    }
    
    private void PrintFooter(PDPageContentStream cs, PDFont font, float pageWidth, float margin, int pageCounter) throws Exception
    {
        PrintLine(cs, font, 8, pageWidth, margin, 50, String.valueOf(pageCounter), Alignment.Center);
        PrintLine(cs, font, 8, pageWidth, margin, 50, "APU Medical Center", Alignment.Right);
        PrintLine(cs, font, 8, pageWidth, margin, 50, "Computer-generated", Alignment.Left);
    }
    
    private List<List<String>> SeparateToPages(List<String> sortedKeys)
    {
        List<List<String>> keyByPage = new ArrayList<>();
        for (int i = 0; i < sortedKeys.size(); i++)
        {
            int pageNum = Math.floorDiv(i, 10);
            if (keyByPage.size() <= pageNum) {
                keyByPage.add(new ArrayList<>());
            }
            keyByPage.get(pageNum).add(sortedKeys.get(i));
        }
        return keyByPage;
    }
    
    private float PrintSummary(PDPageContentStream cs, PDFont bold, PDFont normal,
                           float margin, float y,
                           IntSummaryStatistics apptSummary,
                           DoubleSummaryStatistics incomeSummary,
                           String group) throws Exception
    {
        y = Separator(cs, margin, y);
            
        //Summarized data of doctor
        y = PrintLine(cs, bold, 12, margin, y, "Appointment and Income Summarized by "+group);
        y = PrintPairs(cs, bold, normal, 10, margin, y - 30, "Total Appointment Completed: ", String.valueOf(apptSummary.getSum()));
        y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Lowest Appointment Completed: ", String.valueOf(apptSummary.getMin()));
        y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Average Appointment Completed: ", String.format("%.2f", apptSummary.getAverage()));
        y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Highest Appointment Completed: ", String.valueOf(apptSummary.getMax()));

        y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Total Income:", "RM"+String.format("%.2f", incomeSummary.getSum()));
        y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Lowest Income Made:", "RM"+String.format("%.2f", incomeSummary.getMin()));
        y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Average Income Made:", "RM"+String.format("%.2f", incomeSummary.getAverage()));
        y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Highest Income Made:", "RM"+String.format("%.2f", incomeSummary.getMax()));

        return y;
    }
    
    private int PrintSection(PDDocument document, PDFont bold, PDFont normal, 
            List<String> sortedKeys,
            Map<String, ? extends HasAppointmentAndIncome> data,
            int pageCounter) throws Exception
    {
        List<List<String>> keyByPage = SeparateToPages(sortedKeys);
        
        String group = data.values().iterator().next().group();
        
        for (List<String> row : keyByPage)
        {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(document, page)) 
            {
                float startX = 150, startY = 700, barHeight = 20, unitHeight = 45, gap = 15, y = 750, margin = 50;
                float pageWidth = page.getMediaBox().getWidth() - 2 * margin;

                PrintLine(cs, bold, 14, pageWidth, margin, y, "Appointment and Income by " + group, Alignment.Center);
                
                float chartHeight = row.size() * (unitHeight + gap) + 10;
                cs.moveTo(startX + 400, startY - chartHeight);
                cs.lineTo(startX, startY - chartHeight);
                cs.lineTo(startX, startY);
                cs.stroke();

                for (int i = 0; i < row.size(); i++) {
                    float yloc = startY - (i + 1) * (unitHeight + gap);
                    
                    var item = data.get(row.get(i));

                    float income = (float) item.income();
                    float incomeWidth = income / 2;
                    float incomeLabelX = startX + 5 + incomeWidth;
                    float incomeLabelY = yloc + 5;

                    cs.addRect(startX, yloc, incomeWidth, barHeight);
                    cs.fill();
                    PrintLine(cs, normal, 8, incomeLabelX, incomeLabelY, "RM" + String.format("%.2f", income) + " Income");

                    int appt = item.appointment();
                    float apptWidth = appt * 10;
                    float apptLabelX = startX + 5 + apptWidth;
                    float apptLabelY = yloc + 30;

                    cs.addRect(startX, yloc + 25, apptWidth, barHeight);
                    cs.fill();
                    PrintLine(cs, normal, 8, apptLabelX, apptLabelY, appt + " Appointments");
                    
                    PrintWrapped(cs, normal, 8, startX - 100, yloc + 25, item.label(row.get(i)), 80, 15);
                }
                PrintFooter(cs, normal, pageWidth, margin, pageCounter);
                pageCounter++;
            }  
        }
        return pageCounter;
    }
    
    //--PDF Report Generator--
    private void GetReportText(PDDocument document, int year) throws Exception
    {
        PDPage page = new PDPage();
        document.addPage(page);
        //initialize fonts, pointers and data
        PDFont bold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDFont normal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        
        int pageCounter = 1;

        List<String[]> user = ReadFile("users");
        List<String[]> appointment = ReadFile("appointments");
        List<String[]> invoice = ReadFile("invoices");
        
        DoctorReportData datasetD = GetDoctorDetail(user, appointment, invoice, year);
        Map<String, DataDoctor> dataD = datasetD.doctorData();
        IntSummaryStatistics apptSummaryD = datasetD.appointmentSummary();
        DoubleSummaryStatistics incomeSummaryD = datasetD.incomeSummary();
        
        MonthlyReportData datasetM = GetMonthlyDetail(appointment, invoice, year);
        Map<String, DataMonth> dataM = datasetM.monthlyData();
        IntSummaryStatistics apptSummaryM = datasetM.appointmentSummary();
        DoubleSummaryStatistics incomeSummaryM = datasetM.incomeSummary();

        try (PDPageContentStream cs = new PDPageContentStream(document, page))
        {
            float y = 750, margin = 50;
            float pageWidth = page.getMediaBox().getWidth() - 2 * margin;
            //Header
            PrintLine(cs, bold, 16, pageWidth, margin, y, "Annual Report", Alignment.Center);
            y = PrintLine(cs, normal, 8, pageWidth, margin, y - 20, "APU Medical Centre", Alignment.Center);
            y = PrintLine(cs, normal, 8, pageWidth, margin, y - 30, "Generated by: " + ManagerModel.GetCurrentDateTime(), Alignment.Right);
            
            y = Separator(cs, margin, y);
            
            //Staff Structure
            int[] workForce = GetWorkForce(user);
            y = PrintLine(cs, bold, 12, margin, y, "Staff Structure");
            y = PrintPairs(cs, bold, normal, 10, margin, y - 30, "Number of Managers: ", String.valueOf(workForce[0]));
            y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Number of Staffs: ", String.valueOf(workForce[0]));
            y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Number of Doctors: ", String.valueOf(workForce[0]));
            y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Total Workforce: ", String.valueOf(workForce[0]));
            
            y = Separator(cs, margin, y);
            
            //Appointments Completed and Cancelled
            Map<String, Integer> counts = GetAppointmentCount(year, appointment);
            y = PrintLine(cs, bold, 12, margin, y, "Appointments Status");
            y = PrintPairs(cs, bold, normal, 10, margin, y - 30, "Appointments Completed: ", counts.getOrDefault("completed", 0).toString());
            y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Appointments Cancelled: ", counts.getOrDefault("cancelled", 0).toString());
            y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Appointments Scheduled: ", counts.getOrDefault("scheduled", 0).toString());
            y = PrintPairs(cs, bold, normal, 10, margin, y - 15, "Appointments Pending: ", counts.getOrDefault("pending", 0).toString());
            
            y = PrintSummary(cs, bold, normal, margin, y, apptSummaryD, incomeSummaryD, "Doctor");
            PrintSummary(cs, bold, normal, margin, y, apptSummaryM, incomeSummaryM, "Month");
            
            PrintFooter(cs, normal, pageWidth, margin, pageCounter);
            pageCounter++;
        }
        
        //--Doctor Section--
        List<String> sortedKeys = dataD.keySet().stream().sorted().toList();
        pageCounter = PrintSection(document, bold, normal, sortedKeys, dataD, pageCounter);
        
        //--Monthly Section--
        sortedKeys = dataM.keySet().stream().sorted(Comparator.comparing(entry -> YearMonth.parse(entry, formatter))).toList();
        PrintSection(document, bold, normal, sortedKeys, dataM, pageCounter);
    }
    
    public boolean GenerateReport(File file, int year) throws Exception
    {
        String filePath = file.getAbsolutePath().toLowerCase();
        if (filePath.endsWith(".txt")) 
        {
            try (FileWriter fileWriter = new FileWriter(file)) 
            {
                fileWriter.write(GetReportText(year));
            }
            return true;
        }
        else if (filePath.endsWith(".pdf"))
        {
            try (PDDocument document = new PDDocument())
            {
               GetReportText(document, year); 
               document.save(file); 
            }
            return true;
        }
        return false;
    }
    
   
}