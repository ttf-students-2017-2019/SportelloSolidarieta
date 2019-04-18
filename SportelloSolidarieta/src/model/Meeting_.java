package model;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-04-18T14:46:12.014+0200")
@StaticMetamodel(Meeting.class)
public class Meeting_ {
	public static volatile SingularAttribute<Meeting, Long> id;
	public static volatile SingularAttribute<Meeting, LocalDate> date;
	public static volatile SingularAttribute<Meeting, Assisted> assisted;
	public static volatile SingularAttribute<Meeting, String> description;
	public static volatile SingularAttribute<Meeting, Float> amount;
}
