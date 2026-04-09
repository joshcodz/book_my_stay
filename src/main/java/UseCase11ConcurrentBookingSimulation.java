import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 * Application Entry Point - Multi-Threaded Booking Request Processing
 *
 * This class demonstrates thread safety in a concurrent booking environment.
 * It shows how race conditions can occur without proper synchronization and
 * how synchronized access ensures correct behavior under multi-user conditions.
 *
 * Key Design Principles Demonstrated:
 * - Race condition prevention through synchronization
 * - Thread-safe shared resource access
 * - Critical sections for atomic operations
 * - ConcurrentLinkedQueue for thread-safe request submission
 * - synchronized methods for exclusive inventory access
 * - Proper thread coordination and lifecycle management
 *
 * Problem Solved:
 * Previous use cases assumed single-threaded execution, which is unsafe
 * in real production environments with concurrent user access. This use
 * case demonstrates safe multi-user booking processing.
 *
 * Flow:
 * 1. Initialize thread-safe booking processor
 * 2. Create multiple guest threads submitting concurrent requests
 * 3. Launch worker threads to process requests simultaneously
 * 4. Demonstrate synchronized inventory operations
 * 5. Show race condition prevention and state consistency
 * 6. Analyze concurrent processing results and statistics
 * 7. Display thread safety benefits and system reliability
 *
 * Thread Safety Mechanisms:
 * - synchronized processNextRequest(): Exclusive request processing
 * - ConcurrentLinkedQueue: Thread-safe request submission
 * - synchronized statistics: Safe counter updates
 * - Atomic operations: Prevent interleaving of critical sections
 *
 * @author Development Team
 * @version 11.0
 * @since 2026
 */
public class UseCase11ConcurrentBookingSimulation {

    // Simulation parameters
    private static final int NUM_GUEST_THREADS = 8;
    private static final int NUM_WORKER_THREADS = 4;
    private static final int REQUESTS_PER_GUEST = 3;
    private static final int TOTAL_REQUESTS = NUM_GUEST_THREADS * REQUESTS_PER_GUEST;

