package CalendarResource.ExchangeCommunication.ReservationModels;

public class Body {
    private String contentType;
    private String content;


    // Getter Methods

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    // Setter Methods

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
