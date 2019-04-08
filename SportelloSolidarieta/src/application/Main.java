package application;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;

import windows.AssistedDetailsController;
import windows.ReportController;
import windows.ScheduleController;
import windows.SearchAssistedController;
import windows.SettingsController;
import windows.SplashScreenController;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application implements MainCallback {
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;

			showScene(createSplashScreen());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void switchScene(MainCallback.Pages requiredPage) 
	{
		Platform.runLater(() -> 
		{	
			try
			{
				switch (requiredPage)
				{	
				case SearchPerson:
					showScene(createSearchPerson());
					break;

				case Report:
					showScene(createReport());
					break;

				case Schedule:
					showScene(createSchedule());
					break;	
					
				case AssistedDetail:
					showScene(createAssistedDetailsLayout());
					break;
				case Settings:
					showPopup(createSettingsLayout());
				break;
				
				}
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});	
	}
	
	// Create scene Splash Screen page
	private Scene createSplashScreen() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/SplashScreen.fxml"));
		SplashScreenController fxmlController = new SplashScreenController(this);	
		fxmlLoader.setController(fxmlController);
		BorderPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
		
		return scene;
	}
	
	// Create scene Registry page
	private Scene createSearchPerson() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/SearchAssisted.fxml"));
		SearchAssistedController fxmlController = new SearchAssistedController(this);	
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
		
		return scene;
	}
	
	// Create scene Report page
	private Scene createReport() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/Report.fxml"));
		ReportController fxmlController = new ReportController(this);	
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
		
		return scene;
	}	
	
	// Create scene Schedule page
	private Scene createSchedule() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/Schedule.fxml"));
		ScheduleController fxmlController = new ScheduleController(this);	
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
		return scene;
	}	
	
	// Create scene AssistedDetailsLayout page
	private Scene createAssistedDetailsLayout() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/AssistedDetails.fxml"));
		AssistedDetailsController fxmlController = new AssistedDetailsController(this, null);	
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
		
		return scene;
	}	
	
	private Scene createSettingsLayout() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../windows/Settings.fxml"));
		SettingsController fxmlController = new SettingsController(this);	
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 512, 768);
		
		return scene;
	}	
	
	// Load a scene into the stage
	private void showPopup(Scene scene)
	{
		Stage popup = new Stage();
		popup.initModality(Modality.WINDOW_MODAL);
		popup.initOwner(primaryStage);
		popup.setResizable(false);
		popup.centerOnScreen();
		popup.setScene(scene);
		popup.show();
	}
	
	// Load a scene into the stage
	private void showScene(Scene scene)
	{
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	public Stage getStage() {
		return primaryStage;
	}
	
	// Application stage
	private Stage primaryStage;
	
}
