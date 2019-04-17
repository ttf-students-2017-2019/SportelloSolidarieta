package dal;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.mysql.cj.jdbc.MysqlDataSource;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Meeting;
import report.ObservableMeeting;
import model.Assisted;
import model.Meeting;

public class DbUtil {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SportelloSolidarieta");

	public static Connection connect() throws SQLException {
		MysqlDataSource ds = null;
		try {
			ds = new MysqlDataSource();

			ds.setServerName("localhost");
			ds.setDatabaseName("sportellosolidarieta");
			ds.setUser("root");
			ds.setPassword("mysql");
		} catch (Exception e) {
			DbUtil.showAlertDatabaseError();
			throw new SQLException();
		}
		return ds.getConnection();
	}

	public static void disconnect(Connection dbConnection) {
		try {
			dbConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbConnection = null;
		}
	}

	// used by SearchAssistedController
	public static EntityManager getEntityManager() throws Exception {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
		} catch (Exception e) {
			// TODO change with proper logging
			System.out.println(DbUtil.class.getName() + " getEntityManager():" + e);
			throw e;
		}
		return em;
	}

	// used by SearchAssistedController
	public static void closeEntityManager(EntityManager em) {
		em.close();
	}

	public static List<Meeting> getMeetings(LocalDate from, LocalDate to) {
		EntityManager em = emf.createEntityManager();
		List<Meeting> meetings = em.createNamedQuery("Meeting.findMeetings", Meeting.class)
				.setParameter("donationString", ObservableMeeting.DONATION_STRING).setParameter("from", from)
				.setParameter("to", to).getResultList();
		em.close();
		return meetings;
	}

	public static List<Meeting> getDonations(LocalDate from, LocalDate to) {
		EntityManager em = emf.createEntityManager();
		List<Meeting> meetings = em.createNamedQuery("Meeting.findDonations", Meeting.class)
				.setParameter("donationString", ObservableMeeting.DONATION_STRING).setParameter("from", from)
				.setParameter("to", to).getResultList();
		em.close();
		return meetings;
	}

	public static List<Meeting> getMeetingsAndDonations(LocalDate from, LocalDate to) {
		EntityManager em = emf.createEntityManager();
		List<Meeting> meetings = em.createNamedQuery("Meeting.findMeetingsAndDonations", Meeting.class)
				.setParameter("from", from).setParameter("to", to).getResultList();
		em.close();
		return meetings;
	}

	public static void saveAssisted(Assisted toSave) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.merge(toSave);
		em.getTransaction().commit();
		em.close();
	}

	// If no result, doesn't return null but returns an empty list
	public static List<Assisted> searchAssisted(EntityManager em, String surname, String name) {

		Query query;
		List<Assisted> assisteds;

		boolean surnameEmpty = (surname == null || surname.equals(""));
		boolean nameEmpty = (name == null || name.equals(""));

		if (surnameEmpty && nameEmpty) {
			assisteds = null;
		} else if (surnameEmpty) {
			query = em.createNamedQuery("Assisted.findName");
			query.setParameter("name", name);
			assisteds = query.getResultList(); // NOTE: this returns null if no result
		} else {
			query = em.createNamedQuery("Assisted.findSurnameName");
			query.setParameter("surname", surname);
			query.setParameter("name", name);
			assisteds = query.getResultList();
		}

		if (assisteds == null) { // that is, no search or no result
			assisteds = new ArrayList<Assisted>(); // build an empty list, to prevent NullPointerExceptions later

			// TODO change with proper logging
			System.out.println("**QUERY RESULT OF \"SEARCH ASSISTED\": no results**");
		} else {
			// TODO change with proper logging
			System.out.println("**QUERY RESULT OF \"SEARCH ASSISTED\"**");
			for (Assisted a : assisteds)
				System.out.println(a.getSurname() + " " + a.getName() + " " + a.getBirthdate());
			System.out.println("**/QUERY RESULT OF \"SEARCH ASSISTED\"**");
		}

		return assisteds;
	}

	public static void showAlertDatabaseError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(null);
		alert.setHeaderText("Errore di connessione al database");
		alert.setContentText(null);

		alert.showAndWait();
	}

}
