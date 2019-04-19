package report;

import java.time.LocalDate;

import application.MainCallback.Pages;
import javafx.beans.property.SimpleStringProperty;
import model.Meeting;
import utilities.Formatter;
import application.MainCallback;

public class ObservableMeeting{
	
	public static final String DONATION_STRING = "Donazioni";
	
	private Meeting meeting;
//	private SimpleStringProperty date;
	private SimpleStringProperty assistedSurname;
	private SimpleStringProperty assistedName;
	private SimpleStringProperty description;
	private SimpleStringProperty outgoings;
	private SimpleStringProperty incomes;
		
	public ObservableMeeting(Meeting meeting, Pages page) {
		this.meeting = meeting;
//		date = new SimpleStringProperty(Formatter.formatDate(meeting.getDate()));
		assistedSurname = new SimpleStringProperty(meeting.getAssistedSurname());
		
		switch (page) {
		case Report:
			if (meeting.getAssistedSurname().equals(DONATION_STRING)) {
				assistedName = new SimpleStringProperty("");
				outgoings = new SimpleStringProperty("");
				incomes = new SimpleStringProperty(Formatter.formatNumber(meeting.getAmount()));
			} else {
				assistedName = new SimpleStringProperty(meeting.getAssistedName());
				outgoings = new SimpleStringProperty(Formatter.formatNumber(meeting.getAmount()));
				incomes = new SimpleStringProperty("");
			}
			break;

		case MeetingDetail:
			assistedName = new SimpleStringProperty(meeting.getAssistedName());
			outgoings = new SimpleStringProperty(Formatter.formatNumber(meeting.getAmount()));
			description = new SimpleStringProperty(meeting.getDescription());
			break;
		}
	}

	public LocalDate getDate() {
		return meeting.getDate();
	}
	
	public Meeting getMeeting() {
		return meeting;
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

	public SimpleStringProperty getDescription() {
		return description;
	}

}