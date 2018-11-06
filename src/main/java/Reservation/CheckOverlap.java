package Reservation;

import Communication.ReservationProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CheckOverlap {

    public boolean CheckOverlap(int reservationId, int roomId, Changed changed){
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        ArrayList<Reservation> reservations = collection.getRoom(roomId).getReservations();
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

    public boolean CheckOverlapAddTime(int reservationId, int extensionMinutes, int roomId, Changed changed){
        ReservationProvider reservationProvider = ReservationProvider.getInstance();
        RoomCollection collection = reservationProvider.getCollection();
        ArrayList<Reservation> reservations = collection.getRoom(roomId).getReservations();
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
