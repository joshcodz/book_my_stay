import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BookingHistory Class - Historical Tracking of Confirmed Reservations
 * 
 * This class maintains a chronological record of all confirmed bookings
 * using a List data structure. It provides operational visibility into
 * past transactions and supports administrative review and reporting.
 * 
 * Key Design Principles:
 * - List<Reservation> for ordered storage of confirmed bookings
 * - Insertion order preservation for chronological tracking
 * - Read-only access for audit and reporting purposes
 * - Separation of storage from reporting logic
 * - Persistence mindset without external storage medium
 * 
 * The booking history serves as an audit trail, enabling administrators
 * to review past transactions, analyze system usage, and resolve customer
 * issues. While stored in memory, the design prepares for future persistence
 * to external storage systems.
 * 
 * @author Development Team
 * @version 8.0
 * @since 2026
 */
public class BookingHistory {
    
    // List<Reservation> - stores confirmed bookings in chronological order
    // ArrayList preserves insertion order, enabling chronological tracking
    private List<Reservation> confirmedBookings;
    
    // Statistics tracking for reporting
    private Map<String, Integer> roomTypeBookings;
    private double totalRevenue;
    private int totalBookings;
    
    /**
     * Constructor for BookingHistory.
     * Initializes the booking history with empty collections.
     */
    public BookingHistory() {
        this.confirmedBookings = new ArrayList<>();
        this.roomTypeBookings = new HashMap<>();
        this.totalRevenue = 0.0;
        this.totalBookings = 0;
    }
    
    /**
     * Add a confirmed reservation to the booking history.
     * 
     * This method stores a confirmed booking in chronological order.
     * The reservation is added to the end of the list, preserving
     * the sequence in which bookings were confirmed.
     * 
     * Time Complexity: O(1) - ArrayList.add() is amortized O(1)
     * 
     * @param reservation The confirmed reservation to store
     * @param roomPrice The price of the room (for revenue tracking)
     * @param addOnCost The cost of any add-on services
     */
    public void addConfirmedBooking(Reservation reservation, double roomPrice, double addOnCost) {
        // Add to chronological list
        confirmedBookings.add(reservation);
        
        // Update statistics
        String roomType = reservation.getRoomTypeRequested();
        roomTypeBookings.put(roomType, roomTypeBookings.getOrDefault(roomType, 0) + 1);
        
        totalRevenue += roomPrice + addOnCost;
        totalBookings++;
    }
    
    /**
     * Get all confirmed bookings in chronological order.
     * 
     * Returns a copy of the booking list to prevent external modification
     * of the historical record.
     * 
     * @return List of all confirmed bookings in chronological order
     */
    public List<Reservation> getAllBookings() {
        return new ArrayList<>(confirmedBookings);
    }
    
    /**
     * Get the total number of confirmed bookings.
     * 
     * @return Total number of bookings in history
     */
    public int getTotalBookings() {
        return totalBookings;
    }
    
    /**
     * Get the total revenue from all bookings.
     * 
     * @return Total revenue including room charges and add-on services
     */
    public double getTotalRevenue() {
        return totalRevenue;
    }
    
    /**
     * Get booking count by room type.
     * 
     * @return Map of room type to booking count
     */
    public Map<String, Integer> getBookingsByRoomType() {
        return new HashMap<>(roomTypeBookings);
    }
    
    /**
     * Get bookings within a specific date range.
     * 
     * Note: In a real system, this would use actual dates.
     * For this use case, we simulate by returning bookings in chronological order.
     * 
     * @param startIndex Starting index in the chronological list
     * @param endIndex Ending index in the chronological list
     * @return List of bookings within the specified range
     */
    public List<Reservation> getBookingsInRange(int startIndex, int endIndex) {
        List<Reservation> result = new ArrayList<>();
        int size = confirmedBookings.size();
        
        // Ensure valid range
        int start = Math.max(0, startIndex);
        int end = Math.min(size, endIndex + 1);
        
        for (int i = start; i < end; i++) {
            result.add(confirmedBookings.get(i));
        }
        
        return result;
    }
    
    /**
     * Find bookings by guest name (case-insensitive search).
     * 
     * @param guestName The name to search for
     * @return List of bookings matching the guest name
     */
    public List<Reservation> findBookingsByGuestName(String guestName) {
        List<Reservation> matches = new ArrayList<>();
        String searchName = guestName.toLowerCase();
        
        for (Reservation booking : confirmedBookings) {
            if (booking.getGuestName().toLowerCase().contains(searchName)) {
                matches.add(booking);
            }
        }
        
        return matches;
    }
    
    /**
     * Find bookings by room type.
     * 
     * @param roomType The room type to search for
     * @return List of bookings for the specified room type
     */
    public List<Reservation> findBookingsByRoomType(String roomType) {
        List<Reservation> matches = new ArrayList<>();
        
        for (Reservation booking : confirmedBookings) {
            if (booking.getRoomTypeRequested().equals(roomType)) {
                matches.add(booking);
            }
        }
        
        return matches;
    }
    
    /**
     * Get the most recent booking.
     * 
     * @return The last confirmed booking, or null if no bookings exist
     */
    public Reservation getMostRecentBooking() {
        if (confirmedBookings.isEmpty()) {
            return null;
        }
        return confirmedBookings.get(confirmedBookings.size() - 1);
    }
    
    /**
     * Get the oldest booking.
     * 
     * @return The first confirmed booking, or null if no bookings exist
     */
    public Reservation getOldestBooking() {
        if (confirmedBookings.isEmpty()) {
            return null;
        }
        return confirmedBookings.get(0);
    }
    
    /**
     * Check if booking history is empty.
     * 
     * @return true if no bookings have been recorded
     */
    public boolean isEmpty() {
        return confirmedBookings.isEmpty();
    }
    
    /**
     * Display all bookings in chronological order.
     */
    public void displayAllBookings() {
        System.out.println("\n*** BOOKING HISTORY - ALL CONFIRMED RESERVATIONS ***");
        System.out.println("(Stored in chronological order using List<Reservation>)\n");
        
        if (confirmedBookings.isEmpty()) {
            System.out.println("No confirmed bookings in history.");
            return;
        }
        
        for (int i = 0; i < confirmedBookings.size(); i++) {
            Reservation booking = confirmedBookings.get(i);
            System.out.println("Booking #" + (i + 1) + ":");
            booking.displayDetails();
            System.out.println();
        }
    }
    
    /**
     * Display summary statistics.
     */
    public void displaySummaryStatistics() {
        System.out.println("\n*** BOOKING HISTORY STATISTICS ***");
        System.out.println("Total Confirmed Bookings: " + totalBookings);
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
        
        if (!roomTypeBookings.isEmpty()) {
            System.out.println("\nBookings by Room Type:");
            for (Map.Entry<String, Integer> entry : roomTypeBookings.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " bookings");
            }
        }
        
        if (totalBookings > 0) {
            double avgRevenue = totalRevenue / totalBookings;
            System.out.println("\nAverage Revenue per Booking: $" + String.format("%.2f", avgRevenue));
        }
    }
}
