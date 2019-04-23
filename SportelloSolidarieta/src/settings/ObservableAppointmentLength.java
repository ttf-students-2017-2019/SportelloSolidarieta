package settings;

import javafx.beans.property.SimpleStringProperty;

public class ObservableAppointmentLength {
	
	public SimpleStringProperty lengthString = new SimpleStringProperty();
	
	private int lengthValue;
	
	public ObservableAppointmentLength(int length) 
	{
		// Length to string adding a 0 if single digit
		String textToset = String.valueOf(length);
		if (textToset.length()==1)
			textToset = "0"+ textToset;
		
		this.lengthString.set(textToset + " minuti");
		
		setLengthValue(length);
	}

	public int getLengthValue() {
		return lengthValue;
	}

	public void setLengthValue(int lengthValue) {
		this.lengthValue = lengthValue;
	}
	
	@Override
	public String toString() 
	{
		return lengthString.get();
	}

}
