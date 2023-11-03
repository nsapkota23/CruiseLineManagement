package application;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ManifestContent extends ContentArea {

	private TableView<Passenger> manifestTable;

	@Override
	public void initialize() {
		VBox vbox = new VBox(15); // space between components

		Label manifestLabel = new Label("Cruise Ship Manifest Information");

		manifestTable = createManifestTable();

		vbox.getChildren().addAll(manifestLabel, manifestTable);
		populateData(); // New line added to populate data on initialization

		content = vbox;
		content.getStyleClass().add("overview-content");
	}

	private TableView<Passenger> createManifestTable() {
		TableView<Passenger> table = new TableView<>();

		TableColumn<Passenger, String> shipNameColumn = new TableColumn<>("Ship Name");
		shipNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));

		TableColumn<Passenger, String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		TableColumn<Passenger, String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

		TableColumn<Passenger, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

		TableColumn<Passenger, Number> cabinTypeColumn = new TableColumn<>("Cabin Type");
		cabinTypeColumn.setCellValueFactory(new PropertyValueFactory<>("cabinType"));

		table.getColumns().addAll(shipNameColumn, lastNameColumn, firstNameColumn, emailColumn, cabinTypeColumn);

		return table;
	}

	private void populateData() {
		List<Passenger> passengerList = DatabaseController.getAllPassengersForTable();
		ObservableList<Passenger> data = FXCollections.observableArrayList(passengerList);
		manifestTable.setItems(data);
	}

	public static class Passenger {
		private final String shipName;
		private final String lastName;
		private final String firstName;
		private final String email;
		private final int cabinType;

		public Passenger(String shipName, String lastName, String firstName, String email, int cabinType) {
			this.shipName = shipName;
			this.lastName = lastName;
			this.firstName = firstName;
			this.email = email;
			this.cabinType = cabinType;
		}

		public String getShipName() {
			return shipName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getEmail() {
			return email;
		}

		public int getCabinType() {
			return cabinType;
		}
	}
}
