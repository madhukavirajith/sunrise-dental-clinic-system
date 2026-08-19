package dentalclinic.model;

import java.math.BigDecimal;

/**
 * Domain model representing a treatment type and its base consultation fee.
 * Kept as its own entity (rather than a hardcoded switch statement) so new
 * treatment types and prices can be added via the database without changing
 * code - this is what makes the Strategy pattern in the billing service
 * meaningful (see BillingService / BillingStrategy).
 */
public class TreatmentType {

    private int treatmentTypeId;
    private String name;
    private BigDecimal baseFee;

    public TreatmentType() {
    }

    public TreatmentType(int treatmentTypeId, String name, BigDecimal baseFee) {
        this.treatmentTypeId = treatmentTypeId;
        this.name = name;
        this.baseFee = baseFee;
    }

    public int getTreatmentTypeId() {
        return treatmentTypeId;
    }

    public void setTreatmentTypeId(int treatmentTypeId) {
        this.treatmentTypeId = treatmentTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(BigDecimal baseFee) {
        this.baseFee = baseFee;
    }
}
