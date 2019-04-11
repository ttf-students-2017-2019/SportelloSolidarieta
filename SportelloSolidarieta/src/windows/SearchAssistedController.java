package windows;

import java.util.List;

import javax.persistence.EntityManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import application.MainCallback;
import dal.DbUtil;
import model.Assisted;

public class SearchAssistedController {
	
	/*
	 * MEMBERS
	 */
	
	private MainCallback interfaceMain; 
	private EntityManager em;
	private Assisted selectedAssisted;
	
	/*
	 * CONSTRUCTOR
	 */
	
	public SearchAssistedController(MainCallback interfaceMain) {
		this.interfaceMain = interfaceMain;   
	}
	
	/*
	 * SCENE INITIALIZATION
	 */
	
	@FXML
	private void initialize() {
		this.em = DbUtil.getEntityManager();	//I need the EntityManager at the very beginning, otherwise the first search would appear delayed
    	interfaceMain.setSelectedAssisted(null);// Reset the selectedAssisted
	}

	/*
	 * JavaFX components
	 */
	
    @FXML
    private Button btn_settings;
	
    @FXML
    private Button btn_report;
    
    @FXML
    private Button btn_calendar;
    
    @FXML
    private Button btn_addAssisted;
    
    @FXML
    private Button btn_detailsAssisted;

    @FXML
    private TextField tfield_surname;

    @FXML
    private TextField tfield_name;

    @FXML
    private TableView<Assisted> resultTable;

    @FXML
    private TableColumn<Assisted, String> colSurname;

    @FXML
    private TableColumn<Assisted, String> colName;

    @FXML
    private TableColumn<Assisted, String> colBirthdate;
    
    /*
     * JavaFX actions
     */
    
    @FXML
    void toDetail(ActionEvent event) {
    	if(event.getSource().equals(btn_addAssisted))
    		selectedAssisted = null;	//resets the previously selected assisted
    	DbUtil.closeEntityManager(this.em);
    	if (selectedAssisted!=null)
    		interfaceMain.setSelectedAssisted(selectedAssisted); // set selectedAssisted in the main
    	interfaceMain.switchScene(MainCallback.Pages.AssistedDetail);	//TODO pass the selected
    }

    @FXML
    void toReport(ActionEvent event) {
    	DbUtil.closeEntityManager(this.em);
    	interfaceMain.switchScene(MainCallback.Pages.Report);
    }

    @FXML
    void toSettings(ActionEvent event) {
    	//DbUtil.closeEntityManager(this.em);	NOTE do *not* do this here, because the scene itsn't reloaded at the exit of "settings", therefore there won't be any EnitityManager
    	interfaceMain.switchScene(MainCallback.Pages.Settings);
    }
    
    @FXML
    void toCalendar(ActionEvent event) {
    	DbUtil.closeEntityManager(this.em);
    	interfaceMain.switchScene(MainCallback.Pages.Calendar);
    }
    
    @FXML
    public void searchAssisted(KeyEvent event) {
    	btn_detailsAssisted.setDisable(true);	//disables "toDetail" button when a new search is made
    	selectedAssisted = null;	//resets the previously selected assisted
    	List<Assisted> assistedsFound = DbUtil.searchAssisted(em, tfield_surname.getText(), tfield_name.getText());
    	bindResults(assistedsFound); 	
    }
    
    @FXML
    void rowSelected(MouseEvent event) {
    	if (event.isPrimaryButtonDown())
    	{
    		Assisted selectedAssited = resultTable.getSelectionModel().getSelectedItem();
    		this.selectedAssisted = selectedAssited;
    		System.out.println("SELECTED ASSISTED: " + this.selectedAssisted);	 //TODO change with a proper logging
    		btn_detailsAssisted.setDisable(false);	//activate "toDetail" button
    	}
    }
    
    /*
     * OTHER METHODS
     */
    
    //binds results to the TableView
    //NOTE each Assisted must at least have Name, Surname and Birthdate
    private void bindResults(List<Assisted> assisteds) {
    	//binds the list
    	ObservableList<Assisted> observableAssisteds = FXCollections.<Assisted>observableArrayList();
    	observableAssisteds.addAll(assisteds);
    	resultTable.setItems(observableAssisteds);
		
    	//sets the columns
    	colSurname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSurname()));
    	colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
    	//TODO fix the following to have an italian format (be aware of consequent new sorting)
    	colBirthdate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthdate().toString()));	
    	
//		final String DATE_FORMAT = "dd/MM/yyyy";
//    	DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
//    	colBirthdate.setCellFactory(cellData -> new TableCell<Assisted, Date>() {
//    	    @Override
//    	    protected void updateItem(Date date, boolean empty) {
//    	        super.updateItem(date, empty);
//    	        if (empty) {
//    	            setText(null);
//    	        } else {
//    	            setText(formatter.format(date));
//    	        }
//    	    }
//    	});
    }
   
}