package Reservation;

import CalendarResource.Calender;

import java.util.ArrayList;
import java.util.List;

public class RoomMemory implements RoomCollection {

    List<Room> rooms = new ArrayList<>();
    private  Calender calender;

    public RoomMemory(Calender calender){
        this.calender = calender;
    }

    @Override
    public void getRoom(int room) {

    }

    @Override
    public List<Room> getAllRooms() {
        return rooms;
    }
}
