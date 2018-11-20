package Communication.server;

import CalendarResource.Calender;
import CalendarResource.DummyCalender;
import Communication.ReservationProvider;
import Communication.SessionProvider;
import Communication.server.models.ReservationJavaScript;
import Reservation.Room;
import Reservation.RoomCollection;
import Reservation.RoomMemory;
import Settings.FrontendSettings;
import Settings.SettingsHandler;
import com.google.gson.Gson;
import org.eclipse.persistence.sessions.factories.SessionManager;
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
    private Session websocketSession;

    private Calender calender;
    private RoomCollection rooms;
    private SessionProvider sessionProvider = SessionProvider.getInstance();

    private SettingsListener settingsListener;

    public WebSocket() {
        messageGenerator = new EncapsulatingMessageGenerator();
        messageToObjectServer = new MessageToObjectServer();
        rooms = ReservationProvider.getInstance().getCollection();
        settingsListener = new SettingsListener();
        SettingsHandler.Instance().addObserver(settingsListener);
    }

    @OnOpen
    public void onWebSocketConnect(Session session)
    {
        System.out.println("Socket Connected: " + session);

        sessionProvider.addSession(session);
        websocketSession = session;
        IMessageSender message = new MessageSender();
        message.sendReservationDump(session);
        FrontendSettings frontendSettings = new FrontendSettings();
        sendTo(session.getId(),frontendSettings);
    }

    @OnMessage
    public void onWebSocketText(String message, Session session)
    {
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

        for(Session s : sessionProvider.getSessions())
        {
            if(s.getId().equals(sessionId))
                return s;
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
            System.out.println("Send " + message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        sessionProvider.removeSession(websocketSession);
        System.out.println("Socket Closed: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }
}
