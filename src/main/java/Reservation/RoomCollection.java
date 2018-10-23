package Reservation;

import java.util.ArrayList;
import java.util.List;

public interface RoomCollection {

    public Room getRoom(int room);

    public List<Room> getAllRooms();
}
