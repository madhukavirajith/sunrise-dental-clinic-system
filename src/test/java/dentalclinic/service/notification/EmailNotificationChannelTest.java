package dentalclinic.service.notification;

import dentalclinic.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class EmailNotificationChannelTest {

    private Appointment buildAppointment(String email) {
        Patient patient = new Patient(1, "Jane Doe", "1 Test Lane", "0771234567");
        patient.setEmail(email);
        Dentist dentist = new Dentist(1, "Dr. Perera", "General Dentistry");
        TreatmentType treatmentType = new TreatmentType(1, "Routine Check-up", new BigDecimal("500.00"));
        return new Appointment("APT-000001", patient, dentist, treatmentType,
                LocalDate.of(2026, 9, 1), LocalTime.of(10, 30), "SCHEDULED");
    }

    @Test
    void notify_withEmail_returnsSimulatedStatus() {
        EmailNotificationChannel channel = new EmailNotificationChannel();
        Notification result = channel.notify(buildAppointment("jane@example.com"));

        assertEquals("EMAIL", result.getChannel());
        assertEquals("jane@example.com", result.getRecipient());
        assertEquals("SIMULATED", result.getStatus());
    }

    @Test
    void notify_withEmail_messageIncludesAppointmentAndPatientDetails() {
        EmailNotificationChannel channel = new EmailNotificationChannel();
        Notification result = channel.notify(buildAppointment("jane@example.com"));

        assertTrue(result.getMessage().contains("APT-000001"));
        assertTrue(result.getMessage().contains("Jane Doe"));
    }

    @Test
    void notify_nullEmail_returnsFailedStatus() {
        EmailNotificationChannel channel = new EmailNotificationChannel();
        Notification result = channel.notify(buildAppointment(null));

        assertEquals("FAILED", result.getStatus());
        assertEquals("(no email on file)", result.getRecipient());
    }

    @Test
    void notify_blankEmail_returnsFailedStatus() {
        // Corner case: whitespace-only is not the same as null, but
        // should be treated the same way.
        EmailNotificationChannel channel = new EmailNotificationChannel();
        Notification result = channel.notify(buildAppointment("   "));

        assertEquals("FAILED", result.getStatus());
    }
}