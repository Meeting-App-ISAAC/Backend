package Reservation;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    Room room = null;
    ArrayList<Reservation> reservations = new ArrayList<>();

    @BeforeEach
    void setUp() {
        room = new Room();
        //Begins before ends in
        Reservation reservation = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0));
        //Begins before ends before
        Reservation reservation1 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 4, 1, 0, 0), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0));
        //Begins after ends after
        Reservation reservation2 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 9, 1, 0, 0), LocalDateTime.of(2018, 10, 23, 10, 0, 0, 0));
        //Begins in ends after
        Reservation reservation3 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 9, 0, 0, 0));
        //Begins in ends in
        Reservation reservation4 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 7, 15, 0, 0));
        //Begins before ends after
        Reservation reservation5 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 9, 0, 0, 0));

        reservations.add(reservation);
        reservations.add(reservation3);
        reservations.add(reservation4);
        room.addReservation(reservation);
        //room.setReservations(reservations);
    }

    @Test
    void addReservation() {
        // Reservation that overlaps with an existing reservation
        Reservation reservationcheckfalse = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 6, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 8, 0, 0, 0));

        // Reservation that doesn't overlap with an existing reservation
        Reservation reservationchecktrue = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 2, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 4, 0, 0, 0));

        // Reservations can't overlap so this should fail and return false
        Assert.assertEquals(false, room.addReservation(reservationcheckfalse));

        // Reservation doesn't overlap with any other reservation so this should return true
        //Assert.assertEquals(true, room.addReservation(reservationchecktrue));

        room.addReservation(new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 4, 1, 0, 0), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0)));

        // Reservation doesn't overlap with any other reservation so this should return true
        Assert.assertEquals(true, room.addReservation(reservationchecktrue));
    }

    @Test
    void removeReservation() {
        Reservation removeReservation1 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 8, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 9, 0, 0, 0));
        room.addReservation(removeReservation1);
        Reservation removeReservation2 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 10, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 11, 0, 0, 0));
        room.addReservation(removeReservation2);

        Assert.assertEquals(3, room.getReservations().size());

        room.removeReservation(removeReservation1);

        Assert.assertEquals(2, room.getReservations().size());

        room.removeReservation(removeReservation2);

        Assert.assertEquals(1, room.getReservations().size());
    }

    @Test
    void isOccupied() {
    }

    @Test
    void roomChanged() {
    }
}