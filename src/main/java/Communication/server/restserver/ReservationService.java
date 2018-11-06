package Communication.server.restserver;

import Communication.ReservationProvider;
import Communication.server.restserver.response.Status;
import Communication.server.restserver.responseModels.ReservationExtendResponse;
import Communication.server.restserver.responseModels.ReservationMeetingResponse;
import Reservation.Changed;
import Reservation.Reservation;
import Reservation.RoomCollection;
import Reservation.CheckOverlap;
import com.google.gson.Gson;
import Communication.server.restserver.response.Reply;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Path("/api")
public class ReservationService {

    Gson gson = new Gson();
    CheckOverlap overlap = new CheckOverlap();

    @POST @Consumes("application/json")
    @Path("/start")
    public Response StartMeeting(String data){
        Reply reply = null;
        ReservationMeetingResponse reservationStartResponse = gson.fromJson(data, ReservationMeetingResponse.class);
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        ArrayList<Reservation> reservations = collection.getRoom(reservationStartResponse.getRoomId()).getReservations();

        for (Reservation reservation : reservations){
            if (reservation.getId() == reservationStartResponse.getReservationId()) {
                reservation.setHasStarted(true);
                reservation.Changed(Changed.StartedMeeting);
                reply = new Reply(Status.OK, gson.toJson(true, boolean.class));
                return Response.status(reply.getStatus().getCode())
                        .entity(reply.getMessage()).build();
            }
        }
        reply = new Reply(Status.ERROR, gson.toJson(false, boolean.class));
        return Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build();
    }

    @POST @Consumes("application/json")
    @Path("/stop")
    public Response StopMeeting(String data) {
        Reply reply = null;

        ReservationMeetingResponse reservationStopResponse = gson.fromJson(data, ReservationMeetingResponse.class);
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        ArrayList<Reservation> reservations = collection.getRoom(reservationStopResponse.getRoomId()).getReservations();

        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationStopResponse.getReservationId() && reservation.getHasStarted()) {
                reservation.setHasStarted(false);
                reservation.setEnd(LocalDateTime.now());
                reservation.Changed(Changed.StoppedMeeting);
                reply = new Reply(Status.OK, true);
                return Response.status(reply.getStatus().getCode())
                        .entity(reply.getMessage()).build();
            }
        }
        reply = new Reply(Status.OK, false);
        return Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build();
    }


    @POST @Consumes("application/json")
    @Path("/extend")
    public Response ExtendMeeting(String data) {
        Reply reply = null;

        ReservationExtendResponse reservationExtendResponse = gson.fromJson(data, ReservationExtendResponse.class);

        if (!overlap.CheckOverlapAddTime(reservationExtendResponse.getReservationId(), reservationExtendResponse.getMinutes(), reservationExtendResponse.getRoomId(), Changed.ExtendedMeeting)){
            reply = new Reply(Status.OK, true);
            return Response.status(reply.getStatus().getCode())
                    .entity(reply.getMessage()).build();
        }

        reply = new Reply(Status.OK, false);
        return Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build();
    }
}
