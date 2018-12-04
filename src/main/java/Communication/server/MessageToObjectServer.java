package Communication.server;

import Communication.ReservationProvider;
import Reservation.RoomCollection;
import Reservation.Room;
import com.google.gson.Gson;
import shared.messages.ReservationsRequest;

class MessageToObjectServer {

    private Gson gson = new Gson();
    private RoomCollection roomCollection = ReservationProvider.getInstance().getCollection();
    private MessageSender messageSender = new MessageSender();

    public void processMessage(String sessionId, String type, String data) {
        //Get the last part from the full package and type name
        String simpleType = type.split("\\.")[type.split("\\.").length - 1];

        switch(simpleType)
        {
            case "ReservationsRequest":
                ReservationsRequest request = gson.fromJson(data,ReservationsRequest.class);
                Room room = roomCollection.getRoom(request.getRoomId());
                messageSender.sendTo(sessionId,room.getReservations());
            default:
        }
    }

}
