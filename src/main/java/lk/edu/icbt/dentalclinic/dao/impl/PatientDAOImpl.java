package lk.edu.icbt.dentalclinic.dao.impl;

import lk.edu.icbt.dentalclinic.dao.PatientDAO;
import lk.edu.icbt.dentalclinic.model.Patient;
import lk.edu.icbt.dentalclinic.util.DBConnectionManager;

import java.sql.*;
import java.util.Optional;

/**
 * DAO PATTERN (implementation half). Talks to MySQL via plain JDBC only -
 * no ORM framework, consistent with the "no third-party frameworks"
 * constraint (a JDBC driver is an allowed database dependency, not a
 * framework).
 *
 * TODO (student): this is a skeleton showing the pattern and the SQL
 * shape expected - extend with update()/delete() and proper exception
 * handling/logging as you build out the feature.
 */
public class PatientDAOImpl implements PatientDAO {

    @Override
    public Patient save(Patient patient) throws SQLException {
        String sql = "INSERT INTO patient (name, address, contact_number) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, patient.getName());
            ps.setString(2, patient.getAddress());
            ps.setString(3, patient.getContactNumber());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    patient.setPatientId(keys.getInt(1));
                }
            }
        }
        return patient;
    }

    @Override
    public Optional<Patient> findById(int patientId) throws SQLException {
        String sql = "SELECT patient_id, name, address, contact_number FROM patient WHERE patient_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Patient patient = new Patient(
                            rs.getInt("patient_id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("contact_number")
                    );
                    return Optional.of(patient);
                }
            }
        }
        return Optional.empty();
    }
}
