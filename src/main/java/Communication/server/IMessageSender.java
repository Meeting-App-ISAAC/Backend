package Communication.server;

import javax.websocket.Session;

interface IMessageSender {
    void sendTo(String sessionId, Object object);
    void sendReservationDump();
    void sendReservationDump(Session s);
}
