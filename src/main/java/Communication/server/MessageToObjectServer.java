package Communication.server;

import com.google.gson.Gson;

public class MessageToObjectServer {
    Gson gson = new Gson();

    public void processMessage(String sessionId, String type, String data) {
        //Get the last part from the full package and type name
        String simpleType = type.split("\\.")[type.split("\\.").length - 1];

        switch(simpleType)
        {
            default:
        }
    }

}
