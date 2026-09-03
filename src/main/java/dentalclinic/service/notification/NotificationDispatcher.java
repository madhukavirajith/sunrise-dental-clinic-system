package dentalclinic.service.notification;

import dentalclinic.dao.NotificationDAO;
import dentalclinic.dao.impl.NotificationDAOImpl;
import dentalclinic.model.Appointment;
import dentalclinic.model.Notification;

import java.sql.SQLException;
import java.util.List;

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
                System.err.println("Failed to save notification record: " + e.getMessage());
            }
        }
    }
}