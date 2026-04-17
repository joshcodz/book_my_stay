import java.util.ArrayList;
import java.util.List;

/**
 * RoomSearchResult Class - Encapsulates a search result item
 * 
 * This class represents a single room type search result, pairing room
 * details (from Room domain objects) with current availability information
 * (from RoomInventory). This clean separation ensures that search results
 * contain all information needed for display without exposing internal
 * system state.
 * 
 * @author Development Team
 * @version 4.0
 * @since 2026
 */
public class RoomSearchResult {
    
    private String roomType;
    private String description;
    private int numberOfBeds;
    private String roomSize;
    private double pricePerNight;
    private String[] amenities;
    private int availableCount;
    
    /**
     * Constructor for RoomSearchResult.
     * 
     * @param roomType Type of room
     * @param description Room description
     * @param numberOfBeds Number of beds in room
     * @param roomSize Size category of room
     * @param pricePerNight Price per night
     * @param amenities Array of amenities
     * @param availableCount Current availability
     */
    public RoomSearchResult(String roomType, String description, int numberOfBeds,
                          String roomSize, double pricePerNight, 
                          String[] amenities, int availableCount) {
        this.roomType = roomType;
        this.description = description;
        this.numberOfBeds = numberOfBeds;
        this.roomSize = roomSize;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
        this.availableCount = availableCount;
    }
    
    // Getters for all properties
    public String getRoomType() {
        return roomType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getNumberOfBeds() {
        return numberOfBeds;
    }
    
    public String getRoomSize() {
        return roomSize;
    }
    
    public double getPricePerNight() {
        return pricePerNight;
    }
    
    public String[] getAmenities() {
        return amenities;
    }
    
    public int getAvailableCount() {
        return availableCount;
    }
    
    /**
     * Display the search result in formatted output.
     */
    public void display() {
        System.out.println("\n--- " + roomType + " ---");
        System.out.println("Description: " + description);
        System.out.println("Beds: " + numberOfBeds + " | Size: " + roomSize);
        System.out.println("Price per Night: $" + String.format("%.2f", pricePerNight));
        System.out.println("Available: " + availableCount + " rooms");
        System.out.println("Amenities: ");
        for (String amenity : amenities) {
            System.out.println("  ??? " + amenity);
        }
    }
}
