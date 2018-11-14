package Settings;

import java.io.IOException;

public class FrontendSettings {

    private int serverPort;
    private boolean reservationStartEnabled;
    private boolean reservationStopEnabled;
    private String language;
    private int maxReservationWindow;

    public FrontendSettings() throws IOException {
        this.serverPort = Integer.parseInt(SettingsHandler.getProperty("SERVER_PORT"));
        this.reservationStartEnabled = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_START_ENABLED"));
        this.reservationStopEnabled = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_STOP_ENABLED"));
        this.language = SettingsHandler.getProperty("LANGUAGE");
        this.maxReservationWindow = Integer.parseInt(SettingsHandler.getProperty("MAX_RESERVATION_WINDOW"));
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getMaxReservationWindow() {
        return maxReservationWindow;
    }

    public void setMaxReservationWindow(int maxReservationWindow) {
        this.maxReservationWindow = maxReservationWindow;
    }

    public void update() {
        try {
            this.serverPort = Integer.parseInt(SettingsHandler.getProperty("SERVER_PORT"));
            this.reservationStartEnabled = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_START_ENABLED"));
            this.reservationStopEnabled = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_STOP_ENABLED"));
            this.language = SettingsHandler.getProperty("LANGUAGE");
            this.maxReservationWindow = Integer.parseInt(SettingsHandler.getProperty("MAX_RESERVATION_WINDOW"));
        }
        catch (IOException e) {
            System.out.println("[error] Could not get properties");
        }
    }
}
