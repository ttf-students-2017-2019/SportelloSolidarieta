package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import schedule.ObservableDailyPlan;
import model.Appointment;
import model.Settings;

public class ScheduleController {
	
	// Page elements
	 @FXML
	    private Button shedule_ok_button;

	    @FXML
	    private Button shedule_back_button;
	
    @FXML
    void toRegistry(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.Registry);
    }
    
    // To do
    @FXML
    void saveAppointment(ActionEvent event) 
    {
    	 DateTimeFormatter italianFormatter = DateTimeFormatter.ofLocalizedDate(
    		        FormatStyle.MEDIUM).withLocale(Locale.ITALIAN);
    	 LocalDate day = LocalDate.parse("04.03.2019", italianFormatter);
    	 new ObservableDailyPlan(day);
    		    
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
    
    // Interface to callback the main class
    private MainCallback interfaceMain;	
}
