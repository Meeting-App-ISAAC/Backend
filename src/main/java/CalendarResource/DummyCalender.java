package CalendarResource;

import Reservation.Reservation;
import Reservation.Room;

import java.util.ArrayList;
import java.util.List;

public class DummyCalender implements Calender {

    @Override
    public void createNewEvent(Reservation reservation) {

    }

    @Override
    public List<Room> getRooms() {
        return new ArrayList<>();
    }
}
