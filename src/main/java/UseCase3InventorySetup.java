/**
 * Use Case 3: Centralized Room Inventory Management
 * Application Entry Point - Room Inventory System
 * 
 * This class demonstrates centralized inventory management using a HashMap
 * to replace the scattered availability variables from Use Case 2.
 * 
 * The RoomInventory class provides:
 * - A single source of truth for room availability
 * - O(1) constant-time access and update operations
 * - Encapsulated inventory logic with controlled access methods
 * - Scalable design that easily accommodates new room types
 * 
 * This use case addresses the key limitation of the previous approach:
 * scattered variables are replaced with a centralized HashMap, enabling
 * consistent state management across the entire system.
 * 
 * Key Concepts Demonstrated:
 * - HashMap: Key-value pairs for O(1) lookups and updates
 * - Single Source of Truth: Centralized availability data
 * - Encapsulation: Inventory logic contained in dedicated class
 * - Scalability: Adding room types requires only HashMap insertion
 * - Controlled Operations: Booking and release methods ensure consistency
 * 
 * @author Development Team
 * @version 3.0
 * @since 2026
 */
public class UseCase3InventorySetup {
    
    /**
     * Main method - Entry point of the Hotel Booking Management System (Use Case 3).
     * 
     * This method demonstrates:
     * 1. Initialization of centralized room inventory
     * 2. Display of current room availability
     * 3. Booking operations and inventory updates
     * 4. Verification of data consistency
     * 
     * @param args Command-line arguments (not used in this use case)
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("========================================");
        System.out.println("  Hotel Booking Management System");
        System.out.println("  Version: 3.0 - Centralized Inventory");
        System.out.println("========================================");
        
        // Initialize the centralized room inventory
        // This replaces the scattered variables from Use Case 2
        RoomInventory inventory = new RoomInventory();
        
        System.out.println("\n[STEP 1] Initializing Room Inventory");
        System.out.println("=========================================");
        System.out.println("HashMap created: inventory = new RoomInventory()");
        System.out.println("Single source of truth established for room availability.");
        
        // Display initial inventory state
        inventory.displayInventory();
        
        // Demonstrate controlled booking operations
        System.out.println("\n[STEP 2] Demonstrating Booking Operations");
        System.out.println("=========================================");
        
        String roomTypeToBook = "Single Room";
        System.out.println("\nAttempting to book: " + roomTypeToBook);
        if (inventory.bookRoom(roomTypeToBook)) {
            System.out.println("??? Booking successful!");
            System.out.println("  Current availability after booking: " + inventory.getAvailability(roomTypeToBook) + " rooms");
        } else {
            System.out.println("??? Booking failed - No rooms available");
        }
        
        // Book another single room
        System.out.println("\nAttempting to book another: " + roomTypeToBook);
        if (inventory.bookRoom(roomTypeToBook)) {
            System.out.println("??? Booking successful!");
            System.out.println("  Current availability after booking: " + inventory.getAvailability(roomTypeToBook) + " rooms");
        }
        
        // Book a double room
        String doubleRoomType = "Double Room";
        System.out.println("\nAttempting to book: " + doubleRoomType);
        if (inventory.bookRoom(doubleRoomType)) {
            System.out.println("??? Booking successful!");
            System.out.println("  Current availability after booking: " + inventory.getAvailability(doubleRoomType) + " rooms");
        }
        
        // Display inventory after bookings
        System.out.println("\n[STEP 3] Inventory State After Bookings");
        System.out.println("=========================================");
        inventory.displayInventory();
        
        // Demonstrate room release (cancellation)
        System.out.println("\n[STEP 4] Demonstrating Room Release (Cancellation)");
        System.out.println("=========================================");
        System.out.println("\nCancelling booking for: " + roomTypeToBook);
        inventory.releaseRoom(roomTypeToBook);
        System.out.println("??? Room released successfully!");
        System.out.println("  Current availability after release: " + inventory.getAvailability(roomTypeToBook) + " rooms");
        
        // Display final inventory state
        System.out.println("\n[STEP 5] Final Inventory State");
        System.out.println("=========================================");
        inventory.displayInventory();
        
        // Demonstrate query methods
        System.out.println("\n[STEP 6] Inventory Query Operations");
        System.out.println("=========================================");
        System.out.println("\nAvailability Status:");
        for (String roomType : inventory.getAllRoomTypes()) {
            boolean isAvailable = inventory.isAvailable(roomType);
            System.out.println("  " + roomType + ": " + (isAvailable ? "AVAILABLE" : "FULLY BOOKED"));
        }
        
        // Display benefits summary
        System.out.println("\n[BENEFITS OF CENTRALIZED INVENTORY MANAGEMENT]");
        System.out.println("=========================================");
        System.out.println("??? Single Source of Truth");
        System.out.println("  - All availability data in one HashMap");
        System.out.println("  - No scattered variables causing inconsistency");
        System.out.println();
        System.out.println("??? O(1) Constant-Time Operations");
        System.out.println("  - HashMap.get() and HashMap.put() are O(1) average case");
        System.out.println("  - Fast availability lookups regardless of inventory size");
        System.out.println();
        System.out.println("??? Scalability");
        System.out.println("  - Adding new room types only requires put() operation");
        System.out.println("  - No changes to reservation or booking logic needed");
        System.out.println();
        System.out.println("??? Encapsulation");
        System.out.println("  - Inventory logic contained in RoomInventory class");
        System.out.println("  - Controlled access through dedicated methods");
        System.out.println("  - Ensures data consistency and prevents misuse");
        
        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================\n");
    }
}
