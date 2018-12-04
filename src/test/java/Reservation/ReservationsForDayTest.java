package Reservation;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ReservationsForDayTest {

    private Room room = new Room();

    @BeforeEach
    void setUp() {
        room = new Room();
        // Reservation starting and ending today
        Reservation reservationTrueA = new Reservation(
                1,
                new User(),
                false,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1));
        room.addReservation(reservationTrueA);
        // Reservation starting yesterday and ending today
        Reservation reservationTrueB = new Reservation(
                2,
                new User(),
                false,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusHours(1)
        );
        room.addReservation(reservationTrueB);
        // Reservation starting today and ending tomorrow
        Reservation reservationTrueC = new Reservation(
                3,
                new User(),
                false,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusDays(1)
        );
        room.addReservation(reservationTrueC);
        // Reservation starting yesterday and ending tomorrow
        Reservation reservationTrueD = new Reservation(
                4,
                new User(),
                false,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );
        room.addReservation(reservationTrueD);
        // Reservation starting and ending yesterday
        Reservation reservationFalseA = new Reservation(
                5,
                new User(),
                false,
                LocalDateTime.now().minusDays(1).minusHours(1),
                LocalDateTime.now().minusDays(1)
        );
        room.addReservation(reservationFalseA);
        // Reservation for today a year ago
        Reservation reservationFalseB = new Reservation(
                6,
                new User(),
                false,
                LocalDateTime.now().minusYears(1).minusHours(1),
                LocalDateTime.now().minusYears(1)
        );
        room.addReservation(reservationFalseB);
    }

    @Test
    void test() {
        for (Reservation r : room.getReservationsForDay(LocalDateTime.now())) {
            Assert.assertFalse(r.getId() == 5 || r.getId() == 6);
        }
    }

}
