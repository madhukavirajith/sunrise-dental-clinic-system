package dentalclinic.dao;

import dentalclinic.model.Notification;

import java.sql.SQLException;
import java.util.List;

public interface NotificationDAO {

    Notification save(Notification notification) throws SQLException;

    List<Notification> findAll() throws SQLException;

    List<Notification> findByAppointmentId(int appointmentId) throws SQLException;
}