package application;

import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.image.ImageView;

public class OverviewContent extends ContentArea {
    private Stage primaryStage;
    private VBox category2;

    @Override
    public void initialize() {
        // Create a VBox to contain the navigation bar and content
        VBox vbox = new VBox();

        // Create a navigation bar at the top
        VBox navigationBar = createNavigationBar();

        // Add padding to the navigation bar
        navigationBar.setPadding(new Insets(10, 10, 10, 10)); // Adjust the values as needed

        // Create a GridPane to organize categories
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal spacing
        gridPane.setVgap(20); // Vertical spacing

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Define the four categories (replace with actual content)
        Label cat1 = new Label("Welcome to the Cruise Liner Management System!");
        VBox category1 = new VBox(cat1);
        cat1.getStyleClass().add("welcome-label");
        category1.setAlignment(Pos.CENTER); 
        ImageView defaultImage = new ImageView(
                getClass().getResource("/application/images/overview/overview.jpg").toExternalForm());
        // Set the dimensions you want for the image
        defaultImage.setFitHeight(400);
        defaultImage.setFitWidth(500);
        defaultImage.setPreserveRatio(true);
        // Preserve the image's aspect ratio
        defaultImage.setPreserveRatio(true);
        category2 = new VBox(defaultImage);
        category2.setAlignment(Pos.CENTER);
        // Add CSS styles to the categories
        category1.getStyleClass().add("overview-category");
        category2.getStyleClass().add("overview-category");

        // Add categories to the GridPane
        gridPane.add(category1, 0, 0);
        gridPane.add(category2, 1, 0);
        gridPane.add(new Label("Ship Overview"), 0, 2, 2, 1);
        gridPane.add(createShipTable(), 0, 3, 2, 1);

        // Set the GridPane to expand within its container
        GridPane.setHgrow(gridPane, Priority.ALWAYS);
        GridPane.setVgrow(gridPane, Priority.ALWAYS);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setMaxHeight(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        gridPane.getColumnConstraints().addAll(col1, col2);

        vbox.getChildren().addAll(navigationBar, gridPane);

        content = vbox;
        content.getStyleClass().add("overview-content");
    }

    private VBox createNavigationBar() {
        VBox navigationBar = new VBox();
        navigationBar.getStyleClass().add("overview-navigation-bar");

        return navigationBar;
    }

    private void configureFileChooser(FileChooser fileChooser) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private TableView<CruiseShip> createShipTable() {
        // Create a WebView to display the location
        WebView webView = new WebView();
        TableView<CruiseShip> shipTable = new TableView<>();

        // Ship Name Column
        TableColumn<CruiseShip, String> shipNameColumn = new TableColumn<>("Ship Name");
        shipNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Company Column
        TableColumn<CruiseShip, String> companyColumn = new TableColumn<>("Company");
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));

        // Location Column (with hyperlink)
        TableColumn<CruiseShip, Hyperlink> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(cellData -> {
            String locationURL = cellData.getValue().getLocation();
            Hyperlink hyperlink = new Hyperlink("View Location");
            hyperlink.setOnAction(e -> {
                webView.getEngine().load(locationURL);
                launchLocationWindow(webView);
            });
            return new SimpleObjectProperty<>(hyperlink);
        });

        // Trip Length Column
        TableColumn<CruiseShip, Integer> tripLengthColumn = new TableColumn<>("Trip Length");
        tripLengthColumn.setCellValueFactory(new PropertyValueFactory<>("tripLength"));

        // Number of Cabins Column
        TableColumn<CruiseShip, Integer> cabinsColumn = new TableColumn<>("Number of Cabins");
        cabinsColumn.setCellValueFactory(new PropertyValueFactory<>("numCabins"));

        // Year of Build Column
        TableColumn<CruiseShip, Integer> yearOfBuildColumn = new TableColumn<>("Year of Build");
        yearOfBuildColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfBuild"));

        // Capacity Column
        TableColumn<CruiseShip, Integer> capacityColumn = new TableColumn<>("Capacity");
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));

        // Origin Column
        TableColumn<CruiseShip, String> originColumn = new TableColumn<>("Origin");
        originColumn.setCellValueFactory(new PropertyValueFactory<>("origin"));

        // Destination Column
        TableColumn<CruiseShip, String> destinationColumn = new TableColumn<>("Destination");
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("finalDestination"));

        // Adding all columns to the table
        shipTable.getColumns().addAll(shipNameColumn, companyColumn, locationColumn, tripLengthColumn, cabinsColumn,
                yearOfBuildColumn, capacityColumn, originColumn, destinationColumn);

        // Fetch ships from the database and add them to the table
        List<CruiseShip> ships = DatabaseController.getAllShips();
        shipTable.getItems().addAll(ships);

        return shipTable;
    }
    
    private void launchLocationWindow(WebView view) {
        Stage newStage = new Stage();
        newStage.setTitle("Current Location");

        GridPane locPane = new GridPane();
        locPane.setAlignment(Pos.CENTER);
        Scene newScene = new Scene(locPane, 800, 600);

        Button close = new Button("Close");
        close.setPrefSize(100, 10);
        close.setOnAction(e -> newStage.close());
        locPane.getChildren().add(view);
        locPane.add(close, 0, 1);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(newScene);
        newStage.show();
        
    }

}
