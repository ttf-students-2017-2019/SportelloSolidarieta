package windows;

import application.MainCallback;
import dal.DbUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Meeting;
import model.Person;

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
    void toSearchPerson(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.SearchPerson);
    }

    @FXML
    void toSchedule(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.Schedule);
    }
    
    @FXML
    void setOnAction(ActionEvent event) 
    {
		if (this.validateField()) {
			DbUtil.savePerson(textfield_name.getText(), textbox_surname.getText(), datepicker_birthdate.getValue(),
					dropdown_sex.getValue(), textbox_nationality.getText(), checkbox_wentbackhome.isSelected(),
					checkbox_rejected.isSelected() , textfield_familycomposition.getText());
		}
    } 
    
    @FXML
    public void initialize() 
    {
		dropdown_sex.setItems(dropBoxValue);
		
		if (assisted != null) {
			// bind text field to bean properties
			textfield_name.setText(assisted.getName());
			textbox_surname.setText(assisted.getSurname());

			datepicker_birthdate.setValue(assisted.getBirthdate());

			dropdown_sex.setValue(assisted.getSex());
			textbox_nationality.setText(assisted.getNationality());
			checkbox_wentbackhome.setSelected(assisted.getIsReunitedWithFamily());
			checkbox_rejected.setSelected(assisted.getIsRefused());
			textfield_familycomposition.setText(assisted.getFamilyComposition());

			// bind columns to bean properties
			date.setCellValueFactory(new PropertyValueFactory<Meeting, String>("date"));
			description.setCellValueFactory(new PropertyValueFactory<Meeting, String>("description"));
			amount.setCellValueFactory(new PropertyValueFactory<Meeting, String>("amount"));

			// initialize data model and bind table
			ObservableList<Meeting> v = FXCollections.<Meeting>observableArrayList();
			v.addAll(assisted.getMeetings());
			table.setItems(v);
		}    	
    }
    
	//
    // Instance constructor
	//
	// parameters	
	//		interfaceMain 		interface to callback the main class
	//
	// returned
	//		none
	//
    public AssistedDetailsController(MainCallback interfaceMain, Person assisted)
    {
    	this.interfaceMain = interfaceMain;  
    	
		this.assisted = assisted;
    } 
    
	private boolean validateField() 
	{
		return assisted != null && textfield_name.getText() != "" && textbox_surname.getText() != "";
	}
	
    // Interface to callback the main class
    private MainCallback interfaceMain;	
    
	private ObservableList<Character> dropBoxValue = (FXCollections.observableArrayList('M', 'F', 'T'));
	
	private Person assisted; 
}
