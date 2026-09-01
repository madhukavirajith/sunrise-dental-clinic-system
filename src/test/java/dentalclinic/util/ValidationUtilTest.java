package dentalclinic.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Boundary value / equivalence partitioning tests - this is the third
 * distinct test strategy alongside unit testing and integration testing
 * (see Task C report). Each method targets a specific boundary: exactly
 * 10 digits vs 9/11, starts with 0 vs not, today vs yesterday, etc.
 */
class ValidationUtilTest {

    @Test
    void isBlank_null_returnsTrue() {
        assertTrue(ValidationUtil.isBlank(null));
    }

    @Test
    void isBlank_emptyString_returnsTrue() {
        assertTrue(ValidationUtil.isBlank(""));
    }

    @Test
    void isBlank_whitespaceOnly_returnsTrue() {
        assertTrue(ValidationUtil.isBlank("   "));
    }

    @Test
    void isBlank_nonEmptyString_returnsFalse() {
        assertFalse(ValidationUtil.isBlank("Jane Doe"));
    }

    @Test
    void contactNumber_correctFormat_returnsTrue() {
        assertTrue(ValidationUtil.isValidContactNumber("0771234567"));
    }

    @Test
    void contactNumber_nineDigits_returnsFalse() {
        assertFalse(ValidationUtil.isValidContactNumber("077123456")); // boundary: one short
    }

    @Test
    void contactNumber_elevenDigits_returnsFalse() {
        assertFalse(ValidationUtil.isValidContactNumber("07712345678")); // boundary: one long
    }

    @Test
    void contactNumber_doesNotStartWithZero_returnsFalse() {
        assertFalse(ValidationUtil.isValidContactNumber("1771234567"));
    }

    @Test
    void contactNumber_containsLetters_returnsFalse() {
        assertFalse(ValidationUtil.isValidContactNumber("077ABC4567"));
    }

    @Test
    void contactNumber_null_returnsFalse() {
        assertFalse(ValidationUtil.isValidContactNumber(null));
    }

    @Test
    void date_today_returnsTrue() {
        assertTrue(ValidationUtil.isTodayOrFutureDate(LocalDate.now()));
    }

    @Test
    void date_tomorrow_returnsTrue() {
        assertTrue(ValidationUtil.isTodayOrFutureDate(LocalDate.now().plusDays(1)));
    }

    @Test
    void date_yesterday_returnsFalse() {
        assertFalse(ValidationUtil.isTodayOrFutureDate(LocalDate.now().minusDays(1))); // boundary
    }

    @Test
    void date_null_returnsFalse() {
        assertFalse(ValidationUtil.isTodayOrFutureDate(null));
    }
}