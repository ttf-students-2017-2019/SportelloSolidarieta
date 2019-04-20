package windows;

import java.time.LocalDate;

import application.MainCallback;
import application.PageCallback;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.Meeting;
import model.Assisted;

public class AssistedDetailsController implements PageCallback {

	public enum Operation {
		CREATE, UPDATE
	}

	/*
	 * MEMBERS
	 */

	private MainCallback main; // Interface to callback the main class
	private Meeting selectedMeeting;
	private Operation operation;
	private Assisted assisted;
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
		assisted = main.getSelectedAssisted();
	}

	/*
	 * GETTERS AND SETTERS
	 */

	public Meeting getSelectedMeeting() {
		return selectedMeeting;
	}

	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/*
	 * SCENE INITIALIZATION
	 */

	@FXML
	public void initialize() {
		dropdown_sex.setItems(dropBoxValue);
		button_meeting_detail.setDisable(true);
		button_meeting_remove.setDisable(true);

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
		textfield_name.setText(assisted.getName());
		textbox_surname.setText(assisted.getSurname());
		datepicker_birthdate.setValue(assisted.getBirthdate());
		dropdown_sex.setValue(assisted.getSex());
		textbox_nationality.setText(assisted.getNationality());
		checkbox_wentbackhome.setSelected(assisted.getIsReunitedWithFamily());
		checkbox_rejected.setSelected((assisted.getIsRefused()));
		textfield_familycomposition.setText(assisted.getFamilyComposition());

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
		meetings = FXCollections.observableArrayList(assisted.getMeetings());
		table.setItems(meetings);
	}

	/*
	 * JAVAFX ACTIONS
	 */

	@FXML
	void toSearchAssisted(ActionEvent event) {
		main.switchScene(MainCallback.Page.SEARCH_ASSISTED, null);
	}

	@FXML
	void toSchedule(ActionEvent event) {
		main.switchScene(MainCallback.Page.SCHEDULE, null);
	}

	@FXML
	void saveAssisted(ActionEvent event) {
		assisted.setName(textfield_name.getText());
		assisted.setSurname(textbox_surname.getText());
		assisted.setBirthdate(datepicker_birthdate.getValue());
		assisted.setSex(dropdown_sex.getValue());
		assisted.setNationality(textbox_nationality.getText());
		assisted.setReunitedWithFamily(checkbox_wentbackhome.isSelected());
		assisted.setRefused(checkbox_rejected.isSelected());
		assisted.setFamilyComposition(textfield_familycomposition.getText());

		if (this.isValid()) {
			assisted = DbUtil.saveAssisted(assisted);
			Alert alert = new Alert(AlertType.INFORMATION, "Utente Salvato con successo.", ButtonType.OK);
			alert.showAndWait();
		}
	}

	@FXML
	void onRowSelected(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			Meeting selectedMeeting = table.getSelectionModel().getSelectedItem();
			setSelectedMeeting(table.getSelectionModel().getSelectedItem());
			System.out.println("SELECTED MEETING: " + selectedMeeting); // TODO change with a proper logging
			if (selectedMeeting != null) // note: it selects a null Meeting if I click in the empty area of the Table,
											// so I need this one
				button_meeting_detail.setDisable(false);
			button_meeting_remove.setDisable(false);
		}
	}

	@FXML
	void toMeetingDetails(ActionEvent event) {
		if (event.getSource() == button_meeting_add) {
			Meeting meeting = new Meeting();
			meeting.setDate(LocalDate.now());
			meeting.setDescription("");
			meeting.setAmount(0);
			meeting.setAssisted(assisted);
			setSelectedMeeting(meeting);
			setOperation(Operation.CREATE);
		} else if (event.getSource() == button_meeting_detail) {
			setSelectedMeeting(table.getSelectionModel().getSelectedItem());
			setOperation(Operation.UPDATE);
		}
		main.switchScene(Page.MEETING_DETAILS, this);
	}

	@FXML
	void removeMeeting(ActionEvent event) {

	}

	/*
	 * OTHER METHODS
	 */

	private boolean isValid() {
		return assisted != null && textfield_name.getText() != "" && textbox_surname.getText() != "";
	}

	public void refresh() {
		meetings = FXCollections.observableArrayList(assisted.getMeetings());
		table.setItems(meetings);
	}

}
