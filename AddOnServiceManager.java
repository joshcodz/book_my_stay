import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AddOnServiceManager Class - Manages Add-On Services for Reservations
 * 
 * This class handles the association between reservations and their selected
 * add-on services using a Map<String, List<AddOnService>> data structure.
 * It models the one-to-many relationship where a single reservation can have
 * multiple associated services.
 * 
 * Key Design Principles:
 * - Map<String, List<AddOnService>> for reservation-to-services mapping
 * - One-to-many relationship modeling
 * - Composition over inheritance for flexibility
 * - Separation of concerns: add-ons independent of core booking
 * - Cost aggregation for total pricing
 * - Easy extensibility for new service types
 * 
 * The manager provides methods to:
 * - Add services to reservations
 * - Remove services from reservations
 * - Calculate total add-on costs
 * - Display service selections
 * - Support business extensibility
 * 
 * @author Development Team
 * @version 7.0
 * @since 2026
 */
public class AddOnServiceManager {
    
    // Map<String, List<AddOnService>> - Key: Reservation ID, Value: List of selected services
    // This models the one-to-many relationship between reservations and services
    private Map<String, List<AddOnService>> reservationServices;
    
    // Available services catalog
    private List<AddOnService> availableServices;
    
    /**
     * Constructor for AddOnServiceManager.
     * Initializes the service mapping and populates the available services catalog.
     */
    public AddOnServiceManager() {
        this.reservationServices = new HashMap<>();
        this.availableServices = new ArrayList<>();
        initializeAvailableServices();
    }
    
    /**
     * Initialize the catalog of available add-on services.
     * 
     * This represents the business offerings that guests can select
     * to enhance their hotel stay.
     */
    private void initializeAvailableServices() {
        // Dining services
        availableServices.add(new AddOnService("BREAKFAST", "Daily Breakfast", 
            "Continental breakfast served in the dining room", 15.00, "Dining"));
        availableServices.add(new AddOnService("DINNER", "Evening Dinner", 
            "Three-course dinner with wine pairing", 45.00, "Dining"));
        
        // Transportation services
        availableServices.add(new AddOnService("AIRPORT_PICKUP", "Airport Pickup", 
            "Private car transfer from airport to hotel", 75.00, "Transportation"));
        availableServices.add(new AddOnService("AIRPORT_DROPOFF", "Airport Drop-off", 
            "Private car transfer from hotel to airport", 75.00, "Transportation"));
        
        // Wellness services
        availableServices.add(new AddOnService("SPA_DAY", "Full Day Spa Package", 
            "Complete spa treatment with massage and facial", 150.00, "Wellness"));
        availableServices.add(new AddOnService("GYM_ACCESS", "Premium Gym Access", 
            "Extended hours access to fitness center", 25.00, "Wellness"));
        
        // Entertainment services
        availableServices.add(new AddOnService("MOVIE_PACKAGE", "In-Room Movie Package", 
            "Premium movie channels and pay-per-view", 12.00, "Entertainment"));
        availableServices.add(new AddOnService("CONCIERGE_UPGRADE", "Concierge Upgrade", 
            "Priority concierge service and reservations", 35.00, "Entertainment"));
    }
    
    /**
     * Add an add-on service to a reservation.
     * 
     * This method associates a service with a reservation by adding it to
     * the reservation's service list. If the reservation doesn't have any
     * services yet, a new list is created.
     * 
     * @param reservationId The ID of the reservation
     * @param service The service to add
     * @return true if service was added successfully
     */
    public boolean addServiceToReservation(String reservationId, AddOnService service) {
        // Get or create the service list for this reservation
        List<AddOnService> services = reservationServices.computeIfAbsent(reservationId, 
            k -> new ArrayList<>());
        
        // Check if service is already added (prevent duplicates)
        if (services.contains(service)) {
            return false; // Service already added
        }
        
        // Add the service to the list
        services.add(service);
        return true;
    }
    
    /**
     * Add multiple services to a reservation at once.
     * 
     * @param reservationId The ID of the reservation
     * @param services List of services to add
     * @return Number of services successfully added
     */
    public int addServicesToReservation(String reservationId, List<AddOnService> services) {
        int addedCount = 0;
        for (AddOnService service : services) {
            if (addServiceToReservation(reservationId, service)) {
                addedCount++;
            }
        }
        return addedCount;
    }
    
    /**
     * Remove a service from a reservation.
     * 
     * @param reservationId The ID of the reservation
     * @param service The service to remove
     * @return true if service was removed successfully
     */
    public boolean removeServiceFromReservation(String reservationId, AddOnService service) {
        List<AddOnService> services = reservationServices.get(reservationId);
        if (services != null) {
            return services.remove(service);
        }
        return false;
    }
    
