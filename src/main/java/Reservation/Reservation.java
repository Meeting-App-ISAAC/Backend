package Reservation;

import java.time.LocalDateTime;
import java.util.Observable;

public class Reservation extends Observable {

    private User user;
    private Boolean hasStarted;

    private LocalDateTime start;
    private LocalDateTime end;

    private Reservation(User user,LocalDateTime start,LocalDateTime end) {
        this.user = user;
        this.start = start;
        this.end = end;
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

    public Boolean getHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(Boolean hasStarted) {
        this.hasStarted = hasStarted;
    }
}
