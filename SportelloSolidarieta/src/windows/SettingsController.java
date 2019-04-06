package windows;

import application.MainCallback;

import model.Setting;
import setting.ObservableAppointmentLength;
import setting.ObservableHour;
import setting.ObservableMinute;
import setting.ObservableWeekDay;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class SettingsController {

	// Settings loaded from the database
	private Setting settings; 

	@FXML
	private ComboBox<ObservableWeekDay> id_week_day;

	@FXML
	private Label id_setting_label;

	@FXML
	private Button shedule_ok_button;

	@FXML
	private Button shedule_back_button;

	@FXML
	private ComboBox<ObservableHour> id_hour_from;

	@FXML
	private ComboBox<ObservableMinute> id_minutes_from;

	@FXML
	private TextField id_max_appointment;

	@FXML
	private ComboBox<ObservableHour> id_hour_to;

	@FXML
	private ComboBox<ObservableMinute> id_minutes_to;

	@FXML
	private ComboBox<ObservableAppointmentLength> id_appointment_length;

	@FXML
	private void initialize() {
		try 
		{
			// Getting the settings from the db
			settings = Setting.findAllSettings();

			// Setting up the appointment week day combo-box
			ObservableList<ObservableWeekDay> weekDayList = setupWeekDayList();
			bindWeekDayAndSelectDeafult(weekDayList);

			// Setting up the from time combo-boxes 
			// Hours 
			ObservableList<ObservableHour> fromHourList = setupHourList();
			bindHourAndSelectDeafult(fromHourList, id_hour_from);
			// Minutes
			ObservableList<ObservableMinute> fromMinuteList = setupMinuteList();
			bindMinuteAndSelectDeafult(fromMinuteList, id_minutes_from);

			// Setting up the to time combo-boxes 
			// Hours 
			ObservableList<ObservableHour> toHourList = setupHourList();
			bindHourAndSelectDeafult(toHourList, id_hour_to);
			// Minutes
			ObservableList<ObservableMinute> toMinuteList = setupMinuteList();
			bindMinuteAndSelectDeafult(fromMinuteList, id_minutes_to);

			// Setting up the appointment length combo-box
			ObservableList<ObservableAppointmentLength> appointmentLengthList = setupAppointmentLengthList();
			bindApopointmentLengthAndSelectDeafult(appointmentLengthList);
			
			// Setting up the max appointment label
			id_max_appointment.setText(String.valueOf(settings.getMaxDailyAppointments()));
			
		} catch (Exception e) 
		{
			showAlertDatabaseErrorToMainPage();
			e.printStackTrace();
		}
	}

	@FXML
	void saveSetting(ActionEvent event) 
	{
		// Getting the new settings
		int newDefaultDay = id_week_day.getSelectionModel().getSelectedItem().getWeekDay(); 
		
		String newFromtHour = id_hour_from.getSelectionModel().getSelectedItem().hourString.get();
		String newFromMinute = id_minutes_from.getSelectionModel().getSelectedItem().minuteString.get();
		
		String newToHour = id_hour_to.getSelectionModel().getSelectedItem().hourString.get();
		String newToMinute = id_minutes_to.getSelectionModel().getSelectedItem().minuteString.get();
		
		int newAppointmentLength = id_appointment_length.getSelectionModel().getSelectedItem().getLengthValue();
		
		String newMaxAppointment = id_max_appointment.getText();
		
		// Check the From-Time and Max Appointment
		Time startTime  = Time.valueOf(newFromtHour + ":" + newFromMinute +":00");
		Time endTime = Time.valueOf(newToHour + ":"+ newToMinute + ":00");
		
		// Checking time 
		if (endTime.after(startTime)) 
		{
			 long timeDifference = endTime.getTime() - startTime.getTime();
			 int timeDifferenceInMinutes = (int)timeDifference/60000;
			 int maxAppointmentAllowed  = timeDifferenceInMinutes / newAppointmentLength;  
			 
			 try 
			 {
				 int maxAppointmentInTextField = Integer.valueOf(newMaxAppointment);
				 
				 // If the inserted value is not compatible with appointment length and start end time
				 if (maxAppointmentInTextField > maxAppointmentAllowed) 
				 {
					 showAlertMaxAppointmentError(maxAppointmentAllowed);
					 return;
				 }
				 // Otherwise everything is good and we can update the settings in the database
				 
				 try 
				 {
					 // Default weekday set false and set the new one as true
					 changeFlag(settings.findDefaultWeekDay(), false);
					 changeFlag(newDefaultDay, true);
					 
					 // Setting the start and end time
					 settings.setHStart(startTime);
					 settings.setHEnd(endTime);
					 
					 // Setting the appointment length
					 settings.setAppointmentLength(newAppointmentLength);
					 
					 // Setting the max number of appointments
					 settings.setMaxDailyAppointments(maxAppointmentInTextField);
					 
				 } catch (Exception e) {
					// TODO: handle exception
				 }

				 
			 } 
			 catch (Exception e) // No numbers 
			 {
				 showAlertNotANumberMaxAppointmentError();
				 e.printStackTrace();
			 }
			 
		}
		else // To Time < From Time
		{
			 showAlertTimeWarning();
		}
		// Updating the Settings and then save it to the db if end time is after start time 
	
	}

	@FXML
	void toSearchPerson(ActionEvent event) 
	{
		Stage stage = (Stage) id_setting_label.getParent().getScene().getWindow();
		stage.close();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// WEEKDAY LIST//////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Set up of the weekDay List
	private ObservableList<ObservableWeekDay> setupWeekDayList()
	{
		List<ObservableWeekDay> days =  new ArrayList<ObservableWeekDay>();

		// From Monday to Saturday
		for (int i = Calendar.MONDAY; i<=Calendar.SATURDAY; i++) 
		{
			days.add(new ObservableWeekDay(i));
		}
		// Adding Sunday as last day of the week
		days.add(new ObservableWeekDay(Calendar.SUNDAY));

		ObservableList<ObservableWeekDay> daysObservable = FXCollections.<ObservableWeekDay>observableArrayList();
		daysObservable.addAll(days);

		return daysObservable;
	}

	// Binding the combo-box selecting the default day for appointments
	public void bindWeekDayAndSelectDeafult(ObservableList<ObservableWeekDay> observalbeList)
	{
		id_week_day.setItems(observalbeList);
		id_week_day.getSelectionModel().select(getDeafultWeekDayIndex(observalbeList, settings.findDefaultWeekDay()));
	}   

	// Get the position of the week day to select in the combo-box
	public int getDeafultWeekDayIndex(ObservableList<ObservableWeekDay> observalbeList, int dayToFind)
	{
		int position = 0;

		for (ObservableWeekDay currentDay : observalbeList) 
		{
			if(currentDay.getWeekDay() == dayToFind) 
			{
				position = observalbeList.indexOf(currentDay);
				break;
			}	
		}
		return position;
	}  

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// HOURLIST /////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Setting up hour list for the day
	private ObservableList<ObservableHour> setupHourList() {
		List<ObservableHour> hours =  new ArrayList<ObservableHour>();

		// From 00 to 23
		for (int i = 0; i<24; i++) 
		{
			hours.add(new ObservableHour(i));
		}	    	
		ObservableList<ObservableHour> hoursObservable = FXCollections.<ObservableHour>observableArrayList();
		hoursObservable.addAll(hours);

		return hoursObservable;
	}

	// Binding the From Hour combo-box and selecting the default setting
	private void bindHourAndSelectDeafult(ObservableList<ObservableHour> hourList, 
			ComboBox<ObservableHour> currentComboBox) 
	{
		currentComboBox.setItems(hourList);
		Calendar cal = Calendar.getInstance();

		// Determine the kind of combo-box
		if (currentComboBox.getId().equals("id_hour_from"))
			cal.setTimeInMillis(settings.getHStart().getTime());
		else 
			cal.setTimeInMillis(settings.getHEnd().getTime());

		// The hour list is in order so just taking the hour of day gets the item to select
		currentComboBox.getSelectionModel().select(cal.get(Calendar.HOUR_OF_DAY));
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// MINUTELIST ///////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Setting up hour list for the day
	private ObservableList<ObservableMinute> setupMinuteList() {
		List<ObservableMinute> minutes =  new ArrayList<ObservableMinute>();

		// From 00 to 55 with a 5 minutes gap
		for (int i = 0; i<=55; i+=5) 
		{
			minutes.add(new ObservableMinute(i));
		}	    	
		ObservableList<ObservableMinute> minutesObservable = FXCollections.<ObservableMinute>observableArrayList();
		minutesObservable.addAll(minutes);

		return minutesObservable;
	}

	// Binding the From Hour combo-box and selecting the default setting
	private void bindMinuteAndSelectDeafult(ObservableList<ObservableMinute> fromMinuteList, 
			ComboBox<ObservableMinute> currentComboBox) 
	{
		currentComboBox.setItems(fromMinuteList);
		Calendar cal = Calendar.getInstance();

		// Determine the kind of combo-box
		if (currentComboBox.getId().equals("id_minute_from"))
			cal.setTimeInMillis(settings.getHStart().getTime());
		else 
			cal.setTimeInMillis(settings.getHEnd().getTime());

		// The minute list is in order so just taking the minutes and divide by 5 min gap
		currentComboBox.getSelectionModel().select(cal.get(Calendar.MINUTE)/5);		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// APPOINTMENT LENGTH LIST///////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Setting up appointment length list
	private ObservableList<ObservableAppointmentLength> setupAppointmentLengthList () {
		List<ObservableAppointmentLength> appointmentLengthList =  new ArrayList<ObservableAppointmentLength>();

		// From 5 to 60 with a 5 minutes gap
		for (int i = 5; i<=60; i+=5) 
		{
			appointmentLengthList.add(new ObservableAppointmentLength(i));
		}	    	
		ObservableList<ObservableAppointmentLength> appointmentLengthObservable = FXCollections.<ObservableAppointmentLength>observableArrayList();
		appointmentLengthObservable.addAll(appointmentLengthList);

		return appointmentLengthObservable;
	}
	
	// Change the flag of the default day of week 
	private void changeFlag (int flagToChange, boolean value)
	{
		 switch (flagToChange) 
		 {
			 case Calendar.MONDAY:
				 settings.setFMonday(value);
				 break; 
			  case Calendar.TUESDAY:
				  settings.setFTuesday(value);
				  break;  
			  case Calendar.WEDNESDAY:
				  settings.setFWednesday(value);
				  break;  
			  case Calendar.THURSDAY:
				  settings.setFThursday(value);
				  break;  
			  case Calendar.FRIDAY:
				  settings.setFFriday(value);
				  break;  
			  case Calendar.SATURDAY:
				  settings.setFSaturday(value);
				  break;  
			  case Calendar.SUNDAY:
				  settings.setFSunday(value);
				  break;  
		}
	}
	
	// Binding the From Hour combo-box and selecting the default setting
	private void bindApopointmentLengthAndSelectDeafult(ObservableList<ObservableAppointmentLength> appointmentLengthList) 
	{
		id_appointment_length.setItems(appointmentLengthList);
		// The list is in order so just taking the minutes and divide by 5 min gap the settings value -1
		id_appointment_length.getSelectionModel().select(settings.getAppointmentLength()/5 - 1);		
	}
	
	// To main page error
	private void showAlertDatabaseErrorToMainPage() 
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Messaggio di errore");
		alert.setHeaderText("Errore di connessione al database");
		alert.setContentText("Riprovare più tardi");
		alert.setOnCloseRequest(new EventHandler<DialogEvent>() {

			@Override
			public void handle(DialogEvent event) {
				Stage stage = (Stage) id_setting_label.getParent().getScene().getWindow();
				stage.close();
			}
		});
		alert.showAndWait();	
	}
	
	// Error alert for no numbers in max appointment text field
	private void showAlertMaxAppointmentError(int maxAllowedAppointment) 
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Messaggio di errore");
		alert.setHeaderText("Il numero massimo di appuntamenti della giornata è maggiore del più alto valore consentito");
		alert.setContentText("Il numero massimo di appuntamenti con le impostazioni correnti è: " + maxAllowedAppointment);
		alert.showAndWait();
	}
	
	// Error alert for no numbers in max appointment text field
	private void showAlertNotANumberMaxAppointmentError() 
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Messaggio di errore");
		alert.setHeaderText("Il numero massimo di appuntamenti non è un numero");
		alert.setContentText("Inserire un numero");
		alert.showAndWait();
	}
	
	// Warning for wrong time selection
	private void showAlertTimeWarning() 
	{
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Messaggio di avviso");
		alert.setHeaderText("L'orario di chiusura dello sportello deve seguire quello di apertura");
		alert.setContentText("Selezionare orari differenti");
		alert.showAndWait();
	}

	// Interface to callback the main class
	private MainCallback interfaceMain;	

	public SettingsController(MainCallback interfaceMain)
	{
		this.interfaceMain = interfaceMain;
	}


}
