package application;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import java.io.InputStream;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

public class CruisesContent extends ContentArea {
	
	// Ship
    private Label shipName;
    private Label shipCruiseLine;
    private Label tripLength;
    private Label yearBuilt;
    private Label origin;
    private Label finalDestination;
    private Label passengers;
    InputStream imageURL;
    ImageView shipView;
    Image shipImage;
	
    // Lodging
    Label estCost;
    Spinner<Integer> passengerCount;
    InputStream cabinImageURL;
    ImageView cabinView;
    Image cabinImage;
    Label cabinType;
    Label cabinAmenities;
    
    // Windows
    boolean lodgingWindow;
    boolean passengerWindow;
    boolean billingWindow;
    
    // Database
    CLMS db;
    
    // Selections
    int selectedCabin;
    CruiseShip selectedShip;
    int selectedShipID;
    String[] passengerNames;
    
    public CruisesContent(CLMS db) {
    	
		shipName = new Label("");
		shipCruiseLine = new Label("");
		tripLength = new Label("");
		yearBuilt = new Label("");
		origin = new Label("");
		finalDestination = new Label("");
		passengers = new Label("");
		imageURL = getClass().getResourceAsStream("/application/images/ships/empty.jpg");
		shipView = new ImageView();
		shipImage = new Image(imageURL);
		estCost = new Label("$0.00");
        passengerCount = new Spinner<Integer>(1, 10, 1);
        cabinView = new ImageView();
        cabinType = new Label("null");
        cabinAmenities = new Label("null");
        lodgingWindow = false;
        passengerWindow = false;
        billingWindow = false;
        this.db = db;
        selectedCabin = 0;
        selectedShip = getShip("Carnival Breeze");
    }
    
    @Override
    public void initialize() {
        GridPane selectPane = new GridPane();
        VBox vbox = new VBox();

        Label searchLabel = new Label("Select ship by name");
        
        ComboBox<String> searchComboBox = new ComboBox<String>();
        searchComboBox.setPrefSize(200, 25);
        searchComboBox.setItems(getShips());
        searchComboBox.getSelectionModel().selectFirst();
        updateCruiseInfo(searchComboBox.getValue());
        
        // search box
        HBox searchContainer = new HBox(searchLabel, searchComboBox);
        searchContainer.setSpacing(20);
        searchContainer.getStyleClass().add("search-box");
        
        // ship info
        GridPane shipInfoContainer = new GridPane();

        shipView.setImage(shipImage);
        shipView.setFitHeight(350);
        shipView.setFitWidth(350);
        shipView.setPreserveRatio(true);
        
        shipInfoContainer.add(shipName, 0, 0);
        shipInfoContainer.add(shipCruiseLine, 0, 1);
        shipInfoContainer.add(new HBox(tripLength, new Label(" Days")), 0, 2);
        shipInfoContainer.add(new HBox(new Label("Built in " ), yearBuilt), 0, 3);
        shipInfoContainer.add(new HBox(origin, new Label(" - "), finalDestination), 0, 4);
        shipInfoContainer.add(new HBox(passengers, new Label(" passengers")), 0, 5);
        
        // ship display box
        HBox shipBox = new HBox(shipView, shipInfoContainer);
        shipBox.setSpacing(15);
        shipBox.setPrefWidth(800);
        shipBox.setPrefHeight(300);
        
        shipBox.getStyleClass().add("ship-box");
        
        Button shipButton = new Button("Select Cruise");

        selectPane.setHgap(10);
        selectPane.setVgap(10);

        selectPane.add(searchContainer, 0, 0);
        selectPane.add(shipBox, 0, 1);
        selectPane.add(shipButton, 0, 2);

        // Set the GridPane to expand within its container
        GridPane.setHgrow(selectPane, Priority.ALWAYS);
        GridPane.setVgrow(selectPane, Priority.ALWAYS);
        selectPane.setMaxWidth(Double.MAX_VALUE);

        vbox.getChildren().addAll(selectPane);
        
        // Event Listeners
        shipButton.setOnAction(e -> {
        	openLodgingWindow();
        });
        
        searchComboBox.valueProperty().addListener(new ChangeListener<String>() {
        	@Override public void changed(ObservableValue ov, String t, String t1) {
        		selectedShip = getShip(t1);
        		updateCruiseInfo(t1);
              }    
        });

		selectedShip = getShip("Carnival Breeze");
        
        // Super class content set
        content = vbox;
        content.getStyleClass().add("overview-content");
    }
      
