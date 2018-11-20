package shared.messages;

public class EncapsulatingMessage {
    private String messageType;

    private Object messageData;

    public EncapsulatingMessage(String type, Object data)
    {
        this.messageType = type;
        this.messageData = data;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public Object getMessageData(){
        return messageData;
    }
}

