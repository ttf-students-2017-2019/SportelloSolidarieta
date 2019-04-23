package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-04-23T11:17:12.778+0200")
@StaticMetamodel(Meeting.class)
public class Meeting_ {
	public static volatile SingularAttribute<Meeting, Long> id;
	public static volatile SingularAttribute<Meeting, LocalDate> date;
	public static volatile SingularAttribute<Meeting, Assisted> assisted;
	public static volatile SingularAttribute<Meeting, String> description;
	public static volatile SingularAttribute<Meeting, BigDecimal> amount;
	public static volatile SingularAttribute<Meeting, Boolean> fDeleted;
}