    private void openLodgingWindow() {
	        Stage newStage = new Stage();
	        newStage.setTitle("Lodging");       
	        GridPane lodgingLayout = new GridPane();
	        Scene newScene = new Scene(lodgingLayout, 800, 400); // Adjust window size
	
	        lodgingLayout.setHgap(10); 
	        lodgingLayout.setVgap(10);
	        lodgingLayout.setStyle("-fx-padding: 20;");
	
	        // Fields
	        ComboBox<String> cabinTypes = new ComboBox<String>(getCabins());
	        cabinTypes.getSelectionModel().selectFirst();
	        passengerCount.setPrefWidth(100);
	        
	        // lodging components
	        HBox selectContainer = new HBox(new Label("Select Room "), cabinTypes);
	        selectContainer.setSpacing(20);
	        selectContainer.getStyleClass().add("search-box");
	        HBox passengerContainer = new HBox(new Label("Passengers"), passengerCount);
	        HBox estContainer = new HBox(new Label("Estimated Cost: "), estCost);
	        
	        HBox topMenu = new HBox(selectContainer, passengerContainer, estContainer);
	        topMenu.setSpacing(15);
	        
	        HBox cabinInfo = new HBox();
	      
	        // Buttons
		    Button reserveButton = new Button("Reserve");
		    reserveButton.setDisable(true);
	        Button bookButton = new Button("Book");
	        cabinImageURL = getClass().getResourceAsStream("/application/images/cabins/" + cabinTypes.getValue() + ".jpg");
	        cabinImage = new Image(cabinImageURL);
	        cabinView.setImage(cabinImage);
	        cabinView.setFitHeight(300);
	        cabinView.setFitWidth(300);
	        cabinView.setPreserveRatio(true);
	        
	        GridPane cabinInfoContainer = new GridPane();
	        cabinInfoContainer.add(cabinType, 0, 0);
	        cabinInfoContainer.add(cabinAmenities, 0, 1);
	        
	        cabinInfo.getChildren().addAll(cabinView, cabinInfoContainer);
	        cabinInfo.setSpacing(15);
	        
	        // Add Components to layout
	        lodgingLayout.add(topMenu, 0, 0);
	        lodgingLayout.add(cabinInfo, 0, 3);
	        lodgingLayout.add(bookButton, 0, 4); // Adjust column index
	        lodgingLayout.add(reserveButton, 1, 4); // Adjust column index
	        
	        GridPane.setHgrow(lodgingLayout, Priority.ALWAYS);
	        GridPane.setVgrow(lodgingLayout, Priority.ALWAYS);
	        lodgingLayout.setMaxWidth(Double.MAX_VALUE);
	
	        // Event Listeners
	        bookButton.setOnAction(e -> {
	        	if (cabinTypes.getValue() != null) {
	        		selectedCabin = getCabin(cabinTypes.getValue());
	        		openPassengerWindow(passengerCount.getValue(), newStage);
	        	}
	        	else {
	        		openErrorWindow("CRUISE");
	        	}
	        });
	        
	        cabinTypes.valueProperty().addListener(new ChangeListener<String>() {
	            @Override public void changed(ObservableValue ov, String t, String t1) {
	            	updateLodgingInfo(t1);
	              }
	        });
	        
	        passengerCount.valueProperty().addListener((obs, oldValue, newValue) ->    {     
	        	updateLodgingInfo(cabinTypes.getValue());
	        });
	        updateLodgingInfo("Standard Cabin");
	        newStage.initModality(Modality.APPLICATION_MODAL);
	        newStage.setScene(newScene);
	        newStage.show();
    }
    
