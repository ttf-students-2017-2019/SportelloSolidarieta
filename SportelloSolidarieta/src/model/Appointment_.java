package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-03-31T14:48:22.120+0200")
@StaticMetamodel(Appointment.class)
public class Appointment_ {
	public static volatile SingularAttribute<Appointment, Integer> idAppointment;
	public static volatile SingularAttribute<Appointment, Date> appointmentDateTime;
	public static volatile SingularAttribute<Appointment, Integer> appointmentLength;
	public static volatile SingularAttribute<Appointment, Boolean> fDone;
	public static volatile SingularAttribute<Appointment, Person> person;
}