package model;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-04-06T23:47:41.882+0200")
@StaticMetamodel(Person.class)
public class Person_ {
	public static volatile SingularAttribute<Person, Long> id;
	public static volatile SingularAttribute<Person, Boolean> isDeleted;
	public static volatile SingularAttribute<Person, String> surname;
	public static volatile SingularAttribute<Person, String> name;
	public static volatile SingularAttribute<Person, Character> sex;
	public static volatile SingularAttribute<Person, LocalDate> birthdate;
	public static volatile SingularAttribute<Person, String> nationality;
	public static volatile SingularAttribute<Person, String> familyComposition;
	public static volatile SingularAttribute<Person, Boolean> isReunitedWithFamily;
	public static volatile SingularAttribute<Person, Boolean> isRefused;
	public static volatile ListAttribute<Person, Meeting> meetings;
	public static volatile ListAttribute<Person, Appointment> appointments;
}
