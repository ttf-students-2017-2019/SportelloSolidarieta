package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import schedule.ObservableDailyPlan;
import model.Appointment;
import model.Settings;

public class ScheduleController {
	
    // Interface to callback the main class
    private MainCallback interfaceMain;	
    
    // Page elements
    @FXML
    private Button shedule_ok_button;

    @FXML
    private Button shedule_back_button;
	   
    @FXML
    void toAssistedDetail(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.AssistedDetail);
    }
    
    @FXML
    private void initialize() {
    	// Loading the 
    	Date date = new GregorianCalendar(2019, Calendar.APRIL, 25).getTime();
    	List<Appointment> dailyAppointments = Appointment.findAppointmentsByDate(date);
    	
    	for (Appointment app :dailyAppointments) {
    		System.out.println(app.toString());
    	}
    	
	}
    
    // To do
    @FXML
    void saveAppointment(ActionEvent event) 
    {
    	 
    		    
    }
    
    @FXML
    void showSettings(ActionEvent event) 
    {
    
    	System.out.println(Settings.findAllSettings().toString());
    	Appointment app = Appointment.findAllAppointments().get(0);
    	System.out.println(app.toString());
    }	
	
   
    
	//
    // Instance constructor
	//
	// parameters	
	//		interfaceMain 		interface to callback the main class
	//
	// returned
	//		none
	//
    
    public ScheduleController(MainCallback interfaceMain)
    {
    	this.interfaceMain = interfaceMain;   
    } 
    
}
