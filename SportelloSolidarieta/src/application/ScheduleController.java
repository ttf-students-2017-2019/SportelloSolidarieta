package application;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Appointment;
import model.Settings;
import schedule.DailyPlan;
import schedule.ObservableSlot;

public class ScheduleController {
	
    // Interface to callback the main class
    private MainCallback interfaceMain;	
    
    // Settings loaded from the database
    private Settings settings = Settings.findAllSettings();
    
    public static final int SKIP_DAYS = 7;
    
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
    private TableColumn<ObservableSlot, String> idColumnLength;

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
    	Date defaultDay = getNextWeekDefaultAppointmentDay();
    	
    	// Setting that day in the datePicker
    	LocalDate calendarDate = defaultDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	idDatePicker.setValue(calendarDate);
    	
    	// Logging 
    	System.out.println(defaultDay.toString());
    	
    	DailyPlan defaultDailyplan = new DailyPlan(defaultDay, settings);
       	
    	ObservableList<ObservableSlot> slotObservableList = FXCollections.<ObservableSlot>observableArrayList();

    	slotObservableList.addAll(defaultDailyplan.getDailyPlan());
		
    	idColumnTime.setCellValueFactory(cellData -> cellData.getValue().appointmentTimeDate);
    	idColumnLength.setCellValueFactory(cellData -> cellData.getValue().appointmentLength);
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
    void previousWeekDailyPlan(ActionEvent event) 
    {
    	// Getting the datePickerDate and adding one week
    	LocalDate datePickerLocalDate = idDatePicker.getValue();
    	Calendar cal = Calendar.getInstance();
    	Date datePickerDate = Date.from(datePickerLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    	cal.setTime(datePickerDate);
    	cal.add(Calendar.DATE, - SKIP_DAYS);
    	Date dateToGo = cal.getTime();
    	
    	updateDailyPlan(dateToGo);
    	updateDatePicker(dateToGo);
    }

    @FXML
    void nextWeekDailyPlan(ActionEvent event) 
    {
       	// Getting the datePickerDate and adding one week
    	LocalDate datePickerLocalDate = idDatePicker.getValue();
    	Calendar cal = Calendar.getInstance();
    	Date datePickerDate = Date.from(datePickerLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    	cal.setTime(datePickerDate);
    	cal.add(Calendar.DATE, SKIP_DAYS);
    	Date dateToGo = cal.getTime();
    	
    	updateDailyPlan(dateToGo);
    	updateDatePicker(dateToGo);   	
    }
    
    @FXML
    void updateDailyPlanFromDatePicker(ActionEvent event) 
    {
    	// Getting the datePickerDate
    	LocalDate datePickerLocalDate = idDatePicker.getValue();
    	Date datePickerDate = Date.from(datePickerLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    	
    	// Logging 
    	System.out.println(datePickerDate.toString());
    	
    	// Updating the dailyPlan displayed
    	updateDailyPlan(datePickerDate);	
    }
   
    // Other methods
    public ScheduleController(MainCallback interfaceMain)
    {
    	this.interfaceMain = interfaceMain;   
    }
    
	// Get next month default day appointment
	public Date getNextWeekDefaultAppointmentDay () 
	{
	   	// Getting the same day in the next month, selecting from that week the default weekday
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.DATE, SKIP_DAYS);
		cal.set(Calendar.DAY_OF_WEEK, Settings.findDefaultWeekDay());
		
		// Setting time to 00:00:00
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);	
	    	
		//Return the date
		return cal.getTime();
	}
	
	// Update the dailyPan displayed
	public void updateDailyPlan(Date date) 
	{	
		// Getting the new dailyPlan and updating the TableView
    	DailyPlan currentDailyplan = new DailyPlan(date, settings);
    	ObservableList<ObservableSlot> slotObservableList = FXCollections.<ObservableSlot>observableArrayList();
    	slotObservableList.addAll(currentDailyplan.getDailyPlan());
    	idTableView.setItems(slotObservableList);
    	
    	updateDatePicker(date);
	}
	
	// Update the datePicker   
	public void updateDatePicker(Date date) 
	{
    	LocalDate calendarDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	idDatePicker.setValue(calendarDate);
	}
}