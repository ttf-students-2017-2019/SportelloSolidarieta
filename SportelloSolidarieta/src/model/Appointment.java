package model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the appuntamento database table.
 * 
 */
@Entity
@Table(name="appuntamento")
@NamedQueries({
	@NamedQuery(name="Appointment.findAll", query="SELECT a FROM Appointment a"),
	@NamedQuery(name="Appointment.findAppointmentsByDate", 
					query="SELECT a FROM Appointment a WHERE a.appointmentDateTime >= :begin AND a.appointmentDateTime < :end "
							+ " ORDER BY a.appointmentDateTime")
})	

public class Appointment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_appuntamento")
	private int idAppointment;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_ora_appuntamento")
	private Date appointmentDateTime;

	@Column(name="f_effettuato")
	private boolean fDone;

	//bi-directional many-to-one association to Assistito
	@ManyToOne
	@JoinColumn(name="id_assistito")
	private Assisted assisted;

	public Appointment() {
	}

	public int getIdAppointment() {
		return this.idAppointment;
	}

	public void setIdAppointment(int idAppointment) {
		this.idAppointment = idAppointment;
	}

	public Date getAppointmentDateTime() {
		return this.appointmentDateTime;
	}

	public void setAppointmentDateTime(Date appointmentDateTime) {
		this.appointmentDateTime = appointmentDateTime;
	}

	public boolean getFDone() {
		return this.fDone;
	}

	public void setFDone(boolean fDone) {
		this.fDone = fDone;
	}

	public Assisted getAssisted() {
		return this.assisted;
	}

	public void setAssisted(Assisted assisted) {
		this.assisted = assisted;
	}
	
	// Other methods
	public static List<Appointment> findAllAppointments() {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SportelloSolidarieta");
		EntityManager em = emf.createEntityManager();
		Query query =  em.createNamedQuery("Appointment.findAll");

		em.getTransaction().begin();
		List<Appointment> appointments = (List<Appointment>) query.getResultList();
		em.getTransaction().commit();
		em.close();
		
		if (appointments != null) {
			return appointments;			
		}
		
		return null;
	}

	public static List<Appointment> findAppointmentsByDate(Date searchDate) {
		
		// Adding a day to the selected day 
		Calendar c = Calendar.getInstance();
		c.setTime(searchDate);
		c.add(Calendar.DATE, 1);
		Date searchDayPlusOne = c.getTime();
		
		System.out.println(searchDate.toString() + " -> " + searchDayPlusOne.toString());
		
		// Setting the queries parameter
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SportelloSolidarieta");
		EntityManager em = emf.createEntityManager();
		Query query =  em.createNamedQuery("Appointment.findAppointmentsByDate");
		query.setParameter("begin", searchDate).setParameter("end", searchDayPlusOne);
	
		// Getting the results from db
		em.getTransaction().begin();
		List<Appointment> appointmentsOfTheDay = (List<Appointment>) query.getResultList();
		em.getTransaction().commit();
		em.close();
			
		if (appointmentsOfTheDay != null) {
			return appointmentsOfTheDay;			
		}
		
		return null;
	}
		
	@Override
	public String toString() 
	{
		return getIdAppointment() + " " + getAppointmentDateTime().toString();
	}
}