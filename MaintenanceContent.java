package application;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;

public class MaintenanceContent extends ContentArea {
	
	private CLMS db;
	VBox tableContainer;
	private TableView<CruiseShip> underMaintenance;
	private TableView<CruiseShip> upcomingMaintenance;
	private TableView<CruiseShip> pastMaintenance;
	private TableView<CruiseShip> scheduledMaintenance;
	
	public MaintenanceContent(CLMS db) {
		this.db = db;
		tableContainer = new VBox();
	}
	
	@Override
	public void initialize() {
		
		VBox vbox = new VBox(15);
		//Create and configure the ComboBox
		ComboBox<String> maintenanceComboBox = new ComboBox<>();
		maintenanceComboBox.getItems().addAll("Under maintenance", "Upcoming maintenance", "Past maintenance", "Scheduled maintenance");
		maintenanceComboBox.setValue("Under maintenance"); // Default selection          
		Label maintenanceLabel = new Label("Cruise Ship Maintenance Information");

		// Create Tables
		underMaintenance = createTable();
		upcomingMaintenance = createTable();
		pastMaintenance = createTable();
		scheduledMaintenance = createTable();
		tableContainer.getChildren().add(underMaintenance);
		populateTableData();
		vbox.getChildren().addAll(maintenanceLabel, maintenanceComboBox, tableContainer);

		maintenanceComboBox.setOnAction(event -> {
		String selectedMaintenance = maintenanceComboBox.getValue();
			changeTable(selectedMaintenance);
		});
		
		content = vbox;
		content.getStyleClass().add("overview-content");
	
	}
	
	private void changeTable(String table) {
		switch (table) {
		case "Under maintenance":
			tableContainer.getChildren().clear();
			tableContainer.getChildren().add(underMaintenance);
			break;
		case "Upcoming maintenance":
			tableContainer.getChildren().clear();
			tableContainer.getChildren().add(upcomingMaintenance);
			break;
		case "Past maintenance":
			tableContainer.getChildren().clear();
			tableContainer.getChildren().add(pastMaintenance);
			break;
		case "Scheduled maintenance":
			tableContainer.getChildren().clear();
			tableContainer.getChildren().add(scheduledMaintenance);
			break;
		}
	}
	
	private void populateTableData() {
		ObservableList<CruiseShip> under = FXCollections.observableArrayList();
		ObservableList<CruiseShip> upcoming = FXCollections.observableArrayList();
		ObservableList<CruiseShip> past = FXCollections.observableArrayList();
		ObservableList<CruiseShip> scheduled = FXCollections.observableArrayList();
		for (CruiseShip ship : db.ships) {
			if (ship.getMaintenance() == 0) {
				upcoming.add(ship);
				continue;
			}
			else if (ship.getMaintenance() < 2023) {
				past.add(ship);
				continue;
			}
			else if (ship.getMaintenance() == 2024) {
				scheduled.add(ship);
				continue;
			}
			else {
				under.add(ship);
				continue;
			}
		}
		underMaintenance.setItems(under);
		upcomingMaintenance.setItems(upcoming);
		pastMaintenance.setItems(past);
		scheduledMaintenance.setItems(scheduled);
	}
	
	private TableView<CruiseShip> createTable() {
	    TableView<CruiseShip> maintenanceTable = new TableView<>();
	    maintenanceTable.getStyleClass().add("overview-table");

	    TableColumn<CruiseShip, String> shipColumn = new TableColumn<>("Ship name");
	    shipColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

	    TableColumn<CruiseShip, String> statusColumn = new TableColumn<>("Status");
	    statusColumn.setCellValueFactory(cellData -> {
	        if (cellData.getValue().getStatus()) {
	            return new SimpleStringProperty("In Service");
	        } else {
	            return new SimpleStringProperty("Out of Service");
	        }
	    });

	    TableColumn<CruiseShip, String> dateColumn = new TableColumn<>("Date of service");
	    dateColumn.setCellValueFactory(new PropertyValueFactory<>("maintenance"));
	    
	    dateColumn.setCellValueFactory(cellData -> {
	        int maintenanceDate = cellData.getValue().getMaintenance();
	        if (maintenanceDate == 0) {
	            return new SimpleStringProperty("TBD");
	        } else {
	            return new SimpleStringProperty(String.valueOf(maintenanceDate));
	        }
	    });

	    maintenanceTable.getColumns().addAll(shipColumn, statusColumn, dateColumn);

	    return maintenanceTable;
	}
}
