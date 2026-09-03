package dentalclinic.dao;

import dentalclinic.dao.impl.AppointmentDAOImpl;
import dentalclinic.dao.impl.PatientDAOImpl;
import dentalclinic.model.*;
import dentalclinic.util.DBConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the prevent_double_booking MySQL TRIGGER (see
 * database/advanced_features_v2.sql). Requires that script to already
 * be applied. *IT naming: skipped automatically by CI.
 */
class DoubleBookingTriggerIT {

    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();

    private int patientAId = -1;
    private int patientBId = -1;
    private int firstAppointmentId = -1;

    @AfterEach
    void cleanUp() throws SQLException {
        try (Connection conn = DBConnectionManager.getInstance().getConnection()) {
            if (firstAppointmentId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM appointment WHERE appointment_id = ?")) {
                    ps.setInt(1, firstAppointmentId);
                    ps.executeUpdate();
                }
            }
            if (patientAId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM patient WHERE patient_id = ?")) {
                    ps.setInt(1, patientAId);
                    ps.executeUpdate();
                }
            }
            if (patientBId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM patient WHERE patient_id = ?")) {
                    ps.setInt(1, patientBId);
                    ps.executeUpdate();
                }
            }
        }
    }

    @Test
    void secondBooking_sameDentistSameDateTime_isRejected() throws SQLException {
        LocalDate date = LocalDate.now().plusDays(90); // far future, unlikely to clash with existing data
        LocalTime time = LocalTime.of(14, 0);

        Patient patientA = new Patient(0, "Double Book A", "1 Test Lane", "0775551111");
        patientDAO.save(patientA);
        patientAId = patientA.getPatientId();

        Appointment first = new Appointment(null, patientA, new Dentist(1, null, null),
                new TreatmentType(1, null, null), date, time, "SCHEDULED");
        appointmentDAO.save(first);
        firstAppointmentId = first.getAppointmentId();

        Patient patientB = new Patient(0, "Double Book B", "2 Test Lane", "0775552222");
        patientDAO.save(patientB);
        patientBId = patientB.getPatientId();

        Appointment secondSameSlot = new Appointment(null, patientB, new Dentist(1, null, null),
                new TreatmentType(1, null, null), date, time, "SCHEDULED");

        SQLException thrown = assertThrows(SQLException.class, () -> appointmentDAO.save(secondSameSlot));
        assertTrue(thrown.getMessage().contains("already has an appointment"),
                "Expected the trigger's rejection message, got: " + thrown.getMessage());
    }

    @Test
    void secondBooking_sameDentistDifferentTime_succeeds() throws SQLException {
        LocalDate date = LocalDate.now().plusDays(91);
        int secondAppointmentId = -1;

        Patient patientA = new Patient(0, "Double Book C", "1 Test Lane", "0775553333");
        patientDAO.save(patientA);
        patientAId = patientA.getPatientId();

        Appointment first = new Appointment(null, patientA, new Dentist(1, null, null),
                new TreatmentType(1, null, null), date, LocalTime.of(9, 0), "SCHEDULED");
        appointmentDAO.save(first);
        firstAppointmentId = first.getAppointmentId();

        Appointment secondDifferentTime = new Appointment(null, patientA, new Dentist(1, null, null),
                new TreatmentType(1, null, null), date, LocalTime.of(10, 0), "SCHEDULED");

        assertDoesNotThrow(() -> appointmentDAO.save(secondDifferentTime));
        secondAppointmentId = secondDifferentTime.getAppointmentId();

        // Clean up the second appointment too, since it succeeded and
        // won't be caught by this test's own @AfterEach fields.
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM appointment WHERE appointment_id = ?")) {
            ps.setInt(1, secondAppointmentId);
            ps.executeUpdate();
        }
    }
}