package application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Appointment;
import model.Settings;
import schedule.DailyPlan;
import schedule.Slot;
import schedule.ObservableSlot;

public class ScheduleController {
	
    // Interface to callback the main class
    private MainCallback interfaceMain;	
    
    // Page elements
    @FXML
    private GridPane idAssistedNameLabel;

    @FXML
    private DatePicker idDatePicker;

    @FXML
    private TextField idStartTimeTextField;

    @FXML
    private TextField idEndTimeTextField;

    @FXML
    private Button shedule_ok_button;

    @FXML
    private Button shedule_back_button;

    @FXML
    private TableView<ObservableSlot> idTableView;

    @FXML
    private TableColumn<ObservableSlot, String> idColumnTime;

    @FXML
    private TableColumn<ObservableSlot, String> idColumnStatus;

    @FXML
    private TableColumn<ObservableSlot, String> idColumAssisted;

    @FXML
    private Button idPreviousWeekButton;

    @FXML
    private Button idNextWeekButton;
	   
    @FXML
    void toAssistedDetail(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.AssistedDetail);
    }
    
    @FXML
    private void initialize() {
    	
		// Getting the next month defaultAppointmentDay
    	Date defaultDayPlan = getNextMonthDefaultAppointmentDay();
    	
    	// Logging 
    	System.out.println(defaultDayPlan.toString());
    	
    	// Getting settings
    	Settings settings = Settings.findAllSettings();
    	DailyPlan defaultDailyplan = new DailyPlan(defaultDayPlan, settings);
       	
    	ObservableList<ObservableSlot> slotObservableList = FXCollections.<ObservableSlot>observableArrayList();

    	slotObservableList.addAll(defaultDailyplan.dailyPlan);
		
    	idColumnTime.setCellValueFactory(cellData -> cellData.getValue().appointmentTimeDate);
    	idColumnStatus.setCellValueFactory(cellData -> cellData.getValue().status);
    	idColumAssisted.setCellValueFactory(cellData -> cellData.getValue().assistedOwner);
    	idTableView.setItems(slotObservableList);
    		
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
	
    @FXML
    void checkTimeFormatting(ActionEvent event) {

    }

    @FXML
    void previousWeekDailyPlan(ActionEvent event) {

    }

    @FXML
    void updateDailyPlan(ActionEvent event) {
    	
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
    
    // Other methods
    public ScheduleController(MainCallback interfaceMain)
    {
    	this.interfaceMain = interfaceMain;   
    }
    
	// Get next month default day appointment
	public Date getNextMonthDefaultAppointmentDay () {
	    	
		// Getting the same day in the next month, selecting from that week the default weekday
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_WEEK, Settings.findDefaultWeekDay());
		
		// Setting time to 00:00:00
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);	
	    	
		//Return the date
		return cal.getTime();   	
	}
       
}
