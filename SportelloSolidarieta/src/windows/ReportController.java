package windows;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import application.MainCallback;
import application.MainCallback.Page;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import model.Meeting;
import report.ObservableMeeting;
import utilities.PdfUtil;
import utilities.PdfUtil.ReportType;

public class ReportController {

	/*
	 * MEMBERS
	 */

	private MainCallback main; // Interface to callback the main class
	private List<Meeting> meetings;
	private ObservableList<ObservableMeeting> observableMeetings;

	/*
	 * JAVAFX COMPONENTS
	 */

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
	private TableColumn<ObservableMeeting, LocalDate> date;

	@FXML
	private TableColumn<ObservableMeeting, String> outgoings;

	@FXML
	private TableColumn<ObservableMeeting, String> incomes;

	@FXML
	private Button export;

	@FXML
	private Button back;

	@FXML
	private Label totalOutgoingsLabel;

	@FXML
	private Label totalOutgoingsValue;

	@FXML
	private Label totalIncomesLabel;

	@FXML
	private Label totalIncomesValue;

	@FXML
	private Label balanceLabel;

	@FXML
	private Label balanceValue;

	/*
	 * CONSTRUCTOR
	 */

	public ReportController(MainCallback main) {
		this.main = main;
	}

	/*
	 * SCENE INITIALIZATION
	 */

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

		table.setPlaceholder(new Label("Nessun risultato"));

		// bind columns to bean properties
		surname.setCellValueFactory(cellData -> cellData.getValue().getAssistedSurname());
		name.setCellValueFactory(cellData -> cellData.getValue().getAssistedName());
//		date.setCellValueFactory(cellData -> cellData.getValue().getDate());
		date.setCellValueFactory(new PropertyValueFactory<ObservableMeeting, LocalDate>("date"));
		date.setCellFactory(cellData -> new TableCell<ObservableMeeting, LocalDate>() {
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
		outgoings.setCellValueFactory(cellData -> cellData.getValue().getOutgoings());
		incomes.setCellValueFactory(cellData -> cellData.getValue().getIncomes());

		// initialize data model and bind table
		observableMeetings = FXCollections.observableArrayList();
		table.setItems(observableMeetings);

		// hide incomes labels. The default report is for outgoings
		totalIncomesLabel.setVisible(false);
		totalIncomesValue.setVisible(false);
		balanceLabel.setVisible(false);
		balanceValue.setVisible(false);
	}

	/*
	 * JAVAFX ACTIONS
	 */

	@FXML
	void toRegistry(ActionEvent event) {
		main.switchScene(MainCallback.Page.ASSISTED_SEARCH, null);
	}

	@FXML
	void loadData(ActionEvent event) {

		if (outgoingsOnly.isSelected()) {
			// hide incomes and balance labels
			totalIncomesLabel.setVisible(false);
			totalIncomesValue.setVisible(false);
			balanceLabel.setVisible(false);
			balanceValue.setVisible(false);

			// show outgoings labels
			totalOutgoingsLabel.setVisible(true);
			totalOutgoingsValue.setVisible(true);

		} else if (incomesOnly.isSelected()) {
			// hide outgoings labels and balance labels
			totalOutgoingsLabel.setVisible(false);
			totalOutgoingsValue.setVisible(false);
			balanceLabel.setVisible(false);
			balanceValue.setVisible(false);

			// show incomes labels
			totalIncomesLabel.setVisible(true);
			totalIncomesValue.setVisible(true);

		} else if (outgoingsAndIncomes.isSelected()) {
			// show everything
			totalOutgoingsLabel.setVisible(true);
			totalOutgoingsValue.setVisible(true);
			totalIncomesLabel.setVisible(true);
			totalIncomesValue.setVisible(true);
			balanceLabel.setVisible(true);
			balanceValue.setVisible(true);
		}

		if ((from.getValue() == null) || (to.getValue() == null)) {
			return;
		}

		// clear table results so placeholder is shown in case of error
		observableMeetings.clear();

		if (from.getValue().isAfter(to.getValue())) {
			Label label = new Label("Errore! La data di inizio è successiva alla data di fine");
			label.setTextFill(Color.web("#ff0000"));
			table.setPlaceholder(label);
		} else {

			// formatter needed to display totals
			NumberFormat numberFormat = NumberFormat.getInstance(Locale.ITALIAN);
			numberFormat.setMinimumFractionDigits(2);

			// load data into model (view updates automatically)
			if (outgoingsOnly.isSelected()) {
				meetings = DbUtil.getMeetings(from.getValue(), to.getValue());

				// total outgoings value is in the first position of the results array
				Float outgoings = calculateTotals().get(0);

				// display total outgoings
				totalOutgoingsValue.setText(utilities.Formatter.formatNumber(outgoings));
			} else if (incomesOnly.isSelected()) {
				meetings = DbUtil.getDonations(from.getValue(), to.getValue());

				// total incomes value is in the first position of the results array
				Float incomes = calculateTotals().get(1);

				// display total incomes
				totalIncomesValue.setText(utilities.Formatter.formatNumber(incomes));
			} else if (outgoingsAndIncomes.isSelected()) {
				meetings = DbUtil.getMeetingsAndDonations(from.getValue(), to.getValue());

				// total outgoings value is in the first position of the results array, incomes
				// in the second
				Float outgoings = calculateTotals().get(0);
				Float incomes = calculateTotals().get(1);

				// display totals
				totalOutgoingsValue.setText(utilities.Formatter.formatNumber(outgoings));
				totalIncomesValue.setText(utilities.Formatter.formatNumber(incomes));
				balanceValue.setText(utilities.Formatter.formatNumber(incomes - outgoings));
			}
			populateObservableList(meetings);

			if (observableMeetings.isEmpty()) {
				table.setPlaceholder(new Label("Nessun risultato"));
			}
		}
	}

	@FXML
	void onExportClicked(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
		File file = fileChooser.showSaveDialog(main.getStage());
		if (file != null) {
			try {
				if (outgoingsOnly.isSelected()) {
					PdfUtil.export(ReportType.OutgoingsOnly, observableMeetings, from.getValue(), to.getValue(),
							totalOutgoingsValue.getText(), null, null, file.getCanonicalPath());
				} else if (incomesOnly.isSelected()) {
					PdfUtil.export(ReportType.IncomesOnly, observableMeetings, from.getValue(), to.getValue(), null,
							totalIncomesValue.getText(), null, file.getCanonicalPath());
				} else if (outgoingsAndIncomes.isSelected()) {
					PdfUtil.export(ReportType.OutgoingsAndIncomes, observableMeetings, from.getValue(), to.getValue(),
							totalOutgoingsValue.getText(), totalIncomesValue.getText(), balanceValue.getText(),
							file.getCanonicalPath());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
    /*
     * OTHER METHODS
     */

	private void populateObservableList(List<Meeting> meetings) {
		for (Meeting m : meetings) {
			ObservableMeeting om = new ObservableMeeting(m, Page.REPORT);
			observableMeetings.add(om);
		}
	}

	// calculate total outgoings and incomes results contains outgoings and incomes
	// in this order
	private List<Float> calculateTotals() {
		List<Float> results = new ArrayList<Float>();

		float totalOutgoings = 0;
		float totalIncomes = 0;

		for (Meeting m : meetings) {
			if (m.getAssistedSurname().equals(ObservableMeeting.DONATION_STRING))
				totalIncomes += m.getAmount();
			else
				totalOutgoings += m.getAmount();
		}

		// adding in order outgoings, incomes
		results.add(totalOutgoings);
		results.add(totalIncomes);

		return results;
	}

}
