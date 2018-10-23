package Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public boolean addReservation(Reservation reservation){
        List<Reservation> reservationschecksize = new ArrayList<>();

        for (Reservation reservationCheck : reservations){
            if (reservation.getStart().isAfter(reservationCheck.getEnd()) && reservation.getEnd().isBefore(reservationCheck.getStart())){
                reservationschecksize.add(reservationCheck);
            }
        }
        if (reservationschecksize.size() == reservations.size()){
            reservations.add(reservation);
            return true;
        }
        else {
            return false;
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

    public void roomChanged(Changed changed){
        ChangedObject roomObj = new ChangedObject();
        roomObj.setChanged(changed);
        roomObj.setArg(this);
        System.out.println(changed);
        setChanged();
        notifyObservers(roomObj);
    }

}
