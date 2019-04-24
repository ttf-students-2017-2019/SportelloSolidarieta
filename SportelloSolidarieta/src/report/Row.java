package report;

import java.time.LocalDate;

import model.Meeting;
import utilities.Formatter;

public class Row {
		
	private Meeting meeting;
	private String outgoings = "";
	private String incomes = "";
		
	public Row(Meeting meeting) {
		this.meeting = meeting;
		if (meeting.getAssistedSurname().equals(Meeting.DONATION_STRING_1) || meeting.getAssistedSurname().equals(Meeting.DONATION_STRING_2)) {
			incomes = Formatter.formatNumber(meeting.getAmount().toString());
		} else {
			outgoings = Formatter.formatNumber(meeting.getAmount().toString());
		}
	}
	
	public Meeting getMeeting() {
		return meeting;
	}
	
	public String getAssistedSurname() {
		return meeting.getAssistedSurname();
	}

	public String getAssistedName() {
		return meeting.getAssistedName() == null ? "" : meeting.getAssistedName();
	}

	public LocalDate getDate() {
		return meeting.getDate();
	}

	public String getOutgoings() {
		return outgoings;
	}

	public String getIncomes() {
		return incomes;
	}

}