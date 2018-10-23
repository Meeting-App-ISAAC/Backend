package Communication.server.restserver;

import Communication.ReservationProvider;
import Communication.server.restserver.responseModels.ReservationStartResponse;
import Reservation.RoomCollection;
import com.google.gson.Gson;
import Communication.server.restserver.response.Reply;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/reservation")
public class ReservationService {

    Gson gson = new Gson();

    @POST @Consumes("application/json")
    @Path("/start")
    public Response startReservation(String data){
        Reply reply = null;
        ReservationStartResponse reservation = gson.fromJson(data, ReservationStartResponse.class);
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        collection.getRoom(0).getReservations();

        return Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build();
    }
}
