import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentBookingProcessor Class - Thread-Safe Booking Request Processing
 *
 * This class demonstrates thread-safe processing of booking requests in a
 * multi-threaded environment. It uses synchronization to protect shared
 * resources and prevent race conditions that could lead to double-booking
 * or inconsistent inventory states.
 *
 * Key Design Principles:
 * - synchronized methods for critical sections
 * - Thread-safe queue for concurrent request submission
 * - Atomic inventory operations to prevent race conditions
 * - Proper synchronization of shared mutable state
 * - Prevention of interleaving operations on shared data
 *
 * The processor simulates a realistic multi-user booking system where
 * multiple guests can submit requests simultaneously, and the system
 * must maintain correctness under concurrent access patterns.
 *
 * Synchronization Strategy:
 * - Booking queue: ConcurrentLinkedQueue for thread-safe additions
 * - Inventory operations: synchronized methods for exclusive access
 * - Request processing: synchronized blocks for atomic operations
 * - State consistency: All shared state changes are synchronized
 *
 * @author Development Team
 * @version 11.0
 * @since 2026
 */
public class ConcurrentBookingProcessor {

    // Thread-safe queue for concurrent booking request submission
    // ConcurrentLinkedQueue allows multiple threads to add requests safely
    private final ConcurrentLinkedQueue<Reservation> bookingQueue;

    // Shared resources requiring synchronization
    private final RoomInventory roomInventory;
    private final BookingHistory bookingHistory;

    // Processing statistics (also need synchronization)
    private int processedRequests;
    private int successfulBookings;
    private int failedBookings;

    // Synchronization lock for statistics updates
    private final Object statsLock = new Object();

    /**
     * Constructor for ConcurrentBookingProcessor.
     *
     * @param roomInventory The shared room inventory
     * @param bookingHistory The shared booking history
     */
    public ConcurrentBookingProcessor(RoomInventory roomInventory, BookingHistory bookingHistory) {
        this.bookingQueue = new ConcurrentLinkedQueue<>();
        this.roomInventory = roomInventory;
        this.bookingHistory = bookingHistory;
        this.processedRequests = 0;
        this.successfulBookings = 0;
        this.failedBookings = 0;
    }

    /**
     * Submit a booking request to the queue (thread-safe).
     *
     * This method can be called concurrently by multiple threads without
     * synchronization issues. The ConcurrentLinkedQueue handles concurrent
     * additions safely.
     *
     * @param reservation The booking request to submit
     * @return true if the request was added successfully
     */
    public boolean submitBookingRequest(Reservation reservation) {
        boolean added = bookingQueue.offer(reservation);
        if (added) {
            System.out.println("Thread " + Thread.currentThread().getName() +
                             " submitted: " + reservation.getSummary());
        }
        return added;
    }

    /**
     * Process the next booking request from the queue (thread-safe).
     *
     * This method is synchronized to ensure only one thread can process
     * a request at a time, preventing race conditions in inventory updates.
     * The entire booking operation (check availability → allocate room →
     * update history) is performed atomically.
     *
     * @return ProcessingResult indicating success/failure with details
     */
    public synchronized ProcessingResult processNextRequest() {
        // Retrieve next request from queue
        Reservation request = bookingQueue.poll();

        if (request == null) {
            return ProcessingResult.noRequestAvailable();
        }

        // Update processing statistics
        synchronized (statsLock) {
            processedRequests++;
        }

        System.out.println("Thread " + Thread.currentThread().getName() +
                         " processing: " + request.getSummary());

        try {
            // Step 1: Validate the request (fail-fast)
            BookingValidationService validator = new BookingValidationService();
            validator.validateReservation(request, roomInventory);

            // Step 2: Attempt to book the room (critical section)
            boolean roomBooked = roomInventory.bookRoom(request.getRoomTypeRequested());

            if (roomBooked) {
                // Step 3: Confirm the booking and update history
                request.setStatus("CONFIRMED");

                // Calculate revenue (simplified)
                double roomPrice = getRoomPrice(request.getRoomTypeRequested());
                double totalRevenue = roomPrice * request.getNumberOfNights();

                bookingHistory.addConfirmedBooking(request, roomPrice, 0.0);

                // Update success statistics
                synchronized (statsLock) {
                    successfulBookings++;
                }

                System.out.println("Thread " + Thread.currentThread().getName() +
                                 " SUCCESS: " + request.getSummary() + " (Revenue: $" + totalRevenue + ")");

                return ProcessingResult.success(request, "Booking confirmed successfully");

            } else {
                // Room not available
                request.setStatus("DECLINED");

                synchronized (statsLock) {
                    failedBookings++;
                }

                System.out.println("Thread " + Thread.currentThread().getName() +
                                 " FAILED: " + request.getSummary() + " (Room not available)");

                return ProcessingResult.failure(request, "Room not available");
            }

        } catch (BookingValidationException e) {
            // Validation failed
            request.setStatus("DECLINED");

            synchronized (statsLock) {
                failedBookings++;
            }

            System.out.println("Thread " + Thread.currentThread().getName() +
                             " VALIDATION FAILED: " + request.getSummary() + " (" + e.getMessage() + ")");

            return ProcessingResult.validationFailure(request, e.getMessage());
        }
    }

    /**
     * Get the number of pending requests in the queue.
     *
     * @return Number of requests waiting to be processed
     */
    public int getPendingRequestCount() {
        return bookingQueue.size();
    }

    /**
     * Check if there are any pending requests.
     *
     * @return true if requests are available for processing
     */
    public boolean hasPendingRequests() {
        return !bookingQueue.isEmpty();
    }

    /**
     * Get processing statistics (thread-safe access).
     *
     * @return Array with [processed, successful, failed] counts
     */
    public int[] getProcessingStatistics() {
        synchronized (statsLock) {
            return new int[]{processedRequests, successfulBookings, failedBookings};
        }
    }

    /**
     * Clear all pending requests (use with caution).
     */
    public void clearPendingRequests() {
        bookingQueue.clear();
    }

    /**
     * Get a snapshot of pending requests for monitoring.
     *
     * @return Array of pending reservations (defensive copy)
     */
    public Reservation[] getPendingRequestsSnapshot() {
        return bookingQueue.toArray(new Reservation[0]);
    }

    /**
     * Simulate processing delay for demonstration purposes.
     * In a real system, this would represent actual processing time.
     */
    private void simulateProcessingDelay() {
        try {
            Thread.sleep(50 + (int)(Math.random() * 100)); // 50-150ms random delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Helper method to get room prices.
     */
    private double getRoomPrice(String roomType) {
        switch (roomType) {
            case "Single Room": return 75.00;
            case "Double Room": return 125.00;
            case "Suite Room": return 250.00;
            default: return 0.00;
        }
    }
}
