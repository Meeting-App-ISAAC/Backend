package Reservation;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.UUID;

public class Reservation extends Observable {

    private int id;
    private User user;
    private Boolean hasStarted;


    private LocalDateTime start;
    private LocalDateTime end;

    public Reservation(User user,LocalDateTime start,LocalDateTime end) {
        this.user = user;
        this.start = start;
        this.end = end;
    }

    public Reservation(int id, User user, boolean hasStarted, LocalDateTime start, LocalDateTime end){
        this.id = id;
        this.user = user;
        this.hasStarted = hasStarted;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void Changed(Changed changed){
        ChangedObject obj = new ChangedObject();
        obj.setChanged(changed);
        obj.setArg(this);

        setChanged();
        notifyObservers(obj);
    }
}
