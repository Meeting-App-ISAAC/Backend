package Communication.server;

import Reservation.Room;
import javax.websocket.Session;
import java.util.Observable;
import java.util.Observer;

public class RoomListener implements Observer {

    private IMessageSender messageSender = new MessageSender();

    private Room room;
    private Session session;

    public RoomListener() {
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        this.room.addObserver(this);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.messageSender.sendReservationDump();
    }
}
