package lk.edu.icbt.dentalclinic.service.strategy;

import lk.edu.icbt.dentalclinic.model.TreatmentType;

import java.math.BigDecimal;

/**
 * STRATEGY PATTERN (concrete strategy).
 * Default pricing: the treatment cost is simply the treatment type's base
 * fee from the database. Used for routine consultations/check-ups.
 */
public class StandardConsultationBillingStrategy implements BillingStrategy {

    @Override
    public BigDecimal calculateTreatmentCost(TreatmentType treatmentType) {
        return treatmentType.getBaseFee();
    }
}
