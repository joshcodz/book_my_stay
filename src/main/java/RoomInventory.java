import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory Class - Manages room availability for the hotel.
 *
 * This class provides synchronized inventory access so that concurrent
 * booking threads can safely check and book rooms without causing
 * inconsistent counts.
 */
public class RoomInventory {

    private final Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public synchronized boolean bookRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available <= 0) {
            return false;
        }
        inventory.put(roomType, available - 1);
        return true;
    }

    public synchronized int getAvailableRooms(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public synchronized int getTotalAvailableRooms() {
        int total = 0;
        for (int count : inventory.values()) {
            total += count;
        }
        return total;
    }

    public synchronized boolean isRoomTypeValid(String roomType) {
        return inventory.containsKey(roomType);
    }

    public synchronized void displayInventory() {
        System.out.println("  Single Room available: " + getAvailableRooms("Single Room"));
        System.out.println("  Double Room available: " + getAvailableRooms("Double Room"));
        System.out.println("  Suite Room available: " + getAvailableRooms("Suite Room"));
    }
}
