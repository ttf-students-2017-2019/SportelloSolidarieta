package windows;

import java.util.Timer;
import java.util.TimerTask;

import application.MainCallback;
import javafx.fxml.FXML;

public class SplashScreenController {
	
	/*
	 * MEMBERS
	 */
	
    private MainCallback main; // Interface to callback the main class
    private Timer timerAutoHide; // Timer to auto-hide this scene
    
	/*
	 * CONSTRUCTOR
	 */
    
    public SplashScreenController(MainCallback main)
    {
    	this.main = main;   
    }
	    
	/*
	 * SCENE INITIALIZATION
	 */
    
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
                main.switchScene(MainCallback.Page.SEARCH_ASSISTED, null);
            }
        }, 1500);      
    }
    	
}
