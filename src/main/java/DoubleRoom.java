/**
 * DoubleRoom Class - Concrete implementation of a double room
 * 
 * This class represents a double occupancy room in the hotel.
 * It extends the abstract Room class and specializes attributes
 * for the double room type, including comprehensive amenities and pricing.
 * 
 * A double room is designed for two guests with enhanced comfort features.
 * 
 * @author Development Team
 * @version 2.0
 * @since 2026
 */
public class DoubleRoom extends Room {
    
    /**
     * Constructor for DoubleRoom class.
     * Initializes a double room with predefined characteristics.
     * 
     * @param roomNumber Unique identifier for the double room
     */
    public DoubleRoom(int roomNumber) {
        super(
            roomNumber,
            "Double Room",
            "Spacious room for two guests with premium amenities",
            2,  // numberOfBeds
            "Medium",  // roomSize
            125.00,  // pricePerNight
            new String[]{"WiFi", "Flat-screen TV", "Mini Bar", "Work Area", "Private Bathroom", "Air Conditioning", "Room Service"}
        );
    }
    
    /**
     * Display detailed information about the double room.
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
     * Get a description string for the double room.
     * 
     * @return String describing this double room
     */
    @Override
    public String getRoomDescription() {
        return "Double Room: " + description + " - $" + String.format("%.2f", pricePerNight) + "/night";
    }
}
