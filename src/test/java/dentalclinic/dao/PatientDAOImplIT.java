package dentalclinic.dao;

import dentalclinic.dao.impl.PatientDAOImpl;
import dentalclinic.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test - requires a running local MySQL (WAMP) with the
 * schema already imported. Named *IT (not *Test) so Maven's default
 * Surefire test runner skips it automatically during CI, where no
 * database is available. Run manually in IntelliJ during development.
 */
class PatientDAOImplIT {

    private final PatientDAO dao = new PatientDAOImpl();
    private int createdPatientId = -1;

    @AfterEach
    void cleanUp() throws SQLException {
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
}