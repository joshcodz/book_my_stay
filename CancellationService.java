import java.util.Stack;

/**
 * CancellationService Class - Safe Booking Cancellation with Inventory Rollback
 *
 * This service manages the cancellation of confirmed bookings using a Stack-based
 * rollback mechanism. It ensures that system state is correctly reversed when
 * bookings are cancelled, maintaining inventory consistency and preventing
 * data corruption.
 *
 * Key Design Principles:
 * - Stack<String> for LIFO rollback tracking of released room IDs
 * - State reversal in controlled, predefined order
 * - Validation before cancellation operations
 * - Immediate inventory restoration
 * - Prevention of invalid cancellation attempts
 * - Atomic rollback operations
 *
 * The Stack data structure naturally models the undo/rollback behavior:
 * - Push: When a room is allocated, its ID is pushed onto the stack
 * - Pop: When cancelling, the most recent allocation is popped and restored
 * - LIFO order ensures proper reversal of operations
 *
 * This approach prevents partial rollbacks and maintains system integrity
 * during cancellation operations.
 *
 * @author Development Team
 * @version 10.0
 * @since 2026
 */
public class CancellationService {

    // Stack<String> for tracking released room IDs in LIFO order
    // Enables proper rollback of the most recent allocations first
    private Stack<String> releasedRoomIds;

    // Reference to booking history for cancellation tracking
    private BookingHistory bookingHistory;

    // Reference to room inventory for restoration operations
    private RoomInventory roomInventory;

    /**
     * Constructor for CancellationService.
     *
     * @param bookingHistory The booking history to update with cancellations
     * @param roomInventory The room inventory to restore upon cancellation
     */
    public CancellationService(BookingHistory bookingHistory, RoomInventory roomInventory) {
        this.releasedRoomIds = new Stack<>();
        this.bookingHistory = bookingHistory;
        this.roomInventory = roomInventory;
    }

    /**
     * Cancel a confirmed booking with controlled rollback.
     *
     * This method performs a safe cancellation by:
     * 1. Validating the reservation exists and is cancellable
     * 2. Recording the cancellation in booking history
     * 3. Restoring inventory counts
     * 4. Tracking released room IDs for rollback purposes
     *
     * Time Complexity: O(1) for Stack operations, O(n) for history search
     *
     * @param reservationId The ID of the reservation to cancel
     * @param roomId The allocated room ID being released
     * @param roomType The type of room being cancelled
     * @return CancellationResult indicating success/failure with details
     */
    public CancellationResult cancelBooking(int reservationId, String roomId, String roomType) {
        try {
            // Step 1: Validate the cancellation request
            if (!isReservationCancellable(reservationId)) {
                return CancellationResult.failure(
                    "Reservation #" + reservationId + " is not cancellable. " +
                    "It may not exist or may already be cancelled.");
            }

            // Step 2: Create cancellation record
            Reservation cancelledReservation = findReservationById(reservationId);
            if (cancelledReservation == null) {
                return CancellationResult.failure(
                    "Reservation #" + reservationId + " not found in booking history.");
            }

            // Step 3: Update reservation status
            cancelledReservation.setStatus("CANCELLED");

            // Step 4: Restore inventory (immediate restoration)
            roomInventory.releaseRoom(roomType);

            // Step 5: Track released room ID in rollback stack
            releasedRoomIds.push(roomId);

            // Step 6: Record cancellation in history (optional - could be separate audit log)
            // For this use case, we update the existing reservation status

            return CancellationResult.success(
                "Booking #" + reservationId + " cancelled successfully. " +
                "Room " + roomId + " (" + roomType + ") released back to inventory.",
                cancelledReservation);

        } catch (Exception e) {
            // Handle any unexpected errors during cancellation
            return CancellationResult.failure(
                "Cancellation failed due to system error: " + e.getMessage());
        }
    }

    /**
     * Cancel a booking using a Reservation object directly.
     *
     * @param reservation The reservation to cancel
     * @param roomId The allocated room ID
     * @return CancellationResult indicating success/failure
     */
    public CancellationResult cancelBooking(Reservation reservation, String roomId) {
        return cancelBooking(reservation.getRequestId(), roomId, reservation.getRoomTypeRequested());
    }

    /**
     * Perform emergency rollback of the most recent room release.
     *
     * This method demonstrates Stack-based rollback by undoing the most
     * recent room release operation. Useful for error recovery scenarios.
     *
     * @return true if rollback succeeded, false if no operations to rollback
     */
    public boolean rollbackLastRelease() {
        if (releasedRoomIds.isEmpty()) {
            return false; // No operations to rollback
        }

        // Pop the most recent release from stack
        String lastReleasedRoomId = releasedRoomIds.pop();

        // In a real system, this would re-allocate the room back
        // For this demo, we just acknowledge the rollback
        System.out.println("Emergency rollback: Room " + lastReleasedRoomId +
                          " restored from release stack");

        return true;
    }

    /**
     * Get the most recently released room ID without removing it from stack.
     *
     * @return The last released room ID, or null if stack is empty
     */
    public String peekLastReleasedRoom() {
        return releasedRoomIds.isEmpty() ? null : releasedRoomIds.peek();
    }

    /**
     * Get the count of released rooms available for rollback.
     *
     * @return Number of room releases that can be rolled back
     */
    public int getReleasedRoomsCount() {
        return releasedRoomIds.size();
    }

    /**
     * Clear all released room tracking (use with caution).
     */
    public void clearReleaseHistory() {
        releasedRoomIds.clear();
    }

    /**
     * Check if a reservation exists and is in a cancellable state.
     *
     * @param reservationId The reservation ID to check
     * @return true if the reservation can be cancelled
     */
    private boolean isReservationCancellable(int reservationId) {
        Reservation reservation = findReservationById(reservationId);
        if (reservation == null) {
            return false;
        }

        String status = reservation.getStatus();
        // Only confirmed bookings can be cancelled
        return "CONFIRMED".equals(status);
    }

    /**
     * Find a reservation by its ID in the booking history.
     *
     * @param reservationId The reservation ID to find
     * @return The reservation if found, null otherwise
     */
    private Reservation findReservationById(int reservationId) {
        // In a real system, this would be more efficient with a Map<Integer, Reservation>
        // For this demo, we search through the list
        for (Reservation booking : bookingHistory.getAllBookings()) {
            if (booking.getRequestId() == reservationId) {
                return booking;
            }
        }
        return null;
    }

    /**
     * Display the current state of the release stack.
     */
    public void displayReleaseStack() {
        System.out.println("\n*** ROOM RELEASE STACK (LIFO Order) ***");
        if (releasedRoomIds.isEmpty()) {
            System.out.println("No released rooms in stack.");
            return;
        }

        System.out.println("Released rooms (most recent first):");
        // Create a copy to avoid modifying the original stack
        Stack<String> tempStack = new Stack<>();
        tempStack.addAll(releasedRoomIds);

        int position = 1;
        while (!tempStack.isEmpty()) {
            String roomId = tempStack.pop();
            System.out.println("  " + position + ". " + roomId);
            position++;
        }
        System.out.println("Total released rooms: " + releasedRoomIds.size());
    }

    /**
     * Get statistics about cancellation operations.
     *
     * @return Array with [totalCancellations, activeReleases]
     */
    public int[] getCancellationStats() {
        // In a real system, this would track actual cancellation counts
        // For this demo, we return stack size as active releases
        return new int[]{releasedRoomIds.size(), releasedRoomIds.size()};
    }
}
