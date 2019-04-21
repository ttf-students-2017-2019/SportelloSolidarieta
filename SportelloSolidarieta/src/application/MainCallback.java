package application;

import javafx.stage.Stage;
import model.Assisted;
import model.Meeting;

// Services exposed from Main class
public interface MainCallback 
{
	// Identifiers for all application pages
	public enum Page
	{
		ASSISTED_SEARCH,	// User interface page where search for a assisted
		REPORT,				// User interface page to create reports
		SCHEDULE,			// User interface page to schedule an appointment
		ASSISTED_DETAILS,	// User interface page to show assisted details
		SETTINGS,			// User interface page to show settings detail
		CALENDAR,			// User interface page to show calendar
		MEETING_DETAILS,	// User interface page to show meeting details
	}
	
	public enum Operation {
		CREATE, UPDATE
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
	void switchScene(Page requestedPage, PageCallback currentPage);
	
	Stage getStage();
	
	Assisted getSelectedAssisted();
	void setSelectedAssisted(Assisted selectedAssisted);
	
	Meeting getSelectedMeeting();
	void setSelectedMeeting(Meeting selectedMeeting);
	
	Operation getRequestedOperation();
	void setRequestedOperation(Operation requestedOperation);
	
}
