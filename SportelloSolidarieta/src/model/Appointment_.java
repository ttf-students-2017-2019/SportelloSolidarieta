package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-03-29T14:11:46.634+0100")
@StaticMetamodel(Appointment.class)
public class Appointment_ {
	public static volatile SingularAttribute<Appointment, Integer> idAppointment;
	public static volatile SingularAttribute<Appointment, Date> appointmentDateTime;
	public static volatile SingularAttribute<Appointment, Integer> appointmentLength;
	public static volatile SingularAttribute<Appointment, Boolean> fDone;
	public static volatile SingularAttribute<Appointment, Person> person;
}
