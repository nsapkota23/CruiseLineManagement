package application;
import java.util.ArrayList;


public class CruiseShip {

	//variables
	 int shipID;
	String name;
	String company;
	String location;
	int tripLength; 
	int numCabins;
	int yearOfBuild;
	int maintenance;
	int maxCapacity;
	String origin;
	String finalDestination;
	String destination1;
	String destination2;
	String destination3;
	String destination4;
	String destination5;
	Boolean inService; 
	
	//array of cabins
	ArrayList<Cabin> cabins;
	
	public CruiseShip(int shipID, String name, String company, String location, int tripLength, int numCabins, 
    		int yearOfBuild, int maintenance, int maxCapacity,String origin, String finalDestination, 
    		String destination1,String destination2, String destination3, String destination4, String destination5) {
		this.company = company;
		this.shipID = shipID;
		this.name = name;
		this.location = location;
		this.origin = origin;
		this.finalDestination = finalDestination;
		this.destination1 = destination1;
		this.destination2 = destination2;
		this.destination3 = destination3;
		this.destination4 = destination4;
		this.destination5 = destination5;
		this.tripLength = tripLength; 
		this.numCabins = numCabins;
		this.yearOfBuild = yearOfBuild;
		this.maintenance = maintenance;
		this.maxCapacity = maxCapacity;
		setService(maintenance);
		cabins = new ArrayList<Cabin>();
	}
	
	public void bookCabin(int type) {
		Cabin cabin = null;
		switch (type) {
		case 0:
			cabin = new StandardCabin();
			break;
		case 1:
			cabin = new DeluxeCabin();
			break;
		case 2:
			cabin = new PremiumCabin();
			break;
		case 3:
			cabin = new SpaCabin();
			break;
		}
		cabins.add(cabin);
	}
	
	public void setService(int maintenance) {
		if(maintenance == 2023) {
			inService = false; 
		}else {
				inService = true;
				}
		}
	
	public Boolean getStatus() {
		return inService;
	}
	//getter methods
	public int getShipID(){
		return shipID;
		}
	public String getCompany(){
		return company;
		}
	public String getName(){
		return name;
		}
	public String getLocation(){
		return location;
		}
	public String getOrigin(){
		return origin;
		}
	public String getFinalDestination() {
		return finalDestination;
		}
	public String getDestination1() {
		return destination1;
		}
	public String getDestination2() {
		return destination2;
		} 
	public String getDestination3() {
		return destination3;
		} 
	public String getDestination4() {
		return destination4;
		} 
	public String getDestination5() {
		return destination5;
		} 
	public int getTripLength(){
		return tripLength;
		}
	public int getYearOfBuild(){
		return yearOfBuild;
		}
	public int getNumCabins(){
		return numCabins;
		}
	public int getMaintenance() {
		return maintenance;
		}
	public void setMaintenance(int maintenance) {
		this.maintenance = maintenance;
		DatabaseController.updateShip(shipID+1, maintenance);
		}
	public int getMaxCapacity(){
		return maxCapacity;
		}
	public int getAvailableCabins() {
		return getNumCabins() - cabins.size();	
		}
	public void printShipDetails() {
		System.out.println(shipID + name + company + location + tripLength + numCabins + 
	    		yearOfBuild + maintenance + maxCapacity + origin + finalDestination + 
	    		 destination1 + destination2 + destination3 + destination4 + destination5);
	}
}
