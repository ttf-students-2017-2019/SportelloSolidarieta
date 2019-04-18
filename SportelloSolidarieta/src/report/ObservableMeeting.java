package report;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import model.Meeting;
import utilities.Formatter;

public class ObservableMeeting {
	
	public static final String DONATION_STRING = "Donazioni";
	
	private Meeting meeting;
//	private SimpleStringProperty date;
	private SimpleStringProperty assistedSurname;
	private SimpleStringProperty assistedName;
	private SimpleStringProperty outgoings;
	private SimpleStringProperty incomes;
	
	public ObservableMeeting(Meeting meeting) {
		this.meeting = meeting;
//		date = new SimpleStringProperty(Formatter.formatDate(meeting.getDate()));
		assistedSurname = new SimpleStringProperty(meeting.getAssistedSurname());
		if (meeting.getAssistedSurname().equals(DONATION_STRING)) {
			assistedName = new SimpleStringProperty("");
			outgoings = new SimpleStringProperty("");
			incomes = new SimpleStringProperty(Formatter.formatNumber(meeting.getAmount()));
		} else {
			assistedName = new SimpleStringProperty(meeting.getAssistedName());
			outgoings = new SimpleStringProperty(Formatter.formatNumber(meeting.getAmount()));
			incomes = new SimpleStringProperty("");
		}
		System.out.println(this.toString());
	}

	public LocalDate getDate() {
		return meeting.getDate();
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