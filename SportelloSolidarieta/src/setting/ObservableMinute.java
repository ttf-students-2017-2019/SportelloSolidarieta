package setting;

import javafx.beans.property.SimpleStringProperty;

public class ObservableMinute {

public SimpleStringProperty minuteString = new SimpleStringProperty();
	
	public ObservableMinute(int hour) 
	{
		// Hour to string adding a 0 if single digit
		String textToset = String.valueOf(hour);
		if (textToset.length()==1)
			textToset = "0"+ textToset;
		
		this.minuteString.set(textToset);
	}

	@Override
	public String toString() 
	{
		return minuteString.get();
	}
}
