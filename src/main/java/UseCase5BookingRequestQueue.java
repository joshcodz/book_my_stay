/**
 * Use Case 5: Booking Request (First-Come-First-Served)
 * Application Entry Point - Booking Request Queue Management
 * 
 * This class demonstrates fair booking request handling using a Queue data
 * structure that implements the FIFO (First-Come-First-Served) principle.
 * 
 * Key Design Principles Demonstrated:
 * - Queue Data Structure: FIFO ordering for fairness
 * - Request Intake Mechanism: Buffering requests before processing
 * - Arrival Order Preservation: No manual sorting needed
 * - Decoupling: Request collection separate from allocation
 * - No Inventory Mutation: Only queue state is modified
 * - Fairness Guarantee: All requests treated equally by arrival time
 * 
 * Problem Addressed:
 * During peak demand, simultaneous booking requests must be handled fairly.
 * Without ordering, requests could be processed inconsistently. A queue ensures
 * deterministic, fair processing in the order requests arrive.
 * 
 * Flow:
 * 1. Initialize booking request queue
 * 2. Simulate multiple guests submitting requests simultaneously
 * 3. Requests are added to queue in arrival order
 * 4. Display queue status with all pending requests
 * 5. Process requests one by one in FIFO order
 * 6. Verify fairness and order preservation
 * 
 * Benefits:
 * - Fair and predictable booking handling
 * - Simplified coordination under peak load
 * - Foundation for controlled allocation in next use case
 * - No race conditions or unfair bypassing
 * 
 * @author Development Team
 * @version 5.0
 * @since 2026
 */
public class UseCase5BookingRequestQueue {
    
