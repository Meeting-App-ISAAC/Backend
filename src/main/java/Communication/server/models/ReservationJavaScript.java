package Communication.server.models;

import Reservation.Reservation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReservationJavaScript {

    // models describing a reservation as it is defined within the frontend application
    // Reservations should be converted into this class before being sent to the client

    private int id;
    private String title;
    public double startHour;
    public double length;
    private boolean hasStarted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public double getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setLength(double length) {this.length = length;}

    public boolean getHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public static ArrayList<ReservationJavaScript> Convert(ArrayList<Reservation> reservations) {
        ArrayList<ReservationJavaScript> result = new ArrayList<>();
        for (Reservation reservation : reservations) {
            ReservationJavaScript temp = new ReservationJavaScript();
            temp.id = reservation.getId();
            temp.title = reservation.getTitle();
            temp.length = (double) Math.round((double) (reservation.getEnd().atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli() - reservation.getStart().atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()) / 3600) / 1000;
            temp.hasStarted = reservation.getHasStarted();


            temp.startHour = (double) reservation.getStart().getHour() + (double) reservation.getStart().getMinute() / 60 + (double) reservation.getStart().getSecond() / 3600;
            if(reservation.getStart().getYear() != LocalDateTime.now().getYear() ||
                    reservation.getStart().getDayOfYear() != LocalDateTime.now().getDayOfYear()) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.set(Calendar.HOUR_OF_DAY, 0);


                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                ZonedDateTime dateNow = ZonedDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault());



                temp.length = (double) Math.round((double) (reservation.getEnd().atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli() - dateNow.toInstant().toEpochMilli()) / 3600) / 1000;
                temp.startHour = 0;
            }

            result.add(temp);
        }
        return result;
    }
}
