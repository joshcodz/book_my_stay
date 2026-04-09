/**
 * AddOnService Class - Represents an optional service offering
 * 
 * This class models individual add-on services that guests can select
 * to enhance their hotel stay. Services are independent of room bookings
 * and can be attached to any confirmed reservation.
 * 
 * Services include amenities like breakfast, airport transfer, spa packages,
 * etc., each with their own pricing and descriptions.
 * 
 * @author Development Team
 * @version 7.0
 * @since 2026
 */
public class AddOnService {
    
    // Unique service identifier
    private String serviceId;
    private String serviceName;
    private String description;
    private double price;
    private String category; // e.g., "Dining", "Transportation", "Wellness"
    
    /**
     * Constructor for AddOnService.
     * 
     * @param serviceId Unique identifier for the service
     * @param serviceName Display name of the service
     * @param description Detailed description
     * @param price Cost of the service
     * @param category Service category for organization
     */
    public AddOnService(String serviceId, String serviceName, String description, 
                       double price, String category) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
        this.category = category;
    }
    
    // Getters
    public String getServiceId() {
        return serviceId;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getPrice() {
        return price;
    }
    
    public String getCategory() {
        return category;
    }
    
    /**
     * Display service information in formatted output.
     */
    public void display() {
        System.out.println("Service: " + serviceName + " (" + serviceId + ")");
        System.out.println("Category: " + category);
        System.out.println("Description: " + description);
        System.out.println("Price: $" + String.format("%.2f", price));
    }
    
    /**
     * Get a summary string of the service.
     * 
     * @return Formatted summary for display
     */
    public String getSummary() {
        return serviceName + " - $" + String.format("%.2f", price);
    }
    
    @Override
    public String toString() {
        return serviceName + " ($" + String.format("%.2f", price) + ")";
    }
}
