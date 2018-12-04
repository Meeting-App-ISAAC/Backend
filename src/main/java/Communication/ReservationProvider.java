package Communication;

import CalendarResource.Calender;
import CalendarResource.DummyCalender;
import Reservation.RoomCollection;
import Reservation.RoomMemory;
import Reservation.UserCollection;
import Reservation.UserProfiler;

public class ReservationProvider {

    // Singleton providing an up-to-date list of all reservations stored within the application

    private RoomCollection collection;
    private UserCollection userCollection;
    private static ReservationProvider reservationProvider = new ReservationProvider();

    public static ReservationProvider getInstance(){
        return reservationProvider;
    }

    private ReservationProvider(){
        Calender calender = new DummyCalender();
        this.collection = new RoomMemory(calender);
        this.userCollection = new UserProfiler(calender);
    }

    public RoomCollection getCollection(){
        return this.collection;
    }

    public UserCollection getUserCollection() { return this.userCollection; }
}
