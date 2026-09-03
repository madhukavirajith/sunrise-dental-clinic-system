package dentalclinic.service.notification;

import dentalclinic.model.Appointment;
import dentalclinic.model.Notification;

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
        if (message.length() > SMS_CHARACTER_LIMIT) {
            message = message.substring(0, SMS_CHARACTER_LIMIT - 3) + "...";
        }
        return message;
    }
}