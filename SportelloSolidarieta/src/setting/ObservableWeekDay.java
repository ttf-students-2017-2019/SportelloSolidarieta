package setting;

import java.util.Calendar;

import javafx.beans.property.SimpleStringProperty;

public class ObservableWeekDay {
	
	public SimpleStringProperty dayName = new SimpleStringProperty();
	
	private int weekDay;
	
	public ObservableWeekDay(int dayOfWeek) {
		
		switch (dayOfWeek) {
		  case Calendar.MONDAY:
		  {
				this.dayName.set("Lunedì");
		        this.weekDay = Calendar.MONDAY;
				break;
		  }  
		  case Calendar.TUESDAY:
		  {
			  	this.dayName.set("Martedì");
			  	this.weekDay = Calendar.TUESDAY;
		        break;
		  }
		  case Calendar.WEDNESDAY:
		  {
			  	this.dayName.set("Mercoledì");
			  	this.weekDay = Calendar.WEDNESDAY;
		        break;
		  }
		  case Calendar.THURSDAY:
		  {
				this.dayName.set("Giovedì");
				this.weekDay = Calendar.THURSDAY;
		        break;
		  }  
		  case Calendar.FRIDAY:
		  {
			  	this.dayName.set("Venerdì");
			  	this.weekDay = Calendar.FRIDAY;
		        break; 
		  }
		  case Calendar.SATURDAY:
		  {
			  	this.dayName.set("Sabato");
			  	this.weekDay = Calendar.SATURDAY;
		        break; 
		  }

		  case Calendar.SUNDAY:
		  {
			  	this.dayName.set("Domenica");
			  	this.weekDay = Calendar.SUNDAY;
		        break;  
		  }
		}
		
	}
	
	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}
	
	@Override
	public String toString() 
	{
		return dayName.get();
	}
}
