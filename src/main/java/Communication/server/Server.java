package Communication.server;

import Communication.ReservationProvider;
import Authentication.AuthenticationChecker;
import Communication.server.restserver.ConfigurationService;
import Communication.server.restserver.ReservationService;
import Reservation.Room;
import Reservation.RoomCollection;
import Settings.SettingsHandler;
import Settings.SettingsWatcher;
import com.google.gson.Gson;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.DispatcherType;
import javax.websocket.server.ServerContainer;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Timer;

class Server {
    public static void main(String[] args)
    {

        // Reservation timeout
        setReservationTimeout();

        // Resend settings on change of config file
        setResendOnChange();

        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server();
        ServerConnector connector = new ServerConnector(server);
        ServerConnector connectorWithPort = setServerPort(connector);
        server.addConnector(connectorWithPort);

        Gson gson = new Gson();
        String test = gson.toJson(LocalDateTime.now());
        System.out.println(test);

        System.out.println("[info] Starting server...");

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        FilterHolder cors = context.addFilter(CrossOriginFilter.class,"/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,OPTIONS");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        server.setHandler(context);

        ServletHolder jerseyServlet =
                context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
                "" + ReservationService.class.getCanonicalName() + ";" + ConfigurationService.class.getCanonicalName() + ";" + AuthenticationChecker.class.getCanonicalName() + "");

        try
        {
            // Initialize javax.websocket layer
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
            wscontainer.setDefaultMaxSessionIdleTimeout(0);

            // Add WebSocket endpoint to javax.websocket layer
            wscontainer.addEndpoint(WebSocket.class);

            ArrayList<RoomListener> col = new ArrayList<>();
            RoomCollection collection = ReservationProvider.getInstance().getCollection();
            for (Room room: collection.getAllRooms()) {
                RoomListener roomListener = new RoomListener();
                roomListener.setRoom(room);
                col.add(roomListener);
            }

            server.start();
            server.dump(System.err);
            server.join();
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
    private static void setReservationTimeout() {
        // Reservation timeout
        System.out.println("[info] Setting reservation timeout...");
        boolean timerEnabled = true;
        try {
            timerEnabled = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_TIMEOUT_ENABLED"));
        }
        catch (Exception e) {
            noPropertiesError();
        }
        if (timerEnabled) {
            Timer timer = new Timer();
            timer.schedule(new ReservationTimer(), 0, 6000);
        }
        System.out.println("[info] Done!");
    }
    private static void setResendOnChange() {
        // Resend settings on change of config file
        System.out.println("[info] Setting resend on change...");
        boolean resendOnChange = true;
        try {
            resendOnChange = Boolean.parseBoolean(SettingsHandler.getProperty("RESEND_ON_CONFIG_CHANGE"));
        }
        catch (Exception e) {
            noPropertiesError();
        }
        System.out.println("[info] Done!");
    }
    private static ServerConnector setServerPort(ServerConnector connector) {
        System.out.println("[info] Setting server port...");
        try {
            connector.setPort(Integer.parseInt(SettingsHandler.getProperty("SERVER_PORT")));
        }
        catch (Exception e) {
            noPropertiesError();
        }
        System.out.println("[info] Done!");
        return connector;
    }
    private static void noPropertiesError() {
        // Properties file could not be found, return error and exit application with code 1
        System.out.println("[error] Could not get properties file");
        System.exit(1);
    }
}
