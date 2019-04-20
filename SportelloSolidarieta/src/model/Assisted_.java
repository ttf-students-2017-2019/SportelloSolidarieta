package model;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-04-20T11:06:38.842+0200")
@StaticMetamodel(Assisted.class)
public class Assisted_ {
	public static volatile SingularAttribute<Assisted, Long> id;
	public static volatile SingularAttribute<Assisted, Boolean> isDeleted;
	public static volatile SingularAttribute<Assisted, String> surname;
	public static volatile SingularAttribute<Assisted, String> name;
	public static volatile SingularAttribute<Assisted, Character> sex;
	public static volatile SingularAttribute<Assisted, LocalDate> birthdate;
	public static volatile SingularAttribute<Assisted, String> nationality;
	public static volatile SingularAttribute<Assisted, String> familyComposition;
	public static volatile SingularAttribute<Assisted, Boolean> isReunitedWithFamily;
	public static volatile SingularAttribute<Assisted, Boolean> isRefused;
	public static volatile ListAttribute<Assisted, Meeting> meetings;
	public static volatile ListAttribute<Assisted, Appointment> appointments;
}
