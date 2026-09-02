package dentalclinic.model;

/**
 * Domain model representing one notification attempt (email or SMS)
 * associated with an appointment. Every attempt is recorded, regardless
 * of whether delivery was real, simulated, or failed - this history is
 * what powers the Notification Center feature.
 */
public class Notification {

    private int notificationId;
    private Appointment appointment;
    private String channel;   // "EMAIL" or "SMS"
    private String recipient;
    private String message;
    private String status;    // "SENT", "SIMULATED", or "FAILED"

    public Notification() {
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}