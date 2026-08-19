package dentalclinic.exception;

/**
 * Thrown when a lookup by appointment number finds no matching record.
 * A custom checked/unchecked exception type (rather than propagating raw
 * SQLException or returning null) keeps error handling explicit and
 * meaningful in the service/servlet layers.
 */
public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(String appointmentNumber) {
        super("No appointment found with number: " + appointmentNumber);
    }
}
