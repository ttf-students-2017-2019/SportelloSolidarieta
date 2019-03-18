package application;

import application.MainCallback.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class RegistryController {

    @FXML
    private ImageView registry_logo;

    @FXML
    private Label registry_label_insert;

    @FXML
    private TextField registry_texfield_name;

    @FXML
    private TextField registry_texfield_surname;

    @FXML
    private Label registry_label_found;

    @FXML
    private Button registry_button_add;

    @FXML
    private Button registry_button_details;

    @FXML
    private Button registry_button_report;

    @FXML
    void toReport(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.Report);
    }
    
    @FXML
    void toDetail(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.AssistedDetail);
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
    public RegistryController(MainCallback interfaceMain)
    {
    	this.interfaceMain = interfaceMain;   
    } 
    
    // Interface to callback the main class
    private MainCallback interfaceMain;
}