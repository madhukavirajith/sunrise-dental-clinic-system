package dentalclinic.dao.impl;

import dentalclinic.dao.PatientDAO;
import dentalclinic.model.Patient;
import dentalclinic.util.DBConnectionManager;

import java.sql.*;
import java.util.Optional;

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
    @Override
    public void update(Patient patient) throws SQLException {
        String sql = "UPDATE patient SET name = ?, address = ?, contact_number = ? WHERE patient_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, patient.getName());
            ps.setString(2, patient.getAddress());
            ps.setString(3, patient.getContactNumber());
            ps.setInt(4, patient.getPatientId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No patient found with id " + patient.getPatientId() + " to update");
            }
        }
    }

    @Override
    public void delete(int patientId) throws SQLException {
        String sql = "DELETE FROM patient WHERE patient_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No patient found with id " + patientId + " to delete");
            }
        }
    }
}
