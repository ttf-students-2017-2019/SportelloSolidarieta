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
	private byte fMonday;

	@Column(name="f_martedi")
	private byte fTuesday;

	@Column(name="f_mercoledi")
	private byte fWednesday;
	
	@Column(name="f_giovedi")
	private byte fThursday;

	@Column(name="f_venerdi")
	private byte fFriday;
	
	@Column(name="f_sabato")
	private byte fSaturday;
	
	@Column(name="f_domenica")
	private byte fSunday;

	// Date of execution of Appointment Meeting Control: it is checked if an appointment turned into 
	// a meeting.
	@Temporal(TemporalType.DATE)
	@Column(name="d_controllo_appuntamenti")
	private Date dateAppointmentsMeetingsControl;
	
	// Appointment length minutes. Currently 10 minutes
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
		
	public byte getMonday() {
		return this.fMonday;
	}

	public void setMonday(byte fMonday) {
		this.fMonday = fMonday;
	}
	
	public byte getFTuesday() {
		return this.fTuesday;
	}

	public void setFTuesday(byte fTuesday) {
		this.fTuesday = fTuesday;
	}
	
	public byte getFWednesday() {
		return this.fWednesday;
	}

	public void setFWednesday(byte fWednesday) {
		this.fWednesday = fWednesday;
	}
	
	public byte getFThursday() {
		return this.fThursday;
	}

	public void setFThursday(byte fThursday) {
		this.fThursday = fThursday;
	}

	public byte getFFriday() {
		return this.fFriday;
	}

	public void setFFriday(byte fFriday) {
		this.fFriday = fFriday;
	}

	public byte getFSaturday() {
		return this.fSaturday;
	}

	public void setFSaturday(byte fSaturday) {
		this.fSaturday = fSaturday;
	}
	
	public byte getFSunday() {
		return this.fSunday;
	}

	public void setFSunday(byte fSunday) {
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

}