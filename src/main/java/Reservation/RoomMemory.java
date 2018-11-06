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
        for (Room roomToFind : getAllRooms()){
            if (room ==  roomToFind.getId()){
                return roomToFind;
            }
        }
        return null;
    }

    @Override
    public List<Room> getAllRooms() {
        return rooms;
    }

    @Override
    public void update(Observable o, Object arg) {
       Room changed = (Room) o;
        ChangedObject changedObject = (ChangedObject) arg;

       switch(changedObject.getChanged()) {
           case StoppedMeeting:     calender.updateEvent((Reservation) changedObject.getArg());

                                    break;

           case StartedMeeting:     calender.updateEvent((Reservation) changedObject.getArg());
                                    break;

           case AddedReservation:   calender.createNewEvent((Reservation) changedObject.getArg());
                                    break;

           case ExtendedMeeting:    calender.updateEvent((Reservation) changedObject.getArg());
                                    break;
       }
    }
}
