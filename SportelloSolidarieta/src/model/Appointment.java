package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


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
	private int idAppuntamento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_ora_appuntamento")
	private Date dataOraAppuntamento;

	@Column(name="f_effettuato")
	private byte fEffettuato;

	//bi-directional many-to-one association to Assistito
	@ManyToOne
	@JoinColumn(name="id_assistito")
	private Assisted assistito;

	public Appointment() {
	}

	public int getIdAppuntamento() {
		return this.idAppuntamento;
	}

	public void setIdAppuntamento(int idAppuntamento) {
		this.idAppuntamento = idAppuntamento;
	}

	public Date getDataOraAppuntamento() {
		return this.dataOraAppuntamento;
	}

	public void setDataOraAppuntamento(Date dataOraAppuntamento) {
		this.dataOraAppuntamento = dataOraAppuntamento;
	}

	public byte getFEffettuato() {
		return this.fEffettuato;
	}

	public void setFEffettuato(byte fEffettuato) {
		this.fEffettuato = fEffettuato;
	}

	public Assisted getAssistito() {
		return this.assistito;
	}

	public void setAssistito(Assisted assistito) {
		this.assistito = assistito;
	}

}