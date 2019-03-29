package windows;

import java.util.List;

import javax.persistence.*;

import application.MainCallback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import model.Person;

public class SearchPersonController {
		
    @FXML
    private Button btn_report;
    
    @FXML
    private Button btn_settings;
    
    @FXML
    private Button btn_addAssisted;

    @FXML
    private Button btn_detailsAssisted;

    @FXML
    private TextField tfield_surname;

    @FXML
    private TextField tfield_name;

    @FXML
    void toReport(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.Report);
    }
    
    @FXML
    void toSettings(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.Settings);
    }
    
    @FXML
    void toDetail(ActionEvent event) 
    {
    	interfaceMain.switchScene(MainCallback.Pages.AssistedDetail);
    }
    
    @FXML
    public void searchAssited(KeyEvent event) {
    	Query query;
    	
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("SportelloSolidarieta");
	    EntityManager entityManager = emfactory.createEntityManager();
	      	
    	boolean surnameEmpty = tfield_surname == null || tfield_surname.getText().equals("");
    	boolean nameEmpty = tfield_name == null || tfield_name.getText().equals("");
    	    	
    	if(surnameEmpty && nameEmpty)
    		return;	//do nothing
    	else if (surnameEmpty) {
    		query = entityManager.createNamedQuery("Person.findName");
    		query.setParameter("name", tfield_name.getText());
    	}
    	else {
			query = entityManager.createNamedQuery("Person.findSurnameName");
			query.setParameter("surname", tfield_surname.getText());
			query.setParameter("name", tfield_name.getText());
    	}
    
    	List<Person> assisteds = query.getResultList();
    	
    	entityManager.close();

    	for(Person p : assisteds)
    		System.out.println(p.getSurname() + " " + p.getName());			
    }
    
	//
    // Instance constructor
	//
	// parameters	
	//		interfaceMain 		interface to callback the main class
	//
	// returned
	//		none
	//
    public SearchPersonController(MainCallback interfaceMain)
    {
    	this.interfaceMain = interfaceMain;   
    } 
    
    // Interface to callback the main class
    private MainCallback interfaceMain;    
}