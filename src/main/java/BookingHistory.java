import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BookingHistory Class - Tracks confirmed bookings and revenue.
 *
 * This class keeps a thread-safe history of confirmed bookings so the
 * simulation can verify inventory consistency and calculate totals.
 */
public class BookingHistory {

    private final List<Reservation> confirmedBookings;
    private double totalRevenue;

    public BookingHistory() {
        this.confirmedBookings = Collections.synchronizedList(new ArrayList<>());
        this.totalRevenue = 0.0;
    }

    public void addConfirmedBooking(Reservation reservation, double roomPrice, double extraCharges) {
        reservation.setStatus("CONFIRMED");
        confirmedBookings.add(reservation);
        synchronized (this) {
            totalRevenue += roomPrice * reservation.getNumberOfNights() + extraCharges;
        }
    }

    public int getTotalBookings() {
        return confirmedBookings.size();
    }

    public double getTotalRevenue() {
        synchronized (this) {
            return totalRevenue;
        }
    }

    public List<Reservation> getConfirmedBookings() {
        return new ArrayList<>(confirmedBookings);
    }
}
