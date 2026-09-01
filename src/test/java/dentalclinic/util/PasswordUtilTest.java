package dentalclinic.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void verifyPassword_correctPassword_returnsTrue() {
        String hash = PasswordUtil.hashPassword("myPassword123");
        assertTrue(PasswordUtil.verifyPassword("myPassword123", hash));
    }

    @Test
    void verifyPassword_wrongPassword_returnsFalse() {
        String hash = PasswordUtil.hashPassword("myPassword123");
        assertFalse(PasswordUtil.verifyPassword("wrongPassword", hash));
    }

    @Test
    void hashPassword_sameInputTwice_producesDifferentHashes() {
        // Important security property: a random salt means the same
        // password never produces the same stored value twice.
        String hash1 = PasswordUtil.hashPassword("samePassword");
        String hash2 = PasswordUtil.hashPassword("samePassword");
        assertNotEquals(hash1, hash2);
    }

    @Test
    void verifyPassword_malformedStoredValue_returnsFalseNotException() {
        // Corner case: stored value doesn't have the expected "salt:hash" format
        assertFalse(PasswordUtil.verifyPassword("anything", "not-a-valid-format"));
    }
}