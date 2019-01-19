package Reservation;

import java.time.LocalDateTime;
import java.util.Observable;

public class Reservation extends Observable {

    private Room room;
    private int id;

    private String calId;

    public String getCalId() {
        return calId;
    }

    public void setCalId(String calid) {
        if(calid == null || calid.isEmpty()){
            throw new IllegalArgumentException();
        }
        calId = calid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    private Boolean hasStarted;

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private LocalDateTime start;
    private LocalDateTime end;

    public Reservation(String calId){
        this.setCalId(calId);
    }
    public Reservation(User user,LocalDateTime start,LocalDateTime end) {
        this.start = start;
        this.end = end;
        this.title = user.getName();
        this.userEmail = user.getEmail();
    }

    public Reservation(int id, String email, String name, boolean hasStarted, LocalDateTime start, LocalDateTime end){
        this.id = id;
        this.title = name;
        this.userEmail = email;
        this.hasStarted = hasStarted;
        this.start = start;
        this.end = end;
    }
    public Reservation(int id, User user, boolean hasStarted, LocalDateTime start, LocalDateTime end){
        this.id = id;
        this.userEmail = user.getEmail();
        this.title = user.getName();
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {

        this.room = room;
    }
}