    /**
     * Main method - Entry point of the Hotel Booking Management System (Use Case 11).
     *
     * This method demonstrates:
     * 1. Concurrent booking request submission by multiple threads
     * 2. Thread-safe request processing with synchronization
     * 3. Race condition prevention in inventory operations
     * 4. System state consistency under concurrent load
     * 5. Performance analysis of concurrent processing
     * 6. Thread safety benefits and reliability guarantees
     *
     * @param args Command-line arguments (not used in this use case)
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("========================================");
        System.out.println("  Hotel Booking Management System");
        System.out.println("  Version: 11.0 - Concurrent Booking Simulation");
        System.out.println("========================================");

        // [STEP 1] Initialize thread-safe booking system
        System.out.println("\n[STEP 1] Initializing Thread-Safe Booking System");
        System.out.println("=========================================");

        RoomInventory roomInventory = new RoomInventory();
        BookingHistory bookingHistory = new BookingHistory();
        ConcurrentBookingProcessor bookingProcessor = new ConcurrentBookingProcessor(roomInventory, bookingHistory);

        System.out.println("[OK] RoomInventory initialized with baseline data");
        System.out.println("[OK] BookingHistory initialized for tracking");
        System.out.println("[OK] ConcurrentBookingProcessor initialized");
        System.out.println("Thread Safety: synchronized methods for critical sections");
        System.out.println("Data Structure: ConcurrentLinkedQueue for request submission");

        System.out.println("\nInitial inventory state:");
        roomInventory.displayInventory();

        // [STEP 2] Create concurrent booking requests
        System.out.println("\n[STEP 2] Creating Concurrent Booking Requests");
        System.out.println("=========================================");

        List<Reservation> allRequests = createConcurrentBookingRequests();
        System.out.println("Created " + allRequests.size() + " booking requests from " +
                         NUM_GUEST_THREADS + " concurrent guests");

        // [STEP 3] Demonstrate concurrent request submission
        System.out.println("\n[STEP 3] Concurrent Request Submission");
        System.out.println("=========================================");

        // Use CountDownLatch to coordinate thread execution
        CountDownLatch submissionLatch = new CountDownLatch(NUM_GUEST_THREADS);
        AtomicInteger submittedCount = new AtomicInteger(0);

        // Create guest threads that submit requests concurrently
        List<Thread> guestThreads = new ArrayList<>();
        for (int i = 0; i < NUM_GUEST_THREADS; i++) {
            final int guestId = i;
            Thread guestThread = new Thread(() -> {
                String threadName = "Guest-" + guestId;
                Thread.currentThread().setName(threadName);

                // Submit requests for this guest
                for (int j = 0; j < REQUESTS_PER_GUEST; j++) {
                    int requestIndex = guestId * REQUESTS_PER_GUEST + j;
                    Reservation request = allRequests.get(requestIndex);

                    // Submit request (thread-safe operation)
                    bookingProcessor.submitBookingRequest(request);
                    submittedCount.incrementAndGet();

                    // Small delay to simulate realistic submission timing
                    try {
                        Thread.sleep(10 + (int)(Math.random() * 20));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                submissionLatch.countDown();
                System.out.println(threadName + " completed request submission");
            }, "Guest-" + i);

            guestThreads.add(guestThread);
        }

        // Start all guest threads simultaneously
        System.out.println("Starting " + NUM_GUEST_THREADS + " guest threads for concurrent submission...");
        long startTime = System.currentTimeMillis();
        guestThreads.forEach(Thread::start);

        // Wait for all submissions to complete
        try {
            submissionLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long submissionTime = System.currentTimeMillis() - startTime;
        System.out.println("All submissions completed in " + submissionTime + "ms");
        System.out.println("Total requests submitted: " + submittedCount.get());
        System.out.println("Pending requests in queue: " + bookingProcessor.getPendingRequestCount());

        // [STEP 4] Concurrent request processing
        System.out.println("\n[STEP 4] Concurrent Request Processing");
        System.out.println("=========================================");

        // Create worker threads that process requests concurrently
        CountDownLatch processingLatch = new CountDownLatch(NUM_WORKER_THREADS);
        ExecutorService workerExecutor = Executors.newFixedThreadPool(NUM_WORKER_THREADS);

        System.out.println("Starting " + NUM_WORKER_THREADS + " worker threads for concurrent processing...");

        startTime = System.currentTimeMillis();

        for (int i = 0; i < NUM_WORKER_THREADS; i++) {
            final int workerId = i;
            workerExecutor.submit(() -> {
                String threadName = "Worker-" + workerId;
                Thread.currentThread().setName(threadName);

                // Process requests until queue is empty
                while (bookingProcessor.hasPendingRequests()) {
                    ProcessingResult result = bookingProcessor.processNextRequest();

                    // Only display results for actual processing (not "no request available")
                    if (!result.isNoRequestAvailable()) {
                        result.display();
                    }

                    // Small delay to allow other threads to work
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                processingLatch.countDown();
                System.out.println(threadName + " completed processing");
            });
        }

        // Shutdown executor and wait for completion
        workerExecutor.shutdown();
        try {
            processingLatch.await();
            workerExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long processingTime = System.currentTimeMillis() - startTime;
        System.out.println("All processing completed in " + processingTime + "ms");

        // [STEP 5] Analyze concurrent processing results
        System.out.println("\n[STEP 5] Concurrent Processing Analysis");
        System.out.println("=========================================");

        int[] stats = bookingProcessor.getProcessingStatistics();
        int processed = stats[0];
        int successful = stats[1];
        int failed = stats[2];

        System.out.println("Processing Statistics:");
        System.out.println("  Total Requests: " + TOTAL_REQUESTS);
        System.out.println("  Processed: " + processed);
        System.out.println("  Successful: " + successful);
        System.out.println("  Failed: " + failed);
        System.out.println("  Success Rate: " + String.format("%.1f", (successful * 100.0) / processed) + "%");

        System.out.println("\nFinal inventory state:");
        roomInventory.displayInventory();

        System.out.println("\nBooking history summary:");
        System.out.println("  Total confirmed bookings: " + bookingHistory.getTotalBookings());
        System.out.println("  Total revenue: $" + String.format("%.2f", bookingHistory.getTotalRevenue()));

        // [STEP 6] Demonstrate thread safety guarantees
        System.out.println("\n[STEP 6] Thread Safety Verification");
        System.out.println("=========================================");

        // Verify inventory consistency
        int totalAllocated = bookingHistory.getTotalBookings();
        int totalOriginal = 5 + 3 + 2; // Single + Double + Suite rooms
        int totalAvailable = roomInventory.getTotalAvailableRooms();
        int totalCurrent = totalAllocated + totalAvailable;

        System.out.println("Inventory Consistency Check:");
        System.out.println("  Original rooms: " + totalOriginal);
        System.out.println("  Allocated rooms: " + totalAllocated);
        System.out.println("  Available rooms: " + totalAvailable);
        System.out.println("  Total accounted for: " + totalCurrent);
        System.out.println("  Consistency: " + (totalCurrent == totalOriginal ? "✓ VERIFIED" : "✗ BROKEN"));

        // Verify no double bookings occurred
        boolean hasDoubleBooking = checkForDoubleBookings(bookingHistory);
        System.out.println("  Double booking check: " + (hasDoubleBooking ? "✗ DETECTED" : "✓ NONE FOUND"));

        // [STEP 7] Key insights summary
        System.out.println("\n[KEY INSIGHTS: Concurrent Booking Simulation]");
        System.out.println("=========================================");
        System.out.println("Why Thread Safety Prevents Race Conditions:");
        System.out.println();
        System.out.println("1. Race Condition Prevention");
        System.out.println("   - synchronized processNextRequest() ensures exclusive access");
        System.out.println("   - Multiple threads cannot interleave inventory operations");
        System.out.println("   - Final state depends on logic, not execution timing");
        System.out.println();
        System.out.println("2. Shared Mutable State Protection");
        System.out.println("   - RoomInventory operations are synchronized");
        System.out.println("   - BookingHistory updates are atomic");
        System.out.println("   - Statistics counters use synchronized access");
        System.out.println();
        System.out.println("3. Critical Section Management");
        System.out.println("   - Check availability → allocate room → update history");
        System.out.println("   - Entire booking operation performed atomically");
        System.out.println("   - No partial updates or inconsistent intermediate states");
        System.out.println();
        System.out.println("4. Concurrent Data Structures");
        System.out.println("   - ConcurrentLinkedQueue for thread-safe request submission");
        System.out.println("   - Multiple threads can add requests without blocking");
        System.out.println("   - FIFO ordering maintained under concurrent access");
        System.out.println();
        System.out.println("5. Thread Coordination");
        System.out.println("   - CountDownLatch for synchronized thread startup");
        System.out.println("   - ExecutorService for managed thread lifecycle");
        System.out.println("   - Proper thread cleanup and resource management");
        System.out.println();
        System.out.println("6. State Consistency Guarantees");
        System.out.println("   - Inventory counts always accurate");
        System.out.println("   - No double bookings or over-allocations");
        System.out.println("   - Booking history reflects actual confirmed bookings");
        System.out.println();
        System.out.println("7. Performance vs. Correctness");
        System.out.println("   - Synchronization prioritizes correctness over raw speed");
        System.out.println("   - Controlled concurrency prevents race conditions");
        System.out.println("   - Scalable design for multi-user production systems");
        System.out.println();
        System.out.println("8. Real-World Applicability");
        System.out.println("   - Simulates actual hotel booking website behavior");
        System.out.println("   - Multiple users booking simultaneously");
        System.out.println("   - Thread-safe backend processing ensures fairness");

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================\n");
    }

    /**
     * Create a list of booking requests for concurrent simulation.
     */
    private static List<Reservation> createConcurrentBookingRequests() {
        List<Reservation> requests = new ArrayList<>();

        String[] roomTypes = {"Single Room", "Double Room", "Suite Room"};
        String[] guestNames = {
            "Alice Johnson", "Bob Smith", "Carol Davis", "Dave Wilson",
            "Eve Martinez", "Frank Garcia", "Grace Lee", "Henry Taylor"
        };

        for (int i = 0; i < NUM_GUEST_THREADS; i++) {
            for (int j = 0; j < REQUESTS_PER_GUEST; j++) {
                String guestName = guestNames[i];
                String email = guestName.toLowerCase().replace(" ", ".") + "@email.com";
                String phone = "555-" + String.format("%04d", i * 100 + j);
                String roomType = roomTypes[j % roomTypes.length];
                int nights = 1 + (int)(Math.random() * 5); // 1-5 nights
                int guests = getGuestCountForRoomType(roomType);

                Reservation request = new Reservation(guestName, email, phone, roomType, nights, guests);
                requests.add(request);
            }
        }

        return requests;
    }

    /**
     * Get appropriate guest count for room type.
     */
    private static int getGuestCountForRoomType(String roomType) {
        switch (roomType) {
            case "Single Room": return 1;
            case "Double Room": return 1 + (int)(Math.random() * 2); // 1-2
            case "Suite Room": return 1 + (int)(Math.random() * 4); // 1-4
            default: return 1;
        }
    }

    /**
     * Check for double bookings in history (simplified check).
     */
    private static boolean checkForDoubleBookings(BookingHistory history) {
        // In a real system, this would check for overlapping room assignments
        // For this demo, we just verify no impossible allocations occurred
        int totalBookings = history.getTotalBookings();
        int totalOriginalRooms = 5 + 3 + 2; // Single + Double + Suite

        // If we have more bookings than rooms, something went wrong
        return totalBookings > totalOriginalRooms;
    }
}
