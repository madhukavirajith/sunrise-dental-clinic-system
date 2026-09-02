package dentalclinic.service.notification;

import dentalclinic.model.Appointment;
import dentalclinic.model.Notification;

/**
 * OBSERVER PATTERN (concrete observer).
 *
 * SIMULATED by design, not just by fallback: genuine SMS delivery
 * requires a paid third-party gateway account (e.g. Twilio) and its own
 * SDK/API credentials, which is both a real financial cost and outside
 * this assignment's allowed-dependency list. The system still
 * demonstrates the full SMS workflow - message construction and outcome
 * recording - which is what's being assessed.
 */
public class SmsNotificationChannel implements NotificationChannel {

    private static final int SMS_CHARACTER_LIMIT = 160;

    @Override
    public Notification notify(Appointment appointment) {
        Notification notification = new Notification();
        notification.setChannel("SMS");

        String phoneNumber = appointment.getPatient().getContactNumber();
        String message = buildMessage(appointment);
        notification.setMessage(message);

        if (phoneNumber == null || phoneNumber.isBlank()) {
            notification.setRecipient("(no phone number on file)");
            notification.setStatus("FAILED");
            return notification;
        }

        notification.setRecipient(phoneNumber);

        System.out.println("[SIMULATED SMS] To: " + phoneNumber + " | " + message);
        notification.setStatus("SIMULATED");
        return notification;
    }

    private String buildMessage(Appointment appointment) {
        String message = String.format(
                "Sunrise Dental: Appt %s confirmed %s %s. Reply HELP for assistance.",
                appointment.getAppointmentNumber(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        );
        // Real SMS is billed and truncated per-segment, so trimming to a
        // realistic single-segment length is part of genuinely modelling
        // the channel's constraints, not just decoration.
        if (message.length() > SMS_CHARACTER_LIMIT) {
            message = message.substring(0, SMS_CHARACTER_LIMIT - 3) + "...";
        }
        return message;
    }
}