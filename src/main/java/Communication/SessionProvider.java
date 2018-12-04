package Communication;

import javax.websocket.Session;
import java.util.ArrayList;

public class SessionProvider {

    // Singleton providing an up-to-date list of all current sessions

    private ArrayList<Session> sessions;
    private static SessionProvider sessionProvider = new SessionProvider();

    public static SessionProvider getInstance() {return sessionProvider;}

    private SessionProvider() {
        this.sessions = new ArrayList<>();
    }

    public ArrayList<Session> getSessions() {
        return this.sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }

    public void removeSession(Session session) {
        this.sessions.remove(session);
    }
}
