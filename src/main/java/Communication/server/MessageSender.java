package Communication.server;

import Communication.server.models.ReservationJavaScript;
import Reservation.Reservation;
import com.google.gson.Gson;
import shared.EncapsulatingMessageGenerator;
import shared.IEncapsulatingMessageGenerator;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public void sendReservationDump(Session session, ArrayList<Reservation> reservations){
        Gson gson = new Gson();
        this.sendToClient(session, gson.toJson(ReservationJavaScript.Convert( reservations)));
    }

    public void broadcast(Object object)
    {
        for(Session s : sessions) {
            sendTo(s.getId(), object);
        }
    }
}
