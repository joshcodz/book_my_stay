/**
 * CancellationResult Class - Result of Booking Cancellation Operations
 *
 * This class encapsulates the result of a booking cancellation operation,
 * providing a clean way to communicate success/failure status along with
 * relevant details and data.
 *
 * Key Design Principles:
 * - Immutable result object for thread safety
 * - Clear success/failure indication
 * - Detailed error messages for failure cases
 * - Associated data for successful operations
 * - Type safety with generic result handling
 *
 * The result pattern allows calling code to handle cancellation outcomes
 * gracefully without throwing exceptions for expected failure scenarios.
 *
 * @author Development Team
 * @version 10.0
 * @since 2026
 */
public class CancellationResult {

    private final boolean success;
    private final String message;
    private final Reservation cancelledReservation;
    private final String errorDetails;

    /**
     * Private constructor - use factory methods instead.
     */
    private CancellationResult(boolean success, String message,
                              Reservation cancelledReservation, String errorDetails) {
        this.success = success;
        this.message = message;
        this.cancelledReservation = cancelledReservation;
        this.errorDetails = errorDetails;
    }

    /**
     * Factory method for successful cancellation results.
     *
     * @param message Success message
     * @param cancelledReservation The reservation that was cancelled
     * @return CancellationResult indicating success
     */
    public static CancellationResult success(String message, Reservation cancelledReservation) {
        return new CancellationResult(true, message, cancelledReservation, null);
    }

    /**
     * Factory method for failed cancellation results.
     *
     * @param errorMessage Error message describing the failure
     * @return CancellationResult indicating failure
     */
    public static CancellationResult failure(String errorMessage) {
        return new CancellationResult(false, errorMessage, null, errorMessage);
    }

    /**
     * Check if the cancellation was successful.
     *
     * @return true if cancellation succeeded, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Check if the cancellation failed.
     *
     * @return true if cancellation failed, false otherwise
     */
    public boolean isFailure() {
        return !success;
    }

    /**
     * Get the result message.
     *
     * @return Success message or error description
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the cancelled reservation (only available for successful operations).
     *
     * @return The cancelled reservation, or null if operation failed
     */
    public Reservation getCancelledReservation() {
        return cancelledReservation;
    }

    /**
     * Get error details (only available for failed operations).
     *
     * @return Error details, or null if operation succeeded
     */
    public String getErrorDetails() {
        return errorDetails;
    }

    /**
     * Get a formatted display string for the result.
     *
     * @return User-friendly result description
     */
    public String getDisplayString() {
        if (success) {
            return "[SUCCESS] " + message;
        } else {
            return "[FAILED] " + message;
        }
    }

    /**
     * Display the result to console.
     */
    public void display() {
        System.out.println(getDisplayString());
        if (success && cancelledReservation != null) {
            System.out.println("Cancelled Reservation Details:");
            cancelledReservation.displayDetails();
        }
    }

    @Override
    public String toString() {
        return "CancellationResult{" +
               "success=" + success +
               ", message='" + message + '\'' +
               ", hasReservation=" + (cancelledReservation != null) +
               '}';
    }
}
