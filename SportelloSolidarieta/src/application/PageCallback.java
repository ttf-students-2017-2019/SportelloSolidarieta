package application;

import model.Meeting;
import windows.AssistedDetailsController.Operation;

public interface PageCallback {

	Meeting getSelectedMeeting();
	Operation getOperation();
	void refresh();
	
}
