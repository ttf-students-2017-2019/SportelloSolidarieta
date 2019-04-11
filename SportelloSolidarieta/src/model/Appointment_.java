package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-04-11T17:47:06.818+0200")
@StaticMetamodel(Appointment.class)
public class Appointment_ {
	public static volatile SingularAttribute<Appointment, Integer> idAppointment;
	public static volatile SingularAttribute<Appointment, Date> appointmentDateTime;
	public static volatile SingularAttribute<Appointment, Integer> appointmentLength;
	public static volatile SingularAttribute<Appointment, Boolean> fDone;
	public static volatile SingularAttribute<Appointment, Assisted> assisted;
	public static volatile SingularAttribute<Appointment, Boolean> fDeleted;
}
