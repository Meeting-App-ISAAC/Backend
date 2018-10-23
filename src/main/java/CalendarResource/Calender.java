package CalendarResource;

import Reservation.Reservation;
import Reservation.Room;

import java.util.List;

public interface Calender {

    /***
     * Creates a new event in the calender
     * @param reservation reservation object
     */
    public void createNewEvent(Reservation reservation);

    public void updateEvent(Reservation reservation);

    public List<Room> getRooms();
}
