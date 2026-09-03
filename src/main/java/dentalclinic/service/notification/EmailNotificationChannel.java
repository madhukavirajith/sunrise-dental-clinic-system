package dentalclinic.service.notification;

import dentalclinic.model.Appointment;
import dentalclinic.model.Notification;

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