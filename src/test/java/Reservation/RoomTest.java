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
        Reservation reservation = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0));
        reservations.add(reservation);
        room.setReservations(reservations);
    }

    @Test
    void addReservation() {
        // Reservation that overlaps with an existing reservation
        Reservation reservationcheckfalse = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 6, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 8, 0, 0, 0));

        // Reservation that doesn't overlap with an existing reservation
        Reservation reservationchecktrue = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 2, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 4, 0, 0, 0));

        // Reservations can't overlap so this should fail and return false
        Assert.assertEquals(false, room.addReservation(reservationcheckfalse));

        //TODO Fix addReservation unit test
        // Reservation doesn't overlap with any other reservation so this should return true
        Assert.assertEquals(true, room.addReservation(reservationchecktrue));
    }

    @Test
    void removeReservation() {
    }

    @Test
    void isOccupied() {
    }

    @Test
    void roomChanged() {
    }
}