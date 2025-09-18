package Staff.service;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManageAppointments{
    private static final String appointmentsFile = "src/database/appointments.txt";
    
    public List<String[]> loadAppointments() {
        List<String[]> appointmentData = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(appointmentsFile))){
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] data = line.split(";");
                if (data.length >= 7) {
//                    String appointmentId = data[0];
//                    String appointmentDate = data[1];
//                    String status = data[2];
//                    String doctorId = data[3];
//                    String doctorName = data[4];
//                    String customerId = data[5];
//                    String customerName = data[6];
                    
                    appointmentData.add(data);
                }
                
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return appointmentData;
    }
    
    public List<String[]> loadPastAppointments() {
        return loadAppointments().stream().filter(data -> data[2].trim().equalsIgnoreCase("Completed")).toList();
    }
    
    public boolean saveAppointments(List<String> newData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(appointmentsFile))) {
            for (String appointment : newData) {
                bw.write(appointment);
                bw.newLine();   
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean addAppointment(String appointmentDate, String status, String doctorId, String doctorName, String customerId, String customerName) {
        String newAppId = newAppointmentId();
        String newAppointment = String.join(";", newAppId, appointmentDate, status, doctorId, doctorName, customerId, customerName);
        try (FileWriter fw = new FileWriter(appointmentsFile, true)) {
            fw.write(newAppointment + System.lineSeparator());
        }
        catch (IOException ioE) {
            System.out.println("Encountered an error while writing the appointments file");
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public boolean updateAppointment(String appointmentId, String appointmentDate, String status, String doctorId, String doctorName, String customerId, String customerName) {
        List<String> appointmentLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(appointmentsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] values = line.trim().split(";");
                if (values.length >= 7 && values[0].equalsIgnoreCase(appointmentId)) {
                    values[1] = appointmentDate;
                    values[2] = status;
                    values[3] = doctorId;
                    values[4] = doctorName;
                    values[5] = customerId;
                    values[6] = customerName;
                    line = String.join(";", values);
                    found = true;
                }
                appointmentLines.add(line);
            }
        }   catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (found) {
            return saveAppointments(appointmentLines);
        }
        return false;
    }
    
    public String newAppointmentId(){
        int currentMaxId = 0;
        try{
            List<String[]> appointments = loadAppointments();
            for (String[] appointment : appointments){
                if (appointment.length > 6){
                    String idString = appointment[0];
                    if (idString.startsWith("A")){
                        int id = Integer.parseInt(idString.substring(1));
                        currentMaxId = Math.max(currentMaxId, id);
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
        return String.format("A%04d", currentMaxId + 1);
    }
    
    public boolean checkAppointmentIdExists(String appId) {
        try {
            List<String[]> appointments = loadAppointments();
            for (String[] appointment : appointments) {
                if (appointment[0].equalsIgnoreCase(appId)) {
                    return true;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        
        return false;
    }
    
    public String returnCustomerIDfromAppId(String appointmentId){
        List<String[]> appointments = loadAppointments(); 
        for (String[] appointment : appointments) {
            if (appointment[0].equalsIgnoreCase(appointmentId)) {
                return appointment[5];
            }
        }
        return null;
        
    }
}
