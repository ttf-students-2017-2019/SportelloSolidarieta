package windows;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import application.MainCallback;
import application.PageCallback;
import application.MainCallback.Operation;
import application.MainCallback.Page;
import dal.DbUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.Meeting;
import schedule.ObservableSlot;

public class AssistedDetailsController implements PageCallback {

	/*
	 * MEMBERS
	 */

	private MainCallback main; // Interface to callback the main class
	private ObservableList<Meeting> meetings;
	private ObservableList<Character> dropBoxValue = (FXCollections.observableArrayList('M', 'F', 'T'));

	/*
	 * JAVAFX COMPONENTS
	 */

	@FXML
	private TextField textfield_name;

	@FXML
	private TextField textbox_surname;

	@FXML
	private DatePicker datepicker_birthdate;

	@FXML
	private ComboBox<Character> dropdown_sex;

	@FXML
	private TextField textbox_nationality;

	@FXML
	private CheckBox checkbox_wentbackhome;

	@FXML
	private CheckBox checkbox_rejected;

	@FXML
	private TextField textfield_familycomposition;

	@FXML
	private TableView<Meeting> table;

	@FXML
	private TableColumn<Meeting, LocalDate> date;

	@FXML
	private TableColumn<Meeting, Float> amount;

	@FXML
	private TableColumn<Meeting, String> description;

	@FXML
	private Button button_new_appointment;

	@FXML
	private Button button_save;

	@FXML
	private Button button_meeting_detail;

	@FXML
	private Button button_meeting_add;

	@FXML
	private Button button_meeting_remove;

	/*
	 * CONSTRUCTOR
	 */

	public AssistedDetailsController(MainCallback main) {
		this.main = main;
	}

	/*
	 * SCENE INITIALIZATION
	 */

	@FXML
	public void initialize() {
		dropdown_sex.setItems(dropBoxValue);

		// disable dates in the future for the date picker
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
		datepicker_birthdate.setDayCellFactory(dayCellFactory);

		// bind text fields to assisted properties
		textfield_name.setText(main.getSelectedAssisted().getName());
		textbox_surname.setText(main.getSelectedAssisted().getSurname());
		datepicker_birthdate.setValue(main.getSelectedAssisted().getBirthdate());
		dropdown_sex.setValue(main.getSelectedAssisted().getSex());
		textbox_nationality.setText(main.getSelectedAssisted().getNationality());
		checkbox_wentbackhome.setSelected(main.getSelectedAssisted().getIsReunitedWithFamily());
		checkbox_rejected.setSelected((main.getSelectedAssisted().getIsRefused()));
		textfield_familycomposition.setText(main.getSelectedAssisted().getFamilyComposition());

		table.setPlaceholder(new Label("Nessun risultato"));

		// bind columns to meeting properties
		date.setCellValueFactory(new PropertyValueFactory<Meeting, LocalDate>("date"));
		date.setCellFactory(cellData -> new TableCell<Meeting, LocalDate>() {
			@Override
			protected void updateItem(LocalDate date, boolean isEmpty) {
				super.updateItem(date, isEmpty);
				if (isEmpty) {
					setText(null);
				} else {
					setText(utilities.Formatter.formatDate(date));
				}
			}
		});
		description.setCellValueFactory(new PropertyValueFactory<Meeting, String>("description"));
		amount.setCellValueFactory(new PropertyValueFactory<Meeting, Float>("amount"));
		amount.setCellFactory(cellData -> new TableCell<Meeting, Float>() {
			@Override
			protected void updateItem(Float amount, boolean isEmpty) {
				super.updateItem(amount, isEmpty);
				if (isEmpty) {
					setText(null);
				} else {
					setText(utilities.Formatter.formatNumber(amount));
				}
			}
		});

		// bind table to meetings
		meetings = FXCollections.observableArrayList();
		
		// add to the observable list only not deleted meetings
		for (Meeting m : main.getSelectedAssisted().getMeetings()) 
		{
			if (m.isfDeleted() == false) 
			{
				meetings.add(m);
			}
				
		}
		meetings.sort(new Comparator<Meeting>() 
		{
	        @Override
	        public int compare(Meeting firstMeeting, Meeting secondMeeting)
	        {
	            return  firstMeeting.getDate().compareTo(secondMeeting.getDate());
	        }
	    });
		
		table.setItems(meetings);
		table.scrollTo(meetings.size()-1);
		button_meeting_detail.setDisable(true);
		button_meeting_remove.setDisable(true);

		// if assisted is not persisted disable meeting management
		if (main.getSelectedAssisted().getId() == null ) {
			table.setPlaceholder(new Label("Prima di aggiungere un nuovo incontro è necessario salvare l'anagrafica dell'assistito"));
			button_meeting_add.setDisable(true);
			button_new_appointment.setDisable(true);
		}
		
		// if assisted is rejected or joined with family disable assisted detail management		
		if (checkbox_rejected.isSelected() || checkbox_wentbackhome.isSelected()) 
		{
			enableOrDisableAssistedDetailManagement(true); 
		}
	}

	/*
	 * JAVAFX ACTIONS
	 */

	@FXML
	void toAssistedSearch(ActionEvent event) {
		main.switchScene(MainCallback.Page.ASSISTED_SEARCH, null);
	}

	@FXML
	void toSchedule(ActionEvent event) {
		main.switchScene(MainCallback.Page.SCHEDULE, null);
	}

