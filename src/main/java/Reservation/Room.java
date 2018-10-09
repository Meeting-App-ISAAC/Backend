package Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Room {

    private String name;
    private ArrayList<Reservation> reservations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Boolean isOccupied(LocalDateTime time) {
        boolean reservationFound = false;
        for (Reservation r : this.reservations) {
            if(r.getStart().isBefore(time) && r.getEnd().isAfter(time)) {
                reservationFound = true;
            }
        }
        return reservationFound;
    }

}
