package schedule;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.converter.DateTimeStringConverter;

public class ObservableSlot {
	
	private static final String FREE_SLOT = "Libero";
	private static final String TAKEN_SLOT = "Assegnato";
	private static final String EMPTY_STRING = "";
	
	public SimpleIntegerProperty appointmentLength;
	public SimpleStringProperty status;
	public SimpleStringProperty appointmentTimeDate;
	public SimpleStringProperty assistedOwner;
	
	private Slot associatedSlot;
	
	public ObservableSlot(Slot currentSlot) {
		
		this.appointmentLength = new SimpleIntegerProperty(currentSlot.getAppointmentLength());
				
		if (currentSlot.getAppointmentAssistedOwner()==null) {
			this.status = new SimpleStringProperty(FREE_SLOT);
			this.assistedOwner = new SimpleStringProperty(EMPTY_STRING);
			
		}
		else {
			this.status = new SimpleStringProperty(TAKEN_SLOT);
			this.assistedOwner = new SimpleStringProperty(currentSlot.getAppointmentAssistedOwner().getNome() + " " + currentSlot.getAppointmentAssistedOwner().getCognome());
		}
		
		DateTimeStringConverter converter = new DateTimeStringConverter();
		
		this.appointmentTimeDate = new SimpleStringProperty(converter.toString(currentSlot.getDateTime().getTime()));
		
		this.associatedSlot= currentSlot;
	}
	
}
