package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Assisted;
import model.Meeting;
import windows.MeetingDetailsController;
import windows.AssistedDetailsController;
import windows.CalendarController;
import windows.ReportController;
import windows.ScheduleController;
import windows.AssistedSearchController;
import windows.SettingsController;
import windows.SplashScreenController;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;

public class Main extends Application implements MainCallback {
	
	private Stage primaryStage; // Application stage
	private Assisted selectedAssisted;
	private Meeting selectedMeeting;
	private Operation requestedOperation;
	
	@Override
	public Stage getStage() {
		return primaryStage;
	}
	
	@Override
	public Assisted getSelectedAssisted() {
		return selectedAssisted;
	}

	@Override
	public void setSelectedAssisted(Assisted selectedAssisted) {
		this.selectedAssisted = selectedAssisted;
	}

	@Override
	public Meeting getSelectedMeeting() {
		return selectedMeeting;
	}
	
	@Override
	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	@Override
	public Operation getRequestedOperation() {
		return requestedOperation;
	}

	@Override
	public void setRequestedOperation(Operation requestedOperation) {
		this.requestedOperation = requestedOperation;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../images/icon.png")));
			this.primaryStage.setMaximized(true);
			showScene(createSplashScreen());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void switchScene(Page requestedPage, PageCallback currentPage) {
		Platform.runLater(() -> {
			try {
				switch (requestedPage) {
				case ASSISTED_SEARCH:
					showScene(createSearchPerson());
					break;

				case REPORT:
					showScene(createReport());
					break;

				case SCHEDULE:
					showScene(createSchedule());
					break;

				case ASSISTED_DETAILS:
					showScene(createAssistedDetailsLayout());
					break;
				case CALENDAR:
					showScene(createCalendarLayout());
					break;

				case SETTINGS:
					showPopup(createSettingsLayout());
					break;

				case MEETING_DETAILS:
					showPopup(createMeetingDetailsLayout(currentPage));
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	// Create scene Splash Screen page
	private Scene createSplashScreen() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/SplashScreen.fxml"));
		SplashScreenController fxmlController = new SplashScreenController(this);
		fxmlLoader.setController(fxmlController);
		BorderPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(),
				Screen.getPrimary().getVisualBounds().getHeight());

		return scene;
	}

	// Create scene Registry page
	private Scene createSearchPerson() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/AssistedSearch.fxml"));
		AssistedSearchController fxmlController = new AssistedSearchController(this);
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(),
				Screen.getPrimary().getVisualBounds().getHeight());

		return scene;
	}

	// Create scene Report page
	private Scene createReport() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/Report.fxml"));
		ReportController fxmlController = new ReportController(this);
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(),
				Screen.getPrimary().getVisualBounds().getHeight());

		return scene;
	}

	// Create scene Schedule page
	private Scene createSchedule() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/Schedule.fxml"));
		ScheduleController fxmlController = new ScheduleController(this);
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(),
				Screen.getPrimary().getVisualBounds().getHeight());
		return scene;
	}

	// Create scene AssistedDetailsLayout page
	private Scene createAssistedDetailsLayout() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/AssistedDetails.fxml"));
		AssistedDetailsController fxmlController = new AssistedDetailsController(this);
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(),
				Screen.getPrimary().getVisualBounds().getHeight());

		return scene;
	}

	// Create scene SettingsLayout
	private Scene createSettingsLayout() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/Settings.fxml"));
		SettingsController fxmlController = new SettingsController(this);
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 512, 580);

		return scene;
	}

	// Create scene CalendarLayout page
	private Scene createCalendarLayout() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/Calendar.fxml"));
		CalendarController fxmlController = new CalendarController(this);
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(),
				Screen.getPrimary().getVisualBounds().getHeight());

		return scene;
	}

	// Create scene MeetingDetailLayout
	private Scene createMeetingDetailsLayout(PageCallback currentPage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/MeetingDetails.fxml"));
		MeetingDetailsController fxmlController = new MeetingDetailsController(this, currentPage);
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 565, 640);

		return scene;
	}

	// Load a scene into the stage
	private void showPopup(Scene scene) {
		Stage popup = new Stage();
		popup.getIcons().add(new Image(getClass().getResourceAsStream("../images/icon.png")));
		popup.initModality(Modality.WINDOW_MODAL);
		popup.initOwner(primaryStage);
		popup.setResizable(false);
		popup.centerOnScreen();
		popup.setScene(scene);
		popup.show();
	}

	// Load a scene into the stage
	private void showScene(Scene scene) {
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);

		// Getting the screen size accounting for the windows application bar
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());
		primaryStage.show();
	}

}