    private CruiseShip getShip(String selected) {
    	for (CruiseShip ship : CLMS.ships) {
    		if (ship.getName() == selected.trim()) {
			selectedShipID = ship.shipID;
    			return ship;
    		}
    	}
    	return null;
    	    }

    private void openBillingWindow(Stage lodgingStage, Stage passengerStage) {
    	if (!billingWindow) {
	        Stage newStage = new Stage();
	        newStage.setTitle("Billing");
	        billingWindow = true;;
	        GridPane billingPane = new GridPane();
	        Scene newScene = new Scene(billingPane, 400, 400);
	
	        billingPane.setHgap(10);
	        billingPane.setVgap(10);
	        billingPane.setStyle("-fx-padding: 20;");
	
	        // Define column constraints
	        ColumnConstraints col1 = new ColumnConstraints(100);
	        ColumnConstraints col2 = new ColumnConstraints(250);
	        billingPane.getColumnConstraints().addAll(col1, col2);
	
	        // Define row constraints
	        for (int i = 0; i < 7; i++) {
	            RowConstraints row = new RowConstraints(40);
	            billingPane.getRowConstraints().add(row);
	        }
	
	        // Add labels and input fields
	        Label firstNameLabel = new Label("First Name:");
	        TextField firstNameField = new TextField();
	
	        Label lastNameLabel = new Label("Last Name:");
	        TextField lastNameField = new TextField();
	
	        Label emailLabel = new Label("Email:");
	        TextField emailField = new TextField();
	
	        Label paymentMethodLabel = new Label("Payment Method:");
	        ComboBox<String> paymentMethodComboBox = new ComboBox<>();
	        paymentMethodComboBox.getItems().addAll("Credit Card", "PayPal", "Bank Transfer");
	
	        Label cardNumberLabel = new Label("Card Number:");
	        TextField cardNumberField = new TextField();
	
	        Label expiryDateLabel = new Label("Expiry Date:");
	        DatePicker expiryDatePicker = new DatePicker();
	
	        Button payButton = new Button("Complete");
	        Button cancelButton = new Button("Cancel");
	
	        billingPane.add(firstNameLabel, 0, 0);
	        billingPane.add(firstNameField, 1, 0);
	
	        billingPane.add(lastNameLabel, 0, 1);
	        billingPane.add(lastNameField, 1, 1);
	
	        billingPane.add(emailLabel, 0, 2);
	        billingPane.add(emailField, 1, 2);
	
	        billingPane.add(paymentMethodLabel, 0, 3);
	        billingPane.add(paymentMethodComboBox, 1, 3);
	
	        billingPane.add(cardNumberLabel, 0, 4);
	        billingPane.add(cardNumberField, 1, 4);
	
	        billingPane.add(expiryDateLabel, 0, 5);
	        billingPane.add(expiryDatePicker, 1, 5);
	
	        HBox buttonBox = new HBox(cancelButton, payButton);
	        buttonBox.setAlignment(Pos.CENTER); 
	        billingPane.add(buttonBox, 0, 6, 2, 1);
	        HBox.setMargin(cancelButton, new Insets(10,10,10,10));
	        
	        newStage.initModality(Modality.APPLICATION_MODAL);
	        newStage.setScene(newScene);
	        newStage.show();
	                
	        payButton.setOnAction(event -> {
	        	boolean anyFieldEmpty = false;
	        	for (Node node : billingPane.getChildren()) {
	        	    if (node instanceof TextField) {
	        	        TextField textField = (TextField) node;
	        	        if (textField.getText().isEmpty()) {
	        	            anyFieldEmpty = true;
	        	            break; 
	        	        }
	        	    }
	    	    }
        	    if(anyFieldEmpty) {
        	    	openErrorWindow("BILLING FIELDS CANNOT BE EMPTY");
	        	}
        	    else {
        	    	db.ships[selectedShipID].bookCabin(selectedCabin);
        	    	for(int i = 0; i < passengerNames.length; i+=2) {
        	    		DatabaseController.insertPassenger(db.ships[selectedShipID].getName(), passengerNames[i], 
        	    				passengerNames[i+1], emailField.getText(),selectedCabin);}
    	        	newStage.close();
    	        	passengerStage.close();
    	        	lodgingStage.close();
    	        	lodgingWindow = false;
    	        	passengerWindow = false;
    	        	billingWindow = false;	
        	    }

	        });
	        
	        cancelButton.setOnAction(e -> {
	        	newStage.close();
	        	billingWindow = false;
	        });
	        
	        newStage.setOnCloseRequest(e->{
	        	billingWindow = false;
	        });
    	}
    }
    
