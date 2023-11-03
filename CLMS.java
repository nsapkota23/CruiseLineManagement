package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CLMS {

	static CruiseShip[] ships = new CruiseShip[12]; 

	CLMS() {
		DatabaseController.connect();
		loadShipArray();
	}

	//test for ship array to verify load
	@SuppressWarnings("unused")
	public static void main(String[] args) throws SQLException {  
		CLMS clms = new CLMS();
		for(int i = 0; i < ships.length; i++) {
			ships[i].printShipDetails();
		}

		// loop through the result set  
		ResultSet rs = databaseQuery("ShipName,Maintenance", "ships", "Maintenance", 2021);  
		while (rs.next()) {  
			System.out.println(
					rs.getString("ShipName")+  "\t" +
					rs.getInt("Maintenance"));
		}  
	}


	//method to load array for ship objects from database
	public static void loadShipArray(){
		for(int i = 1; i <= ships.length; i++) {
			queryShipObjectData(i);
		}
	}
	//method to query all ships from database and return as ship object  
	public static void queryShipObjectData(int i) {
		String sql = "SELECT * FROM ships WHERE id = " + i;

		try {  
			Statement stmt  = DatabaseController.getConn().createStatement();  
			ResultSet rs    = stmt.executeQuery(sql);  
			ships[rs.getInt("id")-1] = new CruiseShip(rs.getInt("id"),
					rs.getString("ShipName"),
					rs.getString("ShipCompany"),
					rs.getString("Location"),
					rs.getInt("TripLength"), 
					rs.getInt("Cabins"),
					rs.getInt("YearOfBuild"),
					rs.getInt("Maintenance"),
					rs.getInt("Capacity"),
					rs.getString("Origin"),
					rs.getString("FinalDestination"),
					rs.getString("Destination1"),
					rs.getString("Destination2"),
					rs.getString("Destination3"),
					rs.getString("Destination4"),
					rs.getString("Destination5")); 
		} catch (SQLException e) {  
			System.out.println(e.getMessage());  
		} 
	}


	//method to run SQL database query for string
	public static ResultSet databaseQuery(String column, String tableName, String searchTerm, String value){  
		String sql = "SELECT "+column+" FROM "+tableName+ 
				" WHERE "+searchTerm+" = "+value;
		ResultSet rs = queryCore(sql);
		return rs;
	}  
	//method to run SQL database query for int
	public static ResultSet databaseQuery(String column, String tableName, String searchTerm, int value){  
		String sql = "SELECT "+column+" FROM " +tableName+ " WHERE " +searchTerm+ " = " +value;;  
		ResultSet rs = queryCore(sql);
		return rs;
	}  
	//core of SQL database query.  runs query and returns results
	public static ResultSet queryCore(String sql) {
		ResultSet rs = null;
		try {  
			Statement stmt  = DatabaseController.getConn().createStatement();  
			rs    = stmt.executeQuery(sql);  
		} catch (SQLException e) {  
			System.out.println(e.getMessage());  
		}
		return rs;  
	}


}
