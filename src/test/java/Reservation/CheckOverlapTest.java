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
        Reservation reservation3 = new Reservation(new User(), LocalDateTime.now().plusSeconds(32), LocalDateTime.now().plusSeconds(43));
        reservation1.setId(1);
        reservation2.setId(2);
        reservation3.setId(3);
        reservations.add(reservation1);
        reservations.add(reservation2);
        reservations.add(reservation3);
        room.setReservations(reservations);
    }

    @Test
    void checkoverlap() {
        CheckOverlap checkOverlap = new CheckOverlap();
        Assert.assertEquals(true, checkOverlap.CheckOverlap(1, 1));
    }
}
