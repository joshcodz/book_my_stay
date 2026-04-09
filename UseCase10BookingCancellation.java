/**
 * Use Case 10: Booking Cancellation & Inventory Rollback
 * Application Entry Point - Safe Cancellation with Stack-Based Rollback
 *
 * This class demonstrates safe booking cancellation using Stack data structure
 * for inventory rollback operations. It implements controlled state reversal
 * to ensure inventory consistency and predictable recovery behavior when
 * bookings are cancelled.
 *
 * Key Design Principles Demonstrated:
 * - Stack<String> for LIFO rollback tracking of released room IDs
 * - State reversal in controlled, predefined order
 * - Validation before cancellation operations
 * - Immediate inventory restoration
 * - Prevention of invalid cancellation attempts
 * - Atomic rollback operations
 *
 * Problem Solved:
 * Previous use cases could create bookings but had no safe way to undo them.
 * This use case introduces controlled rollback that maintains system integrity
 * during cancellation operations.
 *
 * Flow:
 * 1. Initialize cancellation service with Stack-based rollback
 * 2. Create confirmed bookings to establish baseline state
 * 3. Demonstrate successful cancellation scenarios
 * 4. Show cancellation validation and error handling
 * 5. Demonstrate Stack-based rollback operations
 * 6. Show inventory restoration and state consistency
 * 7. Display rollback benefits and system reliability
 *
 * Stack Usage Demonstrated:
 * - Push: Track room releases during cancellation
 * - Pop: Rollback most recent operations first (LIFO)
 * - Peek: Inspect most recent release without removal
 * - Size: Track number of reversible operations
 *
 * @author Development Team
 * @version 10.0
 * @since 2026
 */
public class UseCase10BookingCancellation {

