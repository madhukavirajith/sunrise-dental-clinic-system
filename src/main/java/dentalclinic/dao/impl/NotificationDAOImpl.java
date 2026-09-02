package dentalclinic.dao.impl;

import dentalclinic.dao.NotificationDAO;
import dentalclinic.model.Notification;
import dentalclinic.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOImpl implements NotificationDAO {

    @Override
    public Notification save(Notification notification) throws SQLException {
        String sql = "INSERT INTO notification (appointment_id, channel, recipient, message, status) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, notification.getAppointment().getAppointmentId());
            ps.setString(2, notification.getChannel());
            ps.setString(3, notification.getRecipient());
            ps.setString(4, notification.getMessage());
            ps.setString(5, notification.getStatus());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    notification.setNotificationId(keys.getInt(1));
                }
            }
        }
        return notification;
    }

    @Override
    public List<Notification> findAll() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT n.notification_id, n.channel, n.recipient, n.message, n.status, n.sent_at, " +
                "       a.appointment_number " +
                "FROM notification n " +
                "JOIN appointment a ON n.appointment_id = a.appointment_id " +
                "ORDER BY n.sent_at DESC";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                notifications.add(mapRow(rs));
            }
        }
        return notifications;
    }

    @Override
    public List<Notification> findByAppointmentId(int appointmentId) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT n.notification_id, n.channel, n.recipient, n.message, n.status, n.sent_at, " +
                "       a.appointment_number " +
                "FROM notification n " +
                "JOIN appointment a ON n.appointment_id = a.appointment_id " +
                "WHERE n.appointment_id = ? " +
                "ORDER BY n.sent_at DESC";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapRow(rs));
                }
            }
        }
        return notifications;
    }

    /**
     * Lightweight mapping: only appointmentNumber is populated on the
     * nested Appointment (not the full patient/dentist/treatment graph),
     * since that's all the Notification Center view needs to display.
     */
    private Notification mapRow(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getInt("notification_id"));
        notification.setChannel(rs.getString("channel"));
        notification.setRecipient(rs.getString("recipient"));
        notification.setMessage(rs.getString("message"));
        notification.setStatus(rs.getString("status"));

        dentalclinic.model.Appointment appointment = new dentalclinic.model.Appointment();
        appointment.setAppointmentNumber(rs.getString("appointment_number"));
        notification.setAppointment(appointment);

        return notification;
    }
}