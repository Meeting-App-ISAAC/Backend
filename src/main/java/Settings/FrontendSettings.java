package Settings;

import java.io.IOException;

public class FrontendSettings {

    private int serverPort;
    private boolean reservationStartEnabled;
    private boolean reservationStopEnabled;

    public FrontendSettings() throws IOException {
        try {
            this.serverPort = Integer.parseInt(SettingsHandler.getProperty("SERVER_PORT"));
            this.reservationStartEnabled = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_START_ENABLED"));
            this.reservationStopEnabled = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_STOP_ENABLED"));
        }
        catch (IOException e) {
            System.out.println("[error] Could not get properties file");
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public boolean isReservationStartEnabled() {
        return reservationStartEnabled;
    }

    public void setReservationStartEnabled(boolean reservationStartEnabled) {
        this.reservationStartEnabled = reservationStartEnabled;
    }

    public boolean isReservationStopEnabled() {
        return reservationStopEnabled;
    }

    public void setReservationStopEnabled(boolean reservationStopEnabled) {
        this.reservationStopEnabled = reservationStopEnabled;
    }
}
