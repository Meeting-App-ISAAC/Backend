package Communication.server;

import shared.EncapsulatingMessageGenerator;
import shared.IEncapsulatingMessageGenerator;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;

public class MessageSender implements  IMessageSender{
    private IEncapsulatingMessageGenerator messageGenerator;
    private static ArrayList<Session> sessions = new ArrayList<>();

    public MessageSender() {
        messageGenerator = new EncapsulatingMessageGenerator();
    }

    public void setSessions(ArrayList<Session> sessions) {
        MessageSender.sessions = sessions;
    }

    public void sendTo(String sessionId, Object object)
    {
        String msg = messageGenerator.generateMessageString(object);
        sendToClient(getSessionFromId(sessionId), msg);
    }

    public Session getSessionFromId(String sessionId)
    {
        for(Session s : sessions)
        {
            if(s.getId().equals(sessionId))
                return s;
        }
        return null;
    }

    private void sendToClient(Session session, String message)
    {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
