/**
 * SuiteRoom Class - Concrete implementation of a suite room
 * 
 * This class represents a luxury suite room in the hotel.
 * It extends the abstract Room class and specializes attributes
 * for the suite room type, including premium amenities and pricing.
 * 
 * A suite room is designed for guests seeking luxury accommodations
 * with maximum comfort and exclusive features.
 * 
 * @author Development Team
 * @version 2.0
 * @since 2026
 */
public class SuiteRoom extends Room {
    
    /**
     * Constructor for SuiteRoom class.
     * Initializes a suite room with predefined characteristics.
     * 
     * @param roomNumber Unique identifier for the suite room
     */
    public SuiteRoom(int roomNumber) {
        super(
            roomNumber,
            "Suite Room",
            "Luxurious suite with separate living area and premium amenities",
            3,  // numberOfBeds
            "Large",  // roomSize
            250.00,  // pricePerNight
            new String[]{"WiFi", "Smart TV", "Mini Bar", "Lounge Area", "Luxury Bathroom", "Air Conditioning", "Room Service", "Concierge", "Spa Bath", "Premium Bedding"}
        );
    }
    
    /**
     * Display detailed information about the suite room.
     * Prints formatted room details including amenities.
     */
    @Override
    public void displayRoomDetails() {
        System.out.println("\n--- " + roomType + " ---");
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Description: " + description);
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Room Size: " + roomSize);
        System.out.println("Price per Night: $" + String.format("%.2f", pricePerNight));
        System.out.println("Amenities: ");
        for (String amenity : amenities) {
            System.out.println("  ??? " + amenity);
        }
    }
    
    /**
     * Get a description string for the suite room.
     * 
     * @return String describing this suite room
     */
    @Override
    public String getRoomDescription() {
        return "Suite Room: " + description + " - $" + String.format("%.2f", pricePerNight) + "/night";
    }
}
