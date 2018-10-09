package Communication.server;

import javax.websocket.Session;
import java.util.ArrayList;

public interface IMessageSender {
    void sendTo(String sessionId, Object object);
    void setSessions(ArrayList<Session> sessions);
}
