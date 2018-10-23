package CalendarResource;

import Reservation.Reservation;
import Reservation.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DummyCalender implements Calender {

    @Override
    public void createNewEvent(Reservation reservation) {

    }

    @Override
    public void endEvent(Reservation reservation){
        reservation.setEnd(LocalDateTime.now());
    }

    @Override
    public List<Room> getRooms() {
        return new ArrayList<>();
    }
}