    /**
     * Main method - Entry point of the Hotel Booking Management System (Use Case 10).
     *
     * This method demonstrates:
     * 1. Stack-based rollback for booking cancellations
     * 2. Controlled state reversal with inventory restoration
     * 3. Validation of cancellation requests
     * 4. LIFO rollback logic using Stack operations
     * 5. System state consistency during cancellations
     * 6. Error handling for invalid cancellation attempts
     *
     * @param args Command-line arguments (not used in this use case)
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("========================================");
        System.out.println("  Hotel Booking Management System");
        System.out.println("  Version: 10.0 - Booking Cancellation & Inventory Rollback");
        System.out.println("========================================");

        // [STEP 1] Initialize cancellation service and system components
        System.out.println("\n[STEP 1] Initializing Cancellation Service & System Components");
        System.out.println("=========================================");

        BookingHistory bookingHistory = new BookingHistory();
        RoomInventory roomInventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService(bookingHistory, roomInventory);

        System.out.println("[OK] BookingHistory initialized");
        System.out.println("Purpose: Track confirmed bookings for cancellation");
        System.out.println("[OK] RoomInventory initialized with baseline data");
        System.out.println("[OK] CancellationService initialized");
        System.out.println("Data Structure: Stack<String> for LIFO rollback tracking");
        System.out.println("Purpose: Safe cancellation with inventory restoration");

        // [STEP 2] Create confirmed bookings to establish baseline state
        System.out.println("\n[STEP 2] Establishing Baseline Bookings");
        System.out.println("=========================================");

        // Create and confirm several bookings
        Reservation[] bookings = {
            new Reservation("Alice Johnson", "alice@email.com", "555-0101", "Single Room", 3, 1),
            new Reservation("Bob Smith", "bob@email.com", "555-0102", "Double Room", 5, 2),
            new Reservation("Carol Davis", "carol@email.com", "555-0103", "Suite Room", 2, 3),
            new Reservation("Dave Wilson", "dave@email.com", "555-0104", "Single Room", 4, 1),
            new Reservation("Eve Martinez", "eve@email.com", "555-0105", "Double Room", 3, 2)
        };

        // Simulate room allocations and confirm bookings
        String[] allocatedRoomIds = {"S101", "D201", "SU301", "S102", "D202"};

        System.out.println("Creating confirmed bookings with room allocations:");
        for (int i = 0; i < bookings.length; i++) {
            Reservation booking = bookings[i];
            booking.setStatus("CONFIRMED");

            // Book the room (reduce inventory)
            roomInventory.bookRoom(booking.getRoomTypeRequested());

            // Add to booking history
            bookingHistory.addConfirmedBooking(booking, getRoomPrice(booking.getRoomTypeRequested()), 0.0);

            System.out.println("  ✓ Confirmed: " + booking.getSummary() +
                             " (Room: " + allocatedRoomIds[i] + ")");
        }

        System.out.println("\nInitial inventory state:");
        roomInventory.displayInventory();

        // [STEP 3] Demonstrate successful cancellation scenarios
        System.out.println("\n[STEP 3] Successful Cancellation Scenarios");
        System.out.println("=========================================");

        // Cancel first booking (Alice's Single Room)
        System.out.println("Cancelling Alice's booking (#" + bookings[0].getRequestId() + "):");
        CancellationResult result1 = cancellationService.cancelBooking(
            bookings[0].getRequestId(), allocatedRoomIds[0], bookings[0].getRoomTypeRequested());
        result1.display();

        // Cancel third booking (Carol's Suite Room)
        System.out.println("\nCancelling Carol's booking (#" + bookings[2].getRequestId() + "):");
        CancellationResult result2 = cancellationService.cancelBooking(
            bookings[2].getRequestId(), allocatedRoomIds[2], bookings[2].getRoomTypeRequested());
        result2.display();

        System.out.println("\nInventory after cancellations:");
        roomInventory.displayInventory();

        // [STEP 4] Demonstrate Stack-based rollback tracking
        System.out.println("\n[STEP 4] Stack-Based Rollback Tracking");
        System.out.println("=========================================");

        cancellationService.displayReleaseStack();

        System.out.println("\nStack operations demonstration:");
        System.out.println("Stack size: " + cancellationService.getReleasedRoomsCount());
        System.out.println("Most recent release: " + cancellationService.peekLastReleasedRoom());

        // [STEP 5] Demonstrate cancellation validation and error handling
        System.out.println("\n[STEP 5] Cancellation Validation & Error Handling");
        System.out.println("=========================================");

        // Try to cancel non-existent booking
        System.out.println("Attempting to cancel non-existent booking #9999:");
        CancellationResult invalidResult1 = cancellationService.cancelBooking(9999, "X999", "Single Room");
        invalidResult1.display();

        // Try to cancel already cancelled booking
        System.out.println("\nAttempting to cancel already cancelled booking #" + bookings[0].getRequestId() + ":");
        CancellationResult invalidResult2 = cancellationService.cancelBooking(
            bookings[0].getRequestId(), allocatedRoomIds[0], bookings[0].getRoomTypeRequested());
        invalidResult2.display();

        // Try to cancel booking with wrong room type
        System.out.println("\nAttempting to cancel with mismatched room type:");
        // This should still work since we validate by reservation ID first
        CancellationResult result3 = cancellationService.cancelBooking(
            bookings[1].getRequestId(), allocatedRoomIds[1], "Single Room"); // Wrong room type
        result3.display();

        // [STEP 6] Demonstrate emergency rollback operations
        System.out.println("\n[STEP 6] Emergency Rollback Operations");
        System.out.println("=========================================");

        System.out.println("Performing emergency rollback of last release:");
        boolean rollbackSuccess = cancellationService.rollbackLastRelease();
        System.out.println("Rollback result: " + (rollbackSuccess ? "SUCCESS" : "FAILED"));

        System.out.println("\nRelease stack after rollback:");
        cancellationService.displayReleaseStack();

        // [STEP 7] Demonstrate system state consistency
        System.out.println("\n[STEP 7] System State Consistency Check");
        System.out.println("=========================================");

        System.out.println("Final inventory state:");
        roomInventory.displayInventory();

        System.out.println("\nBooking history status:");
        for (Reservation booking : bookingHistory.getAllBookings()) {
            System.out.println("  " + booking.getSummary());
        }

        System.out.println("\nCancellation statistics:");
        int[] stats = cancellationService.getCancellationStats();
        System.out.println("  Total cancellations processed: " + stats[0]);
        System.out.println("  Active releases tracked: " + stats[1]);

        // [STEP 8] Key insights summary
        System.out.println("\n[KEY INSIGHTS: Booking Cancellation & Stack-Based Rollback]");
        System.out.println("=========================================");
        System.out.println("Why Stack<String> Enables Safe State Reversal:");
        System.out.println();
        System.out.println("1. LIFO Rollback Logic");
        System.out.println("   - Stack.pop() returns most recent allocation first");
        System.out.println("   - Natural undo behavior matches user expectations");
        System.out.println("   - Simplifies rollback implementation");
        System.out.println();
        System.out.println("2. Controlled State Reversal");
        System.out.println("   - Strict order: validate → update status → restore inventory → track release");
        System.out.println("   - Prevents partial rollbacks and inconsistent states");
        System.out.println("   - Atomic operations maintain system integrity");
        System.out.println();
        System.out.println("3. Inventory Restoration");
        System.out.println("   - Immediate inventory increment after cancellation");
        System.out.println("   - RoomInventory.releaseRoom() restores availability");
        System.out.println("   - Real-time inventory accuracy");
        System.out.println();
        System.out.println("4. Validation Before Cancellation");
        System.out.println("   - Check reservation exists and is cancellable");
        System.out.println("   - Prevent invalid or duplicate cancellation attempts");
        System.out.println("   - Safe failure handling for edge cases");
        System.out.println();
        System.out.println("5. Stack Operations for Tracking");
        System.out.println("   - push(): Track room releases during cancellation");
        System.out.println("   - peek(): Inspect most recent release without removal");
        System.out.println("   - pop(): Remove and process releases during rollback");
        System.out.println("   - size(): Monitor number of reversible operations");
        System.out.println();
        System.out.println("6. Emergency Rollback Capability");
        System.out.println("   - rollbackLastRelease(): Undo most recent operation");
        System.out.println("   - Error recovery mechanism");
        System.out.println("   - System stability during failure scenarios");
        System.out.println();
        System.out.println("7. State Consistency Guarantees");
        System.out.println("   - Inventory counts always reflect current reality");
        System.out.println("   - Booking status accurately represents lifecycle");
        System.out.println("   - No orphaned allocations or invalid states");
        System.out.println();
        System.out.println("8. Scalability & Performance");
        System.out.println("   - O(1) Stack operations for fast rollback");
        System.out.println("   - Minimal memory overhead for tracking");
        System.out.println("   - Efficient for high-volume cancellation scenarios");

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================\n");
    }

    /**
     * Helper method to get room prices for revenue calculation.
     */
    private static double getRoomPrice(String roomType) {
        switch (roomType) {
            case "Single Room": return 75.00;
            case "Double Room": return 125.00;
            case "Suite Room": return 250.00;
            default: return 0.00;
        }
    }
}
