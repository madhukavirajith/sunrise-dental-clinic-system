package dentalclinic.service.notification;

import dentalclinic.model.Appointment;
import dentalclinic.model.Notification;

/**
 * OBSERVER PATTERN (observer role).
 *
 * Each implementation reacts to an appointment event in its own way
 * (build an email, build an SMS text) without NotificationDispatcher or
 * AppointmentServlet needing to know the details of any specific
 * channel. New channels (e.g. a future push-notification channel) can
 * be added by implementing this interface and registering it in
 * NotificationDispatcher, without modifying existing channel code -
 * Open/Closed Principle, same reasoning as the Strategy pattern used
 * for billing.
 */
public interface NotificationChannel {

    /**
     * Attempts to notify about the given appointment and returns a
     * Notification describing what was attempted and its outcome.
     * Never throws - failures are represented as a Notification with
     * status FAILED, not an exception, so one channel failing never
     * prevents other channels from running.
     */
    Notification notify(Appointment appointment);
}