package Reservation;

import CalendarResource.Calender;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

class UserProfilerTest {

    List<User> users = new ArrayList<>();
    User user1 = new User();
    User user2 = new User();
    User user3 = new User();
    List<Room> rooms = new ArrayList<>();
    User user = new User();
    Calender calender = null;
    UserProfiler userProfiler = null;

    @BeforeEach
    void setUp(){
        List<Reservation> reservations = new ArrayList<>();
        user1 = new User(1, "User1");
        user2 = new User(2, "User2");
        user3 = new User(3, "User3");
        users.add(user1);
        users.add(user2);
        users.add(user3);

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

            @Override
            public List<User> getUsers() { return users; }
        };

        userProfiler = new UserProfiler(calender);
    }

    @Test
    void getUserById() {

        // Assert if roomMemory returns the room
        Assert.assertEquals(user1, userProfiler.getUserById(1));
        Assert.assertEquals(user2, userProfiler.getUserById(2));
        Assert.assertEquals(user3, userProfiler.getUserById(3));

        // If the room does not exist roomMemory should return a null
        Assert.assertEquals(null, userProfiler.getUserById(4));
    }

    @Test
    void getUsers() {

        // Assert if getAllRooms returns all rooms from roomMemory
        Assert.assertEquals(users, userProfiler.getUsers());
    }

    @Test
    void calenderRoomsSize() {
        Assert.assertEquals(users.size(), calender.getUsers().size());
    }

    @Test
    void update() {
    }
}