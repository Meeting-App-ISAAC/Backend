package RoomConfiguration;

import java.util.ArrayList;

public class RoomDataModel {


    private String secret;
    private int id;
    private String email;
    private int capacity;
    private String name;
    private String location;

    public String getEmail() {
        return email;
    }

    public static int getRoomIdByEmail(String email, ArrayList<RoomDataModel> config) {
        for (RoomDataModel roomDataModel : config) {
            if (roomDataModel.getEmail() != null && roomDataModel.getEmail().equals(email)) {
                return roomDataModel.getId();
            }
        }
        return -1;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
