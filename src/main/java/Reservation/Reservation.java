package Reservation;

import java.time.LocalDateTime;

public class Reservation {

    private Room room;
    private User user;

    private LocalDateTime start;
    private LocalDateTime end;

    private Reservation(Room room, User user,LocalDateTime start,LocalDateTime end) {
        this.room = room;
        this.user = user;
        this.start = start;
        this.end = end;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
