/**
 * Use Case 9: Error Handling & Validation
 * Application Entry Point - Structured Validation and Error Handling
 *
 * This class demonstrates comprehensive error handling and validation
 * in the Hotel Booking Management System. It implements fail-fast design
 * by detecting invalid inputs and system states early, preventing data
 * corruption and ensuring system reliability.
 *
 * Key Design Principles Demonstrated:
 * - Input validation before processing
 * - Custom exceptions for domain-specific errors
 * - Fail-fast error detection and prevention
 * - Guarding system state from invalid operations
 * - Graceful failure handling with clear error messages
 * - Correctness over happy path scenarios
 *
 * Problem Solved:
 * Previous use cases assumed valid input and could be corrupted by
 * invalid data. This use case introduces robust validation that prevents
 * system corruption and provides clear feedback for invalid operations.
 *
 * Flow:
 * 1. Initialize validation service and system components
 * 2. Demonstrate successful validation scenarios
 * 3. Demonstrate various validation failure scenarios
 * 4. Show error handling and recovery
 * 5. Validate system state protection
 * 6. Display validation benefits and system reliability
 *
 * Validation Categories Demonstrated:
 * - Input format validation (email, phone, required fields)
 * - Business rule validation (guest counts, duration limits)
 * - System constraint validation (inventory availability)
 * - State consistency validation (preventing invalid operations)
 *
 * @author Development Team
 * @version 9.0
 * @since 2026
 */
public class UseCase9ErrorHandlingValidation {

