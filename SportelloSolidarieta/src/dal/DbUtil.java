package dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.mysql.cj.jdbc.MysqlDataSource;

import model.Meeting;
import model.Person;

import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Connection;

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
	
	public static List<Person> getPeople() {
		return null;
	}
	
	public static List<Meeting> getMeetings(LocalDate from, LocalDate to) {
    	EntityManager em = emf.createEntityManager();    	
    	List<Meeting> meetings = em.createNamedQuery("Meeting.findByDate", Meeting.class).setParameter("from", from).setParameter("to", to).getResultList();
    	em.close();
    	return meetings;
	}
	
	public static void savePerson(String name, String surname, LocalDate birthday, Character sex, String nationality, boolean wentbackhome, boolean rejected, String familycomposition) {
		
	}
	

	
	

	
}
