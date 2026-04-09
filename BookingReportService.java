import java.util.List;
import java.util.Map;

/**
 * BookingReportService Class - Generates Reports from Booking History
 * 
 * This service provides reporting capabilities over the booking history,
 * enabling administrators to analyze system usage, generate summaries,
 * and extract operational insights. It maintains separation between
 * data storage (BookingHistory) and reporting logic.
 * 
 * Key Design Principles:
 * - Separation of storage and reporting concerns
 * - Read-only access to historical data
 * - Multiple report types for different analytical needs
 * - Business intelligence capabilities
 * - Administrative visibility into system operations
 * 
 * The service generates various reports including:
 * - Revenue reports by room type and time periods
 * - Occupancy analysis and trends
 * - Guest demographics and booking patterns
 * - Operational summaries for management
 * 
 * @author Development Team
 * @version 8.0
 * @since 2026
 */
public class BookingReportService {
    
    private BookingHistory bookingHistory;
    
    /**
     * Constructor for BookingReportService.
     * 
     * @param bookingHistory The booking history to generate reports from
     */
    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }
    
    /**
     * Generate a comprehensive booking summary report.
     */
    public void generateBookingSummaryReport() {
        System.out.println("\n=========================================");
        System.out.println("        BOOKING SUMMARY REPORT");
        System.out.println("=========================================");
        
        if (bookingHistory.isEmpty()) {
            System.out.println("No booking data available for reporting.");
            return;
        }
        
        // Basic statistics
        System.out.println("Total Confirmed Bookings: " + bookingHistory.getTotalBookings());
        System.out.println("Total Revenue Generated: $" + String.format("%.2f", bookingHistory.getTotalRevenue()));
        
        // Room type distribution
        Map<String, Integer> roomTypeBookings = bookingHistory.getBookingsByRoomType();
        System.out.println("\nRoom Type Distribution:");
        for (Map.Entry<String, Integer> entry : roomTypeBookings.entrySet()) {
            String roomType = entry.getKey();
            int count = entry.getValue();
            double percentage = (count * 100.0) / bookingHistory.getTotalBookings();
            System.out.println("  " + roomType + ": " + count + " bookings (" + 
                             String.format("%.1f", percentage) + "%)");
        }
        
        // Revenue analysis
        double avgRevenuePerBooking = bookingHistory.getTotalRevenue() / bookingHistory.getTotalBookings();
        System.out.println("\nRevenue Analysis:");
        System.out.println("  Average Revenue per Booking: $" + String.format("%.2f", avgRevenuePerBooking));
        
        // Chronological overview
        Reservation oldest = bookingHistory.getOldestBooking();
        Reservation newest = bookingHistory.getMostRecentBooking();
        
        if (oldest != null && newest != null) {
            System.out.println("\nBooking Timeline:");
            System.out.println("  First Booking: " + oldest.getGuestName() + " (" + oldest.getRoomTypeRequested() + ")");
            System.out.println("  Most Recent: " + newest.getGuestName() + " (" + newest.getRoomTypeRequested() + ")");
        }
        
        System.out.println("=========================================\n");
    }
    
    /**
     * Generate a revenue analysis report.
     */
    public void generateRevenueAnalysisReport() {
        System.out.println("\n=========================================");
        System.out.println("        REVENUE ANALYSIS REPORT");
        System.out.println("=========================================");
        
        double totalRevenue = bookingHistory.getTotalRevenue();
        int totalBookings = bookingHistory.getTotalBookings();
        
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
        System.out.println("Total Bookings: " + totalBookings);
        
        if (totalBookings > 0) {
            double avgRevenue = totalRevenue / totalBookings;
            System.out.println("Average Revenue per Booking: $" + String.format("%.2f", avgRevenue));
            
            // Revenue projections (simplified)
            System.out.println("\nRevenue Projections:");
            System.out.println("  Next 10 bookings: $" + String.format("%.2f", avgRevenue * 10));
            System.out.println("  Next 100 bookings: $" + String.format("%.2f", avgRevenue * 100));
        }
        
        System.out.println("=========================================\n");
    }
    
    /**
     * Generate an occupancy analysis report.
     */
    public void generateOccupancyAnalysisReport() {
        System.out.println("\n=========================================");
        System.out.println("      OCCUPANCY ANALYSIS REPORT");
        System.out.println("=========================================");
        
        Map<String, Integer> roomTypeBookings = bookingHistory.getBookingsByRoomType();
        
        System.out.println("Room Type Occupancy:");
        for (Map.Entry<String, Integer> entry : roomTypeBookings.entrySet()) {
            String roomType = entry.getKey();
            int bookings = entry.getValue();
            
            // Get original inventory for this room type
            int originalInventory = getOriginalInventoryForRoomType(roomType);
            
            double occupancyRate = (bookings * 100.0) / originalInventory;
            
            System.out.println("  " + roomType + ":");
            System.out.println("    Bookings: " + bookings);
            System.out.println("    Original Inventory: " + originalInventory);
            System.out.println("    Occupancy Rate: " + String.format("%.1f", occupancyRate) + "%");
        }
        
        System.out.println("=========================================\n");
    }
    
    /**
     * Generate a guest analysis report.
     */
    public void generateGuestAnalysisReport() {
        System.out.println("\n=========================================");
        System.out.println("        GUEST ANALYSIS REPORT");
        System.out.println("=========================================");
        
        List<Reservation> allBookings = bookingHistory.getAllBookings();
        
        if (allBookings.isEmpty()) {
            System.out.println("No booking data available.");
            return;
        }
        
        // Analyze guest names for patterns (simplified)
        System.out.println("Guest Booking Patterns:");
        System.out.println("Total Unique Guests: " + allBookings.size());
        
        // Count bookings by guest (simplified - in real system would use unique guest IDs)
        Map<String, Integer> guestBookingCount = new java.util.HashMap<>();
        for (Reservation booking : allBookings) {
            String guestName = booking.getGuestName();
            guestBookingCount.put(guestName, guestBookingCount.getOrDefault(guestName, 0) + 1);
        }
        
        System.out.println("\nGuest Booking Frequency:");
        for (Map.Entry<String, Integer> entry : guestBookingCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " booking(s)");
        }
        
        System.out.println("=========================================\n");
    }
    
    /**
     * Generate a chronological booking report.
     */
    public void generateChronologicalReport() {
        System.out.println("\n=========================================");
        System.out.println("    CHRONOLOGICAL BOOKING REPORT");
        System.out.println("=========================================");
        
        List<Reservation> allBookings = bookingHistory.getAllBookings();
        
        if (allBookings.isEmpty()) {
            System.out.println("No booking data available.");
            return;
        }
        
        System.out.println("Bookings in Chronological Order (First to Last):");
        System.out.println();
        
        for (int i = 0; i < allBookings.size(); i++) {
            Reservation booking = allBookings.get(i);
            System.out.println("[" + (i + 1) + "] " + booking.getGuestName() + 
                             " - " + booking.getRoomTypeRequested() + 
                             " (ID: " + booking.getRequestId() + ")");
        }
        
        System.out.println("\nTotal Bookings: " + allBookings.size());
        System.out.println("=========================================\n");
    }
    
    /**
     * Search and display bookings by guest name.
     * 
     * @param guestName The name to search for
     */
    public void searchBookingsByGuest(String guestName) {
        System.out.println("\n=========================================");
        System.out.println("    GUEST SEARCH RESULTS");
        System.out.println("=========================================");
        System.out.println("Searching for: " + guestName);
        System.out.println();
        
        List<Reservation> matches = bookingHistory.findBookingsByGuestName(guestName);
        
        if (matches.isEmpty()) {
            System.out.println("No bookings found for guest: " + guestName);
        } else {
            System.out.println("Found " + matches.size() + " booking(s):");
            for (Reservation booking : matches) {
                booking.displayDetails();
                System.out.println();
            }
        }
        
        System.out.println("=========================================\n");
    }
    
    /**
     * Generate a comprehensive operational report.
     */
    public void generateOperationalReport() {
        System.out.println("\n=========================================");
        System.out.println("      OPERATIONAL SUMMARY REPORT");
        System.out.println("=========================================");
        
        System.out.println("System Status: ACTIVE");
        System.out.println("Report Generated: " + java.time.LocalDateTime.now());
        System.out.println();
        
        // Generate all sub-reports
        generateBookingSummaryReport();
        generateRevenueAnalysisReport();
        generateOccupancyAnalysisReport();
        generateGuestAnalysisReport();
        generateChronologicalReport();
        
        System.out.println("=========================================");
        System.out.println("      END OF OPERATIONAL REPORT");
        System.out.println("=========================================\n");
    }
    
    /**
     * Helper method to get original inventory for room types.
     * (In a real system, this would come from configuration or database)
     */
    private int getOriginalInventoryForRoomType(String roomType) {
        switch (roomType) {
            case "Single Room": return 5;
            case "Double Room": return 3;
            case "Suite Room": return 2;
            default: return 1;
        }
    }
}
