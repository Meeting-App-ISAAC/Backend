package Communication.server;

interface IWebSocket {
    void sendTo(String sessionId, Object object);
    void broadcast(Object object);
}
