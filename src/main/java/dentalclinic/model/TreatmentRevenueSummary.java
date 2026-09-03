package dentalclinic.model;

import java.math.BigDecimal;

public class TreatmentRevenueSummary {

    private String treatmentName;
    private int appointmentCount;
    private BigDecimal totalRevenue;

    public TreatmentRevenueSummary(String treatmentName, int appointmentCount, BigDecimal totalRevenue) {
        this.treatmentName = treatmentName;
        this.appointmentCount = appointmentCount;
        this.totalRevenue = totalRevenue;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public int getAppointmentCount() {
        return appointmentCount;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
}