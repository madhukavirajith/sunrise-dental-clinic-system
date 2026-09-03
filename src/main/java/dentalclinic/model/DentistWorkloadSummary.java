package dentalclinic.model;

/**
 * Aggregate row for the "Dentist Workload" report - not a persisted
 * entity, just a data holder for a GROUP BY query result.
 */
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