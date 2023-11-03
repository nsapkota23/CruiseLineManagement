package application;

public class Cabin{

	Boolean Paid = false;
	String amenities;
	int cabinType;

	public Cabin(String amenities, int cabinType) {
		this.amenities = amenities;
		this.cabinType = cabinType;
	}

	public int findPassenger(String name) {
		return -1;
	}

	public void getPassengers() {
	}
	public String getAmenities() {return amenities;}
}

 class StandardCabin extends Cabin{
	private static int cabinType = 0;
		public StandardCabin() {
		super("Porthole, Toilet,", cabinType);
	}
}
 
 class DeluxeCabin extends Cabin{
		private static int cabinType = 1;
		public DeluxeCabin() {
			super("Window, Toilet", cabinType);
		}
	}

 class PremiumCabin extends Cabin {
	private static int cabinType = 2;	
	public PremiumCabin() {
		super("Balcony, Bath, Toilet", cabinType);
	}
}
 
 class SpaCabin extends Cabin{
		private static int cabinType = 3;
		public SpaCabin() {
			super("Balcony, Bath, Toilet, Storage Closet", cabinType);
		}
	}
