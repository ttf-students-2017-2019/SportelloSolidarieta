package windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Meeting;
import utilities.Formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import application.MainCallback;

public class MeetingDetailController {
	
    @FXML
    private TextField value;

    @FXML
    private TextArea descriptionText;
    
    @FXML
    private DatePicker date;
    
    @FXML
	private void initialize() 
    {
    	// getting selected meeting
    	selectedMeeting = interfaceMain.getSelectedMeeting();
    	
    	// binding the meeting to layout
    	descriptionText.setText(selectedMeeting.getDescription());
    	value.setText(Formatter.formatNumber(selectedMeeting.getAmount()));
    	date.setValue(selectedMeeting.getDate());
    	
    }
    
    @FXML
    void addMeeting(ActionEvent event) {
    	
    	

    }

    @FXML
    void toAssistedDetail(ActionEvent event) 
    {	
		Stage stage = (Stage) descriptionText.getParent().getScene().getWindow();
		stage.close();
    }
	
    
    // Alerts 
    
	// Meeting added to assisted detail
	private void showAlertUpdateSettingsToMainPage() 
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Messaggio di conferma");
		alert.setHeaderText("Incontro aggiunto correttamente");
		alert.setContentText("Ritorno ai dettagli dell'assistito");
		alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
		
			@Override
			public void handle(DialogEvent event) {
				Stage stage = (Stage) descriptionText.getParent().getScene().getWindow();
				stage.close();
			}
		});
		alert.showAndWait();	
	}
    
    
	// Interface to callback the main class
	private MainCallback interfaceMain;	
	
	private Meeting selectedMeeting;
	
	public MeetingDetailController(MainCallback interfaceMain)
	{
		this.interfaceMain = interfaceMain;
	}

}
