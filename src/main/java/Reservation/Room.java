package Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.List;
=======
import java.util.Observable;
import java.util.Observer;
>>>>>>> b62ba034625128d1cef71b8d7adfd792273591be

public class Room extends java.util.Observable implements Observer {

    private int id;
    private String name;
    private ArrayList<Reservation> reservations = new ArrayList<Reservation>();

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
        for (int i = reservations.size() - 1; i >= 0; i--) {
            this.addReservation(reservations.get(i));
        }
    }

<<<<<<< HEAD
    public boolean addReservation(Reservation reservation){
        List<Reservation> reservationschecksize = new ArrayList<>();

        for (Reservation reservationCheck : reservations){
            if (reservation.getStart().isAfter(reservationCheck.getEnd()) && reservation.getEnd().isBefore(reservationCheck.getStart())){
                reservationschecksize.add(reservationCheck);
=======
    public void addReservation(Reservation reservation){
        if(reservations.size() == 0){
            reservations.add(reservation);
            reservation.addObserver(this);
            return;
        }
        for (Reservation reservationCheck : reservations){
            if (!(reservation.getStart().isAfter(reservationCheck.getEnd()) && reservation.getEnd().isBefore(reservationCheck.getStart()))){
                reservations.add(reservation);
                reservation.addObserver(this);
                break;
>>>>>>> b62ba034625128d1cef71b8d7adfd792273591be
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
        reservation.deleteObserver(this);
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

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
