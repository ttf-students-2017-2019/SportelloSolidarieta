package application;

import model.Meeting;

public interface PageCallback {

	public Meeting getSelectedMeeting();
	
	public void setSelectedMeeting(Meeting meeting);
	
	public Boolean getMeetingtoModify();
	
	public void setMeetingToMofify(Boolean meetengToModify);
	
	void refresh();
	
}
