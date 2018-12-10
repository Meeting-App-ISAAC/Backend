package Settings;

import java.io.IOException;

public class FrontendSettings {

    // Set of settings that are retrieved from the config file and sent to the clients

    private String type = "settings";
    private boolean reservationStartEnabled;
    private boolean reservationStopEnabled;
    private String language;
    private int maxReservationWindow;

    public FrontendSettings(){
        try {
            this.reservationStartEnabled = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_START_ENABLED"));
            this.reservationStopEnabled = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_STOP_ENABLED"));
            this.language = SettingsHandler.getProperty("LANGUAGE");
            this.maxReservationWindow = Integer.parseInt(SettingsHandler.getProperty("MAX_RESERVATION_WINDOW"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
