package Authentication;

import Communication.server.restserver.response.Reply;
import Communication.server.restserver.response.Status;
import RoomConfiguration.ReadRoomConfig;
import RoomConfiguration.RoomDataModel;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationChecker implements ContainerRequestFilter {

    private static final String AUTHENTICATION_SCHEME = "Basic";

    @Override
    public void filter(ContainerRequestContext requestContext) {

        // Get the Authorization header from the request
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (!isHeaderValid(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        // Extract the token from the Authorization header
        String key = authorizationHeader
                .substring(AUTHENTICATION_SCHEME.length()).trim();

        try {

            // Validate the token
            checkKey(key);

        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    //Check if header is valid
    private boolean isHeaderValid(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    //If key was not correct return with error
    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        Reply reply = new Reply(Status.NOACCESS,"Not authorized");
        requestContext.abortWith(
                Response.status(reply.getStatus().getCode())
                .entity(reply.getMessage()).build()
        );
    }

    //Check key and perform actions
    public boolean checkKey(String key) {
        ReadRoomConfig readRoomConfig = new ReadRoomConfig();
        for (RoomDataModel roomDataModel : readRoomConfig.GetRoomData()){
            if (roomDataModel.getSecret().equals(key)) {
                return true;
            }
        }
        return false;
    }
}
