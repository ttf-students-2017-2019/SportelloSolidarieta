package windows;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import application.MainCallback;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import model.Appointment;
import model.Assisted;
import model.Setting;

import schedule.DailyPlan;
import schedule.ObservableSlot;

import utilities.Formatter;

public class ScheduleController {

	// Interface to callback the main class
	private MainCallback interfaceMain;	

	// Settings loaded from the database
	private Setting settings; 
	
	private Assisted selectedAssisted;

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

		try 
		{
			// Getting the settings from db
			settings = Setting.findAllSettings();

			// Getting the Assisted and setting the labels
			selectedAssisted = interfaceMain.getSelectedAssisted();

			idAssistedNameSurname.setText(selectedAssisted.getName() + " " + selectedAssisted.getSurname());

			// Getting the next month defaultAppointmentDay
			Date defaultDay = getNextWeekDefaultAppointmentDay();
			DailyPlan defaultDailyplan = new DailyPlan(defaultDay, settings);

			Calendar currentDate = Calendar.getInstance();
			currentDate.setTime(defaultDay);

			// Only if the appointments of the day are less than the default maximum display the dailyPlan
			while (defaultDailyplan.getNumberOfAppointments() >= Setting.findAllSettings().getMaxDailyAppointments())
			{
				currentDate.add(Calendar.DATE, SKIP_DAYS);
				defaultDailyplan = new DailyPlan(currentDate.getTime(), settings);
			} 

			// Update datePicker
			updateDatePicker(Date.from(currentDate.toInstant()));

			// Binding the defaultDailyPlan to the TableView
			ObservableList<ObservableSlot> slotObservableList = FXCollections.<ObservableSlot>observableArrayList();
			slotObservableList.addAll(defaultDailyplan.getDailyPlan());
			bindAndSelectFirstFreeSlot(slotObservableList, defaultDailyplan);

			// Setting up the labels
			idAppointmentNumber.setText(String.valueOf(defaultDailyplan.getNumberOfAppointments()));
			idFullDay.setText(Formatter.getDateAsItalianString(currentDate));
			
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

		} 
		catch (Exception e) 
		{
			showAlertDatabaseErrorToMainPage();
		}
	}

	@FXML
	void saveAppointment(ActionEvent event) 
	{
		ObservableSlot selectedSlot = idTableView.getSelectionModel().getSelectedItem(); 
		
		// Only if the selected slot is free save the appointment to the database
		if (selectedSlot != null && selectedSlot.getAssociatedSlot().getAssocieatedAppointment() == null) 
		{
			System.out.println(selectedAssisted.toString());
			Date appointmentDateTime =  new Date().from(selectedSlot.getAssociatedSlot().getDateTime().toInstant());
			// Saving appointment
			Appointment appToSave = new Appointment();		

			// Check for errors
			if (appToSave.saveAppointment(selectedAssisted, appointmentDateTime, selectedSlot.getAssociatedSlot().getSlotLength())) 
				showAlertWithSuccessfulHeaderText(selectedAssisted, appointmentDateTime, selectedSlot.getAssociatedSlot().getSlotLength());
			else
				showAlertDatabaseErrorToMainPage(); 
		}
		else if (selectedSlot != null && 
				selectedSlot.getAssociatedSlot().getAssocieatedAppointment() != null) // Slot already taken
		{
			showAlertAppointmentTaken(selectedSlot);
		} 
		else // There is no selection
		{
			showAlertNoSelection();
		}
	}

	private void showAlertNoSelection() {

		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Attenzione");
		alert.setHeaderText("Nessuna selezione");
		alert.showAndWait();
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
				LocalDate datePickerLocalDate = idDatePicker.getValue();
				Calendar cal = Calendar.getInstance();
				Date datePickerDate = Date.from(datePickerLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				cal.setTime(datePickerDate);
				updateDailyPlan(cal.getTime());
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
		alert.setContentText(Formatter.getAlertMessage(assisted, cal));
		alert.setHeight(300);
		alert.setOnCloseRequest(new EventHandler<DialogEvent>() {

			@Override
			public void handle(DialogEvent event) {
				interfaceMain.switchScene(MainCallback.Pages.SearchPerson);
			}

		});
		alert.showAndWait();
	}
	
	private void showAlertDatabaseErrorToMainPage() {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Messaggio di errore");
		alert.setHeaderText("Errore di connessione al database");
		alert.setContentText("Riprovare più tardi");
		alert.setOnCloseRequest(new EventHandler<DialogEvent>() {

			@Override
			public void handle(DialogEvent event) {
				interfaceMain.switchScene(MainCallback.Pages.SearchPerson);
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
	void updateDailyPlanFromDatePicker(ActionEvent event) 
	{
		// Getting the datePickerDate
		LocalDate datePickerLocalDate = idDatePicker.getValue();
		Date datePickerDate = Date.from(datePickerLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

		// Updating the dailyPlan displayed, preventing moving to the past
		if (datePickerDate.after(Calendar.getInstance().getTime()))
		{
			try 
			{
				updateDailyPlan(datePickerDate);
			} 
			catch (Exception e) 
			{
				showAlertDatabaseErrorToMainPage();
			}
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
			idTableView.scrollTo(dailyPlan.getFirstFreeSlot());
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
		cal.set(Calendar.DAY_OF_WEEK, Setting.findDefaultWeekDay());

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