    /**
     * Get all services selected for a reservation.
     * 
     * @param reservationId The ID of the reservation
     * @return List of selected services (empty list if none selected)
     */
    public List<AddOnService> getServicesForReservation(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }
    
    /**
     * Calculate total cost of add-on services for a reservation.
     * 
     * @param reservationId The ID of the reservation
     * @return Total cost of all selected services
     */
    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = getServicesForReservation(reservationId);
        double total = 0.0;
        for (AddOnService service : services) {
            total += service.getPrice();
        }
        return total;
    }
    
    /**
     * Get all available services organized by category.
     * 
     * @return Map of category to list of services
     */
    public Map<String, List<AddOnService>> getServicesByCategory() {
        Map<String, List<AddOnService>> categorized = new HashMap<>();
        
        for (AddOnService service : availableServices) {
            String category = service.getCategory();
            categorized.computeIfAbsent(category, k -> new ArrayList<>()).add(service);
        }
        
        return categorized;
    }
    
    /**
     * Get all reservations that have selected services.
     * 
     * @return Set of reservation IDs with add-on services
     */
    public java.util.Set<String> getReservationsWithServices() {
        return reservationServices.keySet();
    }
    
    /**
     * Find a service by its ID.
     * 
     * @param serviceId The service ID to find
     * @return The service if found, null otherwise
     */
    public AddOnService findServiceById(String serviceId) {
        for (AddOnService service : availableServices) {
            if (service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }
    
    /**
     * Get services in a specific price range.
     * 
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of services within price range
     */
    public List<AddOnService> getServicesByPriceRange(double minPrice, double maxPrice) {
        List<AddOnService> filtered = new ArrayList<>();
        for (AddOnService service : availableServices) {
            double price = service.getPrice();
            if (price >= minPrice && price <= maxPrice) {
                filtered.add(service);
            }
        }
        return filtered;
    }
    
    /**
     * Display all available services organized by category.
     */
    public void displayAvailableServices() {
        System.out.println("\n*** AVAILABLE ADD-ON SERVICES ***");
        System.out.println("(Organized by Category)\n");
        
        Map<String, List<AddOnService>> categorized = getServicesByCategory();
        
        for (String category : categorized.keySet()) {
            System.out.println("--- " + category + " ---");
            List<AddOnService> services = categorized.get(category);
            
            for (int i = 0; i < services.size(); i++) {
                AddOnService service = services.get(i);
                System.out.println("  " + (i + 1) + ". " + service.getSummary());
                System.out.println("     " + service.getDescription());
            }
            System.out.println();
        }
    }
    
    /**
     * Display services selected for a specific reservation.
     * 
     * @param reservationId The reservation ID to display services for
     */
    public void displayServicesForReservation(String reservationId) {
        List<AddOnService> services = getServicesForReservation(reservationId);
        
        System.out.println("\n*** SERVICES FOR RESERVATION " + reservationId + " ***");
        
        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
        } else {
            System.out.println("Selected Services:");
            double totalCost = 0.0;
            
            for (int i = 0; i < services.size(); i++) {
                AddOnService service = services.get(i);
                System.out.println("  " + (i + 1) + ". " + service.getSummary());
                totalCost += service.getPrice();
            }
            
            System.out.println("\nTotal Add-On Cost: $" + String.format("%.2f", totalCost));
        }
        System.out.println();
    }
    
    /**
     * Display summary of all reservations with their add-on services.
     */
    public void displayAllReservationsWithServices() {
        System.out.println("\n*** ALL RESERVATIONS WITH ADD-ON SERVICES ***");
        
        if (reservationServices.isEmpty()) {
            System.out.println("No reservations have selected add-on services.");
        } else {
            for (String reservationId : reservationServices.keySet()) {
                List<AddOnService> services = reservationServices.get(reservationId);
                double totalCost = calculateTotalServiceCost(reservationId);
                
                System.out.println("Reservation " + reservationId + ":");
                System.out.println("  Services: " + services.size());
                System.out.println("  Total Cost: $" + String.format("%.2f", totalCost));
                System.out.println("  Service List: " + services);
                System.out.println();
            }
        }
    }
    
    /**
     * Get usage statistics for add-on services.
     * 
     * @return Array with [totalReservationsWithServices, totalServicesSelected, totalRevenue]
     */
    public double[] getServiceUsageStatistics() {
        int totalReservationsWithServices = reservationServices.size();
        int totalServicesSelected = 0;
        double totalRevenue = 0.0;
        
        for (List<AddOnService> services : reservationServices.values()) {
            totalServicesSelected += services.size();
            for (AddOnService service : services) {
                totalRevenue += service.getPrice();
            }
        }
        
        return new double[]{totalReservationsWithServices, totalServicesSelected, totalRevenue};
    }
}
