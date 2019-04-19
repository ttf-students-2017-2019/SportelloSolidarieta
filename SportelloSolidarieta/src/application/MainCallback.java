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
		SEARCH_ASSISTED,	// User interface page where search for a assisted
		REPORT,				// User interface page to create reports
		SCHEDULE,			// User interface page to schedule an appointment
		ASSISTED_DETAIL,	// User interface page to show assisted details
		SETTINGS,			// User interface page to show settings detail
		CALENDAR,			// User interface page to show calendar
		MEETING_DETAILS,	// User interface page to show meeting details
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
	public void switchScene(Page requestedPage, PageCallback currentPage);
	
	public Stage getStage();
	
	public Assisted getSelectedAssisted();
	
	public void setSelectedAssisted(Assisted assisted);
	
	public Meeting getSelectedMeeting();
	
	public void setSelectedMeeting(Meeting meeting);
}
