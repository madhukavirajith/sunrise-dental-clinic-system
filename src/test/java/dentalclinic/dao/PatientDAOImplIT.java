package dentalclinic.dao;

import dentalclinic.dao.impl.AppointmentDAOImpl;
import dentalclinic.dao.impl.PatientDAOImpl;
import dentalclinic.model.Appointment;
import dentalclinic.model.Dentist;
import dentalclinic.model.Patient;
import dentalclinic.model.TreatmentType;
import dentalclinic.util.DBConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test - requires a running local MySQL (WAMP) with the
 * schema, including notifications_and_email.sql and
 * advanced_features_v2.sql, already applied. Named *IT (not *Test) so
 * Maven's default Surefire test runner skips it automatically during
 * CI, where no database is available. Run manually in IntelliJ during
 * development.
 */
class PatientDAOImplIT {

    private final PatientDAO dao = new PatientDAOImpl();
    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private int createdPatientId = -1;
    private int createdAppointmentId = -1;

    @AfterEach
    void cleanUp() throws SQLException {
        if (createdAppointmentId != -1) {
            try (Connection conn = DBConnectionManager.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM appointment WHERE appointment_id = ?")) {
                ps.setInt(1, createdAppointmentId);
                ps.executeUpdate();
            }
        }
        if (createdPatientId != -1) {
            try {
                dao.delete(createdPatientId);
            } catch (SQLException ignored) {
                // already deleted by the test itself - fine
            }
        }
    }

    @Test
    void save_assignsGeneratedId() throws SQLException {
        Patient patient = new Patient(0, "IT Test Patient", "1 Test Lane", "0771111111");

        dao.save(patient);
        createdPatientId = patient.getPatientId();

        assertTrue(patient.getPatientId() > 0);
    }

    @Test
    void save_thenFindById_returnsSameData() throws SQLException {
        Patient patient = new Patient(0, "IT Test Patient", "1 Test Lane", "0771111111");
        dao.save(patient);
        createdPatientId = patient.getPatientId();

        Optional<Patient> found = dao.findById(patient.getPatientId());

        assertTrue(found.isPresent());
        assertEquals("IT Test Patient", found.get().getName());
        assertEquals("0771111111", found.get().getContactNumber());
    }

    @Test
    void findById_nonExistentId_returnsEmpty() throws SQLException {
        Optional<Patient> found = dao.findById(999_999);
        assertTrue(found.isEmpty());
    }

    @Test
    void update_changesStoredAddress() throws SQLException {
        Patient patient = new Patient(0, "IT Test Patient", "1 Test Lane", "0771111111");
        dao.save(patient);
        createdPatientId = patient.getPatientId();

        patient.setAddress("2 Updated Road");
        dao.update(patient);

        Optional<Patient> found = dao.findById(patient.getPatientId());
        assertEquals("2 Updated Road", found.get().getAddress());
    }

    @Test
    void update_nonExistentId_throwsSQLException() {
        Patient ghost = new Patient(999_999, "Nobody", "Nowhere", "0000000000");
        assertThrows(SQLException.class, () -> dao.update(ghost));
    }

    @Test
    void delete_removesPatient() throws SQLException {
        Patient patient = new Patient(0, "IT Test Patient", "1 Test Lane", "0771111111");
        dao.save(patient);
        int id = patient.getPatientId();

        dao.delete(id);
        createdPatientId = -1; // already deleted - don't try again in cleanUp

        assertTrue(dao.findById(id).isEmpty());
    }

    @Test
    void delete_nonExistentId_throwsSQLException() {
        assertThrows(SQLException.class, () -> dao.delete(999_999));
    }

    @Test
    void findByContactNumber_existingPatient_returnsPatient() throws SQLException {
        Patient patient = new Patient(0, "Contact Lookup Patient", "1 Test Lane", "0779998888");
        dao.save(patient);
        createdPatientId = patient.getPatientId();

        Optional<Patient> found = dao.findByContactNumber("0779998888");

        assertTrue(found.isPresent());
        assertEquals("Contact Lookup Patient", found.get().getName());
    }

    @Test
    void findByContactNumber_nonExistentNumber_returnsEmpty() throws SQLException {
        Optional<Patient> found = dao.findByContactNumber("0000000000");
        assertTrue(found.isEmpty());
    }

    @Test
    void countAppointments_noAppointments_returnsZero() throws SQLException {
        Patient patient = new Patient(0, "Zero Appointments Patient", "1 Test Lane", "0778887777");
        dao.save(patient);
        createdPatientId = patient.getPatientId();

        int count = dao.countAppointments(patient.getPatientId());

        assertEquals(0, count);
    }

    @Test
    void countAppointments_afterOneBooking_returnsOne() throws SQLException {
        // Exercises the GetPatientAppointmentCount MySQL FUNCTION
        // end-to-end through PatientDAO, not just testing the DAO's SQL
        // in isolation.
        Patient patient = new Patient(0, "One Appointment Patient", "1 Test Lane", "0778886666");
        dao.save(patient);
        createdPatientId = patient.getPatientId();

        Appointment appointment = new Appointment(null, patient, new Dentist(1, null, null),
                new TreatmentType(1, null, null), LocalDate.now().plusDays(120), LocalTime.of(15, 0), "SCHEDULED");
        appointmentDAO.save(appointment);
        createdAppointmentId = appointment.getAppointmentId();

        int count = dao.countAppointments(patient.getPatientId());

        assertEquals(1, count);
    }
}