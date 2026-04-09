import java.util.HashMap;
import java.util.Set;

/**
 * RoomInventory Class - Centralized Room Availability Management
 * 
 * This class manages and maintains the centralized inventory of available rooms
 * for all room types in the hotel. It uses a HashMap to provide O(1) constant-time
 * access and updates to room availability data.
 * 
 * Key Responsibilities:
 * - Initialize room inventory with predefined room types and availability counts
 * - Provide controlled access to availability information
 * - Support updates to room availability through dedicated methods
 * - Maintain single source of truth for all availability data
 * - Ensure consistency across inventory operations
 * 
 * This class demonstrates how HashMap solves the problem of scattered state
 * management that existed in previous use cases, establishing a scalable and
 * maintainable approach to inventory management.
 * 
 * @author Development Team
 * @version 3.0
 * @since 2026
 */
public class RoomInventory {
    
    // HashMap provides O(1) average-case lookup and insertion for room availability
    // Key: Room type name (e.g., "Single Room", "Double Room")
    // Value: Number of available rooms of that type
    private HashMap<String, Integer> inventory;
    
    /**
     * Constructor for RoomInventory class.
     * Initializes the inventory HashMap and populates it with initial room availability.
     * 
     * This represents the single source of truth for room availability across
     * the entire system, replacing the scattered variables from Use Case 2.
     */
    public RoomInventory() {
        this.inventory = new HashMap<>();
        initializeInventory();
    }
    
    /**
     * Initialize the inventory with predefined room types and their availability counts.
     * 
     * This method populates the HashMap with the initial state of room availability.
     * Each room type is mapped to its available count.
     */
    private void initializeInventory() {
        // Initialize single rooms - 5 available
        inventory.put("Single Room", 5);
        
        // Initialize double rooms - 3 available
        inventory.put("Double Room", 3);
        
        // Initialize suite rooms - 2 available
        inventory.put("Suite Room", 2);
    }
    
    /**
     * Retrieve the current availability for a specific room type.
     * 
     * This method demonstrates O(1) average-case lookup time provided by HashMap.
     * The get() operation directly accesses the value without iteration.
     * 
     * @param roomType The type of room to query (e.g., "Single Room")
     * @return The number of available rooms of the specified type,
     *         or 0 if the room type is not found in the inventory
     */
    public int getAvailability(String roomType) {
        // Return the availability count, or 0 if room type doesn't exist
        return inventory.getOrDefault(roomType, 0);
    }
    
    /**
     * Update the availability count for a specific room type.
     * 
     * This method allows controlled modifications to room availability.
     * It demonstrates O(1) average-case insertion/update time.
     * 
     * @param roomType The type of room to update (e.g., "Single Room")
     * @param availableCount The new availability count for this room type
     * @throws IllegalArgumentException if availableCount is negative
     */
    public void setAvailability(String roomType, int availableCount) {
        if (availableCount < 0) {
            throw new IllegalArgumentException("Availability count cannot be negative");
        }
        inventory.put(roomType, availableCount);
    }
    
    /**
     * Book a room by decrementing the availability count for a room type.
     * 
     * This method supports the core booking operation by reducing available
     * inventory when a room is successfully booked.
     * 
     * @param roomType The type of room being booked
     * @return true if the room was successfully booked (availability > 0 before decrement),
     *         false if no rooms of this type were available
     * @throws IllegalArgumentException if room type is not found in inventory
     */
    public boolean bookRoom(String roomType) {
        if (!inventory.containsKey(roomType)) {
            throw new IllegalArgumentException("Room type not found in inventory: " + roomType);
        }
        
        int currentAvailability = inventory.get(roomType);
        
        // Check if room is available before booking
        if (currentAvailability > 0) {
            // Decrement availability and update the map
            inventory.put(roomType, currentAvailability - 1);
            return true;
        }
        
        return false;
    }
    
    /**
     * Release a room by incrementing the availability count for a room type.
     * 
     * This method supports cancellations by increasing available inventory
     * when a booking is cancelled or a guest checks out.
     * 
     * @param roomType The type of room being released
     * @throws IllegalArgumentException if room type is not found in inventory
     */
    public void releaseRoom(String roomType) {
        if (!inventory.containsKey(roomType)) {
            throw new IllegalArgumentException("Room type not found in inventory: " + roomType);
        }
        
        int currentAvailability = inventory.get(roomType);
        inventory.put(roomType, currentAvailability + 1);
    }
    
    /**
     * Get the total number of available rooms across all room types.
     * 
     * This method demonstrates iteration over HashMap values to compute
     * aggregate statistics.
     * 
     * @return The total number of available rooms in the entire hotel
     */
    public int getTotalAvailableRooms() {
        int total = 0;
        for (int count : inventory.values()) {
            total += count;
        }
        return total;
    }
    
    /**
     * Get all room types currently managed by the inventory.
     * 
     * @return A set of all room type names in the inventory
     */
    public Set<String> getAllRoomTypes() {
        return inventory.keySet();
    }
    
    /**
     * Display the current state of the inventory.
     * 
     * Prints detailed information about all room types and their availability,
     * demonstrating that the HashMap centralizes all state management.
     */
    public void displayInventory() {
        System.out.println("\n*** CURRENT ROOM INVENTORY ***");
        System.out.println("(Single Source of Truth using HashMap)\n");
        
        for (String roomType : inventory.keySet()) {
            int availability = inventory.get(roomType);
            String status = availability > 0 ? "AVAILABLE" : "FULLY BOOKED";
            System.out.println("Room Type: " + roomType);
            System.out.println("  Available: " + availability + " rooms");
            System.out.println("  Status: " + status);
            System.out.println();
        }
        
        System.out.println("Total Available Rooms: " + getTotalAvailableRooms());
    }
    
    /**
     * Check if any rooms of a specific type are available.
     * 
     * @param roomType The type of room to check
     * @return true if at least one room of this type is available, false otherwise
     */
    public boolean isAvailable(String roomType) {
        return getAvailability(roomType) > 0;
    }
}
