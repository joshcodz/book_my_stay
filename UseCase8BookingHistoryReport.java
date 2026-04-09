import java.util.List;

/**
 * Use Case 8: Booking History & Reporting
 * Application Entry Point - Historical Tracking and Administrative Reporting
 * 
 * This class demonstrates historical tracking of confirmed bookings using
 * a List data structure to maintain chronological records. It provides
 * operational visibility, audit capabilities, and reporting functionality
 * for administrative review and business analysis.
 * 
 * Key Design Principles Demonstrated:
 * - List<Reservation> for ordered storage of confirmed bookings
 * - Chronological preservation using insertion order
 * - Read-only historical access for audit and reporting
 * - Separation of data storage from reporting logic
 * - Operational visibility for administrative purposes
 * - Persistence mindset without external storage medium
 * 
 * Problem Solved:
 * Real systems require visibility into past transactions for auditing,
 * analysis, and customer service. Without historical tracking, completed
 * transactions become invisible, preventing proper system oversight.
 * 
 * Flow:
 * 1. Initialize booking history and report service
 * 2. Simulate confirmed bookings with room assignments
 * 3. Store each confirmed booking in chronological history
 * 4. Generate various administrative reports
 * 5. Demonstrate search and analysis capabilities
 * 6. Show operational visibility and audit trail
 * 
 * Benefits:
 * - Complete and traceable booking audit trail
 * - Administrative analysis and reporting capabilities
 * - Customer service support through booking history
 * - Operational visibility into system usage
 * - Business intelligence and revenue analysis
 * 
 * @author Development Team
 * @version 8.0
 * @since 2026
 */
public class UseCase8BookingHistoryReport {
    
