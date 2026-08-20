package dentalclinic.dao;

import dentalclinic.model.Patient;

import java.sql.SQLException;
import java.util.Optional;

public interface PatientDAO {

    Patient save(Patient patient) throws SQLException;

    Optional<Patient> findById(int patientId) throws SQLException;

    void update(Patient patient) throws SQLException;

    void delete(int patientId) throws SQLException;
}