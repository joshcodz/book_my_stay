/**
 * BookingValidationException - Custom Exception for Booking Validation Errors
 *
 * This exception represents validation failures in the booking system.
 * It provides specific error messages for different validation scenarios
 * and supports fail-fast design by stopping processing when invalid
 * input or system state is detected.
 *
 * Key Design Principles:
 * - Domain-specific exception for booking validation
 * - Clear, informative error messages
 * - Supports graceful failure handling
 * - Enables early error detection and prevention
 * - Maintains system stability during invalid operations
 *
 * Common validation scenarios covered:
 * - Invalid room types
 * - Invalid guest counts
 * - Invalid duration values
 * - Inventory availability violations
 * - System state inconsistencies
 *
 * @author Development Team
 * @version 9.0
 * @since 2026
 */
public class BookingValidationException extends Exception {

    /**
     * Error type enumeration for categorizing validation failures.
     */
    public enum ErrorType {
        INVALID_ROOM_TYPE,
        INVALID_GUEST_COUNT,
        INVALID_DURATION,
        INSUFFICIENT_INVENTORY,
        INVALID_EMAIL_FORMAT,
        INVALID_PHONE_FORMAT,
        SYSTEM_STATE_ERROR,
        NULL_OR_EMPTY_INPUT
    }

    private final ErrorType errorType;
    private final String fieldName;
    private final Object invalidValue;

    /**
     * Constructor for BookingValidationException.
     *
     * @param message Detailed error message
     * @param errorType Type of validation error
     * @param fieldName Name of the field that failed validation
     * @param invalidValue The invalid value that caused the error
     */
    public BookingValidationException(String message, ErrorType errorType, String fieldName, Object invalidValue) {
        super(message);
        this.errorType = errorType;
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }

    /**
     * Constructor for BookingValidationException without field details.
     *
     * @param message Detailed error message
     * @param errorType Type of validation error
     */
    public BookingValidationException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
        this.fieldName = null;
        this.invalidValue = null;
    }

    /**
     * Get the error type.
     *
     * @return The type of validation error
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * Get the field name that failed validation.
     *
     * @return Field name, or null if not applicable
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Get the invalid value that caused the error.
     *
     * @return Invalid value, or null if not applicable
     */
    public Object getInvalidValue() {
        return invalidValue;
    }

    /**
     * Get a user-friendly error message.
     *
     * @return Formatted error message for display
     */
    public String getUserFriendlyMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Booking Validation Error: ").append(getMessage());

        if (fieldName != null && invalidValue != null) {
            message.append(" (Field: ").append(fieldName)
                   .append(", Value: ").append(invalidValue).append(")");
        }

        return message.toString();
    }

    /**
     * Static factory method for invalid room type errors.
     */
    public static BookingValidationException invalidRoomType(String roomType) {
        return new BookingValidationException(
            "Invalid room type: '" + roomType + "'. Valid types: Single Room, Double Room, Suite Room",
            ErrorType.INVALID_ROOM_TYPE,
            "roomType",
            roomType
        );
    }

    /**
     * Static factory method for invalid guest count errors.
     */
    public static BookingValidationException invalidGuestCount(int guestCount, String roomType) {
        return new BookingValidationException(
            "Invalid guest count: " + guestCount + " for " + roomType +
            ". Single Room: max 1, Double Room: max 2, Suite Room: max 4",
            ErrorType.INVALID_GUEST_COUNT,
            "guestCount",
            guestCount
        );
    }

    /**
     * Static factory method for invalid duration errors.
     */
    public static BookingValidationException invalidDuration(int duration) {
        return new BookingValidationException(
            "Invalid duration: " + duration + ". Duration must be between 1 and 30 nights",
            ErrorType.INVALID_DURATION,
            "duration",
            duration
        );
    }

    /**
     * Static factory method for insufficient inventory errors.
     */
    public static BookingValidationException insufficientInventory(String roomType, int requested, int available) {
        return new BookingValidationException(
            "Insufficient inventory for " + roomType + ". Requested: " + requested +
            ", Available: " + available,
            ErrorType.INSUFFICIENT_INVENTORY,
            "inventory",
            requested
        );
    }

    /**
     * Static factory method for invalid email format errors.
     */
    public static BookingValidationException invalidEmailFormat(String email) {
        return new BookingValidationException(
            "Invalid email format: '" + email + "'. Email must contain '@' and '.'",
            ErrorType.INVALID_EMAIL_FORMAT,
            "email",
            email
        );
    }

    /**
     * Static factory method for invalid phone format errors.
     */
    public static BookingValidationException invalidPhoneFormat(String phone) {
        return new BookingValidationException(
            "Invalid phone format: '" + phone + "'. Phone must be 10 digits or XXX-XXXX format",
            ErrorType.INVALID_PHONE_FORMAT,
            "phone",
            phone
        );
    }

    /**
     * Static factory method for null or empty input errors.
     */
    public static BookingValidationException nullOrEmptyInput(String fieldName) {
        return new BookingValidationException(
            "Required field '" + fieldName + "' cannot be null or empty",
            ErrorType.NULL_OR_EMPTY_INPUT,
            fieldName,
            null
        );
    }

    /**
     * Static factory method for system state errors.
     */
    public static BookingValidationException systemStateError(String details) {
        return new BookingValidationException(
            "System state error: " + details,
            ErrorType.SYSTEM_STATE_ERROR
        );
    }
}
