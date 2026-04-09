/**
 * Reservation Class - Represents a hotel booking request.
 *
 * This class stores guest details and booking preferences for a
 * reservation. It is used by the concurrent booking processor,
 * validation service, and booking history.
 */
public class Reservation {

    private final String guestName;
    private final String email;
    private final String phone;
    private final String roomTypeRequested;
    private final int numberOfNights;
    private final int numberOfGuests;
    private String status;

    public Reservation(String guestName, String email, String phone,
                       String roomTypeRequested, int numberOfNights,
                       int numberOfGuests) {
        this.guestName = guestName;
        this.email = email;
        this.phone = phone;
        this.roomTypeRequested = roomTypeRequested;
        this.numberOfNights = numberOfNights;
        this.numberOfGuests = numberOfGuests;
        this.status = "PENDING";
    }

    public String getGuestName() {
        return guestName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRoomTypeRequested() {
        return roomTypeRequested;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return "#" + System.identityHashCode(this) + " - " + guestName + " (" + roomTypeRequested + " for " + numberOfNights + " nights) [" + status + "]";
    }

    @Override
    public String toString() {
        return "Reservation{" +
               "guestName='" + guestName + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", roomTypeRequested='" + roomTypeRequested + '\'' +
               ", numberOfNights=" + numberOfNights +
               ", numberOfGuests=" + numberOfGuests +
               ", status='" + status + '\'' +
               '}';
    }
}
