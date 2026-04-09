import java.util.ArrayList;
import java.util.List;

/**
 * RoomSearchService Class - Read-Only Room Search and Availability Check
 * 
 * This service handles all room search operations with a focus on read-only
 * access to inventory and room information. It provides guests with ways to
 * discover available rooms without modifying system state.
 * 
 * Key Design Principles:
 * - Read-Only Access: All methods are non-destructive and return copies
 * - Defensive Programming: Validates data and filters invalid results
 * - Separation of Concerns: Search logic isolated from booking/mutation logic
 * - Validation Logic: Only returns rooms with availability > 0
 * 
 * The service maintains references to both the Room domain model (for
 * descriptive information) and the RoomInventory (for availability counts).
 * This clean separation ensures that search does not interfere with
 * inventory management or booking operations.
 * 
 * @author Development Team
 * @version 4.0
 * @since 2026
 */
public class RoomSearchService {
    
    // Reference to centralized inventory for read-only access
    private RoomInventory inventory;
    
    // Registry of room type definitions for descriptive information
    private List<Room> roomDefinitions;
    
    /**
     * Constructor for RoomSearchService.
     * 
     * @param inventory The centralized RoomInventory (used for read-only access)
     */
    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
        this.roomDefinitions = new ArrayList<>();
        initializeRoomDefinitions();
    }
    
    /**
     * Initialize the room definitions registry with all room types.
     * 
     * These room objects serve as the domain model, providing descriptive
     * information such as amenities, pricing, and characteristics.
     * They remain constant and are never modified by search operations.
     */
    private void initializeRoomDefinitions() {
        roomDefinitions.add(new SingleRoom(101));
        roomDefinitions.add(new DoubleRoom(202));
        roomDefinitions.add(new SuiteRoom(305));
    }
    
    /**
     * Search for all available rooms.
     * 
     * This method performs a comprehensive search that:
     * 1. Iterates through all room definitions
     * 2. Checks availability in inventory
     * 3. Filters out rooms with zero availability
     * 4. Returns only actionable room options
     * 
     * Defensive programming ensures only valid rooms are returned:
     * - Checks that room type exists in inventory
     * - Validates availability is greater than zero
     * - Handles missing room types gracefully
     * 
     * Read-Only Guarantee:
     * - No modifications to inventory occur
     * - Returns new RoomSearchResult objects (copies)
     * - Original room objects remain unchanged
     * 
     * @return List of RoomSearchResult objects for all available rooms
     */
    public List<RoomSearchResult> searchAvailableRooms() {
        List<RoomSearchResult> results = new ArrayList<>();
        
        // Iterate through all room definitions
        for (Room room : roomDefinitions) {
            // Retrieve room type name
            String roomType = room.getRoomType();
            
            // Read availability from inventory (read-only access)
            int availability = inventory.getAvailability(roomType);
            
            // Validation logic: Only include rooms with availability > 0
            if (availability > 0) {
                // Create search result combining room info and availability
                RoomSearchResult result = new RoomSearchResult(
                    roomType,
                    room.getRoomDescription(),
                    room.getNumberOfBeds(),
                    room.getRoomSize(),
                    room.getPricePerNight(),
                    room.getAmenities(),
                    availability
                );
                
                results.add(result);
            }
        }
        
        return results;
    }
    
    /**
     * Search for rooms by specific type.
     * 
     * This method allows guests to search for a specific room type
     * and view its details and current availability.
     * 
     * Defensive programming:
     * - Validates that the room type exists
     * - Checks availability before returning result
     * - Returns null or empty indicator if not found or unavailable
     * 
     * @param roomType The specific room type to search for
     * @return RoomSearchResult if available, null if not found or unavailable
     */
    public RoomSearchResult searchRoomByType(String roomType) {
        // Find the room definition matching the requested type
        for (Room room : roomDefinitions) {
            if (room.getRoomType().equals(roomType)) {
                // Check availability in inventory
                int availability = inventory.getAvailability(roomType);
                
                // Return result only if available
                if (availability > 0) {
                    return new RoomSearchResult(
                        roomType,
                        room.getRoomDescription(),
                        room.getNumberOfBeds(),
                        room.getRoomSize(),
                        room.getPricePerNight(),
                        room.getAmenities(),
                        availability
                    );
                } else {
                    // Room type exists but not available
                    return null;
                }
            }
        }
        
        // Room type not found
        return null;
    }
    
    /**
     * Search for rooms within a specific price range.
     * 
     * This read-only operation allows guests to filter rooms by budget.
     * 
     * @param minPrice Minimum price per night
     * @param maxPrice Maximum price per night
     * @return List of RoomSearchResult objects within price range
     */
    public List<RoomSearchResult> searchRoomsByPriceRange(double minPrice, double maxPrice) {
        List<RoomSearchResult> results = new ArrayList<>();
        
        // Get all available rooms
        List<RoomSearchResult> availableRooms = searchAvailableRooms();
        
        // Filter by price range
        for (RoomSearchResult room : availableRooms) {
            if (room.getPricePerNight() >= minPrice && room.getPricePerNight() <= maxPrice) {
                results.add(room);
            }
        }
        
        return results;
    }
    
    /**
     * Search for rooms that accommodate a minimum number of guests.
     * 
     * This read-only operation allows guests to filter rooms by capacity.
     * 
     * @param minimumBeds Minimum number of beds required
     * @return List of RoomSearchResult objects with at least the specified beds
     */
    public List<RoomSearchResult> searchRoomsByCapacity(int minimumBeds) {
        List<RoomSearchResult> results = new ArrayList<>();
        
        // Get all available rooms
        List<RoomSearchResult> availableRooms = searchAvailableRooms();
        
        // Filter by bed count
        for (RoomSearchResult room : availableRooms) {
            if (room.getNumberOfBeds() >= minimumBeds) {
                results.add(room);
            }
        }
        
        return results;
    }
    
    /**
     * Check if a specific room type is available.
     * 
     * Simple availability check without returning full details.
     * Useful for validation during booking workflow.
     * 
     * @param roomType The room type to check
     * @return true if at least one room of this type is available
     */
    public boolean isRoomTypeAvailable(String roomType) {
        return inventory.isAvailable(roomType);
    }
    
    /**
     * Get the count of available rooms for a specific type.
     * 
     * Read-only access to inventory count for a specific room type.
     * 
     * @param roomType The room type to check
     * @return Number of available rooms of this type
     */
    public int getAvailabilityForRoomType(String roomType) {
        return inventory.getAvailability(roomType);
    }
    
    /**
     * Calculate total revenue potential for available rooms at full occupancy.
     * 
     * Business intelligence query using read-only data.
     * Demonstrates how search service can provide analytical information.
     * 
     * @return Total potential revenue per night if all available rooms are booked
     */
    public double calculateRevenuePotential() {
        double total = 0.0;
        
        for (RoomSearchResult room : searchAvailableRooms()) {
            total += room.getPricePerNight() * room.getAvailableCount();
        }
        
        return total;
    }
}
