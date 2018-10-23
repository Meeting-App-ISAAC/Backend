package CalendarResource;

import Reservation.Reservation;

import java.util.List;

public interface Calender {

    /***
     * Creates a new event in the calender
     * @param reservation reservation object
     */
    public void createNewEvent(Reservation reservation);

    public void endEvent(Reservation reservation);

    public List<Reservation.Room> getRooms();
}
