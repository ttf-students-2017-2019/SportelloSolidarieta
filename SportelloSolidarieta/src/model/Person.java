package model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.NamedQuery;
import org.eclipse.persistence.annotations.Index;
import org.eclipse.persistence.annotations.Indexes;

@Entity
@Indexes({
	@Index(name="Person_Index_SurnameName", columnNames={"SURNAME","NAME"}),
	@Index(name="Person_Index_Name", columnNames={"NAME"})
})
@NamedQueries({
	@NamedQuery(name="Person.findSurnameName", query="SELECT a FROM Person a WHERE a.surname LIKE CONCAT(:surname,'%') AND a.name LIKE CONCAT(:name,'%')"), 
	@NamedQuery(name="Person.findName", query="SELECT a FROM Person a WHERE a.name LIKE CONCAT(:name,'%')"), 
	@NamedQuery(name="Person.findAll", query="SELECT a FROM Person a")
})

public class Person {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private boolean isDeleted;
	@Column(name="SURNAME")	//nb column name needed for the index generation by jpa
	private String surname;
	@Column(name="NAME")
	private String name;
	private char sex;
	private LocalDate birthdate;
	private String nationality;
	private String familyComposition;
	private boolean isReunitedWithFamily;
	private boolean isRefused;

	@OneToMany(mappedBy="person")
	private List<Meeting> meetings;
	
	@OneToMany(mappedBy="person")
	private List<Appointment> appointments;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public char getSex() {
		return sex;
	}
	
	public void setSex(char sex) {
		this.sex = sex;
	}
	
	public LocalDate getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getNationality() {
		return nationality;
	}
	
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	public String getFamilyComposition() {
		return familyComposition;
	}
	
	public void setFamilyComposition(String familyComposition) {
		this.familyComposition = familyComposition;
	}
	
	public boolean getIsReunitedWithFamily() {
		return isReunitedWithFamily;
	}
	
	public void setReunitedWithFamily(boolean isReunitedWithFamily) {
		this.isReunitedWithFamily = isReunitedWithFamily;
	}
	
	public boolean getIsRefused() {
		return isRefused;
	}
	
	public void setRefused(boolean isRefused) {
		this.isRefused = isRefused;
	}
	
	public List<Meeting> getMeetings() {
		return meetings;
	}
	
	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
	}
	
	public List<Appointment> getAppointments() {
		return this.appointments;
	}

	public void setAppointment(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public Appointment addAppuntamento(Appointment appuntamento) {
		getAppointments().add(appuntamento);
		appuntamento.setPerson(this);

		return appuntamento;
	}

	public Appointment removeAppuntamento(Appointment appuntamento) {
		getAppointments().remove(appuntamento);
		appuntamento.setPerson(null);

		return appuntamento;
	}
	
public Person getSamplePerson () 
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SportelloSolidarieta");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		List<Person> res = (List<Person>) em.createNamedQuery("Person.findAll").getResultList();
		em.getTransaction().commit();
		em.close();
		return res.get(0);
	}
	
}