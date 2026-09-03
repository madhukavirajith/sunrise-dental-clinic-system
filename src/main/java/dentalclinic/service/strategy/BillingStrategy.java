package dentalclinic.service.strategy;

import dentalclinic.model.TreatmentType;

import java.math.BigDecimal;

public interface BillingStrategy {

    BigDecimal calculateTreatmentCost(TreatmentType treatmentType);
}
