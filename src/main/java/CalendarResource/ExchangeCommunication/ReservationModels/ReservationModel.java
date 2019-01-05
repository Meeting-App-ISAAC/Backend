package CalendarResource.ExchangeCommunication.ReservationModels;

import java.util.ArrayList;

public class ReservationModel {
    private String subject;
    Body body = new Body();
    Start start = new Start();
    End end = new End();
    Location location = new Location();
    ArrayList<Attendee> attendees = new ArrayList <> ();


    // Getter Methods

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public End getEnd() {
        return end;
    }

    public void setEnd(End end) {
        this.end = end;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
    }
}

