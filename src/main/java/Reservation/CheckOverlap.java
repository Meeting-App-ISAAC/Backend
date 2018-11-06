package Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CheckOverlap {

    public boolean CheckOverlap(ArrayList<Reservation> reservations, int reservationId, Changed changed){
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId && reservation.getHasStarted()) {

                for (int i = 0; i < reservations.size(); i++) {
                    if (reservation.getStart().isBefore(reservations.get(i).getStart()) && reservation.getStart().isBefore(reservations.get(i).getEnd()) && reservation.getEnd().isBefore(reservations.get(i).getStart())) {
                        reservation.Changed(changed);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean CheckOverlapAddTime(ArrayList<Reservation> reservations, int reservationId, int extensionMinutes, Changed changed){
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId && reservation.getHasStarted()) {

                for (int i = 0; i < reservations.size(); i++) {
                    if (reservation.getStart().isBefore(reservations.get(i).getStart()) && reservation.getStart().isBefore(reservations.get(i).getEnd()) && reservation.getEnd().isBefore(reservations.get(i).getStart())) {
                        reservation.setEnd(LocalDateTime.now().plusMinutes(extensionMinutes));
                        reservation.Changed(changed);
                        return true;

                    }
                }

            }
        }
        return false;
    }
}
