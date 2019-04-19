package windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Meeting;
import utilities.Formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import application.MainCallback;
import dal.DbUtil;

public class MeetingDetailController {

	@FXML
	private TextField value;

	@FXML
	private TextArea descriptionText;

	@FXML
	private DatePicker date;

	@FXML
	private void initialize() {
		// disable dates in the future for the date pickers
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isAfter(LocalDate.now())) {
							setDisable(true);
						}
					}
				};
			}
		};
		date.setDayCellFactory(dayCellFactory);

		// getting selected meeting
		selectedMeeting = interfaceMain.getSelectedMeeting();

		// binding the meeting to layout
		descriptionText.setText(selectedMeeting.getDescription());
		value.setText(Formatter.formatNumber(selectedMeeting.getAmount()));
		date.setValue(selectedMeeting.getDate());
	}

	@FXML
    void saveMeeting(ActionEvent event) 
    {
    	selectedMeeting.setDate(date.getValue());
    	if (descriptionText.getText().length()<=1000) 
    	{
    		selectedMeeting.setDescription(descriptionText.getText());
			try 
			{
				selectedMeeting.setAmount(Float.valueOf(value.getText()));
				DbUtil.saveMeeting(selectedMeeting);
				showAlertAddedMeetingToAssistedDetail();
				
			} 
			catch (Exception e) 
			{
				showAlertValueError();
			}	
    	}
    	else 
    	{
    		showAlertMaxCharacterError();
		
    	} 
    }

	@FXML
	void toAssistedDetail(ActionEvent event) {
		Stage stage = (Stage) descriptionText.getParent().getScene().getWindow();
		stage.close();
	}

	// Alerts

	// Meeting added to assisted detail
	private void showAlertAddedMeetingToAssistedDetail() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Messaggio di conferma");
		alert.setHeaderText("Incontro aggiunto correttamente");
		alert.setContentText("Ritorno ai dettagli dell'assistito");
		alert.setOnCloseRequest(new EventHandler<DialogEvent>() {

			@Override
			public void handle(DialogEvent event) 
			{
				Stage stage = (Stage) descriptionText.getParent().getScene().getWindow();
				stage.close();	
			}
		});
		alert.showAndWait();
	}

	// Error alert for too many character
	private void showAlertMaxCharacterError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Messaggio di errore");
		alert.setHeaderText("Superato il limite massimo di caratteri per la descrizione");
		alert.setContentText("Numero massimo di caratteri: 1000");
		alert.showAndWait();
	}
	
	// Error alert for invalid value
	private void showAlertValueError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Messaggio di errore");
		alert.setHeaderText("Valore elemosina non corretto");
		alert.setContentText("Inserire un numero valido");
		alert.showAndWait();
	}


	// Interface to callback the main class
	private MainCallback interfaceMain;

	private Meeting selectedMeeting;

	public MeetingDetailController(MainCallback interfaceMain) {
		this.interfaceMain = interfaceMain;
	}

}
