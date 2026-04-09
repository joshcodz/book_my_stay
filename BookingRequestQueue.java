import java.util.Queue;
import java.util.LinkedList;
import java.util.List;

/**
 * BookingRequestQueue Class - FIFO Queue for Booking Requests
 * 
 * This class manages a queue of booking requests from guests, implementing
 * the Queue data structure to ensure first-come-first-served (FIFO) fairness.
 * 
 * Key Design Principles:
 * - FIFO Ordering: Requests are processed in the order they arrive
 * - Request Buffering: Decouples request intake from allocation processing
 * - Fair Allocation: No request can bypass another in the queue
 * - State Preservation: Queue order is maintained automatically
 * 
 * The queue serves as an intake mechanism during peak demand, ensuring that
 * booking requests are handled fairly and predictably without race conditions.
 * The actual room allocation occurs in a subsequent processing stage.
 * 
 * Implementation Detail:
 * - Uses LinkedList as the underlying implementation of Queue
 * - LinkedList provides O(1) enqueue and dequeue operations
 * - Order is preserved automatically by the Queue interface
 * 
 * @author Development Team
 * @version 5.0
 * @since 2026
 */
public class BookingRequestQueue {
    
    // Queue container using LinkedList implementation
    // Queue<E> interface provides FIFO behavior
    // LinkedList efficiently supports add() and remove() at both ends
    private Queue<Reservation> requestQueue;
    
    // Statistics tracking
    private int totalRequestsReceived;
    private int totalRequestsProcessed;
    
    /**
     * Constructor for BookingRequestQueue.
     * Initializes an empty queue for booking requests.
     */
    public BookingRequestQueue() {
        // LinkedList is the implementation of Queue interface
        // This provides efficient FIFO behavior
        this.requestQueue = new LinkedList<>();
        this.totalRequestsReceived = 0;
        this.totalRequestsProcessed = 0;
    }
    
    /**
     * Add a booking request to the queue.
     * 
     * This method represents a guest submitting a booking request.
     * The request is added to the end of the queue (enqueue operation).
     * Request order is preserved automatically by the Queue.
     * 
     * Time Complexity: O(1) - constant time insertion
     * 
     * @param reservation The booking request to add
     * @return true if the request was successfully added
     */
    public boolean addRequest(Reservation reservation) {
        boolean added = requestQueue.add(reservation);
        if (added) {
            totalRequestsReceived++;
        }
        return added;
    }
    
    /**
     * Process the next booking request from the queue.
     * 
     * This method removes and returns the oldest request from the queue
     * (dequeue operation). The FIFO principle ensures fair processing.
     * 
     * Time Complexity: O(1) - constant time removal
     * 
     * @return The oldest reservation in the queue, or null if queue is empty
     */
    public Reservation processNextRequest() {
        Reservation nextRequest = requestQueue.poll();
        if (nextRequest != null) {
            totalRequestsProcessed++;
        }
        return nextRequest;
    }
    
    /**
     * Peek at the next request without removing it.
     * 
     * This method allows inspection of the next request without
     * actually processing it. Useful for preview and status checking.
     * 
     * Time Complexity: O(1) - constant time access
     * 
     * @return The oldest reservation in the queue, or null if queue is empty
     */
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }
    
    /**
     * Get the current number of pending requests in the queue.
     * 
     * @return Number of requests waiting to be processed
     */
    public int getPendingRequestCount() {
        return requestQueue.size();
    }
    
    /**
     * Check if the queue has any pending requests.
     * 
     * @return true if there are pending requests, false if queue is empty
     */
    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
    
    /**
     * Get the total number of requests received since initialization.
     * 
     * @return Total requests received
     */
    public int getTotalRequestsReceived() {
        return totalRequestsReceived;
    }
    
    /**
     * Get the total number of requests processed so far.
     * 
     * @return Total requests processed
     */
    public int getTotalRequestsProcessed() {
        return totalRequestsProcessed;
    }
    
    /**
     * Get the number of requests still pending processing.
     * 
     * @return Total - Processed = Pending
     */
    public int getRequestsStillPending() {
        return totalRequestsReceived - totalRequestsProcessed;
    }
    
    /**
     * Display the current state of the booking request queue.
     * 
     * Shows pending requests in order (oldest first) along with statistics.
     */
    public void displayQueueStatus() {
        System.out.println("\n*** BOOKING REQUEST QUEUE STATUS ***");
        System.out.println("(FIFO - First-Come-First-Served)\n");
        
        System.out.println("Queue Statistics:");
        System.out.println("  Total Requests Received: " + totalRequestsReceived);
        System.out.println("  Total Requests Processed: " + totalRequestsProcessed);
        System.out.println("  Requests Pending: " + getPendingRequestCount());
        
        if (hasPendingRequests()) {
            System.out.println("\nPending Requests (in order of arrival):");
            
            // Create a list to display queue contents without modifying the queue
            List<Reservation> pendingList = new LinkedList<>(requestQueue);
            int position = 1;
            
            for (Reservation reservation : pendingList) {
                System.out.println("  [Position " + position + "] " + reservation.getSummary());
                position++;
            }
        } else {
            System.out.println("\nNo pending requests in queue.");
        }
    }
    
    /**
     * Display detailed information about all pending requests.
     */
    public void displayDetailedQueueContents() {
        System.out.println("\n*** DETAILED PENDING REQUESTS ***\n");
        
        if (hasPendingRequests()) {
            List<Reservation> pendingList = new LinkedList<>(requestQueue);
            int position = 1;
            
            for (Reservation reservation : pendingList) {
                System.out.println("---- Request Position " + position + " ----");
                reservation.displayDetails();
                System.out.println();
                position++;
            }
        } else {
            System.out.println("No pending requests.\n");
        }
    }
    
    /**
     * Clear all pending requests from the queue.
     * (Used mainly for testing/resetting system state)
     */
    public void clearQueue() {
        requestQueue.clear();
    }
}
