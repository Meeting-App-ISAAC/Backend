package Communication.server.restserver;

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
    @Path("/login")
    public Response test(String data){
        Reply reply = null;

        return Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build();
    }
}