    /**
     * Main method - Entry point of the Hotel Booking Management System (Use Case 8).
     * 
     * This method demonstrates:
     * 1. Historical tracking of confirmed bookings
     * 2. Chronological storage using List data structure
     * 3. Administrative reporting and analysis
     * 4. Search and audit capabilities
     * 5. Operational visibility and business intelligence
     * 
     * @param args Command-line arguments (not used in this use case)
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("========================================");
        System.out.println("  Hotel Booking Management System");
        System.out.println("  Version: 8.0 - Booking History & Reporting");
        System.out.println("========================================");
        
        // [STEP 1] Initialize booking history and reporting system
        System.out.println("\n[STEP 1] Initializing Booking History & Reporting");
        System.out.println("=========================================");
        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService(bookingHistory);
        
        System.out.println("[OK] BookingHistory initialized");
        System.out.println("Data Structure: List<Reservation>");
        System.out.println("Purpose: Chronological storage of confirmed bookings");
        System.out.println("[OK] BookingReportService initialized");
        System.out.println("Purpose: Administrative reporting and analysis");
        
        // [STEP 2] Simulate confirmed bookings with room assignments
        System.out.println("\n[STEP 2] Recording Confirmed Bookings");
        System.out.println("=========================================");
        System.out.println("Simulating successful room allocations and storing in history...\n");
        
        // Create confirmed reservations with room assignments
        Reservation[] confirmedBookings = {
            new Reservation("Alice Johnson", "alice@email.com", "555-0001", "Single Room", 3, 1),
            new Reservation("Bob Smith", "bob@email.com", "555-0002", "Double Room", 5, 2),
            new Reservation("Carol Davis", "carol@email.com", "555-0003", "Suite Room", 2, 3),
            new Reservation("Dave Wilson", "dave@email.com", "555-0004", "Double Room", 4, 2),
            new Reservation("Eve Martinez", "eve@email.com", "555-0005", "Single Room", 7, 1),
            new Reservation("Frank Garcia", "frank@email.com", "555-0006", "Single Room", 1, 1),
            new Reservation("Grace Lee", "grace@email.com", "555-0007", "Double Room", 3, 2)
        };
        
        // Room prices for revenue tracking
        double[] roomPrices = {75.00, 125.00, 250.00, 125.00, 75.00, 75.00, 125.00};
        
        // Add-on service costs (simulated)
        double[] addOnCosts = {90.00, 195.00, 47.00, 25.00, 52.00, 0.00, 0.00};
        
        // Record each confirmed booking in history
        for (int i = 0; i < confirmedBookings.length; i++) {
            Reservation booking = confirmedBookings[i];
            booking.setStatus("CONFIRMED");
            
            double roomPrice = roomPrices[i];
            double addOnCost = addOnCosts[i];
            
            bookingHistory.addConfirmedBooking(booking, roomPrice, addOnCost);
            
            System.out.println("Recorded: " + booking.getSummary() + 
                             " (Room: $" + String.format("%.2f", roomPrice) + 
                             ", Add-ons: $" + String.format("%.2f", addOnCost) + ")");
        }
        
        System.out.println("\n[OK] All confirmed bookings stored in chronological history");
        
        // [STEP 3] Display booking history
        System.out.println("\n[STEP 3] Complete Booking History");
        System.out.println("=========================================");
        bookingHistory.displayAllBookings();
        
        // [STEP 4] Generate summary statistics
        System.out.println("\n[STEP 4] Booking History Statistics");
        System.out.println("=========================================");
        bookingHistory.displaySummaryStatistics();
        
        // [STEP 5] Generate comprehensive reports
        System.out.println("\n[STEP 5] Administrative Reports");
        System.out.println("=========================================");
        
        // Generate individual reports
        reportService.generateBookingSummaryReport();
        reportService.generateRevenueAnalysisReport();
        reportService.generateOccupancyAnalysisReport();
        reportService.generateGuestAnalysisReport();
        
        // [STEP 6] Demonstrate search capabilities
        System.out.println("\n[STEP 6] Search & Audit Capabilities");
        System.out.println("=========================================");
        
        // Search by guest name
        reportService.searchBookingsByGuest("Alice");
        reportService.searchBookingsByGuest("Smith");
        
        // Generate chronological report
        reportService.generateChronologicalReport();
        
        // [STEP 7] Demonstrate range queries
        System.out.println("\n[STEP 7] Historical Range Analysis");
        System.out.println("=========================================");
        
        System.out.println("First 3 bookings:");
        List<Reservation> firstThree = bookingHistory.getBookingsInRange(0, 2);
        for (int i = 0; i < firstThree.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + firstThree.get(i).getSummary());
        }
        
        System.out.println("\nLast 3 bookings:");
        List<Reservation> lastThree = bookingHistory.getBookingsInRange(4, 6);
        for (int i = 0; i < lastThree.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + lastThree.get(i).getSummary());
        }
        
        // [STEP 8] Generate complete operational report
        System.out.println("\n[STEP 8] Complete Operational Report");
        System.out.println("=========================================");
        reportService.generateOperationalReport();
        
        // [STEP 9] Key insights summary
        System.out.println("\n[KEY INSIGHTS: Historical Tracking with List Data Structure]");
        System.out.println("=========================================");
        System.out.println("Why List<Reservation> Enables Operational Visibility:");
        System.out.println();
        System.out.println("1. Chronological Order Preservation");
        System.out.println("   - ArrayList maintains insertion order automatically");
        System.out.println("   - Bookings stored in the sequence they were confirmed");
        System.out.println("   - Natural timeline representation without manual sorting");
        System.out.println();
        System.out.println("2. Operational Visibility");
        System.out.println("   - Complete audit trail of all confirmed bookings");
        System.out.println("   - Administrative access to historical transactions");
        System.out.println("   - Support for customer service and issue resolution");
        System.out.println();
        System.out.println("3. Reporting Readiness");
        System.out.println("   - Structured data enables various report types");
        System.out.println("   - Business intelligence and analytics capabilities");
        System.out.println("   - Revenue tracking and operational insights");
        System.out.println();
        System.out.println("4. Separation of Concerns");
        System.out.println("   - BookingHistory: Data storage and retrieval");
        System.out.println("   - BookingReportService: Analysis and reporting");
        System.out.println("   - Clean separation prevents coupling");
        System.out.println();
        System.out.println("5. Persistence Mindset");
        System.out.println("   - Treats history as long-lived information");
        System.out.println("   - Prepares conceptually for external storage");
        System.out.println("   - Memory-based but designed for persistence");
        System.out.println();
        System.out.println("6. Scalability & Performance");
        System.out.println("   - O(1) access to recent bookings");
        System.out.println("   - Efficient chronological iteration");
        System.out.println("   - Suitable for large booking histories");
        System.out.println();
        System.out.println("7. Audit & Compliance");
        System.out.println("   - Immutable historical record");
        System.out.println("   - Traceability of all booking transactions");
        System.out.println("   - Support for regulatory requirements");
        
        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================\n");
    }
}
