package application;
	
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import application.MainCallback.Pages;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.Settings;
import schedule.DailyPlan;
import schedule.Slot;
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
				case Registry:
					showScene(createRegistry());
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
					showScene(createSettingsLayout());
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
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SplashScreen.fxml"));
		SplashScreenController fxmlController = new SplashScreenController(this);	
		fxmlLoader.setController(fxmlController);
		BorderPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
		
		return scene;
	}
	
	// Create scene Registry page
	private Scene createRegistry() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Registry.fxml"));
		RegistryController fxmlController = new RegistryController(this);	
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
		
		return scene;
	}
	
	// Create scene Report page
	private Scene createReport() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Report.fxml"));
		ReportController fxmlController = new ReportController(this);	
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
		
		return scene;
	}	
	
	// Create scene Schedule page
	private Scene createSchedule() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Schedule.fxml"));
		ScheduleController fxmlController = new ScheduleController(this);	
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
	
		return scene;
	}	
	
	// Create scene AssistedDetailsLayout page
	private Scene createAssistedDetailsLayout() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AssistedDetailsLayout.fxml"));
		AssistedDetailsController fxmlController = new AssistedDetailsController(this);	
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
		
		return scene;
	}	
	
	// Create scene Settings page
	private Scene createSettingsLayout() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings.fxml"));
		AssistedDetailsController fxmlController = new AssistedDetailsController(this);	
		fxmlLoader.setController(fxmlController);
		GridPane pane = fxmlLoader.load();
		Scene scene = new Scene(pane, 1024, 768);
		
		return scene;
	}	
	
	// Load a scene into the stage
	private void showScene(Scene scene)
	{
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	// Application stage
	private Stage primaryStage;
	
}
