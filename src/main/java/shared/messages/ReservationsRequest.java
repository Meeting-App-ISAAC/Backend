package shared.messages;

public class ReservationsRequest {

    private int roomId;

    public ReservationsRequest(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

}
