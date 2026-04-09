/**
 * Reservation Class - Represents a Guest's Booking Request
 * 
 * This class encapsulates a guest's intent to book a room. Each reservation
 * captures the essential information needed to process a booking request,
 * including guest details, room preferences, and temporal information.
 * 
 * A Reservation is treated as an immutable request object that will be
 * placed into a Queue for ordered processing. The queue ensures that
 * reservations are handled in the order they were received (FIFO).
 * 
 * @author Development Team
 * @version 5.0
 * @since 2026
 */
public class Reservation {
    
    // Unique booking request ID for tracking
    private static int requestIdCounter = 1000;
    private int requestId;
    
    // Guest information
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    
    // Booking details
    private String roomTypeRequested;
    private int numberOfNights;
    
    // Temporal information
    private long requestTimestamp;
    private int numberOfGuests;
    
    // Request status
    private String status;
    
    /**
     * Constructor for Reservation class.
     * 
     * Creates a new reservation request with guest information and booking
     * preferences. The request timestamp is automatically captured at creation
     * time, reflecting the order in which requests arrive.
     * 
     * @param guestName Name of the guest
     * @param guestEmail Email of the guest
     * @param guestPhone Phone number of the guest
     * @param roomTypeRequested Type of room requested (e.g., "Single Room")
     * @param numberOfNights Number of nights for the stay
     * @param numberOfGuests Number of guests in the party
     */
    public Reservation(String guestName, String guestEmail, String guestPhone,
                      String roomTypeRequested, int numberOfNights, int numberOfGuests) {
        this.requestId = requestIdCounter++;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.guestPhone = guestPhone;
        this.roomTypeRequested = roomTypeRequested;
        this.numberOfNights = numberOfNights;
        this.numberOfGuests = numberOfGuests;
        this.requestTimestamp = System.currentTimeMillis();
        this.status = "PENDING";
    }
    
    // Getters
    public int getRequestId() {
        return requestId;
    }
    
    public String getGuestName() {
        return guestName;
    }
    
    public String getGuestEmail() {
        return guestEmail;
    }
    
    public String getGuestPhone() {
        return guestPhone;
    }
    
    public String getRoomTypeRequested() {
        return roomTypeRequested;
    }
    
    public int getNumberOfNights() {
        return numberOfNights;
    }
    
    public long getRequestTimestamp() {
        return requestTimestamp;
    }
    
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    
    public String getStatus() {
        return status;
    }
    
    /**
     * Update the status of this reservation.
     * 
     * @param newStatus New status (PENDING, CONFIRMED, DECLINED, etc.)
     */
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
    
    /**
     * Display reservation details in formatted output.
     */
    public void displayDetails() {
        System.out.println("Request ID: #" + requestId);
        System.out.println("Guest: " + guestName);
        System.out.println("Email: " + guestEmail);
        System.out.println("Phone: " + guestPhone);
        System.out.println("Room Type: " + roomTypeRequested);
        System.out.println("Number of Guests: " + numberOfGuests);
        System.out.println("Duration: " + numberOfNights + " night(s)");
        System.out.println("Status: " + status);
    }
    
    /**
     * Get a summary string of the reservation.
     * 
     * @return Formatted summary of reservation request
     */
    public String getSummary() {
        return "#" + requestId + " - " + guestName + " (" + roomTypeRequested + 
               " for " + numberOfNights + " nights) [" + status + "]";
    }
}
