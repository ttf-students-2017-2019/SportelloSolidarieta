package setting;

import javafx.beans.property.SimpleStringProperty;

public class ObservableHour {
	
	public SimpleStringProperty hourString = new SimpleStringProperty();
	
	public ObservableHour(int hour) 
	{
		// Hour to string adding a 0 if single digit
		String textToset = String.valueOf(hour);
		if (textToset.length()==1)
			textToset = "0"+ textToset;
		
		this.hourString.set(textToset);
	}

	@Override
	public String toString() 
	{
		return hourString.get();
	}
}
