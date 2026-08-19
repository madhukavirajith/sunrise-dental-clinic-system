package dentalclinic.dao.impl;

import dentalclinic.dao.AppointmentDAO;
import dentalclinic.model.Appointment;
import dentalclinic.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO PATTERN (implementation half) for Appointment persistence.
 *
 * TODO (student): this skeleton intentionally leaves the row -> object
 * mapping simplified (patient/dentist/treatmentType are not fully
 * hydrated here). Extend with JOINs against patient/dentist/treatment_type
 * tables, or compose calls to the other DAOs, once those are wired up.
 */
public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public Appointment save(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointment " +
                "(appointment_number, patient_id, dentist_id, treatment_type_id, appointment_date, appointment_time, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, appointment.getAppointmentNumber());
            ps.setInt(2, appointment.getPatient().getPatientId());
            ps.setInt(3, appointment.getDentist().getDentistId());
            ps.setInt(4, appointment.getTreatmentType().getTreatmentTypeId());
            ps.setDate(5, Date.valueOf(appointment.getAppointmentDate()));
            ps.setTime(6, Time.valueOf(appointment.getAppointmentTime()));
            ps.setString(7, appointment.getStatus());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    appointment.setAppointmentId(keys.getInt(1));
                }
            }
        }
        return appointment;
    }

    @Override
    public Optional<Appointment> findByAppointmentNumber(String appointmentNumber) throws SQLException {
        // TODO (student): implement with a JOIN across patient/dentist/
        // treatment_type and map the full Appointment graph.
        throw new UnsupportedOperationException("Implement lookup by appointment_number");
    }

    @Override
    public List<Appointment> findAll() throws SQLException {
        // TODO (student): implement listing for reports/admin views.
        return new ArrayList<>();
    }
}
