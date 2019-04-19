package windows;

import java.time.LocalDate;
import java.util.List;

import application.MainCallback;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Meeting;
import model.Assisted;

public class AssistedDetailsController {

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
	private TableColumn<Meeting, String> date;
	@FXML
	private TableColumn<Meeting, String> amount;
	@FXML
	private TableColumn<Meeting, String> description;
	
	@FXML
	private Button button_save;
    @FXML
    private Button button_meeting_detail;
    @FXML
    private Button button_meeting_add;
    
	@FXML
	void toSearchPerson(ActionEvent event) {
		interfaceMain.switchScene(MainCallback.Pages.SearchPerson);
	}

	@FXML
	void toSchedule(ActionEvent event) {
		interfaceMain.switchScene(MainCallback.Pages.Schedule);
	}

	@FXML
	void setOnAction(ActionEvent event) {

		Assisted a = new Assisted();
		a.setId(assisted.getId());
		a.setName(textfield_name.getText());
		a.setSurname(textbox_surname.getText());
		a.setBirthdate(datepicker_birthdate.getValue());
		a.setSex(dropdown_sex.getValue());
		a.setNationality(textbox_nationality.getText());
		a.setReunitedWithFamily(checkbox_wentbackhome.isSelected());
		a.setRefused(checkbox_rejected.isSelected());
		a.setFamilyComposition(textfield_familycomposition.getText());

		if (this.validateField()) {
			Alert alert = new Alert(AlertType.INFORMATION, "Utente Salvato con successo.", ButtonType.OK);
			alert.showAndWait();
			DbUtil.saveAssisted(a);
		}
	}

	@FXML
	public void initialize() {
		dropdown_sex.setItems(dropBoxValue);
		// bind text field to bean properties
		textfield_name.setText(assisted.getName());
		textbox_surname.setText(assisted.getSurname());
		datepicker_birthdate.setValue(assisted.getBirthdate());
		dropdown_sex.setValue(assisted.getSex());
		textbox_nationality.setText(assisted.getNationality());
		checkbox_wentbackhome.setSelected(assisted.getIsReunitedWithFamily());
		checkbox_rejected.setSelected((assisted.getIsRefused()));
		textfield_familycomposition.setText(assisted.getFamilyComposition());
		List<Meeting> meetings = assisted.getMeetings();
		// bind columns to bean properties
		date.setCellValueFactory(new PropertyValueFactory<Meeting, String>("date"));
		description.setCellValueFactory(new PropertyValueFactory<Meeting, String>("description"));
		amount.setCellValueFactory(new PropertyValueFactory<Meeting, String>("amount"));

		// initialize data model and bind table
		if (meetings != null) {
			ObservableList<Meeting> v = FXCollections.<Meeting>observableArrayList();
			v.setAll(meetings);
			table.setItems(v);
		}
	}
	
    @FXML
    void removeMeeting(ActionEvent event) {

    }
    
    @FXML
    void toMeetingDetail(ActionEvent event) {
    	
    	if (event.getSource().equals(button_meeting_add)) 
    	{
    		Meeting meeting =new Meeting();
    		meeting.setDate(LocalDate.now());
    		meeting.setDescription("");
    		meeting.setAmount(0);
    		meeting.setAssisted(interfaceMain.getSelectedAssisted());
    		interfaceMain.setSelectedMeeting(meeting);
    	}
    	else 
    		interfaceMain.setSelectedMeeting(table.getSelectionModel().getSelectedItem());
    	
    		
    }
    
	//
	// Instance constructor
	//
	// parameters
	// interfaceMain interface to callback the main class
	//
	// returned
	// none
	//
	public AssistedDetailsController(MainCallback interfaceMain) {
		this.interfaceMain = interfaceMain;
		assisted = interfaceMain.getSelectedAssisted();
	}

	private boolean validateField() {
		return assisted != null && textfield_name.getText() != "" && textbox_surname.getText() != "";
	}

	// Interface to callback the main class
	private MainCallback interfaceMain;

	private ObservableList<Character> dropBoxValue = (FXCollections.observableArrayList('M', 'F', 'T'));

	private Assisted assisted;
}
