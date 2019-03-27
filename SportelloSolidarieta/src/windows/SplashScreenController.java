package windows;

import java.util.Timer;
import java.util.TimerTask;

import application.MainCallback;
import javafx.fxml.FXML;

public class SplashScreenController {
	
    @FXML
    private void initialize() 
    {
    	// Start timer to auto-hide this scene
    	timerAutoHide = new Timer();
    	timerAutoHide.schedule(new TimerTask() 
    	{
            @Override
            public void run() 
            {
                interfaceMain.switchScene(MainCallback.Pages.SearchPerson);
            }
        }, 3000);      
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
    public SplashScreenController(MainCallback interfaceMain)
    {
    	this.interfaceMain = interfaceMain;   
    } 
    
    // Interface to callback the main class
    private MainCallback interfaceMain;
    
    // Timer to auto-hide this scene
    private Timer timerAutoHide;	
}
