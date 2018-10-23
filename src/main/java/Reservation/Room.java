package Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Room extends java.util.Observable{

    private int id;
    private String name;
    private ArrayList<Reservation> reservations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation reservation){
        for (Reservation reservationCheck : reservations){
            if (reservation.getStart().isAfter(reservationCheck.getEnd()) && reservation.getEnd().isBefore(reservationCheck.getStart())){
                reservations.add(reservation);
                break;
            }
        }
    }

    public void removeReservation(Reservation reservation){
        reservations.remove(reservation);
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
