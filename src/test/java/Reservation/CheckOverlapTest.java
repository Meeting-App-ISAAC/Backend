package Reservation;

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
        Reservation reservation = new Reservation(new User(), LocalDateTime.of(2018, 10, 23, 5, 0, 0, 0), LocalDateTime.of(2018, 10, 23, 7, 0, 0, 0));
        reservations.add(reservation);
        room.setReservations(reservations);
    }

    @Test
    void checkoverlap() {

    }
}
