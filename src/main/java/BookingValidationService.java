import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * BookingValidationService - Input Validation and System State Checking
 *
 * This service provides comprehensive validation for booking operations,
 * implementing fail-fast design by detecting errors early and preventing
 * invalid data from corrupting system state. It validates both input data
 * and system constraints before allowing booking operations to proceed.
 *
 * Key Design Principles:
 * - Input validation before processing
 * - System state guarding
 * - Fail-fast error detection
 * - Clear error messaging
 * - Prevention of invalid state changes
 * - Graceful failure handling
 *
 * Validation Categories:
 * - Input format validation (email, phone, required fields)
 * - Business rule validation (guest counts, duration limits)
 * - System constraint validation (inventory availability)
 * - Data consistency validation (room type validity)
 *
 * @author Development Team
 * @version 9.0
 * @since 2026
 */
public class BookingValidationService {

    // Valid room types
    private static final Set<String> VALID_ROOM_TYPES = new HashSet<>(Arrays.asList(
        "Single Room", "Double Room", "Suite Room"
    ));

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Phone validation patterns (supports XXX-XXXX or 10 digits)
    private static final Pattern PHONE_PATTERN_DASH =
        Pattern.compile("^\\d{3}-\\d{4}$");
    private static final Pattern PHONE_PATTERN_DIGITS =
        Pattern.compile("^\\d{10}$");

    // Business rules
    private static final int MIN_DURATION = 1;
    private static final int MAX_DURATION = 30;
    private static final int MAX_GUESTS_SINGLE = 1;
    private static final int MAX_GUESTS_DOUBLE = 2;
    private static final int MAX_GUESTS_SUITE = 4;

    /**
     * Validate a complete reservation request.
     *
     * This method performs comprehensive validation of all reservation fields
     * and checks system constraints before allowing the booking to proceed.
     *
     * @param reservation The reservation to validate
     * @param roomInventory The room inventory to check availability
     * @throws BookingValidationException if validation fails
     */
    public void validateReservation(Reservation reservation, RoomInventory roomInventory)
            throws BookingValidationException {

        // Validate required fields are not null or empty
        validateRequiredFields(reservation);

        // Validate input formats
        validateEmailFormat(reservation.getGuestEmail());
        validatePhoneFormat(reservation.getGuestPhone());

        // Validate business rules
        validateRoomType(reservation.getRoomTypeRequested());
        validateGuestCount(reservation.getNumberOfGuests(), reservation.getRoomTypeRequested());
        validateDuration(reservation.getNumberOfNights());

        // Validate system constraints
        validateInventoryAvailability(reservation.getRoomTypeRequested(), roomInventory);
    }

    /**
     * Validate that required fields are not null or empty.
     *
     * @param reservation The reservation to check
     * @throws BookingValidationException if required fields are missing
     */
    public void validateRequiredFields(Reservation reservation) throws BookingValidationException {
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw BookingValidationException.nullOrEmptyInput("guestName");
        }

        if (reservation.getGuestEmail() == null || reservation.getGuestEmail().trim().isEmpty()) {
            throw BookingValidationException.nullOrEmptyInput("email");
        }

        if (reservation.getGuestPhone() == null || reservation.getGuestPhone().trim().isEmpty()) {
            throw BookingValidationException.nullOrEmptyInput("phone");
        }

