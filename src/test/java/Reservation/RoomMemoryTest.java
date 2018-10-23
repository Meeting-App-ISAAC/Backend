package Reservation;

import CalendarResource.Calender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomMemoryTest {

    List<Room> rooms = new ArrayList<>();
    Room room1 = new Room();
    Room room2 = new Room();
    Room room3 = new Room();
    Calender calender = null;
    RoomMemory roomMemory = null;

    @BeforeEach
    void setup(){
        List<Reservation> reservations = new ArrayList<>();

        room1.setId(1);
        room1.setName("TestRoom1");
        room2.setId(2);
        room2.setName("TestRoom2");
        room3.setId(3);
        room3.setName("TestRoom3");

        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);

        calender = new Calender() {
            @Override
            public void createNewEvent(Reservation reservation) {

            }

            @Override
            public void updateEvent(Reservation reservation) {

            }

            @Override
            public List<Room> getRooms() {
                return rooms;
            }
        };

        roomMemory = new RoomMemory(calender);
    }

    @Test
    void getRoom() {

        // Assert if roomMemory returns the room
        Assert.assertEquals(room1, roomMemory.getRoom(1));
        Assert.assertEquals(room2, roomMemory.getRoom(2));
        Assert.assertEquals(room3, roomMemory.getRoom(3));

        // If the room does not exist roomMemory should return a null
        Assert.assertEquals(null, roomMemory.getRoom(4));
    }

    @Test
    void getAllRooms() {

        // Assert if getAllRooms returns all rooms from roomMemory
        Assert.assertEquals(rooms, roomMemory.getAllRooms());
    }

    @Test
    void update() {
    }
}