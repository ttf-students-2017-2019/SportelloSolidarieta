package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the assistito database table.
 * 
 */
@Entity
@Table(name="assistito")
@NamedQuery(name="Assisted.findAll", query="SELECT a FROM Assisted a")
public class Assisted implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_assistito")
	private int idAssistito;

	@Column(name="c_nazionalita")
	private String cNazionalita;

	@Column(name="c_sesso")
	private String cSesso;

	private String cognome;

	@Temporal(TemporalType.DATE)
	@Column(name="d_nascita")
	private Date dNascita;

	@Column(name="f_deleted")
	private byte fDeleted;

	@Column(name="f_ricongiungimento")
	private byte fRicongiungimento;

	@Column(name="f_rifiutato")
	private byte fRifiutato;

	private String foto;

	private String nome;

	//bi-directional many-to-one association to Appuntamento
	@OneToMany(mappedBy="assistito")
	private List<Appointment> appuntamentos;

	public Assisted() {
	}

	public int getIdAssistito() {
		return this.idAssistito;
	}

	public void setIdAssistito(int idAssistito) {
		this.idAssistito = idAssistito;
	}

	public String getCNazionalita() {
		return this.cNazionalita;
	}

	public void setCNazionalita(String cNazionalita) {
		this.cNazionalita = cNazionalita;
	}

	public String getCSesso() {
		return this.cSesso;
	}

	public void setCSesso(String cSesso) {
		this.cSesso = cSesso;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getDNascita() {
		return this.dNascita;
	}

	public void setDNascita(Date dNascita) {
		this.dNascita = dNascita;
	}

	public byte getFDeleted() {
		return this.fDeleted;
	}

	public void setFDeleted(byte fDeleted) {
		this.fDeleted = fDeleted;
	}

	public byte getFRicongiungimento() {
		return this.fRicongiungimento;
	}

	public void setFRicongiungimento(byte fRicongiungimento) {
		this.fRicongiungimento = fRicongiungimento;
	}

	public byte getFRifiutato() {
		return this.fRifiutato;
	}

	public void setFRifiutato(byte fRifiutato) {
		this.fRifiutato = fRifiutato;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Appointment> getAppuntamentos() {
		return this.appuntamentos;
	}

	public void setAppuntamentos(List<Appointment> appuntamentos) {
		this.appuntamentos = appuntamentos;
	}

	public Appointment addAppuntamento(Appointment appuntamento) {
		getAppuntamentos().add(appuntamento);
		appuntamento.setAssistito(this);

		return appuntamento;
	}

	public Appointment removeAppuntamento(Appointment appuntamento) {
		getAppuntamentos().remove(appuntamento);
		appuntamento.setAssistito(null);

		return appuntamento;
	}

}