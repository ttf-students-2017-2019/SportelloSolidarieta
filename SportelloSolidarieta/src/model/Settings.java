package model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;


/**
 * The persistent class for the impostazioni database table.
 * 
 */
@Entity
@Table(name="impostazioni")
@NamedQuery(name="Settings.findAll", query="SELECT s FROM Settings s")
public class Settings implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_impostazione")
	private int idSetting;
	
	// Flags for default meeting day in the week. Only one per week. Currently Thursday.
	@Column(name="f_lunedi")
	private boolean fMonday;

	@Column(name="f_martedi")
	private boolean fTuesday;

	@Column(name="f_mercoledi")
	private boolean fWednesday;
	
	@Column(name="f_giovedi")
	private boolean fThursday;

	@Column(name="f_venerdi")
	private boolean fFriday;
	
	@Column(name="f_sabato")
	private boolean fSaturday;
	
	@Column(name="f_domenica")
	private boolean fSunday;

	// Date of execution of ObservableAppointment Meeting Control: it is checked if an appointment turned into 
	// a meeting.
	@Temporal(TemporalType.DATE)
	@Column(name="d_controllo_appuntamenti")
	private Date dateAppointmentsMeetingsControl;
	
	// ObservableAppointment length minutes. Currently 10 minutes
	@Column(name="durata")
	private int appointmentLength;
	
	// Maximum number of appointment in a day. Currently 20 appointments per day.  
	@Column(name="max_appuntamenti")
	private int maxDailyAppointments;
	
	// Start of working day. Currently 07:00.
	@Column(name="h_inizio")
	private Time hStart;

	// End of working day. Currently 17:00.
	@Column(name="h_fine")
	private Time hEnd;

	public Settings() {
	}
	
	//General Settings getters and setters
	public int getIdSetting() {
		return this.idSetting;
	}

	public void setIdSetting(int idSetting) {
		this.idSetting = idSetting;
	}

	public Date getDateAppointmentsMeetingsControl() {
		return this.dateAppointmentsMeetingsControl;
	}

	public void setDateAppointmentsMeetingsControl(Date dateAppointmentsMeetingsControl) {
		this.dateAppointmentsMeetingsControl = dateAppointmentsMeetingsControl;
	}

	public int getAppointmentLength() {
		return this.appointmentLength;
	}

	public void setAppointmentLength(int appointmentLength) {
		this.appointmentLength = appointmentLength;
	}
	
	public Time getHEnd() {
		return this.hEnd;
	}

	public void setHEnd(Time hEnd) {
		this.hEnd = hEnd;
	}

	public Time getHStart() {
		return this.hStart;
	}

	public void setHStart(Time hStart) {
		this.hStart = hStart;
	}

	public int getMaxDailyAppointments() {
		return this.maxDailyAppointments;
	}

	public void setMaxDailyAppointments(int maxDailyAppointments) {
		this.maxDailyAppointments = maxDailyAppointments;
	}
		
	public boolean getFMonday() {
		return this.fMonday;
	}

	public void setFMonday(boolean fMonday) {
		this.fMonday = fMonday;
	}
	
	public boolean getFTuesday() {
		return this.fTuesday;
	}

	public void setFTuesday(boolean fTuesday) {
		this.fTuesday = fTuesday;
	}
	
	public boolean getFWednesday() {
		return this.fWednesday;
	}

	public void setFWednesday(boolean fWednesday) {
		this.fWednesday = fWednesday;
	}
	
	public boolean getFThursday() {
		return this.fThursday;
	}

	public void setFThursday(boolean fThursday) {
		this.fThursday = fThursday;
	}

	public boolean getFFriday() {
		return this.fFriday;
	}

	public void setFFriday(boolean fFriday) {
		this.fFriday = fFriday;
	}

	public boolean getFSaturday() {
		return this.fSaturday;
	}

	public void setFSaturday(boolean fSaturday) {
		this.fSaturday = fSaturday;
	}
	
	public boolean getFSunday() {
		return this.fSunday;
	}

	public void setFSunday(boolean fSunday) {
		this.fSunday = fSunday;
	}

	public static Settings findAllSettings() {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SportelloSolidarieta");
		EntityManager em = emf.createEntityManager();
		Query query =  em.createNamedQuery("Settings.findAll");

		em.getTransaction().begin();
		Settings currentSettings = (Settings) query.getSingleResult();
		em.getTransaction().commit();
		em.close();
		
		if (currentSettings != null) {
			return currentSettings;			
		}
		
		return null;
	}
	
	@Override
	public String toString() 
	{
		return "Durata appuntamenti: " + getAppointmentLength() + " minuti "
				+ " F Lunedi: " + getFMonday() + "F Martedi: " + getFTuesday()
				+ " F Mercoledi: " + getFWednesday() + " F Giovedi: " + getFThursday()
				+ " ... " + " Max appuntamenti: " + getMaxDailyAppointments();
	}
}