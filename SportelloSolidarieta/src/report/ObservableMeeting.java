package report;

import javafx.beans.property.SimpleStringProperty;
import model.Meeting;
import utilities.Formatter;

public class ObservableMeeting {
	
	public static final String DONATION_STRING = "Donazioni";
	
	private SimpleStringProperty date;
	private SimpleStringProperty assistedSurname;
	private SimpleStringProperty assistedName;
	private SimpleStringProperty outgoings;
	private SimpleStringProperty incomes;
	
	public ObservableMeeting(Meeting meeting) {
		date = new SimpleStringProperty(Formatter.formatDate(meeting.getDate()));
		assistedSurname = new SimpleStringProperty(meeting.getAssistedSurname());
		assistedName = new SimpleStringProperty(meeting.getAssistedName());
		if (meeting.getAssistedSurname().equals(DONATION_STRING)) {
			incomes = new SimpleStringProperty(Formatter.formatNumber(meeting.getAmount()));
		} else {
			outgoings = new SimpleStringProperty(Formatter.formatNumber(meeting.getAmount()));
		}
	}

	public SimpleStringProperty getDate() {
		return date;
	}

	public SimpleStringProperty getAssistedSurname() {
		return assistedSurname;
	}

	public SimpleStringProperty getAssistedName() {
		return assistedName;
	}

	public SimpleStringProperty getOutgoings() {
		return outgoings;
	}

	public SimpleStringProperty getIncomes() {
		return incomes;
	}

}
