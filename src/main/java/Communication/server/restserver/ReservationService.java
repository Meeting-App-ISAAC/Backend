package Communication.server.restserver;

import CalendarResource.Calender;
import CalendarResource.DummyCalender;
import Communication.ReservationProvider;
import Communication.server.restserver.response.Status;
import Communication.server.restserver.responseModels.ReservationMeetingResponse;
import Reservation.Reservation;
import Reservation.RoomCollection;
import com.google.gson.Gson;
import Communication.server.restserver.response.Reply;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/reservation")
public class ReservationService {

    Gson gson = new Gson();
    Calender calender = new DummyCalender();

    @POST @Consumes("application/json")
    @Path("/start")
    public Response StartMeeting(String data){
        Reply reply = null;
        ReservationMeetingResponse reservationStartResponse = gson.fromJson(data, ReservationMeetingResponse.class);
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        ArrayList<Reservation> reservations = collection.getRoom(reservationStartResponse.getRoomId()).getReservations();

        for (Reservation reservation : reservations){
            if (reservation.getId() == reservationStartResponse.getReservationId()){
                reservation.setHasStarted(true);
            }
        }
        reply = new Reply(Status.OK, true);
        return Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build();
    }

    @POST @Consumes("application/json")
    @Path("/stop")
    public Response StopMeeting(String data) {
        Reply reply = null;

        ReservationMeetingResponse reservationStartResponse = gson.fromJson(data, ReservationMeetingResponse.class);
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        ArrayList<Reservation> reservations = collection.getRoom(reservationStartResponse.getRoomId()).getReservations();

        for (Reservation reservation : reservations){
            if (reservation.getId() == reservationStartResponse.getReservationId() && reservation.getHasStarted()){
                reservation.setHasStarted(false);
                calender.endEvent(reservation);
                reply = new Reply(Status.OK, true);
            } else {
                reply = new Reply(Status.OK, false);
            }
        }
        return Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build();
    }
}
