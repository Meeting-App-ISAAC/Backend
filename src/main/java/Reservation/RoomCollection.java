package Reservation;

import java.util.List;

public interface RoomCollection {

    /***
     * Gets one specific room
     * @param room room number
     */
    Room getRoom(int room);

    /***
     * Get a list of all rooms
     */
    List<Room> getAllRooms();
}
