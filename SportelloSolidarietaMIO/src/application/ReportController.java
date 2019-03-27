package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ReportController {

    @FXML
    private DatePicker to;

    @FXML
    private DatePicker from;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableColumn<?, ?> surname;

    @FXML
    private TableColumn<?, ?> date;

    @FXML
    private TableColumn<?, ?> amount;

    @FXML
    private Button print;

    @FXML
    private Label total;

    @FXML
    private Button back;
    

    @FXML
    void toRegistry(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.Registry);
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
    public ReportController(MainCallback interfaceMain)
    {
    	this.interfaceMain = interfaceMain;   
    } 
    
    // Interface to callback the main class
    private MainCallback interfaceMain; 
}
