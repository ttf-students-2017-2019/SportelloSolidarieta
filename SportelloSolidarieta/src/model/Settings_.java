package model;

import java.sql.Time;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-03-27T17:02:34.317+0100")
@StaticMetamodel(Settings.class)
public class Settings_ {
	public static volatile SingularAttribute<Settings, Integer> idSetting;
	public static volatile SingularAttribute<Settings, Boolean> fMonday;
	public static volatile SingularAttribute<Settings, Boolean> fTuesday;
	public static volatile SingularAttribute<Settings, Boolean> fWednesday;
	public static volatile SingularAttribute<Settings, Boolean> fThursday;
	public static volatile SingularAttribute<Settings, Boolean> fFriday;
	public static volatile SingularAttribute<Settings, Boolean> fSaturday;
	public static volatile SingularAttribute<Settings, Boolean> fSunday;
	public static volatile SingularAttribute<Settings, Date> dateAppointmentsMeetingsControl;
	public static volatile SingularAttribute<Settings, Integer> appointmentLength;
	public static volatile SingularAttribute<Settings, Integer> maxDailyAppointments;
	public static volatile SingularAttribute<Settings, Time> hStart;
	public static volatile SingularAttribute<Settings, Time> hEnd;
}
