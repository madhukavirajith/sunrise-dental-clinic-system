package dentalclinic.model;

public class DentistWorkloadSummary {

    private String dentistName;
    private int totalAppointments;
    private int completedAppointments;

    public DentistWorkloadSummary(String dentistName, int totalAppointments, int completedAppointments) {
        this.dentistName = dentistName;
        this.totalAppointments = totalAppointments;
        this.completedAppointments = completedAppointments;
    }

    public String getDentistName() {
        return dentistName;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public int getCompletedAppointments() {
        return completedAppointments;
    }
}