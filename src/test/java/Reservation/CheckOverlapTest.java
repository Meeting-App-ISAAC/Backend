package Reservation;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CheckOverlapTest {

    Room room = null;
    ArrayList<Reservation> reservations = new ArrayList<>();

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1);
        Reservation reservation1 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0));
        Reservation reservation2 = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0));
        reservation1.setId(1);
        reservation2.setId(2);
        reservations.add(reservation1);
        reservations.add(reservation2);
        room.setReservations(reservations);
    }

    @Test
    void checkoverlap() {
        CheckOverlap checkOverlap = new CheckOverlap();
        Assert.assertTrue(checkOverlap.CheckOverlap(1, 1));
    }
}
