/**
 * SingleRoom Class - Concrete implementation of a single room
 * 
 * This class represents a single occupancy room in the hotel.
 * It extends the abstract Room class and specializes attributes
 * for the single room type, including standard amenities and pricing.
 * 
 * A single room is designed for one guest with basic comfort amenities.
 * 
 * @author Development Team
 * @version 2.0
 * @since 2026
 */
public class SingleRoom extends Room {
    
    /**
     * Constructor for SingleRoom class.
     * Initializes a single room with predefined characteristics.
     * 
     * @param roomNumber Unique identifier for the single room
     */
    public SingleRoom(int roomNumber) {
        super(
            roomNumber,
            "Single Room",
            "Comfortable room for one guest with modern amenities",
            1,  // numberOfBeds
            "Small",  // roomSize
            75.00,  // pricePerNight
            new String[]{"WiFi", "Flat-screen TV", "Work Desk", "Private Bathroom", "Air Conditioning"}
        );
    }
    
    /**
     * Display detailed information about the single room.
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
            System.out.println("  • " + amenity);
        }
    }
    
    /**
     * Get a description string for the single room.
     * 
     * @return String describing this single room
     */
    @Override
    public String getRoomDescription() {
        return "Single Room: " + description + " - $" + String.format("%.2f", pricePerNight) + "/night";
    }
}
