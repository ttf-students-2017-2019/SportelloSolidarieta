package application;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.mysql.cj.jdbc.SuspendableXAConnection;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewFocusModel;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.VirtualFlow;
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
    	    	
    	DailyPlan defaultDailyplan = new DailyPlan(defaultDay, settings);
       	
    	Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(defaultDay);
		
    	// Only if the appointments of the day are less than the default maximum display the dailyPlan
    	while (defaultDailyplan.getNumberOfAppointments() >= Settings.findAllSettings().getMaxDailyAppointments())
    	{
    		currentDate.add(Calendar.DATE, SKIP_DAYS);
    		defaultDailyplan = new DailyPlan(Date.from(currentDate.toInstant()), settings);
    	} 
    	
    	// Update datePicker
    	updateDatePicker(Date.from(currentDate.toInstant()));
    	
    	// Binding the defaultDailyPlan to the TableView
    	ObservableList<ObservableSlot> slotObservableList = FXCollections.<ObservableSlot>observableArrayList();
    	slotObservableList.addAll(defaultDailyplan.getDailyPlan());
    	bindAndSelectFirstFreeSlot(slotObservableList, defaultDailyplan);

    	// Setting up the labels
    	idAppointmentNumber.setText(String.valueOf(defaultDailyplan.getNumberOfAppointments()));
    	idFullDay.setText(getDateAsItalianString(currentDate));
	}
    
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
			showAlertWithSuccessfulHeaderText(sampleAssisted, appointmentDateTime, selectedSlot.getAssociatedSlot().getAppointmentLength());
    	}
    	else if (selectedSlot != null && 
    				selectedSlot.getAssociatedSlot().getAppointmentAssistedOwner() != null) // Slot already taken
    	{
    		showAlertAppointmentTaken(selectedSlot);
    	}
    }
    
    private void showAlertAppointmentTaken(ObservableSlot selectedSlot) {
    	
    	Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Attenzione");
        alert.setHeaderText("Appuntamento non disponibile");
        alert.setContentText("Selezione del primo appuntamento disponibile della giornata");
        alert.setOnCloseRequest(new EventHandler<DialogEvent>() {

			@Override
			public void handle(DialogEvent event) {
				
				// Update the dailyPlan selecting the first freeSlot 
				updateDailyPlan(selectedSlot.getAssociatedSlot().getDateTime().getTime());
			}
        	
		});
        alert.showAndWait();
    }
    
    private void showAlertWithSuccessfulHeaderText(Assisted assisted, Date appointmentDateTime, int appointmentLength) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(appointmentDateTime);
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Messaggio di conferma");
        alert.setHeaderText("Appuntamento salvato correttamente");
        alert.setContentText(getAlertMessage(assisted, cal));
        alert.setHeight(300);
        alert.setOnCloseRequest(new EventHandler<DialogEvent>() {

			@Override
			public void handle(DialogEvent event) {
				interfaceMain.switchScene(MainCallback.Pages.Registry);
			}
        	
		});
        alert.showAndWait();
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
    	else // Reverting the date picker to the current day plus one if we try to move to the past 
    	{	
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.DATE,1);		
    		updateDatePicker(cal.getTime());
    	}
    		
    }
    
    // Bind the dailyPlan to TableView   
    private void bindAndSelectFirstFreeSlot(ObservableList<ObservableSlot>  observalbeList, DailyPlan dailyPlan) 
    {    	
    	idColumnTime.setCellValueFactory(cellData -> cellData.getValue().appointmentTimeDate);
    	idColumnLength.setCellValueFactory(cellData -> cellData.getValue().appointmentLength);
    	idColumnStatus.setCellValueFactory(cellData -> cellData.getValue().status);
    	idColumAssisted.setCellValueFactory(cellData -> cellData.getValue().assistedOwner);
    	idTableView.setItems(observalbeList);
    	
    	// Selecting the first free slot of the day
    	if(dailyPlan.getFirstFreeSlot() != null) 
    	{
        	idTableView.getSelectionModel().select(dailyPlan.getFirstFreeSlot());
        	idTableView.scrollTo(observalbeList.indexOf(dailyPlan.getFirstFreeSlot()));
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
    		idTableView.scrollTo(slotObservableList.indexOf(currentDailyplan.getFirstFreeSlot()));
    	}
    	
    	// Setting the labels
    	updateDatePicker(date);
    	idAppointmentNumber.setText(currentDailyplan.getNumberOfAppointments() + "");
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	idFullDay.setText(getDateAsItalianString(cal));
	}
	
	// Update the datePicker   
	public void updateDatePicker(Date date) 
	{
    	LocalDate calendarDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	idDatePicker.setValue(calendarDate);
	}
	
	// Get lowerCase day of the week
	private String getDayOfWeek(int dayOfWeek)
	{	
		String dayString = "";
		switch (dayOfWeek) {
		case Calendar.MONDAY:
			dayString = "lunedì";
			break;
		case Calendar.TUESDAY:
			dayString = "martedì";
			break;
		case Calendar.WEDNESDAY:
			dayString = "mercoledì";
			break;
		case Calendar.THURSDAY:
			dayString = "giovedì";
			break;
		case Calendar.FRIDAY:
			dayString = "venerdì";
			break;
		case Calendar.SATURDAY:
			dayString = "sabato";
			break;
		case Calendar.SUNDAY:
			dayString = "domenica";
			break;
		}
		return dayString;
	}
	
	// Get CamelCase day of the week
	private String getCamelCaseDayOfWeek(int dayOfWeek)
	{	
		String dayString = "";
		switch (dayOfWeek) {
		case Calendar.MONDAY:
			dayString = "Lunedì";
			break;
		case Calendar.TUESDAY:
			dayString = "Martedì";
			break;
		case Calendar.WEDNESDAY:
			dayString = "Mercoledì";
			break;
		case Calendar.THURSDAY:
			dayString = "Giovedì";
			break;
		case Calendar.FRIDAY:
			dayString = "Venerdì";
			break;
		case Calendar.SATURDAY:
			dayString = "Sabato";
			break;
		case Calendar.SUNDAY:
			dayString = "Domenica";
			break;
		}
		return dayString;
	}
	
	private String getAlertMessage(Assisted assisted, Calendar cal)
	{
        return "Appuntamento assegnato a " + assisted.getNome() + " " + assisted.getCognome() + " il " + 
        			+ cal.get(Calendar.DAY_OF_MONTH) +"/" + cal.get(Calendar.MONTH) +
        				"/" + cal.get(Calendar.YEAR) + " (" +getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK))  + ") alle ore " + cal.get(Calendar.HOUR_OF_DAY)+ ":" +
        					cal.get(Calendar.MINUTE);
	}
	
	private String getDateAsItalianString(Calendar cal)
	{
        return getCamelCaseDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)) + " " + cal.get(Calendar.DAY_OF_MONTH) +"/" + cal.get(Calendar.MONTH) +
			"/" + cal.get(Calendar.YEAR);
	}
}