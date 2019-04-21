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
import utilities.Formatter;

import java.time.LocalDate;

import application.MainCallback;
import application.PageCallback;
import dal.DbUtil;

public class MeetingDetailsController {

	/*
	 * MEMBERS
	 */

	private MainCallback main; // Interface to callback the main class
	private PageCallback previousPage;

	/*
	 * JAVAFX COMPONENTS
	 */

	@FXML
	private TextField value;

	@FXML
	private TextArea descriptionText;

	@FXML
	private DatePicker date;

	/*
	 * CONSTRUCTOR
	 */

	public MeetingDetailsController(MainCallback main, PageCallback currentPage) {
		this.main = main;
		previousPage = currentPage;
	}

	/*
	 * SCENE INITIALIZATION
	 */

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

		// binding the meeting to layout
		descriptionText.setText(main.getSelectedMeeting().getDescription());
		value.setText(Formatter.formatNumber(main.getSelectedMeeting().getAmount()));
		date.setValue(main.getSelectedMeeting().getDate());
	}

	/*
	 * JAVAFX ACTIONS
	 */

	@FXML
	void saveMeeting(ActionEvent event) {

		int meetingIndex = main.getSelectedAssisted().getMeetings().indexOf(main.getSelectedMeeting());

		main.getSelectedMeeting().setDate(date.getValue());
		if (descriptionText.getText().length() <= 1000) {
			main.getSelectedMeeting().setDescription(descriptionText.getText());
			try {
				main.getSelectedMeeting().setAmount(Float.valueOf(Formatter.reverseFormatNumber(value.getText())));
				main.setSelectedMeeting(DbUtil.saveMeeting(main.getSelectedMeeting()));
				showAlertAddedMeetingToAssistedDetail();

				switch (main.getRequestedOperation()) {
				case CREATE:
					main.getSelectedAssisted().getMeetings().add(main.getSelectedMeeting());
					break;

				case UPDATE:
					main.getSelectedAssisted().getMeetings().set(meetingIndex, main.getSelectedMeeting());
					System.out.println(main.getSelectedMeeting().getAssisted().getMeetings().toString());
					break;
				}
				previousPage.refresh();
			} catch (NumberFormatException e) {
				showAlertValueError();
			}
		} else {
			showAlertMaxCharacterError();
		}
	}

	@FXML
	void toAssistedDetails(ActionEvent event) {
		previousPage.refresh();
		Stage stage = (Stage) descriptionText.getParent().getScene().getWindow();
		stage.close();
	}

    /*
     * OTHER METHODS
     */
	
	// Alerts

	// Meeting added to assisted detail
	private void showAlertAddedMeetingToAssistedDetail() {
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

}
