package windows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import application.MainCallback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import model.Appointment;
import model.Assisted;
import schedule.DailyPlan;
import schedule.ObservableSlot;
import utilities.Formatter;


public class CalendarController {
	
	// Interface to callback the main class
	private MainCallback interfaceMain;	

	public static final int SKIP_DAYS = 7;
	
    @FXML
    private DatePicker idDatePicker;

    @FXML
    private Button calendar_delete_button;

    @FXML
    private Button shedule_back_button;

    @FXML
    private Label idFullDay;

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
    private Label idAppointmentNumber;
    
    @FXML
    void toSearchPage(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Page.SEARCH_ASSISTED, null);	
    }
    
	@FXML
	private void initialize() {
		
    	// set table placeholder to blank
    	
		try 
		{
			// Getting today date
			Calendar todayCal = Calendar.getInstance();
			Date defaultDay = getDateForDailyPlan(Date.from(todayCal.toInstant()));
			DailyPlan defaultDailyplan = new DailyPlan(defaultDay);
			
			// Update datePicker
			updateDatePicker(defaultDay);
			

			Label label = new Label("Nessun appuntemento in giornata");
			idTableView.setPlaceholder(label);

			// Binding the defaultDailyPlan to the TableView
			ObservableList<ObservableSlot> slotObservableList = FXCollections.<ObservableSlot>observableArrayList();
			slotObservableList.addAll(defaultDailyplan.getDailyPlan());
			bind(slotObservableList);
			
			// Setting up the labels
			idAppointmentNumber.setText(String.valueOf(defaultDailyplan.getNumberOfAppointments()));
			
			Calendar defaultDayCalendar = Calendar.getInstance();
			defaultDayCalendar.setTime(defaultDay);
			idFullDay.setText(Formatter.getDateAsItalianString(defaultDayCalendar));
			
		} 
		catch (Exception e) 
		{
			showAlertDatabaseErrorToMainPage();
		}
	}
	
    @FXML
    void deleteAppointment(ActionEvent event) {
    	
    	ObservableSlot selectedSlot = idTableView.getSelectionModel().getSelectedItem(); 
		
		// Only with a selected appointment try to delete it
		if (selectedSlot != null) 
		{
			showConfirmationDialog(selectedSlot);
		}
		else // There is no selection
		{
			showAlertNoSelection();
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
		try 
		{
			Date dateToGo = cal.getTime();
			updateDailyPlan(dateToGo);
			updateDatePicker(dateToGo);
		} 
		catch (Exception e) 
		{
			showAlertDatabaseErrorToMainPage();
		}
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

		try 
		{
			Date dateToGo = cal.getTime();
			updateDailyPlan(dateToGo);
			updateDatePicker(dateToGo);
		} 
		catch (Exception e) 
		{
			showAlertDatabaseErrorToMainPage();
		}
	}

    @FXML
	void updateDailyPlanFromDatePicker(ActionEvent event) 
	{
		// Getting the datePickerDate
		LocalDate datePickerLocalDate = idDatePicker.getValue();
		Date datePickerDate = Date.from(datePickerLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

		try 
		{
			updateDailyPlan(datePickerDate);
		} 
		catch (Exception e) 
		{
			showAlertDatabaseErrorToMainPage();
		}
	}
    
	// Other methods
	public CalendarController(MainCallback interfaceMain)
	{
		this.interfaceMain = interfaceMain;   
	}

	// Setting a date for the db query 
	public Date getDateForDailyPlan(Date date) 
	{
		// Getting current date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
	
		// Setting time to 00:00:00
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);	

		//Return the date
		return cal.getTime();
	}
	
	// Alerts
	private void showAlertNoSelection() {

		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Attenzione");
		alert.setHeaderText("Nessuna selezione");
		alert.setContentText("Selezionare l'appuntamento da cancellare");
		alert.showAndWait();
	}
	
	private void showConfirmationDialog(ObservableSlot selectedSlot) 
	{
		// Creating custom button
		ButtonType yesButton = new ButtonType("S�",ButtonData.OK_DONE);
		ButtonType noButton = new ButtonType("No",ButtonData.CANCEL_CLOSE);
		
		Alert alert = new Alert(AlertType.CONFIRMATION, null, yesButton, noButton);
		alert.setTitle("Avviso di conferma");
		alert.setHeaderText("Conferma la tua scelta");
		alert.setContentText("Sicuro di voler cancellare l'appuntamento selezionato?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.orElse(noButton) == yesButton)
		{
			Appointment appointmentToDelete = selectedSlot.getAssociatedSlot().getAssocieatedAppointment();
			
			// Check for errors
			if (Appointment.deleteAppointment(appointmentToDelete)) 
			{
				Date date = getDateForDailyPlan(appointmentToDelete.getAppointmentDateTime());
				updateDailyPlan(date);
			} 
			else
				showAlertDatabaseErrorToMainPage(); 
	    } 
		else 
		{
			// Do nothing
		}
		
	}
	
	private void showAlertDatabaseErrorToMainPage() 
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Messaggio di errore");
		alert.setHeaderText("Errore di connessione al database");
		alert.setContentText("Riprovare pi� tardi");
		alert.setOnCloseRequest(new EventHandler<DialogEvent>() {

			@Override
			public void handle(DialogEvent event) {
				interfaceMain.switchScene(MainCallback.Page.SEARCH_ASSISTED, null);
			}

		});
		alert.showAndWait();
	}
	
	// Bind the dailyPlan to TableView   
	private void bind(ObservableList<ObservableSlot>  observalbeList) 
	{    	
		idColumnTime.setCellValueFactory(cellData -> cellData.getValue().appointmentTimeDate);
		idColumnLength.setCellValueFactory(cellData -> cellData.getValue().appointmentLength);
		idColumnStatus.setCellValueFactory(cellData -> cellData.getValue().status);
		idColumAssisted.setCellValueFactory(cellData -> cellData.getValue().assistedOwner);
		idTableView.setItems(observalbeList);
	}
	
	// Update the dailyPan displayed
	public void updateDailyPlan(Date date) 
	{	
		// Getting the new dailyPlan and updating the TableView
		DailyPlan currentDailyplan = new DailyPlan(date);
	
		ObservableList<ObservableSlot> slotObservableList = FXCollections.<ObservableSlot>observableArrayList();
		slotObservableList.addAll(currentDailyplan.getDailyPlan());
		idTableView.setItems(slotObservableList);
			
		// Setting the labels
		updateDatePicker(date);
		idAppointmentNumber.setText(String.valueOf(currentDailyplan.getNumberOfAppointments()));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		idFullDay.setText(Formatter.getDateAsItalianString(cal));
	}
	
	// Update the datePicker   
	public void updateDatePicker(Date date) 
	{
		LocalDate calendarDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		idDatePicker.setValue(calendarDate);
	}

}

