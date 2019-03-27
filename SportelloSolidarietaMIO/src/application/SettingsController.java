package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SettingsController {
	
    // Interface to callback the main class
    private MainCallback interfaceMain;	
    
    @FXML
    private Button shedule_ok_button;

    @FXML
    private Button shedule_back_button;

    @FXML
    void showSettings(ActionEvent event) {

    }

    @FXML
    void toRegistry(ActionEvent event) {
    	
    	interfaceMain.switchScene(MainCallback.Pages.Registry);
    }

}
