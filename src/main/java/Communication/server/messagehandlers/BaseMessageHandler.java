package Communication.server.messagehandlers;

public abstract class BaseMessageHandler {
    String sessionId;

    BaseMessageHandler(String sessionId){
        this.sessionId = sessionId;
    }
}
