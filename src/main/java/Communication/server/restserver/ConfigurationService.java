package Communication.server.restserver;

import CalendarResource.Calender;
import CalendarResource.DummyCalender;
import Communication.server.restserver.response.Reply;
import Communication.server.restserver.response.Status;
import Communication.server.restserver.responseModels.RoomDataResponse;
import Reservation.Reservation;
import Reservation.Room;
import RoomConfiguration.ReadRoomConfig;
import RoomConfiguration.RoomDataModel;
import com.google.gson.Gson;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Path("/config")
public class ConfigurationService {
    Gson gson = new Gson();
    ReadRoomConfig readRoomConfig = new ReadRoomConfig();


    @GET
    @Path("/secret")
    public Response getSecret() {
        Reply reply = null;
        ArrayList<RoomDataModel> roomData = readRoomConfig.GetRoomData();
        RoomDataModel roomDataModel = new RoomDataModel();
        roomDataModel.setId(roomData.size() + 1);
        roomDataModel.setSecret("test");
        RoomDataResponse roomDataResponse = new RoomDataResponse();
        roomDataResponse.setId(roomData.size() + 1);
        roomDataResponse.setSecret("test");
        roomData.add(roomDataModel);
        readRoomConfig.SaveRoomData(roomData);

        reply = new Reply(Status.OK, gson.toJson(roomDataResponse));
        return Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build();
    }

    @GET
    @Path("/rooms")
    public Response getRooms() {
        Reply reply = null;
        Calender calender = new DummyCalender();
        List<Room> rooms = new ArrayList<Room>();
        for (Room room: calender.getRooms()){
            Room tempRoom = new Room();
            tempRoom.setId(room.getId());
            tempRoom.setName(room.getName());
            rooms.add(tempRoom);
        }
        reply = new Reply(Status.OK, gson.toJson(rooms));
        return Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build();
    }

    @POST
    @Consumes("application/json")
    @Path("/createroom")
    public Response CreateRoom(String data) {
        Reply reply = null;

        RoomDataModel configCreateResponse = gson.fromJson(data, RoomDataModel.class);
        ArrayList<RoomDataModel> roomData = readRoomConfig.GetRoomData();
        for (RoomDataModel room: roomData) {
            if (room.getId() == configCreateResponse.getId()){
                if (room.getSecret().equals(configCreateResponse.getSecret())){
                    room.setCapacity(configCreateResponse.getCapacity());
                    room.setLocation(configCreateResponse.getLocation());
                    room.setName(configCreateResponse.getName());
                    readRoomConfig.SaveRoomData(roomData);
                    reply = new Reply(Status.OK, true);
                    return Response.status(reply.getStatus().getCode())
                            .entity(reply.getMessage()).build();
                } else {
                    roomData.remove(room);
                    reply = new Reply(Status.NOACCESS, false);
                }
                reply = new Reply(Status.NOTFOUND, false);
            }
        }

        return Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build();
    }

}
