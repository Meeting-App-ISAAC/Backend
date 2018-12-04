package Communication.server.messagehandlers;

abstract class BaseMessageHandler {
    private String sessionId;

    BaseMessageHandler(String sessionId){
        this.sessionId = sessionId;
    }
}
