package Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class Room extends java.util.Observable implements Observer {

    private int id;
    private String name;
    private ArrayList<Reservation> reservations = new ArrayList<Reservation>();

    public Room(){

    }

    public Room(int id, String name, ArrayList<Reservation> reservations){
        this.id = id;
        this.name = name;
        for (Reservation reservation : reservations){
            addReservation(reservation);
        }
    }

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

    // Returns reservations for the day specified in the parameters
    public ArrayList<Reservation> getReservationsForDay(LocalDateTime time) {
        ArrayList<Reservation> reservationsToday = new ArrayList<Reservation>();
        for (Reservation r : reservations) {
            // Check if there are any reservations for the same year as the given time
            if (r.getEnd().getYear() == time.getYear() || r.getStart().getYear() == time.getYear()) {
                // Check if there are any reservations that start or end on the same day as the given time
                // If so, add to list
                if (r.getEnd().getDayOfYear() == time.getDayOfYear() || r.getStart().getDayOfYear() == time.getDayOfYear()) {
                    reservationsToday.add(r);
                }
                // Check if there are any reservations that start before the given time and end after
                // If so, add to list
                else if (time.isBefore(r.getEnd()) && time.isAfter(r.getStart())) {
                    reservationsToday.add(r);
                }
            }
        }
        return reservationsToday;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        for (int i = reservations.size() - 1; i >= 0; i--) {
            this.addReservation(reservations.get(i));
        }
    }

    public boolean addReservation(Reservation reservation){
        if(reservations.size() == 0){
            reservations.add(reservation);
            reservation.addObserver(this);
            return true;
        }
        for (Reservation reservationCheck : reservations){
            if (!(reservation.getStart().isAfter(reservationCheck.getEnd()) && reservation.getEnd().isBefore(reservationCheck.getStart()))){
                reservations.add(reservation);
                reservation.addObserver(this);
                return true;
            }
        }
        return false;
    }

    public void removeReservation(Reservation reservation){
        reservation.deleteObserver(this);
        reservations.remove(reservation);
    }

    public void overwriteReservations(ArrayList<Reservation> reservations){
        this.reservations.clear();
        setReservations(reservations);
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

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
