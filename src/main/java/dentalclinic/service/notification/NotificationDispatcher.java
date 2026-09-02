package dentalclinic.service.notification;

import dentalclinic.dao.NotificationDAO;
import dentalclinic.dao.impl.NotificationDAOImpl;
import dentalclinic.model.Appointment;
import dentalclinic.model.Notification;

import java.sql.SQLException;
import java.util.List;

/**
 * OBSERVER PATTERN (subject role).
 *
 * AppointmentServlet only ever calls notifyAppointmentRegistered() -
 * it has no knowledge of how many notification channels exist or what
 * they do. Adding a future channel (e.g. push notifications) means
 * adding one line to the "channels" list below; AppointmentServlet
 * never needs to change. This decoupling is the entire point of the
 * Observer pattern, and is what distinguishes it from simply calling
 * two methods directly from the servlet.
 */
public class NotificationDispatcher {

    private final List<NotificationChannel> channels;
    private final NotificationDAO notificationDAO;

    public NotificationDispatcher() {
        this.channels = List.of(
                new EmailNotificationChannel(),
                new SmsNotificationChannel()
        );
        this.notificationDAO = new NotificationDAOImpl();
    }

    public void notifyAppointmentRegistered(Appointment appointment) {
        for (NotificationChannel channel : channels) {
            Notification notification = channel.notify(appointment);
            notification.setAppointment(appointment);
            try {
                notificationDAO.save(notification);
            } catch (SQLException e) {
                // A persistence failure here should not break appointment
                // registration itself, which has already succeeded by the
                // time this runs - log and continue to the next channel.
                System.err.println("Failed to save notification record: " + e.getMessage());
            }
        }
    }
}