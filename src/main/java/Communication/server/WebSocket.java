package Communication.server;

import CalendarResource.Calender;
import CalendarResource.DummyCalender;
import Communication.ReservationProvider;
import Communication.SessionProvider;
import Communication.server.models.ReservationJavaScript;
import Reservation.Room;
import Reservation.RoomCollection;
import Reservation.RoomMemory;
import com.google.gson.Gson;
import shared.EncapsulatingMessageGenerator;
import shared.IEncapsulatingMessageGenerator;
import shared.messages.ReservationsRequest;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;

@ClientEndpoint
@ServerEndpoint(value="/reservation/")
public class WebSocket implements IWebSocket{
    private static ArrayList<RoomListener> listener = new ArrayList<>();
    private Gson gson = new Gson();
    private MessageToObjectServer messageToObjectServer;
    private IEncapsulatingMessageGenerator messageGenerator;

    private Calender calender;
    private RoomCollection rooms;
    private SessionProvider sessionProvider = SessionProvider.getInstance();


    public WebSocket() {
        messageGenerator = new EncapsulatingMessageGenerator();
        messageToObjectServer = new MessageToObjectServer();
        rooms = ReservationProvider.getInstance().getCollection();
    }

    @OnOpen
    public void onWebSocketConnect(Session session)
    {
        System.out.println("Socket Connected: " + session);

        listener.add(new RoomListener(session));
        sessionProvider.addSession(session);
    }

    @OnMessage
    public void onWebSocketText(String message, Session session)
    {
        ReservationsRequest roomId = gson.fromJson(message, ReservationsRequest.class);
        Room room = rooms.getRoom(roomId.getRoomId());
        addRoomToListener(session, room);

        sendToClient(session,gson.toJson(ReservationJavaScript.Convert( room.getReservations())));
    }


    private void addRoomToListener(Session session, Room room) {
        for (RoomListener roomListener : listener) {
            if(roomListener.getSession().equals(session)){
                roomListener.setRoom(room);
                return;
            }
        }
    }

    public void sendTo(String sessionId, Object object)
    {
        String msg = messageGenerator.generateMessageString(object);
        sendToClient(getSessionFromId(sessionId), msg);
    }

    public Session getSessionFromId(String sessionId)
    {
        for(RoomListener r : listener)
        {
            if(r.getSession().getId().equals(sessionId))
                return r.getSession();
        }
        return null;
    }


    public void broadcast(Object object)
    {
        for(RoomListener r : listener) {
            sendTo(r.getSession().getId(), object);
        }
    }

    private void sendToClient(Session session, String message)
    {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("Socket Closed: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }
}
