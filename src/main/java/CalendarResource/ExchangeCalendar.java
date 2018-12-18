package CalendarResource;

import Reservation.Reservation;
import Reservation.Room;
import Reservation.User;
import RoomConfiguration.RoomConfig;

import java.util.List;

public class ExchangeCalendar implements Calender {


    private RoomConfig config;

    public ExchangeCalendar(RoomConfig config) {
        this.config = config;
    }

    @Override
    public void createNewEvent(Reservation reservation) {

    }

    @Override
    public List<Room> getRooms() {
        return null;
    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public void updateEvent(Reservation reservation) {

    }
}
