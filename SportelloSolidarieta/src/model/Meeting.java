package model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "Meeting.findMeetings", query = "SELECT m FROM Meeting m WHERE m.fDeleted = false AND m.assisted.surname <> :donationString AND m.date BETWEEN :from AND :to ORDER BY m.date"),
	@NamedQuery(name = "Meeting.findDonations", query = "SELECT m FROM Meeting m WHERE m.fDeleted = false AND m.assisted.surname = :donationString AND m.date BETWEEN :from AND :to ORDER BY m.date"),
	@NamedQuery(name = "Meeting.findMeetingsAndDonations", query = "SELECT m FROM Meeting m WHERE m.fDeleted = false AND m.date BETWEEN :from AND :to ORDER BY m.date")
})
public class Meeting {
	
	public static final String DONATION_STRING = "offerte";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate date;
	@ManyToOne
	@JoinColumn(name = "PERSON_ID")
	private Assisted assisted;
	@Lob
	private String description;
	@Column(precision=38, scale=2)
	private BigDecimal amount;
	private boolean fDeleted;

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

	public Assisted getAssisted() {
		return assisted;
	}

	public void setAssisted(Assisted assisted) {
		this.assisted = assisted;
	}
	
	public String getAssistedSurname() {
		return assisted.getSurname();
	}
	
	public String getAssistedName() {
		return assisted.getName();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean isfDeleted() {
		return fDeleted;
	}

	public void setfDeleted(boolean fDeleted) {
		this.fDeleted = fDeleted;
	}

	@Override
	public String toString() {
		return "Meeting [deleted=" + fDeleted +  ", id=" + id + ", date=" + date + ", description=" + description + ", amount=" + amount + "]";
	}

}