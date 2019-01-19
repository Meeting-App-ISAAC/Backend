package CalendarResource;

import Reservation.Reservation;
import Reservation.Room;
import Reservation.User;
import RoomConfiguration.ReadRoomConfig;
import RoomConfiguration.RoomDataModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DummyCalender implements Calender {

    @Override
    public void createNewEvent(Reservation reservation) {

    }

    @Override
    public void Reload() {

    }

    @Override
    public void Reset() {

    }

    @Override
    public void updateEvent(Reservation reservation){
        System.out.println(reservation.getEnd());
    }

    @Override
    public List<Room> getRooms() {

        ReadRoomConfig readRoomConfig = new ReadRoomConfig();
        ArrayList<Room> rooms = new ArrayList<Room>();
        List<User> users = getUsers();
        int idcounter = 1;

        for(RoomDataModel roomData : readRoomConfig.GetRoomData()){
            Random random = new Random();
            User user = users.get(random.nextInt(4));
            int start = 31;
            Reservation reservation = new Reservation(idcounter++, user, false,  LocalDateTime.now().plusMinutes(start), LocalDateTime.now().plusMinutes(start + random.nextInt(120)));

            ArrayList<Reservation> reservations = new ArrayList<Reservation>();
            reservations.add(reservation);
            Room room = new Room(roomData.getId(), roomData.getName(), reservations);
            rooms.add(room);
        }
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
