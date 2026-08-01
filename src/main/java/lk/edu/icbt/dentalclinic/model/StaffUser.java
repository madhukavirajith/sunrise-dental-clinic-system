package lk.edu.icbt.dentalclinic.model;

/**
 * Domain model representing an authorized staff member who can log in.
 * NOTE: passwordHash must never store a plain-text password - see
 * util.PasswordUtil for hashing (document your choice, e.g. BCrypt-style
 * salted hashing, and justify it in the report under the ETHICAL/data
 * protection requirement in the brief).
 */
public class StaffUser {

    private int userId;
    private String username;
    private String passwordHash;
    private String role; // e.g. RECEPTIONIST, DENTIST, ADMIN

    public StaffUser() {
    }

    public StaffUser(int userId, String username, String passwordHash, String role) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
