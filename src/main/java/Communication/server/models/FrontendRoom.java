package Communication.server.models;

import Communication.ReservationProvider;
import RoomConfiguration.ReadRoomConfig;
import RoomConfiguration.RoomDataModel;
import Reservation.RoomCollection;
import Reservation.Room;

import java.util.ArrayList;

public class FrontendRoom {

    // models describing a room as it is defined within the frontend application
    // Rooms should be converted into this class before being sent to clients

    private int id;
    private int capacity;
    private String name;
    private String location;
    public ArrayList<ReservationJavaScript> reservations;

    private FrontendRoom() {

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

    public ArrayList<ReservationJavaScript> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<ReservationJavaScript> reservations) {
        this.reservations = reservations;
    }

    private void update() {
        if(this.id > 0) {
            ReadRoomConfig readRoomConfig = new ReadRoomConfig();
            ArrayList<RoomDataModel> roomDataModels = readRoomConfig.GetRoomData();
            for (RoomDataModel r : roomDataModels) {
                if (r.getId() == this.id) {
                    this.capacity = r.getCapacity();
                    this.location = r.getLocation();
                    this.name = r.getName();
                }
            }
            RoomCollection roomCollection = ReservationProvider.getInstance().getCollection();
            Room room = roomCollection.getRoom(this.id);
            this.reservations = ReservationJavaScript.Convert(room.getReservations());
        }
    }

    public static FrontendRoom Convert(Room room){
        FrontendRoom result = new FrontendRoom();
        result.id = room.getId();
        result.update();
        return result;
    }

}
