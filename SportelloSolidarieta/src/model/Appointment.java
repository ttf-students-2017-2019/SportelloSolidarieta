package model;

import java.io.Serializable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the appuntamento database table.
 * 
 */
@Entity
@Table(name="appuntamento")
@NamedQuery(name="Appointment.findAll", query="SELECT a FROM Appointment a")
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
	
	@Override
	public String toString() 
	{
		return getIdAppointment() + " " + getAppointmentDateTime().toString();
	}
}