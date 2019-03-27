package model;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-03-27T17:02:34.310+0100")
@StaticMetamodel(Meeting.class)
public class Meeting_ {
	public static volatile SingularAttribute<Meeting, Long> id;
	public static volatile SingularAttribute<Meeting, LocalDate> date;
	public static volatile SingularAttribute<Meeting, Person> person;
	public static volatile SingularAttribute<Meeting, String> description;
	public static volatile SingularAttribute<Meeting, Float> amount;
}