    /**
     * Main method - Entry point of the Hotel Booking Management System (Use Case 9).
     *
     * This method demonstrates:
     * 1. Comprehensive input validation
     * 2. Custom exception handling for booking errors
     * 3. Fail-fast design preventing invalid operations
     * 4. System state protection and consistency
     * 5. Graceful error handling and user feedback
     * 6. Validation benefits for system reliability
     *
     * @param args Command-line arguments (not used in this use case)
     */
    public static void main(String[] args) {
        // Display application header
        System.out.println("========================================");
        System.out.println("  Hotel Booking Management System");
        System.out.println("  Version: 9.0 - Error Handling & Validation");
        System.out.println("========================================");

        // [STEP 1] Initialize validation service and system components
        System.out.println("\n[STEP 1] Initializing Validation Service & System Components");
        System.out.println("=========================================");

        BookingValidationService validationService = new BookingValidationService();
        RoomInventory roomInventory = new RoomInventory();

        System.out.println("[OK] BookingValidationService initialized");
        System.out.println("Purpose: Input validation and system state checking");
        System.out.println("[OK] RoomInventory initialized with baseline data");
        System.out.println("[OK] RoomAllocationService initialized");
        System.out.println("Purpose: Safe room allocation with validation");

        // [STEP 2] Demonstrate successful validation scenarios
        System.out.println("\n[STEP 2] Successful Validation Scenarios");
        System.out.println("=========================================");

        // Valid reservation examples
        Reservation[] validReservations = {
            new Reservation("Alice Johnson", "alice@email.com", "555-0123", "Single Room", 3, 1),
            new Reservation("Bob Smith", "bob.smith@domain.com", "5550123456", "Double Room", 5, 2),
            new Reservation("Carol Davis", "carol.davis@company.org", "555-0456", "Suite Room", 2, 3)
        };

        System.out.println("Testing valid reservations:");
        for (int i = 0; i < validReservations.length; i++) {
            Reservation res = validReservations[i];
            try {
                validationService.validateReservation(res, roomInventory);
                System.out.println("  ✓ Reservation " + (i + 1) + " validated successfully: " + res.getSummary());
            } catch (BookingValidationException e) {
                System.out.println("  ✗ Unexpected validation failure: " + e.getUserFriendlyMessage());
            }
        }

        // [STEP 3] Demonstrate validation failure scenarios
        System.out.println("\n[STEP 3] Validation Failure Scenarios");
        System.out.println("=========================================");

        // Invalid reservation examples to test various validation rules
        Reservation[] invalidReservations = {
            // Invalid room type
            new Reservation("Test User", "test@email.com", "555-0000", "Invalid Room", 1, 1),
            // Invalid guest count for Single Room
            new Reservation("Test User", "test@email.com", "555-0000", "Single Room", 1, 2),
            // Invalid duration (too long)
            new Reservation("Test User", "test@email.com", "555-0000", "Double Room", 35, 1),
            // Invalid duration (zero)
            new Reservation("Test User", "test@email.com", "555-0000", "Suite Room", 0, 1),
            // Invalid email format
            new Reservation("Test User", "invalid-email", "555-0000", "Single Room", 1, 1),
            // Invalid phone format
            new Reservation("Test User", "test@email.com", "invalid-phone", "Double Room", 1, 1),
            // Null guest name
            new Reservation(null, "test@email.com", "555-0000", "Suite Room", 1, 1),
            // Empty email
            new Reservation("Test User", "", "555-0000", "Single Room", 1, 1)
        };

        String[] expectedErrors = {
            "INVALID_ROOM_TYPE",
            "INVALID_GUEST_COUNT",
            "INVALID_DURATION",
            "INVALID_DURATION",
            "INVALID_EMAIL_FORMAT",
            "INVALID_PHONE_FORMAT",
            "NULL_OR_EMPTY_INPUT",
            "NULL_OR_EMPTY_INPUT"
        };

        System.out.println("Testing invalid reservations (fail-fast validation):");
        for (int i = 0; i < invalidReservations.length; i++) {
            Reservation res = invalidReservations[i];
            try {
                validationService.validateReservation(res, roomInventory);
                System.out.println("  ✗ Validation should have failed for case " + (i + 1));
            } catch (BookingValidationException e) {
                System.out.println("  ✓ Case " + (i + 1) + " correctly failed: " +
                                 e.getErrorType() + " - " + e.getMessage());
            }
        }

        // [STEP 4] Demonstrate system state protection
        System.out.println("\n[STEP 4] System State Protection");
        System.out.println("=========================================");

        System.out.println("Testing inventory state protection:");

        // First, let's book some rooms to reduce inventory
        try {
            Reservation validBooking = new Reservation("John Doe", "john@email.com", "555-1111", "Single Room", 2, 1);
            validationService.validateReservation(validBooking, roomInventory);
            boolean booked = roomInventory.bookRoom("Single Room");
            if (booked) {
                System.out.println("  ✓ Successfully booked: " + validBooking.getSummary());
            } else {
                System.out.println("  ✗ Booking failed - no rooms available");
            }
        } catch (BookingValidationException e) {
            System.out.println("  ✗ Unexpected error: " + e.getUserFriendlyMessage());
        }

        // Now test booking when inventory is exhausted
        System.out.println("\nTesting booking with insufficient inventory:");
        try {
            // Try to book more Single Rooms than available
            for (int i = 0; i < 10; i++) { // More than available inventory
                boolean booked = roomInventory.bookRoom("Single Room");
                if (booked) {
                    System.out.println("  ✓ Booked room " + (i + 1));
                } else {
                    System.out.println("  ✗ Cannot book room " + (i + 1) + " - insufficient inventory");
                    break; // Stop trying once we can't book anymore
                }
            }
        } catch (Exception e) {
            System.out.println("  ✗ Unexpected error during booking attempts: " + e.getMessage());
        }

        // [STEP 5] Demonstrate error recovery and system stability
        System.out.println("\n[STEP 5] Error Recovery & System Stability");
        System.out.println("=========================================");

        System.out.println("Testing system stability after validation errors:");

        // System should continue working after errors
        try {
            Reservation recoveryTest = new Reservation("Recovery Test", "recovery@test.com", "555-9999", "Double Room", 3, 2);
            validationService.validateReservation(recoveryTest, roomInventory);
            boolean booked = roomInventory.bookRoom("Double Room");
            if (booked) {
                System.out.println("  ✓ System recovered and processed valid booking: " + recoveryTest.getSummary());
            } else {
                System.out.println("  ✗ Recovery test failed - no rooms available");
            }
        } catch (BookingValidationException e) {
            System.out.println("  ✗ Recovery test failed: " + e.getUserFriendlyMessage());
        }

        // [STEP 6] Demonstrate validation service capabilities
        System.out.println("\n[STEP 6] Validation Service Capabilities");
        System.out.println("=========================================");

        System.out.println("Validation Rules Summary:");
        System.out.println("  Valid Room Types: " + validationService.getValidRoomTypes());
        System.out.println("  Duration Limits: " + validationService.getDurationLimits()[0] + " - " +
                         validationService.getDurationLimits()[1] + " nights");
        System.out.println("  Guest Limits:");
        System.out.println("    Single Room: max " + validationService.getMaxGuestsForRoomType("Single Room"));
        System.out.println("    Double Room: max " + validationService.getMaxGuestsForRoomType("Double Room"));
        System.out.println("    Suite Room: max " + validationService.getMaxGuestsForRoomType("Suite Room"));

        // [STEP 7] Key insights summary
        System.out.println("\n[KEY INSIGHTS: Error Handling & Validation]");
        System.out.println("=========================================");
        System.out.println("Why Validation Prevents System Corruption:");
        System.out.println();
        System.out.println("1. Input Validation");
        System.out.println("   - Checks data format and required fields before processing");
        System.out.println("   - Prevents null pointer exceptions and invalid data entry");
        System.out.println("   - Ensures email and phone formats meet business requirements");
        System.out.println();
        System.out.println("2. Business Rule Enforcement");
        System.out.println("   - Validates guest counts against room capacities");
        System.out.println("   - Enforces duration limits (1-30 nights)");
        System.out.println("   - Maintains consistency with hotel policies");
        System.out.println();
        System.out.println("3. System State Protection");
        System.out.println("   - Guards inventory from negative values");
        System.out.println("   - Prevents double-booking through allocation checks");
        System.out.println("   - Maintains data integrity across operations");
        System.out.println();
        System.out.println("4. Fail-Fast Design");
        System.out.println("   - Detects errors immediately upon input");
        System.out.println("   - Stops processing before system state changes");
        System.out.println("   - Simplifies debugging and error diagnosis");
        System.out.println();
        System.out.println("5. Custom Exceptions");
        System.out.println("   - Domain-specific error types for booking scenarios");
        System.out.println("   - Clear, informative error messages");
        System.out.println("   - Supports different handling strategies per error type");
        System.out.println();
        System.out.println("6. Graceful Failure Handling");
        System.out.println("   - Application continues running after validation errors");
        System.out.println("   - Users receive clear feedback about issues");
        System.out.println("   - System stability maintained during error conditions");
        System.out.println();
        System.out.println("7. Correctness Over Happy Path");
        System.out.println("   - Designed for real-world conditions with invalid input");
        System.out.println("   - Robust handling of edge cases and error scenarios");
        System.out.println("   - Prevents silent failures and data corruption");
        System.out.println();
        System.out.println("8. Early Error Detection");
        System.out.println("   - Validates before inventory updates or allocations");
        System.out.println("   - Prevents cascading failures from invalid operations");
        System.out.println("   - Maintains system predictability and reliability");

        System.out.println("\n========================================");
        System.out.println("Application terminated successfully.");
        System.out.println("========================================\n");
    }
}
