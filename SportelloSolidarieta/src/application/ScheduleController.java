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
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import model.Appointment;
import model.Assisted;
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
    private Label idAssistedNameSurname;
    
    @FXML
    private DatePicker idDatePicker;

    @FXML
    private Button shedule_ok_button;

    @FXML
    private Button shedule_back_button;
    
    @FXML
    private Label idFullDay;
    
    @FXML
    private Label idAppointmentNumber;

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
    	
    	// Disable datePicker for the current day and for the past
    	final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
  	      @Override
  	      public DateCell call(final DatePicker datePicker) {
  	        return new DateCell() {
  	          @Override
  	          public void updateItem(LocalDate item, boolean empty) {
  	            super.updateItem(item, empty);

  	            if (item.isBefore(LocalDate.now().plusDays(1))) {
  	              setDisable(true);
  	            }
  	          }
  	        };
  	      }
  	    };
  	    
  	    idDatePicker.setDayCellFactory(dayCellFactory);
    	
    	// Getting the Assisted and setting the labels
    	Assisted assisted = new Assisted();
		assisted = assisted.getSampleAssisted();
		
		idAssistedNameSurname.setText(assisted.getNome() + " " + assisted.getCognome());
		
		// Getting the next month defaultAppointmentDay
    	Date defaultDay = getNextWeekDefaultAppointmentDay();
    	
    	// Setting that day in the datePicker
    	LocalDate calendarDate = defaultDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	idDatePicker.setValue(calendarDate);
    	
    	// Logging 
    	System.out.println(defaultDay.toString());
    	
    	DailyPlan defaultDailyplan = new DailyPlan(defaultDay, settings);
       	
    	Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(defaultDay);
		
    	// Only if the appointments of the day are less than the default maximum display the dailyPlan
    	while (defaultDailyplan.getNumberOfAppointments() >= Settings.findAllSettings().getMaxDailyAppointments())
    	{
    		currentDate.add(Calendar.DATE, SKIP_DAYS);
    		defaultDailyplan = new DailyPlan(Date.from(currentDate.toInstant()), settings);
    	} 
    	
    	ObservableList<ObservableSlot> slotObservableList = FXCollections.<ObservableSlot>observableArrayList();

    	slotObservableList.addAll(defaultDailyplan.getDailyPlan());
		
    	idColumnTime.setCellValueFactory(cellData -> cellData.getValue().appointmentTimeDate);
    	idColumnLength.setCellValueFactory(cellData -> cellData.getValue().appointmentLength);
    	idColumnStatus.setCellValueFactory(cellData -> cellData.getValue().status);
    	idColumAssisted.setCellValueFactory(cellData -> cellData.getValue().assistedOwner);
    	idTableView.setItems(slotObservableList);
    	idTableView.getSelectionModel().select(defaultDailyplan.getFirstFreeSlot());
    	idTableView.requestFocus();
    	// Setting up the labels
    	idAppointmentNumber.setText(defaultDailyplan.getNumberOfAppointments() + "");
    	idFullDay.setText(Date.from(currentDate.toInstant()).toString());
    	updateDatePicker(Date.from(currentDate.toInstant()));
	}
    
    // To do
    @FXML
    void saveAppointment(ActionEvent event) 
    {
    	ObservableSlot selectedSlot = idTableView.getSelectionModel().getSelectedItem(); 
    	
    	// Only if the selected slot is free save the appointment to the database
    	if (selectedSlot != null && selectedSlot.getAssociatedSlot().getAppointmentAssistedOwner() == null) 
    	{
    		Assisted sampleAssisted = new Assisted();
    		sampleAssisted = sampleAssisted.getSampleAssisted();
    		
    		System.out.println(sampleAssisted.toString());
			Date appointmentDateTime =  new Date().from(selectedSlot.getAssociatedSlot().getDateTime().toInstant());
    		// Saving appointment
			Appointment appToSave = new Appointment();		
			appToSave.saveAppointment(sampleAssisted, appointmentDateTime, selectedSlot.getAssociatedSlot().getAppointmentLength()); 
			
    	}
    	else if (selectedSlot != null && 
    				selectedSlot.getAssociatedSlot().getAppointmentAssistedOwner() != null) // Slot already taken
    	{
    		// To do warning
    	}
    	else // Nothing selected 
    	{
    		// To do warning
    	}
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
    	// Getting the datePickerDate minus one week
    	LocalDate datePickerLocalDate = idDatePicker.getValue();
    	Calendar cal = Calendar.getInstance();
    	Date datePickerDate = Date.from(datePickerLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    	cal.setTime(datePickerDate);
    	cal.add(Calendar.DATE, - SKIP_DAYS);
    	
    	// Prevent moving to the past
    	if (cal.after(Calendar.getInstance())) 
    	{
        	Date dateToGo = cal.getTime();
        	updateDailyPlan(dateToGo);
        	updateDatePicker(dateToGo);
    	}
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
    	
    	// Updating the dailyPlan with that day
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
    	
    	// Updating the dailyPlan displayed, preventing moving to the past
    	if (datePickerDate.after(Calendar.getInstance().getTime()))
    	{
    		updateDailyPlan(datePickerDate);
    	}	
    	else 
    	{
    		// Reverting the date picker to the current day plus one
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.DATE,1);		
    		updateDatePicker(cal.getTime());
    	}
    		
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
    	
    	// Selecting the first free slot if there is one
    	if (currentDailyplan.getFirstFreeSlot()!= null) 
    	{
    		idTableView.getSelectionModel().select(currentDailyplan.getFirstFreeSlot());
    	}
    	
    	// Setting the labels
    	updateDatePicker(date);
    	idAppointmentNumber.setText(currentDailyplan.getNumberOfAppointments() + "");
    	idFullDay.setText(date.toString());
	}
	
	// Update the datePicker   
	public void updateDatePicker(Date date) 
	{
    	LocalDate calendarDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	idDatePicker.setValue(calendarDate);
	}
}