package Reservation;

import RoomConfiguration.RoomDataModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class Room extends java.util.Observable implements Observer {

    private int id;
    private String name;
    private ArrayList<Reservation> reservations = new ArrayList<Reservation>();


    public static Room SearchRoomById(int id, ArrayList<Room> rooms) {
        for (Room room : rooms) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }


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
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        for (int i = reservations.size() - 1; i >= 0; i--) {
            this.addReservation(reservations.get(i));
        }
    }

    public boolean addReservation(Reservation reservation){
        reservations.add(reservation);
        reservation.setRoom(this);
        reservation.addObserver(this);
        return true;
    }

    public void removeReservation(Reservation reservation){
        reservation.deleteObserver(this);
        reservation.setRoom(null);
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
        System.out.println("UHM");
        notifyObservers(arg);
    }
}
