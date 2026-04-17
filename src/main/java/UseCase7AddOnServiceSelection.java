import java.util.Arrays;
import java.util.List;

/**
 * Use Case 7: Add-On Service Selection
 * Application Entry Point - Add-On Service Management System
 * 
 * This class demonstrates business extensibility by adding optional services
 * to the booking system using Map and List data structures. It shows how
 * real-world features can be added without modifying core booking logic.
 * 
 * Key Design Principles Demonstrated:
 * - Map<String, List<AddOnService>> for one-to-many relationship modeling
 * - Composition over inheritance for flexible feature growth
 * - Separation of concerns: add-ons independent of core booking
 * - Business extensibility without disrupting existing logic
 * - Cost aggregation for total pricing calculations
 * - Easy addition of new service types and categories
 * 
 * Problem Solved:
 * Real-world bookings include additional offerings beyond the primary product.
 * The system must support new features without disrupting existing booking
 * and allocation workflows.
 * 
 * Flow:
 * 1. Initialize add-on service manager with available services
 * 2. Create confirmed reservations (simulating completed bookings)
 * 3. Guests select add-on services for their reservations
 * 4. Services are attached using reservation-to-services mapping
 * 5. Total costs are calculated including add-on services
 * 6. Business analytics show service usage and revenue
 * 
 * Benefits:
 * - Flexible attachment of optional services to reservations
 * - Clean mapping between bookings and value-added features
 * - Easy expansion of services without core booking changes
 * - Independent pricing and cost aggregation
 * - Scalable service catalog management
 * 
 * @author Development Team
 * @version 7.0
 * @since 2026
 */
public class UseCase7AddOnServiceSelection {
    
