package dentalclinic.dao;

import dentalclinic.model.Patient;

import java.sql.SQLException;
import java.util.Optional;

/**
 * DAO PATTERN (interface half).
 *
 * Justification: the service/servlet layers depend on this interface, not
 * on a concrete JDBC implementation - this is the Dependency Inversion
 * Principle (the "D" in SOLID) in action. It also means PatientDAOImpl can
 * be swapped for a different persistence mechanism (or a mock, for unit
 * testing) without changing any calling code.
 */
public interface PatientDAO {

    Patient save(Patient patient) throws SQLException;

    Optional<Patient> findById(int patientId) throws SQLException;
}
