package model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name="Meeting.findByDate", query="SELECT m FROM Meeting m WHERE m.date BETWEEN :from AND :to ORDER BY m.date") 
public class Meeting {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private LocalDate date;
	@ManyToOne
	@JoinColumn(name="PERSON_ID")
	private Assisted assisted;
	private String description;
	private float amount;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Assisted getPerson() {
		return assisted;
	}

	public void setPerson(Assisted assisted) {
		this.assisted = assisted;
	}
	
	public String getPersonName() {
		return assisted.getName();
	}
	
	public String getPersonSurname() {
		return assisted.getSurname();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getAmount() {
		return amount;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
}