    // Opens Passenger window (need to add in value if room is booked can't proceed)
    private void openPassengerWindow(int passengers, Stage lodgingStage) {
    	if (!passengerWindow) {
    		passengerWindow = true;
	        Stage passengerStage = new Stage();
	        passengerStage.setTitle("Passenger Information Form");
	
	        GridPane grid = new GridPane();
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(10, 10, 10, 10)); // Add padding to the grid
	
	        // Define column constraints
	        ColumnConstraints col1 = new ColumnConstraints(150);
	        ColumnConstraints col2 = new ColumnConstraints(150);
	        grid.getColumnConstraints().addAll(col1, col2);
	
	        // Create input fields for passenger information
		 passengerNames = new String[passengers*2];
	        for (int i = 0; i < passengers; i++) {
	            Label firstNameLabel = new Label("Passenger " + (i + 1) + " First Name:");
	            TextField firstNameField = new TextField();
	            Label lastNameLabel = new Label("Last Name:");
	            TextField lastNameField = new TextField();
	
	            int row = i * 4; // Adjust the row index to ensure fields are aligned horizontally
	
	            // Add some spacing to the left of the labels
	            HBox firstNameBox = new HBox(10); // Adjust the spacing as needed
	            firstNameBox.getChildren().addAll(firstNameLabel);
	            
	            HBox lastNameBox = new HBox(10); // Adjust the spacing as needed
	            lastNameBox.getChildren().addAll(lastNameLabel);
	
	            grid.add(firstNameBox, 0, row);
	            grid.add(firstNameField, 1, row);
	            grid.add(lastNameBox, 0, row + 1);
	            grid.add(lastNameField, 1, row + 1);
	        }
	
	        Button submitButton = new Button("Submit");
	        GridPane.setHalignment(submitButton, HPos.RIGHT);
	        int numRows = passengers * 4 + 1; // Calculate the number of rows required
	
	        grid.add(submitButton, 1, numRows);
	
	        submitButton.setOnAction(event -> {
	        	boolean anyFieldEmpty = false;
			int i = 0;
	        	for (Node node : grid.getChildren()) {
	        	    if (node instanceof TextField) {
	        	        TextField textField = (TextField) node;
				passengerNames[i] = textField.getText();
	        	        i++;
	        	        if (textField.getText().isEmpty()) {
	        	            anyFieldEmpty = true;
	        	            break; 
	        	        }
	        	    }
	    	    }
        	    if(anyFieldEmpty) {
        	    	openErrorWindow("PASSENGER FIELDS CANNOT BE EMPTY");
	        	}
        	    else {
    	        	openBillingWindow(passengerStage, lodgingStage);	
        	    }

	        });
	
	        Scene scene = new Scene(grid, 400, 30 + numRows * 25); // Adjust the height based on numRows
	        passengerStage.initModality(Modality.APPLICATION_MODAL);
	        passengerStage.setScene(scene);
	        passengerStage.show();
	               
	        passengerStage.setOnCloseRequest(e->{
	        	passengerWindow = false;
	        });
    	}
    }

    
    private void openErrorWindow(String error) {
        Stage newStage = new Stage();
        newStage.setTitle("ERROR");

        GridPane errorPane = new GridPane();
        errorPane.setAlignment(Pos.CENTER);
        Scene newScene = new Scene(errorPane, 300, 70);

        Button close = new Button("Close");
        close.setPrefSize(100, 10);
        close.setOnAction(e -> newStage.close());
        errorPane.add(new Label(error), 0, 0);
        errorPane.add(close, 0, 1);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(newScene);
        newStage.show();
        
    }
    
    private void updateLodgingInfo(String cabin) {
    	switch (cabin) {
		case "Standard Cabin":
			cabinType.setText("Standard Cabin");
			cabinAmenities.setText("Porthole, Toilet");
	    	estCost.setText(String.valueOf(calculateCost(0)));
	    	cabinImageURL = getClass().getResourceAsStream("/application/images/cabins/Standard Cabin.jpg");
			break;
		case "Deluxe Cabin":
			cabinType.setText("Deluxe Cabin");
			cabinAmenities.setText("Window, Toilet");
	    	estCost.setText(String.valueOf(calculateCost(1)));
	    	cabinImageURL = getClass().getResourceAsStream("/application/images/cabins/Deluxe Cabin.jpg");
			break;
		case "Premium Cabin":
			cabinType.setText("Premium Cabin");
			cabinAmenities.setText("Balcony, Bath, Toilet");
	    	estCost.setText(String.valueOf(calculateCost(2)));
	    	cabinImageURL = getClass().getResourceAsStream("/application/images/cabins/Premium Cabin.jpg");
			break;
		case "Spa Cabin":
			cabinType.setText("Spa Cabin");
			cabinAmenities.setText("Balcony, Bath, Toilet, Storage Closet");
	    	estCost.setText(String.valueOf(calculateCost(3)));
	    	cabinImageURL = getClass().getResourceAsStream("/application/images/cabins/Spa Cabin.jpg");
			break;
    	}
    	cabinImage = new Image(cabinImageURL);
        cabinView.setImage(cabinImage);
       }
    
    // Updates Cruise Information (Need to update)
    private void updateCruiseInfo(String ship) {  	
        for (CruiseShip cruiseShip : CLMS.ships) {
            if (cruiseShip.getName().trim() == ship) {
            	String img = cruiseShip.getName().replace('\u00A0', ' ').trim();
    			imageURL = getClass().getResourceAsStream("/application/images/ships/" + img + ".jpg");
    			shipName.setText(img);
    			shipCruiseLine.setText(cruiseShip.getCompany()); // replace with company
    			tripLength.setText(String.valueOf(cruiseShip.getTripLength()));
    			yearBuilt.setText(String.valueOf(cruiseShip.getYearOfBuild()));
    			origin.setText(cruiseShip.getOrigin());
    			finalDestination.setText(cruiseShip.getFinalDestination()); // replace with final destination
    			passengers.setText(String.valueOf(cruiseShip.getMaxCapacity()));
            }
        }
    	shipImage = new Image(imageURL);
        shipView.setImage(shipImage);
    }
    
    // Get Ships from database (Need to update)
    private ObservableList<String> getShips() {
        ObservableList<String> shipNames = FXCollections.observableArrayList();
        for (CruiseShip cruiseShip : CLMS.ships) {
            shipNames.add(cruiseShip.getName());
        }
            return shipNames;
    }

    // Gets Rooms from database (Need to Update)
    private ObservableList<String> getCabins() {
    	return FXCollections.observableArrayList("Standard Cabin", "Deluxe Cabin", "Premium Cabin", "Spa Cabin");
    }
    
    private int getCabin(String cabin) {
    	switch (cabin) {
    	case "Standard Cabin":
    		return 0;
    	case "Deluxe Cabin":
    		return 1;
    	case "Premium Cabin":
    		return 2;
    	case "Spa Cabin":
    		return 3;
    	}
    	return 0;
    }
    
    public int calculateCost(int cabinType) {
    	int cost = 0;
    	switch (cabinType) {
	    	case 0: 
	    		cost = 400;
	    		break;
	    	case 1:
	    		cost = 600;
	    		break;
	    	case 2:
	    		cost = 800;
	    		break;
	    	case 3:
	    		cost = 1000;
	    		break;
    	 }
    	return cost*passengerCount.getValue();
    }
    
}
