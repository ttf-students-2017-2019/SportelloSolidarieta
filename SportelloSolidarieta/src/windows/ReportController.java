package windows;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import application.MainCallback;
import dal.DbUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import model.Meeting;
import report.ObservableMeeting;
import utilities.PdfUtil;

public class ReportController {

	// Interface to callback the main class
	private MainCallback interfaceMain;

	private List<Meeting> meetings;
	private ObservableList<ObservableMeeting> observableMeetings;

	@FXML
	private RadioButton outgoingsOnly;

	@FXML
	private RadioButton incomesOnly;

	@FXML
	private RadioButton outgoingsAndIncomes;

	@FXML
	private DatePicker from;

	@FXML
	private DatePicker to;

	@FXML
	private TableView<ObservableMeeting> table;

	@FXML
	private TableColumn<ObservableMeeting, String> name;

	@FXML
	private TableColumn<ObservableMeeting, String> surname;

	@FXML
	private TableColumn<ObservableMeeting, String> date;

	@FXML
	private TableColumn<ObservableMeeting, String> outgoings;
	
	@FXML
	private TableColumn<ObservableMeeting, String> incomes;

	@FXML
	private Button export;

	@FXML
	private Label total;

	@FXML
	private Button back;

	public ReportController(MainCallback interfaceMain) {
		this.interfaceMain = interfaceMain;
	}

	@FXML
	private void initialize() {
		// create toggle group for report type and add radio buttons
		// set outgoings only as default
		final ToggleGroup group = new ToggleGroup();
		outgoingsOnly.setToggleGroup(group);
		incomesOnly.setToggleGroup(group);
		outgoingsAndIncomes.setToggleGroup(group);
		outgoingsOnly.setSelected(true);

		// disable dates in the future for the date pickers
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isAfter(LocalDate.now())) {
							setDisable(true);
						}
					}
				};
			}
		};
		from.setDayCellFactory(dayCellFactory);
		to.setDayCellFactory(dayCellFactory);

		// set table placeholder to blank
//		table.setPlaceholder(new Label(""));

		// bind columns to bean properties
		surname.setCellValueFactory(cellData -> cellData.getValue().getAssistedSurname());
		name.setCellValueFactory(cellData -> cellData.getValue().getAssistedName());
		date.setCellValueFactory(cellData -> cellData.getValue().getDate());
		outgoings.setCellValueFactory(cellData -> cellData.getValue().getOutgoings());
		incomes.setCellValueFactory(cellData -> cellData.getValue().getIncomes());

		// initialize data model and bind table
		observableMeetings = FXCollections.observableArrayList();
		table.setItems(observableMeetings);
	}

	@FXML
	void toRegistry(ActionEvent event) {
		interfaceMain.switchScene(MainCallback.Pages.SearchPerson);
	}

	//
	// Instance constructor
	//
	// parameters
	// interfaceMain interface to callback the main class
	//
	// returned
	// none
	//

	@FXML
	void onDateSelected(ActionEvent event) {

		if ((from.getValue() == null) || (to.getValue() == null)) {
			return;
		}

		if (from.getValue().isAfter(to.getValue())) {
			Label label = new Label("Errore! La data di inizio è successiva alla data di fine");
			label.setTextFill(Color.web("#ff0000"));
			table.setPlaceholder(label);
		} else {
			// load data into model (view updates automatically)
			if (outgoingsOnly.isSelected()) {
				meetings = DbUtil.getMeetings(from.getValue(), to.getValue());
			} else if (incomesOnly.isSelected()) {
				meetings = DbUtil.getDonations(from.getValue(), to.getValue());
			} else /* outgoings and incomes is selected */ {
				meetings = DbUtil.getMeetingsAndDonations(from.getValue(), to.getValue());
			}
			populateObservableList(meetings);
			if (observableMeetings.isEmpty()) {
				table.setPlaceholder(new Label("Nessun risultato"));
			}
			// display total
			NumberFormat numberFormat = NumberFormat.getInstance(Locale.ITALIAN);
			numberFormat.setMinimumFractionDigits(2);
			total.setText(utilities.Formatter.formatNumber(calculateTotal()));
		}
	}

	@FXML
	void onExportClicked(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
		File file = fileChooser.showSaveDialog(interfaceMain.getStage());
		if (file != null) {
			try {
				PdfUtil.export(meetings, from.getValue(), to.getValue(), total.getText(), file.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void populateObservableList(List<Meeting> meetings) {
		observableMeetings.clear();
		for (Meeting m : meetings) {
			ObservableMeeting om = new ObservableMeeting(m);
			observableMeetings.add(om);
		}
	}

	private float calculateTotal() {
		float total = 0;
		for (Meeting m : meetings) {
			total += m.getAmount();
		}
		return total;
	}

}
