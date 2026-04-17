import java.util.ArrayList;
import java.util.List;

/**
 * Use Case 12: Data Persistence & System Recovery (File-Based Persistence)
 * Application Entry Point - Persistence and Recovery Demonstration
 *
 * This class demonstrates how to persist application state to disk and recover
 * from it during application restart. It shows how a stateful application can
 * survive crashes and restarts by maintaining a durable snapshot of its state.
 *
 * Key Design Principles Demonstrated:
 * - Stateful application design with persistence
 * - Serialization of complex data structures
 * - Graceful handling of missing or corrupted persistence files
 * - Snapshot-based consistency model
 * - Recovery during application startup
 *
 * Problem Solved:
 * Previous use cases lost all data on application termination.
 * Real production systems must preserve state across restarts,
 * crashes, and redeployments. This use case introduces file-based
 * persistence as a bridge toward database-backed systems.
 *
 * @author Development Team
 * @version 12.0
 * @since 2026
 */
public class UseCase12DataPersistenceRecovery {

    /**
     * Main method - Entry point of the Hotel Booking Management System (Use Case 12).
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  Hotel Booking Management System");
        System.out.println("  Version: 12.0 - Data Persistence & Recovery");
        System.out.println("========================================");

        // [STEP 1] Initialize persistence service
        System.out.println("\n[STEP 1] Initializing Persistence Service");
        System.out.println("=========================================");

        PersistenceService persistenceService = new PersistenceService();
        persistenceService.displayPersistenceStatus();

        // [STEP 2] Attempt to restore system state from persistent storage
        System.out.println("\n[STEP 2] Attempting to Restore System State");
        System.out.println("=========================================");

        Object[] restoredState = persistenceService.restoreSystemState();
        BookingHistory bookingHistory = (BookingHistory) restoredState[0];
        RoomInventory roomInventory = (RoomInventory) restoredState[1];

        System.out.println("\n[Status] System state initialized");
        System.out.println("  Booking history: " + bookingHistory.getTotalBookings() + " confirmed bookings");
        System.out.println("  Inventory ready: " + roomInventory.getTotalAvailableRooms() + " rooms in stock");

        // [STEP 3] Display current state after recovery
        System.out.println("\n[STEP 3] Current System State After Recovery");
        System.out.println("=========================================");

        roomInventory.displayInventory();

        System.out.println("\nBooking History Summary:");
        System.out.println("  Total confirmed bookings: " + bookingHistory.getTotalBookings());
        System.out.println("  Total revenue: $" + String.format("%.2f", bookingHistory.getTotalRevenue()));

        // [STEP 4] Demonstrate booking operations on recovered state
        System.out.println("\n[STEP 4] Processing New Bookings on Recovered State");
        System.out.println("=========================================");

        BookingValidationService validator = new BookingValidationService();
        List<Reservation> newBookings = createNewBookingRequests();
        System.out.println("Created " + newBookings.size() + " new booking requests");

        int successfulNewBookings = 0;
        int failedNewBookings = 0;

        for (Reservation booking : newBookings) {
            try {
                validator.validateReservation(booking, roomInventory);

                if (roomInventory.bookRoom(booking.getRoomTypeRequested())) {
                    booking.setStatus("CONFIRMED");
                    bookingHistory.addConfirmedBooking(booking, 100.0, 0.0);
                    successfulNewBookings++;
                    System.out.println("Booking confirmed: " + booking.getGuestName());
                } else {
                    booking.setStatus("DECLINED");
                    failedNewBookings++;
                    System.out.println("Booking declined: " + booking.getGuestName() + " (No availability)");
                }
            } catch (BookingValidationException e) {
                booking.setStatus("DECLINED");
                failedNewBookings++;
                System.out.println("Validation failed: " + booking.getGuestName());
            }
        }

        System.out.println("\nNew Bookings Summary:");
        System.out.println("  Successful: " + successfulNewBookings);
        System.out.println("  Failed: " + failedNewBookings);

        // [STEP 5] Display updated state
        System.out.println("\n[STEP 5] Updated System State");
        System.out.println("=========================================");

        roomInventory.displayInventory();
        System.out.println("Updated Booking History:");
        System.out.println("  Total bookings: " + bookingHistory.getTotalBookings());

        // [STEP 6] Save updated state to persistent storage
        System.out.println("\n[STEP 6] Saving Updated State to Persistent Storage");
        System.out.println("=========================================");

        boolean saveSuccess = persistenceService.saveSystemState(bookingHistory, roomInventory);

        if (saveSuccess) {
            System.out.println("\nSystem state successfully persisted");
        } else {
            System.out.println("\nFailed to persist system state");
        }

        persistenceService.displayPersistenceStatus();

        // [STEP 7] Key insights
        System.out.println("\n[KEY INSIGHTS: Data Persistence & System Recovery]");
        System.out.println("=========================================");
        System.out.println("Why Persistence Matters:");
        System.out.println("- Prevents data loss on application restarts");
        System.out.println("- Enables business continuity");
        System.out.println("- Supports stateful application design");
        System.out.println("- Bridge toward database-backed systems");
        System.out.println("- Graceful recovery from failures");

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("System state preserved for next startup.");
        System.out.println("========================================\n");
    }

    /**
     * Create a list of new booking requests.
     */
    private static List<Reservation> createNewBookingRequests() {
        List<Reservation> requests = new ArrayList<>();

        String[] guestNames = {
            "John Patterson", "Sarah Miller", "Michael Chen",
            "Emily Rodriguez", "David Thompson"
        };
        String[] roomTypes = {"Single Room", "Double Room", "Suite Room"};

        for (int i = 0; i < guestNames.length; i++) {
            String name = guestNames[i];
            String email = name.toLowerCase().replace(" ", ".") + "@guest.com";
            String phone = "555-0" + (100 + i);
            String roomType = roomTypes[i % roomTypes.length];
            int nights = 1 + (i % 4);

            Reservation reservation = new Reservation(name, email, phone, roomType, nights, 1);
            requests.add(reservation);
        }

        return requests;
    }
}
