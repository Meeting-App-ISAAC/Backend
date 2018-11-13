package Reservation;

import Communication.ReservationProvider;
import java.util.ArrayList;

public class CheckOverlap {

    public boolean CheckOverlap(int reservationId, int roomId) {
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        ArrayList<Reservation> reservations = collection.getRoom(roomId).getReservations();
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId) {
                if (reservationChecker(reservations, reservation)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean CheckOverlapAddTime(int reservationId, int extensionMinutes, int roomId, Changed changed) {
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        ArrayList<Reservation> reservations = collection.getRoom(roomId).getReservations();
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId && reservation.getHasStarted()) {
                if (reservationChecker(reservations, reservation)) {
                    return true;
                }
                reservation.setEnd(reservation.getEnd().plusMinutes(extensionMinutes));
                reservation.Changed(changed);
            }
        }

        return false;
    }

    public boolean reservationChecker(ArrayList<Reservation> reservations, Reservation reservation) {
        boolean overlap = false;
        for (int i = 0; i < reservations.size(); i++) {
            if (!(reservation.getId() == reservations.get(i).getId())) {
                if (reservation.getStart().isBefore(reservations.get(i).getStart()) && reservation.getEnd().isBefore(reservations.get(i).getStart())) {
                    overlap = false;
                } else if (reservation.getStart().isAfter(reservations.get(i).getEnd())) {
                    overlap = false;
                } else {
                    return true;
                }

            }
        }
        return overlap;
    }
}
