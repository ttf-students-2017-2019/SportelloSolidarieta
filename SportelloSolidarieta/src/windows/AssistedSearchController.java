package windows;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import application.MainCallback;
import dal.DbUtil;
import model.Assisted;

public class AssistedSearchController {

	/*
	 * MEMBERS
	 */

	private MainCallback main; // Interface to callback the main class
	private EntityManager em;

	/*
	 * JAVAFX COMPONENTS
	 */

	@FXML
	private Button btn_settings;

	@FXML
	private Button btn_report;

	@FXML
	private Button btn_calendar;

	@FXML
	private Button btn_addAssisted;

	@FXML
	private Button btn_detailsAssisted;

	@FXML
	private TextField tfield_surname;

	@FXML
	private TextField tfield_name;

	@FXML
	private TableView<Assisted> resultTable;

	@FXML
	private TableColumn<Assisted, String> colSurname;

	@FXML
	private TableColumn<Assisted, String> colName;

	@FXML
	private TableColumn<Assisted, LocalDate> colBirthdate;

	/*
	 * CONSTRUCTOR
	 */

	public AssistedSearchController(MainCallback main) {
		this.main = main;
	}

	/*
	 * SCENE INITIALIZATION
	 */

	@FXML
	private void initialize() {
		try {
			this.em = DbUtil.getEntityManager(); // I need the EntityManager at the very beginning, otherwise the first search would appear delayed
		} catch (Exception e) {
			// TODO improve
			e.printStackTrace();
			DbUtil.showAlertDatabaseError();
		}
		main.setSelectedAssisted(null); // Reset the selectedAssisted
	}

	/*
	 * JAVAFX ACTIONS
	 */

	@FXML
	void toAssistedDetails(ActionEvent event) {
		if (event.getSource().equals(btn_addAssisted)) { // if ADD button create a new Assisted with the Name and Surname in the search fields
			Assisted assisted = new Assisted();
			assisted.setName(tfield_name.getText());
			assisted.setSurname(tfield_surname.getText());
			main.setSelectedAssisted(assisted); // set selectedAssisted in the main
		}
		System.out.println("PASSING ASSISTED: " + main.getSelectedAssisted()); // TODO change with a proper logging
		DbUtil.closeEntityManager(em);
		main.switchScene(MainCallback.Page.ASSISTED_DETAILS, null);
	}

	@FXML
	void toReport(ActionEvent event) {
		DbUtil.closeEntityManager(this.em);
		main.switchScene(MainCallback.Page.REPORT, null);
	}

	@FXML
	void toSettings(ActionEvent event) {
		// DbUtil.closeEntityManager(this.em); NOTE do *not* do this here, because the
		// scene itsn't reloaded at the exit of "settings", therefore there won't be any
		// EnitityManager
		main.switchScene(MainCallback.Page.SETTINGS, null);
	}

	@FXML
	void toCalendar(ActionEvent event) {
		DbUtil.closeEntityManager(this.em);
		main.switchScene(MainCallback.Page.CALENDAR, null);
	}

	@FXML
	public void searchAssisted(KeyEvent event) {
		btn_detailsAssisted.setDisable(true); // disables "toDetail" button when a new search is made
		main.setSelectedAssisted(null);; // resets the previously selected assisted
		List<Assisted> assistedsFound = DbUtil.searchAssisted(em, tfield_surname.getText(), tfield_name.getText());
		if (assistedsFound == null || assistedsFound.isEmpty()) {
			resultTable.setDisable(true);
		} else {
			resultTable.setDisable(false);
		}
		bindResults(assistedsFound);
	}

	@FXML
	void onRowSelected(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			main.setSelectedAssisted(resultTable.getSelectionModel().getSelectedItem());
			System.out.println("SELECTED ASSISTED: " + main.getSelectedAssisted()); // TODO change with a proper logging
			if (main.getSelectedAssisted() != null) // note: it selects a null Assisted if I click in the empty area of the Table, so I need this one
				btn_detailsAssisted.setDisable(false); // activate "toDetail" button
		}
	}

	/*
	 * OTHER METHODS
	 */

	// binds results to the TableView
	// NOTE each Assisted must at least have Name, Surname and Birthdate
	private void bindResults(List<Assisted> assisteds) {
		// binds the list
		ObservableList<Assisted> observableAssisteds = FXCollections.<Assisted>observableArrayList();
		observableAssisteds.addAll(assisteds);
		resultTable.setItems(observableAssisteds);

		// sets the columns
		colSurname.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSurname()));
		colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

		// set the date column and formatting (NOTE: the sort works because this one)
		colBirthdate.setCellValueFactory(new PropertyValueFactory<Assisted, LocalDate>("birthdate")); // NOTE: "birthdate" is the field of the model bean
		colBirthdate.setCellFactory(cellData -> new TableCell<Assisted, LocalDate>() {
			@Override
			protected void updateItem(LocalDate date, boolean isEmpty) {
				super.updateItem(date, isEmpty);
				if (isEmpty) {
					setText(null);
				} else {
					setText(utilities.Formatter.formatDate(date));
				}
			}
		});
	}

}