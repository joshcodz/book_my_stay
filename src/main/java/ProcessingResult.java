/**
 * ProcessingResult Class - Result of Concurrent Booking Request Processing
 *
 * This class encapsulates the result of processing a booking request in a
 * multi-threaded environment. It provides detailed information about the
 * processing outcome while maintaining thread safety.
 *
 * Key Design Principles:
 * - Immutable result object for thread safety
 * - Clear processing outcome indication
 * - Detailed error messages for different failure types
 * - Associated reservation data for successful operations
 * - Type safety with specific result types
 *
 * The result pattern allows calling threads to handle processing outcomes
 * appropriately without throwing exceptions in concurrent scenarios.
 *
 * @author Development Team
 * @version 11.0
 * @since 2026
 */
public class ProcessingResult {

    public enum ResultType {
        SUCCESS,
        FAILURE,
        VALIDATION_FAILURE,
        NO_REQUEST_AVAILABLE
    }

    private final ResultType resultType;
    private final Reservation reservation;
    private final String message;
    private final String threadName;
    private final long processingTime;

    /**
     * Private constructor - use factory methods instead.
     */
    private ProcessingResult(ResultType resultType, Reservation reservation,
                           String message, String threadName, long processingTime) {
        this.resultType = resultType;
        this.reservation = reservation;
        this.message = message;
        this.threadName = threadName;
        this.processingTime = processingTime;
    }

    /**
     * Factory method for successful processing results.
     *
     * @param reservation The successfully processed reservation
     * @param message Success message
     * @return ProcessingResult indicating success
     */
    public static ProcessingResult success(Reservation reservation, String message) {
        return new ProcessingResult(ResultType.SUCCESS, reservation, message,
                                  Thread.currentThread().getName(), System.currentTimeMillis());
    }

    /**
     * Factory method for failed processing results (e.g., room not available).
     *
     * @param reservation The reservation that failed processing
     * @param message Failure message
     * @return ProcessingResult indicating failure
     */
    public static ProcessingResult failure(Reservation reservation, String message) {
        return new ProcessingResult(ResultType.FAILURE, reservation, message,
                                  Thread.currentThread().getName(), System.currentTimeMillis());
    }

    /**
     * Factory method for validation failure results.
     *
     * @param reservation The reservation that failed validation
     * @param validationMessage Validation error message
     * @return ProcessingResult indicating validation failure
     */
    public static ProcessingResult validationFailure(Reservation reservation, String validationMessage) {
        return new ProcessingResult(ResultType.VALIDATION_FAILURE, reservation, validationMessage,
                                  Thread.currentThread().getName(), System.currentTimeMillis());
    }

    /**
     * Factory method for no request available results.
     *
     * @return ProcessingResult indicating no requests to process
     */
    public static ProcessingResult noRequestAvailable() {
        return new ProcessingResult(ResultType.NO_REQUEST_AVAILABLE, null, "No requests available",
                                  Thread.currentThread().getName(), System.currentTimeMillis());
    }

    /**
     * Get the result type.
     *
     * @return The type of processing result
     */
    public ResultType getResultType() {
        return resultType;
    }

    /**
     * Check if processing was successful.
     *
     * @return true if the request was processed successfully
     */
    public boolean isSuccess() {
        return resultType == ResultType.SUCCESS;
    }

    /**
     * Check if processing failed.
     *
     * @return true if processing failed for any reason
     */
    public boolean isFailure() {
        return resultType == ResultType.FAILURE || resultType == ResultType.VALIDATION_FAILURE;
    }

    /**
     * Check if no request was available for processing.
     *
     * @return true if no requests were available
     */
    public boolean isNoRequestAvailable() {
        return resultType == ResultType.NO_REQUEST_AVAILABLE;
    }

    /**
     * Get the processed reservation.
     *
     * @return The reservation that was processed, or null if no request available
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * Get the result message.
     *
     * @return Detailed message about the processing result
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the name of the thread that performed the processing.
     *
     * @return Thread name for debugging concurrent operations
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * Get the processing timestamp.
     *
     * @return Timestamp when processing completed
     */
    public long getProcessingTime() {
        return processingTime;
    }

    /**
     * Get a formatted display string for the result.
     *
     * @return User-friendly result description
     */
    public String getDisplayString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(threadName).append("] ");

        switch (resultType) {
            case SUCCESS:
                sb.append("✓ SUCCESS: ").append(message);
                break;
            case FAILURE:
                sb.append("✗ FAILURE: ").append(message);
                break;
            case VALIDATION_FAILURE:
                sb.append("⚠ VALIDATION: ").append(message);
                break;
            case NO_REQUEST_AVAILABLE:
                sb.append("○ NO REQUEST: ").append(message);
                break;
        }

        if (reservation != null) {
            sb.append(" - ").append(reservation.getSummary());
        }

        return sb.toString();
    }

    /**
     * Display the result to console.
     */
    public void display() {
        System.out.println(getDisplayString());
    }

    @Override
    public String toString() {
        return "ProcessingResult{" +
               "resultType=" + resultType +
               ", threadName='" + threadName + '\'' +
               ", message='" + message + '\'' +
               ", hasReservation=" + (reservation != null) +
               '}';
    }
}
