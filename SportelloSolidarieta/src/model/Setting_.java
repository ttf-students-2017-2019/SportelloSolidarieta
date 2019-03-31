package model;

import java.sql.Time;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-03-31T14:48:22.130+0200")
@StaticMetamodel(Setting.class)
public class Setting_ {
	public static volatile SingularAttribute<Setting, Integer> idSetting;
	public static volatile SingularAttribute<Setting, Boolean> fMonday;
	public static volatile SingularAttribute<Setting, Boolean> fTuesday;
	public static volatile SingularAttribute<Setting, Boolean> fWednesday;
	public static volatile SingularAttribute<Setting, Boolean> fThursday;
	public static volatile SingularAttribute<Setting, Boolean> fFriday;
	public static volatile SingularAttribute<Setting, Boolean> fSaturday;
	public static volatile SingularAttribute<Setting, Boolean> fSunday;
	public static volatile SingularAttribute<Setting, Date> dateAppointmentsMeetingsControl;
	public static volatile SingularAttribute<Setting, Integer> appointmentLength;
	public static volatile SingularAttribute<Setting, Integer> maxDailyAppointments;
	public static volatile SingularAttribute<Setting, Time> hStart;
	public static volatile SingularAttribute<Setting, Time> hEnd;
}
