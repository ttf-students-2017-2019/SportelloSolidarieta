package windows;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import model.Meeting;
import utilities.PdfUtil;

public class ReportController {

	// Interface to callback the main class
	private MainCallback interfaceMain;

	private ObservableList<Meeting> meetings;

	@FXML
	private DatePicker to;

	@FXML
	private DatePicker from;

	@FXML
	private TableView<Meeting> table;

	@FXML
	private TableColumn<Meeting, String> name;

	@FXML
	private TableColumn<Meeting, String> surname;

	@FXML
	private TableColumn<Meeting, LocalDate> date;

	@FXML
	private TableColumn<Meeting, Float> amount;

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
		table.setPlaceholder(new Label(""));

		// bind columns to bean properties
		name.setCellValueFactory(new PropertyValueFactory<Meeting, String>("personName"));
		surname.setCellValueFactory(new PropertyValueFactory<Meeting, String>("personSurname"));
		date.setCellValueFactory(new PropertyValueFactory<Meeting, LocalDate>("date"));
		amount.setCellValueFactory(new PropertyValueFactory<Meeting, Float>("amount"));

		// format date and amount for display in the table
		date.setCellFactory(tc -> new TableCell<Meeting, LocalDate>() {
			@Override
			protected void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				if (empty) {
					setText(null);
				} else {
					setText(utilities.Formatter.formatDate(date));
				}
			}
		});
		amount.setCellFactory(tc -> new TableCell<Meeting, Float>() {
			@Override
			protected void updateItem(Float amount, boolean empty) {
				super.updateItem(amount, empty);
				if (empty) {
					setText(null);
				} else {
					setText(utilities.Formatter.formatNumber(amount));
				}
			}
		});

		// initialize data model and bind table
		meetings = FXCollections.observableArrayList();
		table.setItems(meetings);
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
			meetings.setAll(DbUtil.getMeetings(from.getValue(), to.getValue()));
			if (meetings.isEmpty()) {
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

	private float calculateTotal() {
		float total = 0;
		for (Meeting m : meetings) {
			total += m.getAmount();
		}
		return total;
	}

}