	@FXML
	void saveAssisted(ActionEvent event) {
		main.getSelectedAssisted().setName(textfield_name.getText());
		main.getSelectedAssisted().setSurname(textbox_surname.getText());
		main.getSelectedAssisted().setBirthdate(datepicker_birthdate.getValue());
		main.getSelectedAssisted().setSex(dropdown_sex.getValue());
		main.getSelectedAssisted().setNationality(textbox_nationality.getText());
		main.getSelectedAssisted().setReunitedWithFamily(checkbox_wentbackhome.isSelected());
		main.getSelectedAssisted().setRefused(checkbox_rejected.isSelected());
		main.getSelectedAssisted().setFamilyComposition(textfield_familycomposition.getText());

		if (main.getSelectedAssisted().isValid()) {
			main.setSelectedAssisted(DbUtil.saveAssisted(main.getSelectedAssisted()));
			Alert alert = new Alert(AlertType.INFORMATION, "Assistito salvato con successo", ButtonType.OK);
			alert.showAndWait();
			// once assisted has been persisted activate meeting management
			table.setPlaceholder(new Label("Nessun risultato"));
			button_meeting_add.setDisable(false);
			button_new_appointment.setDisable(false);
			  
			if (main.getSelectedAssisted().getIsRefused() || main.getSelectedAssisted().getIsReunitedWithFamily()) 
			{
				// disable assisted detail management
				enableOrDisableAssistedDetailManagement(true);		
			}			
			else
			{
				// enable assisted detail management
				enableOrDisableAssistedDetailManagement(false);	
			}
			
			// reload the meeting list 
			refresh();
			
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Inserire cognome e nome", ButtonType.OK);
			alert.showAndWait();
		}
	}

	@FXML
	void onRowSelected(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			main.setSelectedMeeting(table.getSelectionModel().getSelectedItem());
			System.out.println("SELECTED MEETING: " + main.getSelectedMeeting()); // TODO change with a proper logging
			if (main.getSelectedMeeting() != null) { // note: it selects a null Meeting if I click in the empty area of
														// the Table, so I need this one
				button_meeting_detail.setDisable(false);
				
				if (!checkbox_rejected.isSelected() && !checkbox_wentbackhome.isSelected())
					button_meeting_remove.setDisable(false);
			}
		}
	}

	@FXML
	void toMeetingDetails(ActionEvent event) {
		if (event.getSource() == button_meeting_add) {
			Meeting meeting = new Meeting();
			meeting.setDate(LocalDate.now());
			meeting.setDescription("");
			meeting.setAmount(0);
			meeting.setAssisted(main.getSelectedAssisted());
			main.setSelectedMeeting(meeting);
			main.setRequestedOperation(Operation.CREATE);
		} else if (event.getSource() == button_meeting_detail) {
			main.setRequestedOperation(Operation.UPDATE);
		}
		main.switchScene(Page.MEETING_DETAILS, this);
	}

	@FXML
	void removeMeeting(ActionEvent event) 
	{
		showRemoveConfirmationDialog();
	}

	/*
	 * OTHER METHODS
	 */

	public void refresh() {

		meetings = FXCollections.observableArrayList();
		
		// add to the observable list only not deleted meetings
		for (Meeting m : main.getSelectedAssisted().getMeetings()) 
		{
			if (m.isfDeleted() == false)
				meetings.add(m);
		}
		
		meetings.sort(new Comparator<Meeting>() 
		{
	        @Override
	        public int compare(Meeting firstMeeting, Meeting secondMeeting)
	        {
	            return  firstMeeting.getDate().compareTo(secondMeeting.getDate());
	        }
	    });
		
		table.setItems(meetings);
		table.getSelectionModel().clearSelection();
		table.scrollTo(meetings.size()-1);
		main.setSelectedMeeting(null);
		button_meeting_detail.setDisable(true);
		button_meeting_remove.setDisable(true);
	}

	private void showRemoveConfirmationDialog() {
		// Creating custom button
		ButtonType yesButton = new ButtonType("Sì", ButtonData.OK_DONE);
		ButtonType noButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);

		Alert alert = new Alert(AlertType.CONFIRMATION, null, yesButton, noButton);
		alert.setTitle("Avviso di conferma");
		alert.setHeaderText("Conferma la tua scelta");
		alert.setContentText("Sicuro di voler cancellare l'incontro selezionato?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.orElse(noButton) == yesButton) {
			
			// setting true in deleted flag
			main.getSelectedMeeting().setfDeleted(true);
			
			// Saving on data base and setting the selected meeting to the new deleted meeting is enough 
			// because the selected meeting is on the meeting list itself 
			main.setSelectedMeeting(DbUtil.saveMeeting(main.getSelectedMeeting()));
			refresh();
		}
	}
	
	// disable or enable assisted detail meeting and appointment management
	private void enableOrDisableAssistedDetailManagement(boolean value) 
	{	
		// assisted detail
		textfield_name.setDisable(value);
		textbox_surname.setDisable(value);
		datepicker_birthdate.setDisable(value);
		dropdown_sex.setDisable(value);
		textbox_nationality.setDisable(value);
		textfield_familycomposition.setDisable(value);
		
		// meeting
		button_meeting_add.setDisable(value);
		
		// appointment
		button_new_appointment.setDisable(value);
	}
}
