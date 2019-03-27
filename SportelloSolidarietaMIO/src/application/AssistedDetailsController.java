package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AssistedDetailsController {
	
    @FXML
    void toRegistry(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.Registry);
    }

    @FXML
    void toSchedule(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.Schedule);
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
    public AssistedDetailsController(MainCallback interfaceMain)
    {
    	this.interfaceMain = interfaceMain;   
    } 
    
    // Interface to callback the main class
    private MainCallback interfaceMain;	
}
