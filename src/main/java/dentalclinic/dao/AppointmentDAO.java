package dentalclinic.dao;

import dentalclinic.model.Appointment;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * DAO PATTERN (interface half) for Appointment persistence.
 * See PatientDAO for the pattern justification - the same reasoning
 * applies here.
 */
public interface AppointmentDAO {

    Appointment save(Appointment appointment) throws SQLException;

    /**
     * Core lookup used by "Display Appointment Details" (Task requirement):
     * search by the human-facing appointment number, not the internal
     * numeric primary key.
     */
    Optional<Appointment> findByAppointmentNumber(String appointmentNumber) throws SQLException;

    List<Appointment> findAll() throws SQLException;
}
