package dentalclinic.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern CONTACT_NUMBER_PATTERN = Pattern.compile("^0\\d{9}$");

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidContactNumber(String value) {
        return value != null && CONTACT_NUMBER_PATTERN.matcher(value.trim()).matches();
    }

    public static boolean isTodayOrFutureDate(LocalDate date) {
        return date != null && !date.isBefore(LocalDate.now());
    }
}