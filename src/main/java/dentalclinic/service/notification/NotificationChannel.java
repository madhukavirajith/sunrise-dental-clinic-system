package dentalclinic.service.notification;

import dentalclinic.model.Appointment;
import dentalclinic.model.Notification;

public interface NotificationChannel {

    Notification notify(Appointment appointment);
}