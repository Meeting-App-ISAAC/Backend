package Communication.server;

public interface IWebSocket {
    void sendTo(String sessionId, Object object);
    void broadcast(Object object);
}
