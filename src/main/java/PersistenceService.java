import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PersistenceService Class - File-Based State Persistence and Recovery
 *
 * This class handles serialization and deserialization of system state,
 * enabling the booking system to survive application restarts and crashes.
 * It provides a bridge between in-memory data structures and persistent storage.
 *
 * Key Design Principles:
 * - Separation of concerns between business logic and persistence
 * - Graceful handling of missing or corrupted persistence files
 * - Snapshot-based persistence for consistency
 * - Support for future database migration
 *
 * Features:
 * - Save booking history to file
 * - Save inventory state to file
 * - Restore booking history from file
 * - Restore inventory state from file
 * - Handle file not found gracefully
 * - Handle corrupted file gracefully
 * - Provide audit trail of persistence operations
 *
 * @author Development Team
 * @version 12.0
 * @since 2026
 */
public class PersistenceService {

    private static final String PERSISTENCE_DIR = "booking_data/";
    private static final String BOOKING_HISTORY_FILE = PERSISTENCE_DIR + "bookings.txt";
    private static final String INVENTORY_FILE = PERSISTENCE_DIR + "inventory.txt";

    /**
     * Constructor for PersistenceService.
     * Ensures persistence directory exists.
     */
    public PersistenceService() {
        ensurePersistenceDirectoryExists();
    }

    /**
     * Ensure the persistence directory exists.
     * Create it if it doesn't.
     */
    private void ensurePersistenceDirectoryExists() {
        File dir = new File(PERSISTENCE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("[Persistence] Created persistence directory: " + PERSISTENCE_DIR);
        }
    }

