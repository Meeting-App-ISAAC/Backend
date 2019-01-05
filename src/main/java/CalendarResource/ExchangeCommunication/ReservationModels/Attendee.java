package CalendarResource.ExchangeCommunication.ReservationModels;

public class Attendee {
    private EmailAddress emailAddress = new EmailAddress();
    private String type;

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
