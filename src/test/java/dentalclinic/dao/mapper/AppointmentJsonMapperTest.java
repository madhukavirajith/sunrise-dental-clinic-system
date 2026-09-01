package dentalclinic.dao.mapper;

import dentalclinic.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppointmentJsonMapperTest {

    private Appointment buildSampleAppointment(String patientName, String specialization) {
        Patient patient = new Patient(1, patientName, "1 Test Lane", "0771234567");
        Dentist dentist = new Dentist(1, "Dr. Perera", specialization);
        TreatmentType treatmentType = new TreatmentType(1, "Routine Check-up", new BigDecimal("500.00"));
        Appointment appointment = new Appointment(
                "APT-000001", patient, dentist, treatmentType,
                LocalDate.of(2026, 9, 1), LocalTime.of(10, 30), "SCHEDULED"
        );
        return appointment;
    }

    @Test
    void toJson_includesAppointmentNumber() {
        String json = AppointmentJsonMapper.toJson(buildSampleAppointment("Jane Doe", "General"));
        assertTrue(json.contains("\"appointmentNumber\":\"APT-000001\""));
    }

    @Test
    void toJson_includesPatientName() {
        String json = AppointmentJsonMapper.toJson(buildSampleAppointment("Jane Doe", "General"));
        assertTrue(json.contains("\"name\":\"Jane Doe\""));
    }

    @Test
    void toJson_escapesDoubleQuoteInName() {
        // Corner case: a name containing a character that would otherwise
        // break the JSON syntax if not escaped.
        String json = AppointmentJsonMapper.toJson(buildSampleAppointment("Jane \"JD\" Doe", "General"));
        assertTrue(json.contains("Jane \\\"JD\\\" Doe"));
    }

    @Test
    void toJson_nullSpecialization_rendersAsJsonNull() {
        String json = AppointmentJsonMapper.toJson(buildSampleAppointment("Jane Doe", null));
        assertTrue(json.contains("\"specialization\":null"));
    }
}