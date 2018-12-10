package Communication;

import CalendarResource.Calender;
import CalendarResource.DummyCalender;
import Reservation.RoomCollection;
import Reservation.RoomMemory;
import Reservation.UserCollection;
import Reservation.UserProfiler;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationProviderTest {

    private RoomCollection collection;
    private UserCollection userCollection;
    private Calender calender = new DummyCalender();

    @BeforeEach
    void setUp() {
        this.collection = new RoomMemory(calender);
        this.userCollection = new UserProfiler(calender);
    }

    @Test
    void getInstance() {
    }

    @Test
    void getCollection() {
        Assert.assertEquals(collection.getAllRooms().size(), calender.getRooms().size());
    }
}