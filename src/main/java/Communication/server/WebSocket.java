package Communication.server;

import CalendarResource.Calender;
import Communication.ReservationProvider;
import Communication.SessionProvider;
import Authentication.AuthenticationChecker;
import Reservation.RoomCollection;
import Settings.FrontendSettings;
import Settings.SettingsHandler;
import com.google.gson.Gson;
import shared.EncapsulatingMessageGenerator;
import shared.IEncapsulatingMessageGenerator;

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
    private IMessageSender message = new MessageSender();

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
        System.out.println("[info] Socket connected but NOT (yet) authenticated: " + session);
    }

    @OnMessage
    public void onWebSocketText(String key, Session session)
    {
        // Check whether the key received is valid
        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        if(authenticationChecker.checkKey(key)){
            // If so, proceed with sending the client reservations
            System.out.println("[info] Socket connected & authenticated: " + session);
            sessionProvider.addSession(session);
            websocketSession = session;
            message.sendReservationDump(session);
            FrontendSettings frontendSettings = new FrontendSettings();
            sendTo(session.getId(), frontendSettings);
        }
        else {
            System.out.println("[info] Connection " + session + " refused, invalid key");
        }
    }


    public void sendTo(String sessionId, Object object)
    {
        String msg = messageGenerator.generateMessageString(object);
        sendToClient(getSessionFromId(sessionId), msg);
    }

    private Session getSessionFromId(String sessionId)
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
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        sessionProvider.removeSession(websocketSession);
        System.out.println("[info] Socket Closed: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }
}
