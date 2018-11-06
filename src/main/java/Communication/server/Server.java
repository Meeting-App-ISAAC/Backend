package Communication.server;

import Communication.server.restserver.ReservationService;
import Settings.SettingsHandler;
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
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Timer;

public class Server {
    public static void main(String[] args)
    {
        boolean timerEnabled = true;
        try {
            timerEnabled = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_TIMEOUT_ENABLED"));
        }
        catch (IOException e) {
            System.out.println("[error] Could not get properties file");
        }
        if (timerEnabled) {
            Timer timer = new Timer();
            timer.schedule(new ReservationTimer(), 0, 60000);
        }

        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.addConnector(connector);

        Gson gson = new Gson();
        String test = gson.toJson(LocalDateTime.now());
        System.out.println(test);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        FilterHolder cors = context.addFilter(CrossOriginFilter.class,"/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,OPTIONS");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        server.setHandler(context);

        ServletHolder jerseyServlet =
                context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
                ReservationService.class.getCanonicalName());

        try
        {
            // Initialize javax.websocket layer
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);

            // Add WebSocket endpoint to javax.websocket layer
            wscontainer.addEndpoint(WebSocket.class);

            server.start();
            server.dump(System.err);
            server.join();
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
}
