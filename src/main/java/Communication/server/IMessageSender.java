package Communication.server;

import Reservation.Reservation;

import javax.websocket.Session;
import java.util.ArrayList;

public interface IMessageSender {
    void sendTo(String sessionId, Object object);
    void sendReservationDump();
    void sendReservationDump(Session s);
}
