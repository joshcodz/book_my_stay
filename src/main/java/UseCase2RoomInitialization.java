/**
 * Use Case 2: Basic Room Types & Static Availability
 * Application Entry Point - Room Initialization
 * 
 * This class demonstrates object modeling through inheritance and abstraction.
 * It creates room objects representing different hotel room types and displays
 * their details along with availability information stored using simple variables.
 * 
 * This use case intentionally uses primitive variables for availability to highlight
 * the limitations of hardcoded and scattered state management, establishing the need
 * for data structures in subsequent use cases.
 * 
 * Key Concepts Demonstrated:
 * - Abstract Class: Room is defined as an abstract base class
 * - Inheritance: SingleRoom, DoubleRoom, SuiteRoom extend Room
 * - Polymorphism: Room objects are referenced using the Room type
 * - Encapsulation: Room attributes are protected and accessed through methods
 * - Static Availability: Simple variables store room availability
 * 
 * @author Development Team
 * @version 2.0
 * @since 2026
 */
public class UseCase2RoomInitialization {
    
    /**
     * Main method - Entry point of the Hotel Booking Management System (Use Case 2).
     * 
     * This method orchestrates the initialization and display of room types
     * and their availability information.
     * 
     * @param args Command-line arguments (not used in this use case)
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("========================================");
        System.out.println("  Hotel Booking Management System");
        System.out.println("  Version: 2.0 - Room Types & Availability");
        System.out.println("========================================");
        
        // Create room objects representing different room types
        // Using polymorphism: Room references for different concrete implementations
        Room singleRoom = new SingleRoom(101);
        Room doubleRoom = new DoubleRoom(202);
        Room suiteRoom = new SuiteRoom(305);
        
        // Store room availability using simple variables
        // This highlights the limitations of scattered state management
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;
        
        // Display room details and availability information
        System.out.println("\n*** AVAILABLE ROOM TYPES ***\n");
        
        // Display Single Room details
        singleRoom.displayRoomDetails();
        System.out.println("Current Availability: " + singleRoomAvailability + " rooms");
        System.out.println("Status: " + (singleRoomAvailability > 0 ? "AVAILABLE" : "FULLY BOOKED"));
        
        // Display Double Room details
        doubleRoom.displayRoomDetails();
        System.out.println("Current Availability: " + doubleRoomAvailability + " rooms");
        System.out.println("Status: " + (doubleRoomAvailability > 0 ? "AVAILABLE" : "FULLY BOOKED"));
        
        // Display Suite Room details
        suiteRoom.displayRoomDetails();
        System.out.println("Current Availability: " + suiteRoomAvailability + " rooms");
        System.out.println("Status: " + (suiteRoomAvailability > 0 ? "AVAILABLE" : "FULLY BOOKED"));
        
        // Display summary information
        System.out.println("\n*** SYSTEM SUMMARY ***");
        System.out.println("Total Room Types: 3");
        System.out.println("Total Rooms in System: " + (singleRoomAvailability + doubleRoomAvailability + suiteRoomAvailability));
        System.out.println("Total Revenue Potential (per night): $" + 
            String.format("%.2f", 
                (singleRoom.getPricePerNight() * singleRoomAvailability) +
                (doubleRoom.getPricePerNight() * doubleRoomAvailability) +
                (suiteRoom.getPricePerNight() * suiteRoomAvailability)
            )
        );
        
        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================\n");
    }
}
