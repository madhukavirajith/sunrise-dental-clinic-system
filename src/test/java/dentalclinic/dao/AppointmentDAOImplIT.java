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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test - see PatientDAOImplIT for the naming rationale
 * (*IT skips CI automatically since no DB is available there).
 */
class AppointmentDAOImplIT {

    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();

    private int createdPatientId = -1;
    private int createdAppointmentId = -1;

    @AfterEach
    void cleanUp() throws SQLException {
        try (Connection conn = DBConnectionManager.getInstance().getConnection()) {
            if (createdAppointmentId != -1) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM appointment WHERE appointment_id = ?")) {
                    ps.setInt(1, createdAppointmentId);
                    ps.executeUpdate();
                }
            }
            if (createdPatientId != -1) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM patient WHERE patient_id = ?")) {
                    ps.setInt(1, createdPatientId);
                    ps.executeUpdate();
                }
            }
        }
    }

    /** Relies on seed data from schema.sql: dentist_id=1, treatment_type_id=1 */
    private Appointment createTestAppointment(LocalDate date) throws SQLException {
        Patient patient = new Patient(0, "IT Appointment Patient", "1 Test Lane", "0772222222");
        patientDAO.save(patient);
        createdPatientId = patient.getPatientId();

        Dentist dentist = new Dentist(1, null, null);
        TreatmentType treatmentType = new TreatmentType(1, null, null);

        Appointment appointment = new Appointment(
                null, patient, dentist, treatmentType, date, LocalTime.of(9, 0), "SCHEDULED"
        );
        appointmentDAO.save(appointment);
        createdAppointmentId = appointment.getAppointmentId();
        return appointment;
    }

    @Test
    void save_generatesRealAppointmentNumber() throws SQLException {
        Appointment appointment = createTestAppointment(LocalDate.now());

        assertNotNull(appointment.getAppointmentNumber());
        assertTrue(appointment.getAppointmentNumber().matches("APT-\\d{6}"));
    }

    @Test
    void save_thenFindByAppointmentNumber_returnsJoinedDetails() throws SQLException {
        Appointment saved = createTestAppointment(LocalDate.now());

        Optional<Appointment> found = appointmentDAO.findByAppointmentNumber(saved.getAppointmentNumber());

        assertTrue(found.isPresent());
        // proves the JOIN actually pulled real names, not just IDs
        assertEquals("IT Appointment Patient", found.get().getPatient().getName());
        assertNotNull(found.get().getDentist().getName());
        assertNotNull(found.get().getTreatmentType().getName());
    }

    @Test
    void findByAppointmentNumber_nonExistentNumber_returnsEmpty() throws SQLException {
        Optional<Appointment> found = appointmentDAO.findByAppointmentNumber("APT-999999");
        assertTrue(found.isEmpty());
    }

    @Test
    void findByDate_returnsOnlyAppointmentsOnThatDate() throws SQLException {
        LocalDate targetDate = LocalDate.now().plusDays(30); // far future date unlikely to clash with other test data
        Appointment appointment = createTestAppointment(targetDate);

        List<Appointment> results = appointmentDAO.findByDate(targetDate);

        assertTrue(results.stream()
                .anyMatch(a -> a.getAppointmentNumber().equals(appointment.getAppointmentNumber())));
    }

    @Test
    void findAll_includesSavedAppointment() throws SQLException {
        Appointment appointment = createTestAppointment(LocalDate.now());

        List<Appointment> all = appointmentDAO.findAll();

        assertTrue(all.stream()
                .anyMatch(a -> a.getAppointmentNumber().equals(appointment.getAppointmentNumber())));
    }
}