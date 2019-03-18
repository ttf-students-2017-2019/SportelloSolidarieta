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
	private boolean fDeleted;

	@Column(name="f_ricongiungimento")
	private boolean fRicongiungimento;

	@Column(name="f_rifiutato")
	private boolean fRifiutato;
	
	@Column(name="foto")
	private String foto;

	@Column(name="nome")
	private String nome;

	//bi-directional many-to-one association to Appuntamento
	@OneToMany(mappedBy="assisted")
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

	public boolean getFDeleted() {
		return this.fDeleted;
	}

	public void setFDeleted(boolean fDeleted) {
		this.fDeleted = fDeleted;
	}

	public boolean getFRicongiungimento() {
		return this.fRicongiungimento;
	}

	public void setFRicongiungimento(boolean fRicongiungimento) {
		this.fRicongiungimento = fRicongiungimento;
	}

	public boolean getFRifiutato() {
		return this.fRifiutato;
	}

	public void setFRifiutato(boolean fRifiutato) {
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
		appuntamento.setAssisted(this);

		return appuntamento;
	}

	public Appointment removeAppuntamento(Appointment appuntamento) {
		getAppuntamentos().remove(appuntamento);
		appuntamento.setAssisted(null);

		return appuntamento;
	}

}