package dentalclinic.dao.impl;

import dentalclinic.dao.AppointmentDAO;
import dentalclinic.model.*;
import dentalclinic.util.DBConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentDAOImpl implements AppointmentDAO {

    private static final String SELECT_BASE =
            "SELECT a.appointment_id, a.appointment_number, a.appointment_date, a.appointment_time, a.status, " +
                    "       p.patient_id, p.name AS patient_name, p.address AS patient_address, p.contact_number AS patient_contact, " +
                    "       d.dentist_id, d.name AS dentist_name, d.specialization, " +
                    "       t.treatment_type_id, t.name AS treatment_name, t.base_fee " +
                    "FROM appointment a " +
                    "JOIN patient p ON a.patient_id = p.patient_id " +
                    "JOIN dentist d ON a.dentist_id = d.dentist_id " +
                    "JOIN treatment_type t ON a.treatment_type_id = t.treatment_type_id ";

    @Override
    public Appointment save(Appointment appointment) throws SQLException {
        String insertSql = "INSERT INTO appointment " +
                "(appointment_number, patient_id, dentist_id, treatment_type_id, appointment_date, appointment_time, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getInstance().getConnection()) {
            conn.setAutoCommit(false); // start a transaction - explained below
            try {

                String placeholder = "PENDING-" + System.nanoTime();
                try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, placeholder);
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

                String realNumber = String.format("APT-%06d", appointment.getAppointmentId());
                try (PreparedStatement ps2 = conn.prepareStatement(
                        "UPDATE appointment SET appointment_number = ? WHERE appointment_id = ?")) {
                    ps2.setString(1, realNumber);
                    ps2.setInt(2, appointment.getAppointmentId());
                    ps2.executeUpdate();
                }
                appointment.setAppointmentNumber(realNumber);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
        return appointment;
    }

    @Override
    public Optional<Appointment> findByAppointmentNumber(String appointmentNumber) throws SQLException {
        String sql = SELECT_BASE + "WHERE a.appointment_number = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, appointmentNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToAppointment(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Appointment> findAll() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY a.appointment_date, a.appointment_time";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                appointments.add(mapRowToAppointment(rs));
            }
        }
        return appointments;
    }

    private Appointment mapRowToAppointment(ResultSet rs) throws SQLException {
        Patient patient = new Patient(
                rs.getInt("patient_id"),
                rs.getString("patient_name"),
                rs.getString("patient_address"),
                rs.getString("patient_contact")
        );

        Dentist dentist = new Dentist(
                rs.getInt("dentist_id"),
                rs.getString("dentist_name"),
                rs.getString("specialization")
        );

        TreatmentType treatmentType = new TreatmentType(
                rs.getInt("treatment_type_id"),
                rs.getString("treatment_name"),
                rs.getBigDecimal("base_fee")
        );

        Appointment appointment = new Appointment(
                rs.getString("appointment_number"),
                patient,
                dentist,
                treatmentType,
                rs.getDate("appointment_date").toLocalDate(),
                rs.getTime("appointment_time").toLocalTime(),
                rs.getString("status")
        );
        appointment.setAppointmentId(rs.getInt("appointment_id"));

        return appointment;
    }
}