    /**
     * Main method - Entry point of the Hotel Booking Management System (Use Case 7).
     * 
     * This method demonstrates:
     * 1. Add-on service catalog management
     * 2. One-to-many relationship modeling with Map and List
     * 3. Service selection and attachment to reservations
     * 4. Cost aggregation and pricing calculations
     * 5. Business analytics for service usage
     * 
     * @param args Command-line arguments (not used in this use case)
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("========================================");
        System.out.println("  Hotel Booking Management System");
        System.out.println("  Version: 7.0 - Add-On Service Selection");
        System.out.println("========================================");
        
        // [STEP 1] Initialize add-on service manager
        System.out.println("\n[STEP 1] Initializing Add-On Service System");
        System.out.println("=========================================");
        AddOnServiceManager serviceManager = new AddOnServiceManager();
        System.out.println("[OK] AddOnServiceManager initialized");
        System.out.println("Data Structure: Map<String, List<AddOnService>>");
        System.out.println("Purpose: One-to-many relationship between reservations and services");
        
        // [STEP 2] Display available services catalog
        System.out.println("\n[STEP 2] Available Add-On Services Catalog");
        System.out.println("=========================================");
        serviceManager.displayAvailableServices();
        
        // [STEP 3] Create confirmed reservations (simulate completed bookings)
        System.out.println("\n[STEP 3] Creating Confirmed Reservations");
        System.out.println("=========================================");
        
        // Simulate confirmed reservations with assigned room IDs
        String[] reservationIds = {"1000", "1001", "1002", "1003", "1004"};
        String[] guestNames = {"Alice Johnson", "Bob Smith", "Carol Davis", "Dave Wilson", "Eve Martinez"};
        String[] roomTypes = {"Single Room", "Double Room", "Suite Room", "Double Room", "Single Room"};
        
        System.out.println("Confirmed Reservations:");
        for (int i = 0; i < reservationIds.length; i++) {
            System.out.println("  " + reservationIds[i] + " - " + guestNames[i] + " (" + roomTypes[i] + ")");
        }
        
        // [STEP 4] Guests select add-on services
        System.out.println("\n[STEP 4] Guest Service Selections");
        System.out.println("=========================================");
        
        // Alice Johnson (Single Room) - Breakfast and Airport Pickup
        AddOnService breakfast = serviceManager.findServiceById("BREAKFAST");
        AddOnService airportPickup = serviceManager.findServiceById("AIRPORT_PICKUP");
        serviceManager.addServicesToReservation("1000", Arrays.asList(breakfast, airportPickup));
        System.out.println("Alice Johnson (1000): Added Breakfast + Airport Pickup");
        
        // Bob Smith (Double Room) - Dinner and Spa Day
        AddOnService dinner = serviceManager.findServiceById("DINNER");
        AddOnService spaDay = serviceManager.findServiceById("SPA_DAY");
        serviceManager.addServicesToReservation("1001", Arrays.asList(dinner, spaDay));
        System.out.println("Bob Smith (1001): Added Dinner + Spa Day");
        
        // Carol Davis (Suite Room) - Concierge Upgrade and Movie Package
        AddOnService concierge = serviceManager.findServiceById("CONCIERGE_UPGRADE");
        AddOnService moviePackage = serviceManager.findServiceById("MOVIE_PACKAGE");
        serviceManager.addServicesToReservation("1002", Arrays.asList(concierge, moviePackage));
        System.out.println("Carol Davis (1002): Added Concierge Upgrade + Movie Package");
        
        // Dave Wilson (Double Room) - Gym Access only
        AddOnService gymAccess = serviceManager.findServiceById("GYM_ACCESS");
        serviceManager.addServiceToReservation("1003", gymAccess);
        System.out.println("Dave Wilson (1003): Added Gym Access");
        
        // Eve Martinez (Single Room) - Multiple services
        AddOnService airportDropoff = serviceManager.findServiceById("AIRPORT_DROPOFF");
        serviceManager.addServicesToReservation("1004", Arrays.asList(breakfast, gymAccess, moviePackage));
        System.out.println("Eve Martinez (1004): Added Breakfast + Gym Access + Movie Package");
        
        // [STEP 5] Display service selections for each reservation
        System.out.println("\n[STEP 5] Service Selections by Reservation");
        System.out.println("=========================================");
        
        for (String reservationId : reservationIds) {
            serviceManager.displayServicesForReservation(reservationId);
        }
        
        // [STEP 6] Demonstrate cost calculations
        System.out.println("\n[STEP 6] Cost Calculations");
        System.out.println("=========================================");
        
        System.out.println("Add-On Service Costs by Reservation:");
        double totalAddOnRevenue = 0.0;
        
        for (String reservationId : reservationIds) {
            double serviceCost = serviceManager.calculateTotalServiceCost(reservationId);
            totalAddOnRevenue += serviceCost;
            System.out.println("  Reservation " + reservationId + ": $" + String.format("%.2f", serviceCost));
        }
        
        System.out.println("\nTotal Add-On Revenue: $" + String.format("%.2f", totalAddOnRevenue));
        
        // [STEP 7] Business analytics
        System.out.println("\n[STEP 7] Business Analytics");
        System.out.println("=========================================");
        
        double[] stats = serviceManager.getServiceUsageStatistics();
        System.out.println("Service Usage Statistics:");
        System.out.println("  Reservations with Add-Ons: " + (int)stats[0]);
        System.out.println("  Total Services Selected: " + (int)stats[1]);
        System.out.println("  Total Revenue from Services: $" + String.format("%.2f", stats[2]));
        
        // Calculate average services per reservation
        double avgServices = stats[0] > 0 ? stats[1] / stats[0] : 0;
        System.out.println("  Average Services per Reservation: " + String.format("%.1f", avgServices));
        
        // [STEP 8] Demonstrate service filtering
        System.out.println("\n[STEP 8] Service Filtering Examples");
        System.out.println("=========================================");
        
        // Services under $50
        List<AddOnService> budgetServices = serviceManager.getServicesByPriceRange(0, 50);
        System.out.println("Services under $50:");
        for (AddOnService service : budgetServices) {
            System.out.println("  " + service.getSummary());
        }
        
        // Premium services over $100
        List<AddOnService> premiumServices = serviceManager.getServicesByPriceRange(100, Double.MAX_VALUE);
        System.out.println("\nPremium Services over $100:");
        for (AddOnService service : premiumServices) {
            System.out.println("  " + service.getSummary());
        }
        
        // [STEP 9] Demonstrate service removal
        System.out.println("\n[STEP 9] Service Modification Example");
        System.out.println("=========================================");
        
        System.out.println("Before removal:");
        serviceManager.displayServicesForReservation("1004");
        
        // Remove gym access from Eve's reservation
        serviceManager.removeServiceFromReservation("1004", gymAccess);
        System.out.println("After removing Gym Access from Reservation 1004:");
        serviceManager.displayServicesForReservation("1004");
        
        // [STEP 10] Final overview
        System.out.println("\n[STEP 10] Final System Overview");
        System.out.println("=========================================");
        serviceManager.displayAllReservationsWithServices();
        
        // [STEP 11] Key insights summary
        System.out.println("\n[KEY INSIGHTS: Business Extensibility with Map & List]");
        System.out.println("=========================================");
        System.out.println("Why Map<String, List<AddOnService>> Enables Business Growth:");
        System.out.println();
        System.out.println("1. One-to-Many Relationship Modeling");
        System.out.println("   - Single reservation can have multiple services");
        System.out.println("   - Natural representation of real-world bookings");
        System.out.println("   - Flexible association without rigid structures");
        System.out.println();
        System.out.println("2. Composition over Inheritance");
        System.out.println("   - Services composed with reservations, not inherited");
        System.out.println("   - Avoids complex class hierarchies");
        System.out.println("   - Supports dynamic feature addition");
        System.out.println();
        System.out.println("3. Separation of Core and Optional Features");
        System.out.println("   - Add-ons independent of room allocation");
        System.out.println("   - Core booking logic remains unchanged");
        System.out.println("   - Optional features don't complicate critical paths");
        System.out.println();
        System.out.println("4. Business Extensibility");
        System.out.println("   - New service types added without code changes");
        System.out.println("   - Categories and pricing easily expandable");
        System.out.println("   - Revenue streams grow independently");
        System.out.println();
        System.out.println("5. Cost Aggregation & Analytics");
        System.out.println("   - Modular pricing calculations");
        System.out.println("   - Business intelligence capabilities");
        System.out.println("   - Revenue tracking and reporting");
        System.out.println();
        System.out.println("6. Scalability & Performance");
        System.out.println("   - O(1) average-case HashMap lookups");
        System.out.println("   - Efficient for large numbers of reservations");
        System.out.println("   - Memory efficient with shared service objects");
        
        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================\n");
    }
}
