package Reservation;

import CalendarResource.Calender;
import Settings.SettingsHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class RoomMemory implements RoomCollection, Observer {

    private List<Room> rooms = new ArrayList<>();
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
           case StoppedMeeting:     if(stopEnabled()) {
                                    calender.updateEvent((Reservation) changedObject.getArg());
                                    }
                                    break;

           case StartedMeeting:     if(startEnabled()) {
                                    calender.updateEvent((Reservation) changedObject.getArg());
                                    }
                                    break;

           case AddedReservation:   calender.createNewEvent((Reservation) changedObject.getArg());
                                    break;

           case ExtendedMeeting:    calender.updateEvent((Reservation) changedObject.getArg());
                                    break;
       }
    }

    private boolean startEnabled() {
        try {
            return Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_START_ENABLED"));
        }
        catch (IOException e) {
            System.out.println("[error] Could net get properties file");
            return true;
        }
    }

    private boolean stopEnabled() {
        try {
            return Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_STOP_ENABLED"));
        }
        catch (IOException e) {
            System.out.println("[error] Could net get properties file");
            return true;
        }
    }

}
