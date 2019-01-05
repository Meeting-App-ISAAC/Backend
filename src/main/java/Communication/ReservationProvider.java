package Communication;

import CalendarResource.Calender;
import CalendarResource.DummyCalender;
import CalendarResource.ExchangeCalendar;
import Reservation.*;
import RoomConfiguration.ReadRoomConfig;

public class ReservationProvider {

    // Singleton providing an up-to-date list of all reservations stored within the application

    private RoomCollection collection;
    private UserCollection userCollection;
    private Calender calender;
    private static ReservationProvider reservationProvider = new ReservationProvider();

    public static ReservationProvider getInstance(){
        return reservationProvider;
    }

    private ReservationProvider(){
        calender = new ExchangeCalendar(new ReadRoomConfig());
        this.collection = new RoomMemory(calender);
        this.userCollection = new UserProfiler(calender);
    }

    public void Reload(){
        calender.Reload();
    }

    public void addNewEvent(){
    }
    public RoomCollection getCollection(){
        return this.collection;
    }

    public UserCollection getUserCollection() { return this.userCollection; }
}
