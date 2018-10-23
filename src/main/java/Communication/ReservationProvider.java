package Communication;

import CalendarResource.Calender;
import CalendarResource.DummyCalender;
import Reservation.RoomCollection;
import Reservation.RoomMemory;

public class ReservationProvider {

    private RoomCollection collection;
    private static ReservationProvider reservationProvider = new ReservationProvider();


    public static ReservationProvider getInstance(){
        return reservationProvider;
    }

    private ReservationProvider(){
        Calender calender = new DummyCalender();
        this.collection = new RoomMemory(calender);
    }
    public RoomCollection getCollection(){
        return this.collection;
    }
}
