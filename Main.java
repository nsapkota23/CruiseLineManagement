package application;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private StackPane root;
    private String currentTab = "Cruises";
    static CLMS clms;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cruise Liner Management System");

        // Create ToggleButtons for different categories
        ToggleButton overviewButton = new ToggleButton("Overview");
        ToggleButton cruisesButton = new ToggleButton("Cruises");
        ToggleButton manifestButton = new ToggleButton("Manifest");
        ToggleButton maintenanceButton = new ToggleButton("Maintenance");

        // Create a ToggleGroup
        ToggleGroup toggleGroup = new ToggleGroup();
        overviewButton.setToggleGroup(toggleGroup);
        cruisesButton.setToggleGroup(toggleGroup);
        manifestButton.setToggleGroup(toggleGroup);
        maintenanceButton.setToggleGroup(toggleGroup);

        // Create content tabs
        OverviewContent overviewContent = new OverviewContent();
        overviewContent.initialize();

        CruisesContent cruisesContent = new CruisesContent(clms);
        cruisesContent.initialize();

        MaintenanceContent maintenanceContent = new MaintenanceContent(clms);
        maintenanceContent.initialize();

        ManifestContent manifestContent = new ManifestContent();
        manifestContent.initialize();

        // Event handling for ToggleButtons
        overviewButton.setOnAction(e -> {
            showCategoryContent(overviewContent.getContent());
            currentTab = "Overview";
            currentTab();
        });
        cruisesButton.setOnAction(e -> {
            showCategoryContent(cruisesContent.getContent());
            currentTab = "Cruises";
            currentTab();
        });
        manifestButton.setOnAction(e -> {
            showCategoryContent(manifestContent.getContent());
            currentTab = "Manifest";
            currentTab();
        });
        maintenanceButton.setOnAction(e -> {
            showCategoryContent(maintenanceContent.getContent());
            currentTab = "Maintenance";
            currentTab();
        });
        
        // Default to overview
        overviewButton.setSelected(true);

        // Create an HBox for the ToggleButtons
        HBox categoryButtons = new HBox(10);
        categoryButtons.getChildren().addAll(overviewButton, cruisesButton, manifestButton, maintenanceButton);
        categoryButtons.getStyleClass().add("toggle-group");

        // Create the main layout
        root = new StackPane(overviewContent.getContent());
        VBox mainLayout = new VBox(categoryButtons, root);
        Scene scene = new Scene(mainLayout, 800, 600);
        
        // Link the CSS file to the scene
        scene.getStylesheets().add(getClass().getResource("/application/styles.css").toExternalForm());

        // Display GUI
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Display Tab Content
    private void showCategoryContent(VBox content) {
        root.getChildren().clear();
        root.getChildren().add(content);
    }
    
    private void currentTab() {
    	System.out.println(currentTab);
    }

    public static void main(String[] args) {
        clms = new CLMS();
        clms.ships[1].printShipDetails();
        launch(args);
    }
}
