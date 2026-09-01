package dentalclinic.dao;

import dentalclinic.model.Appointment;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentDAO {

    Appointment save(Appointment appointment) throws SQLException;

    Optional<Appointment> findByAppointmentNumber(String appointmentNumber) throws SQLException;

    List<Appointment> findAll() throws SQLException;

    List<Appointment> findByDate(LocalDate date) throws SQLException;
}
