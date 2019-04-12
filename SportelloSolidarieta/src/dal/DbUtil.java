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

import model.Assisted;
import model.Meeting;

public class DbUtil {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SportelloSolidarieta");

	public static Connection connect() throws SQLException {
		MysqlDataSource ds = new MysqlDataSource();

		ds.setServerName("localhost");
		ds.setDatabaseName("sportellosolidarieta");
		ds.setUser("root");
		ds.setPassword("mysql");
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
	public static EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	// used by SearchAssistedController
	public static void closeEntityManager(EntityManager em) {
		em.close();
	}

	public static List<Meeting> getMeetings(LocalDate from, LocalDate to) {
		EntityManager em = emf.createEntityManager();
		List<Meeting> meetings = em.createNamedQuery("Meeting.findByDate", Meeting.class).setParameter("from", from)
				.setParameter("to", to).getResultList();
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

}
