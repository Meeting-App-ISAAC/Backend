package Reservation;

import Communication.ReservationProvider;

import java.util.ArrayList;

public class CheckOverlap {

    public boolean CheckOverlap(int reservationId, int roomId){
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        ArrayList<Reservation> reservations = collection.getRoom(roomId).getReservations();
        Boolean overlap = false;
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId && reservation.getHasStarted()) {

                for (int i = 0; i < reservations.size(); i++) {
                    if (!(reservation.getId() == reservations.get(i).getId())) {
                        if (reservation.getStart().isBefore(reservations.get(i).getStart()) && reservation.getStart().isBefore(reservations.get(i).getEnd()) && reservation.getEnd().isBefore(reservations.get(i).getStart())) {
                            overlap = false;
                        } else {
                            overlap = true;
                        }
                    }
                }
            }
        }
        return overlap;
    }

    public boolean CheckOverlapAddTime(int reservationId, int extensionMinutes, int roomId, Changed changed){
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        ArrayList<Reservation> reservations = collection.getRoom(roomId).getReservations();
        boolean overlap = false;
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId && reservation.getHasStarted()) {
                for (int i = 0; i < reservations.size(); i++) {
                    if (!(reservation.getId() == reservations.get(i).getId())) {
                        if (reservation.getStart().isBefore(reservations.get(i).getStart()) && reservation.getEnd().plusMinutes(extensionMinutes).isBefore(reservations.get(i).getStart())) {
                            overlap = false;
                        } else {
                            overlap = true;
                        }

                    }
                }
                if (!overlap) {
                    reservation.setEnd(reservation.getEnd().plusMinutes(extensionMinutes));
                    reservation.Changed(changed);
                }
            }
        }

        return overlap;
    }
}
