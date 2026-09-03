package dentalclinic.service.notification;

import dentalclinic.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class SmsNotificationChannelTest {

    private Appointment buildAppointment(String contactNumber) {
        Patient patient = new Patient(1, "Jane Doe", "1 Test Lane", contactNumber);
        Dentist dentist = new Dentist(1, "Dr. Perera", "General Dentistry");
        TreatmentType treatmentType = new TreatmentType(1, "Routine Check-up", new BigDecimal("500.00"));
        return new Appointment("APT-000001", patient, dentist, treatmentType,
                LocalDate.of(2026, 9, 1), LocalTime.of(10, 30), "SCHEDULED");
    }

    @Test
    void notify_withPhoneNumber_returnsSimulatedStatus() {
        SmsNotificationChannel channel = new SmsNotificationChannel();
        Notification result = channel.notify(buildAppointment("0771234567"));

        assertEquals("SMS", result.getChannel());
        assertEquals("0771234567", result.getRecipient());
        assertEquals("SIMULATED", result.getStatus());
    }

    @Test
    void notify_messageStaysWithinSmsCharacterLimit() {
        // Boundary check: real SMS is billed/truncated per 160-char
        // segment, so the message must never exceed that.
        SmsNotificationChannel channel = new SmsNotificationChannel();
        Notification result = channel.notify(buildAppointment("0771234567"));

        assertTrue(result.getMessage().length() <= 160);
    }

    @Test
    void notify_nullContactNumber_returnsFailedStatus() {
        SmsNotificationChannel channel = new SmsNotificationChannel();
        Notification result = channel.notify(buildAppointment(null));

        assertEquals("FAILED", result.getStatus());
        assertEquals("(no phone number on file)", result.getRecipient());
    }
}