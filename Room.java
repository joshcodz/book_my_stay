/**
 * Abstract Room Class - Base definition for all room types
 * 
 * This abstract class represents a generalized concept of a hotel room.
 * It defines common attributes and behavior shared by all room types
 * while enforcing a consistent structure through abstract methods.
 * 
 * The Room class serves as the foundation for concrete room implementations
 * (SingleRoom, DoubleRoom, SuiteRoom) and enables polymorphic handling
 * of different room types throughout the system.
 * 
 * @author Development Team
 * @version 2.0
 * @since 2026
 */
public abstract class Room {
    
    // Common attributes for all room types
    protected int roomNumber;
    protected String roomType;
    protected String description;
    protected int numberOfBeds;
    protected String roomSize;
    protected double pricePerNight;
    protected String[] amenities;
    
    /**
     * Constructor for Room class.
     * Initializes common room attributes used by all room types.
     * 
     * @param roomNumber Unique identifier for the room
     * @param roomType Type of room (Single, Double, Suite, etc.)
     * @param description Detailed description of the room
     * @param numberOfBeds Number of beds in the room
     * @param roomSize Size category of the room (Small, Medium, Large)
     * @param pricePerNight Price per night for the room
     * @param amenities Array of available amenities in the room
     */
    public Room(int roomNumber, String roomType, String description, 
                int numberOfBeds, String roomSize, double pricePerNight, 
                String[] amenities) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.description = description;
        this.numberOfBeds = numberOfBeds;
        this.roomSize = roomSize;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
    }
    
    /**
     * Abstract method to display room details.
     * Concrete implementations provide specialized display logic for each room type.
     */
    public abstract void displayRoomDetails();
    
    /**
     * Abstract method to get room description.
     * Allows each room type to provide customized descriptions.
     * 
     * @return String representation of the room description
     */
    public abstract String getRoomDescription();
    
    // Getters for room attributes
    public int getRoomNumber() {
        return roomNumber;
    }
    
    public String getRoomType() {
        return roomType;
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
}