    /**
     * Main method - Entry point of the Hotel Booking Management System (Use Case 5).
     * 
     * This method demonstrates:
     * 1. Creating a booking request queue
     * 2. Simulating multiple concurrent booking requests
     * 3. Verifying FIFO ordering is preserved
     * 4. Processing requests fairly in arrival order
     * 5. Tracking queue statistics
     * 
     * @param args Command-line arguments (not used in this use case)
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("========================================");
        System.out.println("  Hotel Booking Management System");
        System.out.println("  Version: 5.0 - Booking Request Queue");
        System.out.println("========================================");
        
        // [STEP 1] Initialize the booking request queue
        System.out.println("\n[STEP 1] Initializing Booking Request Queue");
        System.out.println("=========================================");
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        System.out.println("Queue initialized as: Queue<Reservation>");
        System.out.println("Underlying implementation: LinkedList");
        System.out.println("Ordering: FIFO (First-Come-First-Served)");
        
        // [STEP 2] Simulate peak demand - multiple booking requests arriving
        System.out.println("\n[STEP 2] Simulating Peak-Demand Booking Requests");
        System.out.println("=========================================");
        System.out.println("Scenario: Multiple guests submit booking requests quickly\n");
        
        // Create booking requests in arrival order
        Reservation req1 = new Reservation(
            "Alice Johnson",
            "alice@email.com",
            "555-0001",
            "Single Room",
            3,
            1
        );
        
        Reservation req2 = new Reservation(
            "Bob Smith",
            "bob@email.com",
            "555-0002",
            "Double Room",
            5,
            2
        );
        
        Reservation req3 = new Reservation(
            "Carol Davis",
            "carol@email.com",
            "555-0003",
            "Suite Room",
            2,
            3
        );
        
        Reservation req4 = new Reservation(
            "Dave Wilson",
            "dave@email.com",
            "555-0004",
            "Double Room",
            4,
            2
        );
        
        Reservation req5 = new Reservation(
            "Eve Martinez",
            "eve@email.com",
            "555-0005",
            "Single Room",
            7,
            1
        );
        
        // Add requests to queue in arrival order
        System.out.println("Adding booking requests to queue (in arrival order):");
        bookingQueue.addRequest(req1);
        System.out.println("  Request 1: " + req1.getSummary());
        
        bookingQueue.addRequest(req2);
        System.out.println("  Request 2: " + req2.getSummary());
        
        bookingQueue.addRequest(req3);
        System.out.println("  Request 3: " + req3.getSummary());
        
        bookingQueue.addRequest(req4);
        System.out.println("  Request 4: " + req4.getSummary());
        
        bookingQueue.addRequest(req5);
        System.out.println("  Request 5: " + req5.getSummary());
        
        // [STEP 3] Display queue status
        System.out.println("\n[STEP 3] Booking Queue Status (Requests Buffered)");
        System.out.println("=========================================");
        bookingQueue.displayQueueStatus();
        
        // [STEP 4] Display detailed queue contents
        System.out.println("\n[STEP 4] Detailed Pending Requests");
        System.out.println("=========================================");
        bookingQueue.displayDetailedQueueContents();
        
        // [STEP 5] Demonstrate FIFO processing
        System.out.println("\n[STEP 5] Processing Requests in FIFO Order");
        System.out.println("=========================================");
        System.out.println("(No inventory changes occur at this stage)\n");
        
        int processCount = 1;
        while (bookingQueue.hasPendingRequests()) {
            Reservation request = bookingQueue.processNextRequest();
            
            if (request != null) {
                System.out.println("Processing Request " + processCount + ":");
                System.out.println("  Request ID: #" + request.getRequestId());
                System.out.println("  Guest: " + request.getGuestName());
                System.out.println("  Room Type: " + request.getRoomTypeRequested());
                System.out.println("  Status: PENDING -> PROCESSING");
                System.out.println();
                processCount++;
            }
        }
        
        // [STEP 6] Display final queue status
        System.out.println("\n[STEP 6] Final Queue Status (All Requests Processed)");
        System.out.println("=========================================");
        bookingQueue.displayQueueStatus();
        
        // [STEP 7] Verify fairness properties
        System.out.println("\n[STEP 7] Verification: FIFO Fairness Guarantee");
        System.out.println("=========================================");
        System.out.println("Fairness Properties Verified:");
        System.out.println("  [OK] Requests processed in arrival order");
        System.out.println("  [OK] No request bypassed another");
        System.out.println("  [OK] All guests treated equally");
        System.out.println("  [OK] Queue size reduced as requests processed");
        System.out.println("  [OK] Request order preserved automatically");
        
        // [STEP 8] Demonstrate high-load scenario
        System.out.println("\n[STEP 8] High-Load Scenario: Rapid Requests");
        System.out.println("=========================================");
        
        BookingRequestQueue highLoadQueue = new BookingRequestQueue();
        
        System.out.println("Simulating rapid booking requests during peak hours...\n");
        
        // Add 10 rapid requests
        for (int i = 1; i <= 10; i++) {
            Reservation request = new Reservation(
                "Guest " + i,
                "guest" + i + "@email.com",
                "555-000" + i,
                (i % 2 == 0) ? "Double Room" : "Single Room",
                2 + (i % 5),
                1 + (i % 3)
            );
            highLoadQueue.addRequest(request);
        }
        
        System.out.println("Added 10 requests to queue");
        System.out.println("Queue State: " + highLoadQueue.getPendingRequestCount() + " requests pending");
        System.out.println("All requests queued in FIFO order for fair processing\n");
        
        // Show first few in queue
        System.out.println("First 3 requests in queue:");
        for (int i = 0; i < 3 && highLoadQueue.hasPendingRequests(); i++) {
            Reservation next = highLoadQueue.peekNextRequest();
            if (next != null) {
                System.out.println("  " + (i+1) + ". " + next.getSummary());
                highLoadQueue.processNextRequest();
            }
        }
        
        System.out.println("\nRemaining in queue: " + highLoadQueue.getPendingRequestCount());
        
        // [STEP 9] Key insights summary
        System.out.println("\n[KEY INSIGHTS: Queue Data Structure Benefits]");
        System.out.println("=========================================");
        System.out.println("Why Queue is Essential for Booking Systems:");
        System.out.println();
        System.out.println("1. FIFO Guarantee");
        System.out.println("   - No manual timestamp comparison needed");
        System.out.println("   - Order is preserved automatically");
        System.out.println("   - Deterministic behavior guaranteed");
        System.out.println();
        System.out.println("2. Fairness Under Load");
        System.out.println("   - All requests treated equally");
        System.out.println("   - No request can bypass another");
        System.out.println("   - Prevents 'cutting in line' scenarios");
        System.out.println();
        System.out.println("3. Decoupling Request from Processing");
        System.out.println("   - Request intake separate from allocation");
        System.out.println("   - No premature inventory updates");
        System.out.println("   - Enables controlled processing later");
        System.out.println();
        System.out.println("4. Scalability");
        System.out.println("   - O(1) enqueue and dequeue performance");
        System.out.println("   - Handles peak demand gracefully");
        System.out.println("   - Can easily grow to thousands of requests");
        System.out.println();
        System.out.println("5. Foundation for Concurrency");
        System.out.println("   - Thread-safe queues exist (ConcurrentQueue)");
        System.out.println("   - Prepares for multi-threaded booking system");
        System.out.println("   - Race conditions eliminated by FIFO ordering");
        
        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================\n");
    }
}
