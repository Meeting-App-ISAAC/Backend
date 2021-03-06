package Communication.server;

import Communication.ReservationProvider;
import Reservation.Reservation;
import Reservation.RoomCollection;
import Reservation.Room;
import Reservation.Changed;
import Settings.SettingsHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

// This class checks for reservations that should have started but haven't been

class ReservationTimer extends TimerTask {

    private List<Reservation> reservations;

    // Sets the amount of minutes before a reservation is considered expired
    private long expiryTime = 10;
    private boolean check;

    ReservationTimer() {
        reservations = new ArrayList<Reservation>();
        try {
            expiryTime = Long.parseLong(SettingsHandler.getProperty("RESERVATION_TIMEOUT"));
            check = Boolean.parseBoolean(SettingsHandler.getProperty("RESERVATION_TIMEOUT_ENABLED"));
        }
        catch (IOException e) {
            System.out.println("[error] Could not get properties file");
        }
    }

    public void run() {
        System.out.println("[info] Requesting Exchange delta...");
        refreshReservations();

        if(check) {
            System.out.println("[info] Checking for expired reservations...");
            for (Reservation r : reservations) {
                if (r.getStart().isBefore(LocalDateTime.now()) && !r.getHasStarted()
                        && LocalDateTime.now().isAfter(r.getStart().plusMinutes(expiryTime))) {
                    r.setHasStarted(true);
                    r.setEnd(LocalDateTime.now());
                    r.Changed(Changed.StartedMeeting);
                    System.out.println("[info] Reservation with id " + r.getId() + " was expired");
                }
            }
        }
    }

    // Refreshes the list of all reservations with those in the ReservationProvider
    private void refreshReservations() {
        ReservationProvider.getInstance().Reload();
        RoomCollection roomCollection = ReservationProvider.getInstance().getCollection();
        List<Room> rooms = roomCollection.getAllRooms();
        reservations.clear();
        for(Room r : rooms) {
            reservations.addAll(r.getReservations());
        }
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

}
