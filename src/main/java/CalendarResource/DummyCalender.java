package CalendarResource;

import Reservation.Reservation;
import Reservation.Room;
import Reservation.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DummyCalender implements Calender {

    @Override
    public void createNewEvent(Reservation reservation) {

    }

    @Override
    public void updateEvent(Reservation reservation){
        System.out.println(reservation.getEnd());
    }

    @Override
    public List<Room> getRooms() {
        User user = new User(1, "Alex");
        //Reservation reservation = new Reservation(1, user, false,  LocalDateTime.now().withSecond(0).plusMinutes(31), LocalDateTime.now().withSecond(0).plusMinutes(60));
        Reservation reservation1 = new Reservation(1, user, false, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        //reservations.add(reservation);
        reservations.add(reservation1);
        ArrayList<Room> rooms = new ArrayList<Room>();
        Room room = new Room(1, "DummyRoom", reservations);
        Room room1 = new Room(2, "Carlo's kamer", new ArrayList<>());
        rooms.add(room);
        rooms.add(room1);
        return rooms;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "Alex"));
        users.add(new User(2, "Carlo"));
        users.add(new User(3, "Joeri"));
        users.add(new User(4, "Yorick"));
        users.add(new User(5, "Tobias"));

        return users;
    }
}
