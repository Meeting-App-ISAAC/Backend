package Reservation;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

class RoomTest {

    private Room room = null;
    ArrayList<Reservation> reservations = new ArrayList<>();

    @BeforeEach
    void setUp() {
        room = new Room();
    }

    @Test
    void addReservation() {
        // Existing reservation
        Reservation reservation = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0));
        room.addReservation(reservation);

        // Reservation that overlaps with an existing reservation
        Reservation reservationcheckfalse = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 6, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 8, 0, 0, 0));

        // Reservation that doesn't overlap with an existing reservation
        Reservation reservationchecktrue = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 2, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 4, 0, 0, 0));

        // Reservations can't overlap so this should fail and return false
        Assert.assertEquals(false, room.addReservation(reservationcheckfalse));

        // Reservation doesn't overlap with any other reservation so this should return true
        Assert.assertEquals(true, room.addReservation(reservationchecktrue));

        Assert.assertEquals(2, room.getReservations().size());
    }

    @Test
    void setReservations() {
        Assert.assertEquals(0, room.getReservations().size());
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 10, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 11, 0, 0, 0)));
        reservations.add(new Reservation(new User(), LocalDateTime.of(2018, 10, 22, 10, 0, 0, 0), LocalDateTime.of(2018, 10, 22, 11, 0, 0, 0)));
        room.setReservations(reservations);
        Assert.assertEquals(2, room.getReservations().size());
    }

    @Test
    void removeReservation() {
        Reservation removeReservation1 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 8, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 9, 0, 0, 0));
        room.addReservation(removeReservation1);
        Reservation removeReservation2 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 10, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 11, 0, 0, 0));
        room.addReservation(removeReservation2);

        Assert.assertEquals(2, room.getReservations().size());

        room.removeReservation(removeReservation1);

        Assert.assertEquals(1, room.getReservations().size());

        room.removeReservation(removeReservation2);

        Assert.assertEquals(0, room.getReservations().size());
    }

    @Test
    void overwriteReservations(){
        room.addReservation(new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 10, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 11, 0, 0, 0)));
        Assert.assertEquals(1, room.getReservations().size());
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 10, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 11, 0, 0, 0)));
        reservations.add(new Reservation(new User(), LocalDateTime.of(2018, 10, 22, 10, 0, 0, 0), LocalDateTime.of(2018, 10, 22, 11, 0, 0, 0)));
        room.overwriteReservations(reservations);
        Assert.assertEquals(2, room.getReservations().size());
    }

    @Test
    void checkReservationOverlap() {
        // Existing reservation
        Reservation reservationcheck = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 6, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 8, 0, 0, 0));

        // Begins before and ends during existing reservation
        Reservation reservation1 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0));
        // Begins before and ends before existing reservation
        Reservation reservation2 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 4, 1, 0, 0), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0));
        // Begins after and ends after existing reservation
        Reservation reservation3 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 9, 1, 0, 0), LocalDateTime.of(2018, 10, 23, 10, 0, 0, 0));
        // Begins during and ends after existing reservation
        Reservation reservation4 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 9, 0, 0, 0));
        // Begins during and ends during existing reservation
        Reservation reservation5 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 7, 15, 0, 0));
        // Begins before and ends after existing reservation
        Reservation reservation6 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 9, 0, 0, 0));

        room.addReservation(reservationcheck);
        Assert.assertEquals(1, room.getReservations().size());

        // This reservation begins before the existing reservation and ends during the existing reservation, this overlaps so this should return false.
        Assert.assertFalse(room.addReservation(reservation1));
        Assert.assertEquals(1, room.getReservations().size());

        // This reservation begins before the existing reservation and also ends before the existing reservation, this doesn't overlap so this should return true.
        Assert.assertTrue(room.addReservation(reservation2));
        Assert.assertEquals(2, room.getReservations().size());
        room.removeReservation(reservation2);
        Assert.assertEquals(1, room.getReservations().size());

        // This reservation begins before the existing reservation and ends during the existing reservation, this overlaps so this should return false.
        Assert.assertTrue(room.addReservation(reservation3));
        Assert.assertEquals(2, room.getReservations().size());
        room.removeReservation(reservation3);
        Assert.assertEquals(1, room.getReservations().size());

        // This reservation begins before the existing reservation and ends during the existing reservation, this overlaps so this should return false.
        Assert.assertFalse(room.addReservation(reservation4));
        Assert.assertEquals(1, room.getReservations().size());

        // This reservation begins before the existing reservation and ends during the existing reservation, this overlaps so this should return false.
        Assert.assertFalse(room.addReservation(reservation5));
        Assert.assertEquals(1, room.getReservations().size());

        // This reservation begins before the existing reservation and ends during the existing reservation, this overlaps so this should return false.
        Assert.assertFalse(room.addReservation(reservation6));
        Assert.assertEquals(1, room.getReservations().size());
    }
}