package CalendarResource;

import Reservation.Reservation;
import Reservation.Room;
import Reservation.User;

import java.util.List;

public interface Calender {

    /***
     * Creates a new event in the calender
     * @param reservation reservation object
     */
    void createNewEvent(Reservation reservation);

    void updateEvent(Reservation reservation);

    List<Room> getRooms();

    List<User> getUsers();
}
