package Reservation;

import CalendarResource.Calender;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class RoomMemory implements RoomCollection, Observer {

    List<Room> rooms = new ArrayList<>();
    private  Calender calender;

    public RoomMemory(Calender calender){
        this.calender = calender;
        rooms = calender.getRooms();
        for (Room room: rooms){
            room.addObserver(this);
        }
    }

    @Override
    public Room getRoom(int room) {
        return rooms.get(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return rooms;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
