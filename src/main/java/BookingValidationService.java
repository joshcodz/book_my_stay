/**
 * BookingValidationService Class - Validates reservations before booking.
 *
 * This service performs fail-fast validation of reservation details and
 * ensures that the request is complete and that the inventory can satisfy it.
 */
public class BookingValidationService {

    public void validateReservation(Reservation reservation, RoomInventory inventory)
            throws BookingValidationException {
        if (reservation == null) {
            throw new BookingValidationException("Reservation request is null.");
        }

        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new BookingValidationException("Guest name is required.");
        }

        if (reservation.getEmail() == null || reservation.getEmail().trim().isEmpty()) {
            throw new BookingValidationException("Guest email is required.");
        }

        if (reservation.getPhone() == null || reservation.getPhone().trim().isEmpty()) {
            throw new BookingValidationException("Guest phone number is required.");
        }

        if (reservation.getNumberOfNights() <= 0) {
            throw new BookingValidationException("Number of nights must be at least 1.");
        }

        if (reservation.getNumberOfGuests() <= 0) {
            throw new BookingValidationException("Number of guests must be at least 1.");
        }

        String roomType = reservation.getRoomTypeRequested();
        if (!inventory.isRoomTypeValid(roomType)) {
            throw new BookingValidationException("Invalid room type requested: " + roomType);
        }

        int available = inventory.getAvailableRooms(roomType);
        if (available <= 0) {
            throw new BookingValidationException("Insufficient inventory for " + roomType + ". Requested: 1, Available: " + available);
        }

        if ("Single Room".equals(roomType) && reservation.getNumberOfGuests() > 1) {
            throw new BookingValidationException("Single Room can only accommodate 1 guest.");
        }

        if ("Double Room".equals(roomType) && reservation.getNumberOfGuests() > 2) {
            throw new BookingValidationException("Double Room can only accommodate up to 2 guests.");
        }

        if ("Suite Room".equals(roomType) && reservation.getNumberOfGuests() > 4) {
            throw new BookingValidationException("Suite Room can only accommodate up to 4 guests.");
        }
    }
}