    /**
     * Save booking history to persistent storage.
     *
     * @param bookingHistory The booking history to persist
     * @return true if save was successful, false otherwise
     */
    public boolean saveBookingHistory(BookingHistory bookingHistory) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKING_HISTORY_FILE))) {
            List<Reservation> bookings = bookingHistory.getAllBookings();
            
            writer.println("# Booking History Snapshot");
            writer.println("# Total Bookings: " + bookings.size());
            writer.println("# Format: guestName | guestEmail | guestPhone | roomType | nights | guests | status");
            writer.println();

            for (Reservation booking : bookings) {
                writer.println(booking.getGuestName() + "|" +
                             booking.getGuestEmail() + "|" +
                             booking.getGuestPhone() + "|" +
                             booking.getRoomTypeRequested() + "|" +
                             booking.getNumberOfNights() + "|" +
                             booking.getNumberOfGuests() + "|" +
                             booking.getStatus());
            }

            System.out.println("[Persistence] Booking history saved (" + bookings.size() + " bookings)");
            return true;

        } catch (IOException e) {
            System.out.println("[Persistence] Failed to save booking history: " + e.getMessage());
            return false;
        }
    }

    /**
     * Save inventory state to persistent storage.
     *
     * @param roomInventory The room inventory to persist
     * @return true if save was successful, false otherwise
     */
    public boolean saveInventory(RoomInventory roomInventory) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INVENTORY_FILE))) {
            Map<String, Integer> inventory = roomInventory.getInventorySnapshot();

            writer.println("# Inventory Snapshot");
            writer.println("# Format: roomType | availableCount");
            writer.println();

            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                writer.println(entry.getKey() + "|" + entry.getValue());
            }

            System.out.println("[Persistence] Inventory saved (" + inventory.size() + " room types)");
            return true;

        } catch (IOException e) {
            System.out.println("[Persistence] Failed to save inventory: " + e.getMessage());
            return false;
        }
    }

    /**
     * Restore booking history from persistent storage.
     * Returns empty history if file doesn't exist or is corrupted.
     *
     * @return Restored BookingHistory or empty BookingHistory if restoration fails
     */
    public BookingHistory restoreBookingHistory() {
        BookingHistory history = new BookingHistory();

        File file = new File(BOOKING_HISTORY_FILE);
        if (!file.exists()) {
            System.out.println("[Persistence] No booking history file found. Starting with empty history.");
            return history;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_HISTORY_FILE))) {
            String line;
            int lineNumber = 0;
            int restoredCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip comments and empty lines
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 7) {
                        String guestName = parts[0].trim();
                        String guestEmail = parts[1].trim();
                        String guestPhone = parts[2].trim();
                        String roomType = parts[3].trim();
                        int nights = Integer.parseInt(parts[4].trim());
                        int guests = Integer.parseInt(parts[5].trim());
                        String status = parts[6].trim();

                        Reservation booking = new Reservation(guestName, guestEmail, guestPhone, roomType, nights, guests);
                        booking.setStatus(status);
                        history.addConfirmedBooking(booking, 0.0, 0.0); // Price calculation not persisted
                        restoredCount++;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[Persistence] Skipped malformed line " + lineNumber + ": " + line);
                }
            }

            System.out.println("[Persistence] Booking history restored (" + restoredCount + " bookings)");
            return history;

        } catch (IOException e) {
            System.out.println("[Persistence] Failed to restore booking history: " + e.getMessage());
            System.out.println("[Persistence] Starting with empty booking history");
            return history;
        }
    }

    /**
     * Restore inventory from persistent storage.
     * Returns default inventory if file doesn't exist or is corrupted.
     *
     * @return Restored RoomInventory or default RoomInventory if restoration fails
     */
    public RoomInventory restoreInventory() {
        RoomInventory inventory = new RoomInventory();

        File file = new File(INVENTORY_FILE);
        if (!file.exists()) {
            System.out.println("[Persistence] No inventory file found. Using default inventory.");
            return inventory;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            int lineNumber = 0;
            int restoredCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip comments and empty lines
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 2) {
                        String roomType = parts[0].trim();
                        int availableCount = Integer.parseInt(parts[1].trim());
                        inventory.restoreRoomAvailability(roomType, availableCount);
                        restoredCount++;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[Persistence] Skipped malformed line " + lineNumber + ": " + line);
                }
            }

            System.out.println("[Persistence] Inventory restored (" + restoredCount + " room types)");
            return inventory;

        } catch (IOException e) {
            System.out.println("[Persistence] Failed to restore inventory: " + e.getMessage());
            System.out.println("[Persistence] Using default inventory");
            return inventory;
        }
    }

    /**
     * Save complete system state (both bookings and inventory).
     *
     * @param bookingHistory The booking history to persist
     * @param roomInventory The room inventory to persist
     * @return true if both saves were successful
     */
    public boolean saveSystemState(BookingHistory bookingHistory, RoomInventory roomInventory) {
        System.out.println("\n[Persistence] Saving system state...");
        boolean historySuccess = saveBookingHistory(bookingHistory);
        boolean inventorySuccess = saveInventory(roomInventory);
        return historySuccess && inventorySuccess;
    }

    /**
     * Restore complete system state (both bookings and inventory).
     *
     * @return Object array containing [BookingHistory, RoomInventory]
     */
    public Object[] restoreSystemState() {
        System.out.println("\n[Persistence] Restoring system state...");
        BookingHistory bookingHistory = restoreBookingHistory();
        RoomInventory roomInventory = restoreInventory();
        return new Object[]{bookingHistory, roomInventory};
    }

    /**
     * Delete persistence files (for clean slate testing).
     */
    public void clearPersistenceData() {
        try {
            File bookingFile = new File(BOOKING_HISTORY_FILE);
            if (bookingFile.exists() && bookingFile.delete()) {
                System.out.println("[Persistence] Cleared booking history file");
            }

            File inventoryFile = new File(INVENTORY_FILE);
            if (inventoryFile.exists() && inventoryFile.delete()) {
                System.out.println("[Persistence] Cleared inventory file");
            }
        } catch (Exception e) {
            System.out.println("[Persistence] Failed to clear persistence data: " + e.getMessage());
        }
    }

    /**
     * Display persistence file status.
     */
    public void displayPersistenceStatus() {
        System.out.println("\n[Persistence] File Status:");
        File bookingFile = new File(BOOKING_HISTORY_FILE);
        File inventoryFile = new File(INVENTORY_FILE);

        System.out.println("  Booking history: " + (bookingFile.exists() ? "exists" : "missing"));
        System.out.println("  Inventory data:  " + (inventoryFile.exists() ? "exists" : "missing"));
    }
}
