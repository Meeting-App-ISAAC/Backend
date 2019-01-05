package CalendarResource.ExchangeCommunication.ReservationModels;

public class End {
    private String dateTime;
    private String timeZone;


    // Getter Methods

    public String getDateTime() {
        return dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    // Setter Methods

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
