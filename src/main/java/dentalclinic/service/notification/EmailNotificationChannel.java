package dentalclinic.service.notification;

import dentalclinic.model.Appointment;
import dentalclinic.model.Notification;

/**
 * OBSERVER PATTERN (concrete observer).
 *
 * Builds a real confirmation message and records a real delivery
 * attempt, but the transport is SIMULATED (logged, not actually sent
 * over SMTP). This project's allowed-dependency list does not include
 * a mail library, so real delivery infrastructure was treated as out
 * of scope - documented here and in the report as a deliberate
 * assumption, not an oversight. The full workflow (message
 * construction, channel selection, outcome recording) is genuinely
 * exercised regardless.
 *
 * See EmailSenderSmtp.java for an optional, dependency-free real-SMTP
 * implementation if you want to demonstrate actual delivery as a
 * stretch goal.
 */
public class EmailNotificationChannel implements NotificationChannel {

    @Override
    public Notification notify(Appointment appointment) {
        Notification notification = new Notification();
        notification.setChannel("EMAIL");

        String recipientEmail = appointment.getPatient().getEmail();
        String message = buildMessage(appointment);
        notification.setMessage(message);

        if (recipientEmail == null || recipientEmail.isBlank()) {
            notification.setRecipient("(no email on file)");
            notification.setStatus("FAILED");
            return notification;
        }

        notification.setRecipient(recipientEmail);

        // SIMULATED delivery - see class Javadoc above for the reasoning.
        System.out.println("[SIMULATED EMAIL] To: " + recipientEmail + " | " + message);
        notification.setStatus("SIMULATED");
        return notification;
    }

    private String buildMessage(Appointment appointment) {
        return String.format(
                "Dear %s, your appointment %s with %s for %s is confirmed on %s at %s.",
                appointment.getPatient().getName(),
                appointment.getAppointmentNumber(),
                appointment.getDentist().getName(),
                appointment.getTreatmentType().getName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );
    }
}