package dentalclinic.dao;

import dentalclinic.model.Patient;

import java.sql.SQLException;
import java.util.Optional;

/**
 * DAO PATTERN (interface half). Calling code depends on this interface,
 * not on a concrete JDBC implementation.
 */
public interface PatientDAO {

    Patient save(Patient patient) throws SQLException;

    Optional<Patient> findById(int patientId) throws SQLException;

    void update(Patient patient) throws SQLException;

    void delete(int patientId) throws SQLException;

    /**
     * Looks up an existing patient by their contact number, so a
     * returning patient's record is reused rather than a duplicate
     * being created on every appointment registration.
     */
    Optional<Patient> findByContactNumber(String contactNumber) throws SQLException;

    /**
     * Calls the GetPatientAppointmentCount MySQL FUNCTION (see
     * database/advanced_features_v2.sql) to count how many
     * appointments this patient has ever had.
     */
    int countAppointments(int patientId) throws SQLException;
}