        if (reservation.getRoomTypeRequested() == null || reservation.getRoomTypeRequested().trim().isEmpty()) {
            throw BookingValidationException.nullOrEmptyInput("roomTypeRequested");
        }
    }

    /**
     * Validate room type against allowed values.
     *
     * @param roomType The room type to validate
     * @throws BookingValidationException if room type is invalid
     */
    public void validateRoomType(String roomType) throws BookingValidationException {
        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw BookingValidationException.invalidRoomType(roomType);
        }
    }

    /**
     * Validate guest count against room capacity limits.
     *
     * @param guestCount Number of guests
     * @param roomType Room type for capacity checking
     * @throws BookingValidationException if guest count exceeds room capacity
     */
    public void validateGuestCount(int guestCount, String roomType) throws BookingValidationException {
        if (guestCount <= 0) {
            throw BookingValidationException.invalidGuestCount(guestCount, roomType);
        }

        int maxGuests;
        switch (roomType) {
            case "Single Room":
                maxGuests = MAX_GUESTS_SINGLE;
                break;
            case "Double Room":
                maxGuests = MAX_GUESTS_DOUBLE;
                break;
            case "Suite Room":
                maxGuests = MAX_GUESTS_SUITE;
                break;
            default:
                throw BookingValidationException.invalidRoomType(roomType);
        }

        if (guestCount > maxGuests) {
            throw BookingValidationException.invalidGuestCount(guestCount, roomType);
        }
    }

    /**
     * Validate booking duration against business rules.
     *
     * @param duration Number of nights
     * @throws BookingValidationException if duration is invalid
     */
    public void validateDuration(int duration) throws BookingValidationException {
        if (duration < MIN_DURATION || duration > MAX_DURATION) {
            throw BookingValidationException.invalidDuration(duration);
        }
    }

    /**
     * Validate email format using regex pattern.
     *
     * @param email Email address to validate
     * @throws BookingValidationException if email format is invalid
     */
    public void validateEmailFormat(String email) throws BookingValidationException {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw BookingValidationException.invalidEmailFormat(email);
        }
    }

    /**
     * Validate phone format (supports XXX-XXXX or 10 digits).
     *
     * @param phone Phone number to validate
     * @throws BookingValidationException if phone format is invalid
     */
    public void validatePhoneFormat(String phone) throws BookingValidationException {
        if (!PHONE_PATTERN_DASH.matcher(phone).matches() &&
            !PHONE_PATTERN_DIGITS.matcher(phone).matches()) {
            throw BookingValidationException.invalidPhoneFormat(phone);
        }
    }

    /**
     * Validate that sufficient inventory is available for the requested room type.
     *
     * @param roomType Room type to check
     * @param roomInventory Inventory to check against
     * @throws BookingValidationException if insufficient inventory
     */
    public void validateInventoryAvailability(String roomType, RoomInventory roomInventory)
            throws BookingValidationException {

        int available = roomInventory.getAvailability(roomType);
        if (available <= 0) {
            throw BookingValidationException.insufficientInventory(roomType, 1, available);
        }
    }

    /**
     * Validate that inventory won't go negative after booking.
     *
     * This method guards against system state corruption by ensuring
     * that booking operations won't result in negative inventory values.
     *
     * @param roomType Room type being booked
     * @param roomInventory Inventory to validate
     * @throws BookingValidationException if booking would cause invalid state
     */
    public void validateInventoryStateAfterBooking(String roomType, RoomInventory roomInventory)
            throws BookingValidationException {

        int currentAvailable = roomInventory.getAvailability(roomType);
        if (currentAvailable <= 0) {
            throw BookingValidationException.systemStateError(
                "Cannot book " + roomType + " - no rooms available (current: " + currentAvailable + ")");
        }

        // Simulate the booking to check final state
        int afterBooking = currentAvailable - 1;
        if (afterBooking < 0) {
            throw BookingValidationException.systemStateError(
                "Booking would result in negative inventory for " + roomType +
                " (current: " + currentAvailable + ", after booking: " + afterBooking + ")");
        }
    }

    /**
     * Get the maximum guest capacity for a room type.
     *
     * @param roomType Room type
     * @return Maximum number of guests, or -1 if invalid room type
     */
    public int getMaxGuestsForRoomType(String roomType) {
        switch (roomType) {
            case "Single Room": return MAX_GUESTS_SINGLE;
            case "Double Room": return MAX_GUESTS_DOUBLE;
            case "Suite Room": return MAX_GUESTS_SUITE;
            default: return -1;
        }
    }

    /**
     * Check if a room type is valid.
     *
     * @param roomType Room type to check
     * @return true if valid, false otherwise
     */
    public boolean isValidRoomType(String roomType) {
        return VALID_ROOM_TYPES.contains(roomType);
    }

    /**
     * Get all valid room types.
     *
     * @return Set of valid room types
     */
    public Set<String> getValidRoomTypes() {
        return new HashSet<>(VALID_ROOM_TYPES);
    }

    /**
     * Get duration limits.
     *
     * @return Array with [min, max] duration values
     */
    public int[] getDurationLimits() {
        return new int[]{MIN_DURATION, MAX_DURATION};
    }
}
