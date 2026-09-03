package dentalclinic.service.notification;

import dentalclinic.dao.AppointmentDAO;
import dentalclinic.dao.NotificationDAO;
import dentalclinic.dao.PatientDAO;
import dentalclinic.dao.impl.AppointmentDAOImpl;
import dentalclinic.dao.impl.NotificationDAOImpl;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test - requires a running local MySQL with schema.sql AND
 * notifications_and_email.sql already applied. *IT naming: skipped
 * automatically by CI (see PatientDAOImplIT for the naming rationale).
 */
class NotificationDispatcherIT {

    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private final NotificationDAO notificationDAO = new NotificationDAOImpl();
    private final NotificationDispatcher dispatcher = new NotificationDispatcher();

    private int createdPatientId = -1;
    private int createdAppointmentId = -1;

    @AfterEach
    void cleanUp() throws SQLException {
        try (Connection conn = DBConnectionManager.getInstance().getConnection()) {
            if (createdAppointmentId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM notification WHERE appointment_id = ?")) {
                    ps.setInt(1, createdAppointmentId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM appointment WHERE appointment_id = ?")) {
                    ps.setInt(1, createdAppointmentId);
                    ps.executeUpdate();
                }
            }
            if (createdPatientId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM patient WHERE patient_id = ?")) {
                    ps.setInt(1, createdPatientId);
                    ps.executeUpdate();
                }
            }
        }
    }

    @Test
    void notifyAppointmentRegistered_createsOneRecordPerChannel() throws SQLException {
        Patient patient = new Patient(0, "Notify Test Patient", "1 Test Lane", "0774445555");
        patient.setEmail("notifytest@example.com");
        patientDAO.save(patient);
        createdPatientId = patient.getPatientId();

        Dentist dentist = new Dentist(1, null, null);
        TreatmentType treatmentType = new TreatmentType(1, null, null);
        Appointment appointment = new Appointment(null, patient, dentist, treatmentType,
                LocalDate.now().plusDays(60), LocalTime.of(11, 0), "SCHEDULED");
        appointmentDAO.save(appointment);
        createdAppointmentId = appointment.getAppointmentId();

        Appointment fullAppointment = appointmentDAO
                .findByAppointmentNumber(appointment.getAppointmentNumber())
                .orElseThrow();

        dispatcher.notifyAppointmentRegistered(fullAppointment);

        List<Notification> notifications = notificationDAO.findByAppointmentId(createdAppointmentId);

        assertEquals(2, notifications.size(), "Expected one EMAIL and one SMS notification record");
        assertTrue(notifications.stream().anyMatch(n -> "EMAIL".equals(n.getChannel())));
        assertTrue(notifications.stream().anyMatch(n -> "SMS".equals(n.getChannel())));
    }
}