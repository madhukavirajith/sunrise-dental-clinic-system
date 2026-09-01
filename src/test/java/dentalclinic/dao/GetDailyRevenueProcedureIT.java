package dentalclinic.dao;

import dentalclinic.dao.impl.AppointmentDAOImpl;
import dentalclinic.dao.impl.PatientDAOImpl;
import dentalclinic.model.*;
import dentalclinic.util.DBConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test for the GetDailyRevenue stored procedure (added
 * Aug 25 - see database/procedures_and_triggers.sql). Requires that
 * script to already be applied to your local database. *IT naming:
 * skipped automatically in CI, same reasoning as the other IT tests.
 */
class GetDailyRevenueProcedureIT {

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

    private BigDecimal callProcedure(LocalDate date) throws SQLException {
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             CallableStatement cs = conn.prepareCall("{CALL GetDailyRevenue(?, ?)}")) {
            cs.setDate(1, java.sql.Date.valueOf(date));
            cs.registerOutParameter(2, Types.DECIMAL);
            cs.execute();
            BigDecimal result = cs.getBigDecimal(2);
            return result != null ? result : BigDecimal.ZERO;
        }
    }

    @Test
    void dateWithNoAppointments_returnsZero() throws SQLException {
        LocalDate emptyDate = LocalDate.now().plusYears(5); // very unlikely to have data
        BigDecimal revenue = callProcedure(emptyDate);
        assertEquals(0, BigDecimal.ZERO.compareTo(revenue));
    }

    @Test
    void dateWithOneAppointment_returnsMatchingBaseFee() throws SQLException {
        LocalDate targetDate = LocalDate.now().plusDays(45); // unlikely to clash

        Patient patient = new Patient(0, "Revenue Test Patient", "1 Test Lane", "0773333333");
        patientDAO.save(patient);
        createdPatientId = patient.getPatientId();

        // treatment_type_id=1 = "Routine Check-up", base_fee 500.00 (from schema.sql seed data)
        Appointment appointment = new Appointment(
                null, patient, new Dentist(1, null, null), new TreatmentType(1, null, null),
                targetDate, LocalTime.of(9, 0), "SCHEDULED"
        );
        appointmentDAO.save(appointment);
        createdAppointmentId = appointment.getAppointmentId();

        BigDecimal revenue = callProcedure(targetDate);

        assertEquals(0, new BigDecimal("500.00").compareTo(revenue));
    }
}