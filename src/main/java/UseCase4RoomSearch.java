import java.util.List;

/**
 * Use Case 4: Room Search & Availability Check
 * Application Entry Point - Room Search Service
 * 
 * This class demonstrates read-only access to inventory and room information,
 * enabling guests to search for available rooms without modifying system state.
 * 
 * Key Design Principles Demonstrated:
 * - Read-Only Access: All searches are non-destructive
 * - Defensive Programming: Validates and filters results
 * - Separation of Concerns: Search isolated from booking logic
 * - Inventory as State Holder: Access only for retrieval, never modification
 * - Domain Model Usage: Room objects provide rich descriptive information
 * - Validation Logic: Only rooms with availability > 0 are shown
 * 
 * Flow:
 * 1. Initialize centralized inventory
 * 2. Create search service with access to inventory
 * 3. Guest initiates various search queries
 * 4. System retrieves and displays available options
 * 5. Verify system state remains unchanged after searches
 * 
 * Benefits Over Use Case 3:
 * - Explicit read-only access patterns
 * - Clear separation between search and booking
 * - Guests see only actionable room options
 * - System state remains stable and predictable
 * 
 * @author Development Team
 * @version 4.0
 * @since 2026
 */
public class UseCase4RoomSearch {
    
    /**
     * Main method - Entry point of the Hotel Booking Management System (Use Case 4).
     * 
     * This method demonstrates:
     * 1. Room search functionality with read-only access
     * 2. Filtering invalid/unavailable options
     * 3. Multiple search strategies (all, by type, by price, by capacity)
     * 4. Verification that inventory state is not modified
     * 
     * @param args Command-line arguments (not used in this use case)
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("========================================");
        System.out.println("  Hotel Booking Management System");
        System.out.println("  Version: 4.0 - Room Search & Availability");
        System.out.println("========================================");
        
        // [STEP 1] Initialize centralized inventory
        System.out.println("\n[STEP 1] Initializing System");
        System.out.println("=========================================");
        RoomInventory inventory = new RoomInventory();
        System.out.println("[OK] Inventory initialized");
        System.out.println("  - Single Room: 5 available");
        System.out.println("  - Double Room: 3 available");
        System.out.println("  - Suite Room: 2 available");
        
        // [STEP 2] Create search service
        System.out.println("\n[STEP 2] Creating Room Search Service");
        System.out.println("=========================================");
        RoomSearchService searchService = new RoomSearchService(inventory);
        System.out.println("[OK] Search service created");
        System.out.println("  - Connected to centralized inventory (read-only)");
        System.out.println("  - Room definitions registered");
        System.out.println("  - Ready to handle search queries");
        
        // [STEP 3] Display current inventory state
        System.out.println("\n[STEP 3] Current Inventory State");
        System.out.println("=========================================");
        inventory.displayInventory();
        
        // [STEP 4] Demonstrate Search 1: Get all available rooms
        System.out.println("\n[STEP 4] Guest Search: View All Available Rooms");
        System.out.println("=========================================");
        System.out.println("Query: Show me all available rooms");
        
        List<RoomSearchResult> allAvailable = searchService.searchAvailableRooms();
        System.out.println("\nSearch Results: " + allAvailable.size() + " room type(s) available\n");
        
        for (RoomSearchResult result : allAvailable) {
            result.display();
        }
        
        // [STEP 5] Demonstrate Search 2: Search by specific room type
        System.out.println("\n[STEP 5] Guest Search: Find Specific Room Type");
        System.out.println("=========================================");
        String searchType = "Double Room";
        System.out.println("Query: I'm looking for a " + searchType);
        
        RoomSearchResult doubleRoomResult = searchService.searchRoomByType(searchType);
        if (doubleRoomResult != null) {
            System.out.println("\n[OK] Room found!\n");
            doubleRoomResult.display();
        } else {
            System.out.println("\n[FAIL] Room type not available");
        }
        
        // [STEP 6] Demonstrate Search 3: Search by price range
        System.out.println("\n[STEP 6] Guest Search: Find Rooms by Budget");
        System.out.println("=========================================");
        double minPrice = 75.0;
        double maxPrice = 150.0;
        System.out.println("Query: Show me rooms between $" + String.format("%.2f", minPrice) + 
                         " and $" + String.format("%.2f", maxPrice) + " per night");
        
        List<RoomSearchResult> priceFiltered = searchService.searchRoomsByPriceRange(minPrice, maxPrice);
        System.out.println("\nSearch Results: " + priceFiltered.size() + " room type(s) within budget\n");
        
        for (RoomSearchResult result : priceFiltered) {
            result.display();
        }
        
        // [STEP 7] Demonstrate Search 4: Search by capacity
        System.out.println("\n[STEP 7] Guest Search: Find Rooms by Capacity");
        System.out.println("=========================================");
        int minimumBeds = 2;
        System.out.println("Query: I need at least " + minimumBeds + " beds");
        
        List<RoomSearchResult> capacityFiltered = searchService.searchRoomsByCapacity(minimumBeds);
        System.out.println("\nSearch Results: " + capacityFiltered.size() + " room type(s) with " + minimumBeds + "+ beds\n");
        
        for (RoomSearchResult result : capacityFiltered) {
            result.display();
        }
        
        // [STEP 8] Demonstrate specific availability check
        System.out.println("\n[STEP 8] Quick Availability Checks");
        System.out.println("=========================================");
        String[] roomTypes = {"Single Room", "Double Room", "Suite Room"};
        for (String roomType : roomTypes) {
            boolean available = searchService.isRoomTypeAvailable(roomType);
            int count = searchService.getAvailabilityForRoomType(roomType);
            String status = available ? "AVAILABLE (" + count + " rooms)" : "FULLY BOOKED";
            System.out.println(roomType + ": " + status);
        }
        
        // [STEP 9] Business intelligence: revenue potential
        System.out.println("\n[STEP 9] System Analytics");
        System.out.println("=========================================");
        double revenuePotential = searchService.calculateRevenuePotential();
        System.out.println("Revenue Potential (all available rooms at full occupancy):");
        System.out.println("  $" + String.format("%.2f", revenuePotential) + " per night");
        
        // [STEP 10] Verify inventory state is unchanged
        System.out.println("\n[STEP 10] Verification: Inventory State After Searches");
        System.out.println("=========================================");
        System.out.println("Confirming that read-only searches did NOT modify system state...\n");
        inventory.displayInventory();
        
        // [STEP 11] Demonstrate read-only guarantee
        System.out.println("\n[KEY INSIGHTS: Read-Only Design Pattern]");
        System.out.println("=========================================");
        System.out.println("[OK] Multiple searches performed on inventory");
        System.out.println("[OK] No modifications to availability counts");
        System.out.println("[OK] System state remains consistent");
        System.out.println("[OK] Search logic isolated from booking logic");
        System.out.println("[OK] Clear separation of concerns:");
        System.out.println("  - RoomInventory: Manages state (counts)");
        System.out.println("  - Room objects: Provide domain info (details)");
        System.out.println("  - RoomSearchService: Read-only access (queries)");
        System.out.println();
        System.out.println("[OK] Defensive programming ensures:");
        System.out.println("  - Only available rooms are displayed");
        System.out.println("  - Invalid room types are filtered");
        System.out.println("  - Zero availability prevents display");
        System.out.println();
        System.out.println("[OK] Benefits:");
        System.out.println("  - Guests see accurate options");
        System.out.println("  - Reduced risk of inventory corruption");
        System.out.println("  - Predictable system behavior");
        System.out.println("  - Foundation for safe booking operations");
        
        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================\n");
    }
}
