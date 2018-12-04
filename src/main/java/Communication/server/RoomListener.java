package Communication.server;

import Reservation.Room;
import javax.websocket.Session;
import java.util.Observable;
import java.util.Observer;

class RoomListener implements Observer {

    // Listens for changes in a room

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
