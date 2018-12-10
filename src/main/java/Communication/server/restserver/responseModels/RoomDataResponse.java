package Communication.server.restserver.responseModels;

public class RoomDataResponse {
    private String secret;
    private int id;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
