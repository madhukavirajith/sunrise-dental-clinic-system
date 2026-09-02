package dentalclinic.model;

/**
 * Domain model representing a registered patient.
 * Plain data holder - persistence logic lives in PatientDAO, not here
 * (Single Responsibility Principle).
 */
public class Patient {

    private int patientId;
    private String name;
    private String address;
    private String contactNumber;
    private String email; // added for the notification feature - nullable

    public Patient() {
    }

    /**
     * Original 4-argument constructor kept as-is so every existing call
     * site across the codebase keeps compiling unchanged. Use setEmail()
     * afterward, or the 5-argument constructor below, to set an email.
     */
    public Patient(int patientId, String name, String address, String contactNumber) {
        this.patientId = patientId;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public Patient(int patientId, String name, String address, String contactNumber, String email) {
        this(patientId, name, address, contactNumber);
        this.email = email;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}