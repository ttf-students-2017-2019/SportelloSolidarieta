package application;

import javafx.stage.Stage;
import model.Assisted;
import model.Meeting;

// Services exposed from Main class
public interface MainCallback 
{
	// Identifiers for all application pages
	public enum Pages
	{
		SearchPerson,		// User interface page where search for a assisted
		Report,				// User interface page to create reports
		Schedule,			// User interface page to schedule an appointment
		AssistedDetail,		// User interface page to show assisted details
		Settings,			// User interface page to show settings detail
		Calendar,			// User interface page to show calendar
		MeetingDetail,		// User interface page to show meeting detail				
	}
	
	//
    // Require the swap of the user interface page
	//
	// parameters	
	//		requiredPage 		new page to show
	//
	// returned
	//		none
	//	
	public void switchScene(Pages requiredPage);
	
	public Stage getStage();
	
	public Assisted getSelectedAssisted();
	
	public void setSelectedAssisted(Assisted assisted);
	
	public Meeting getSelectedMeeting();
	
	public void setSelectedMeeting(Meeting meeting);
}
