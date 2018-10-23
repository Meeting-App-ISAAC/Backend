package Reservation;

import java.util.ArrayList;
import java.util.List;

public interface RoomCollection {

    /***
     * Gets one specific room
     * @param room room number
     */
    Room getRoom(int room);

    public List<Reservation> getAllReservations();

    /***
     * Get a list of all rooms
     */
    List<Room> getAllRooms();
}
