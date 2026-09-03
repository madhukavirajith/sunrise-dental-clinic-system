package dentalclinic.service.strategy;

import dentalclinic.model.TreatmentType;

import java.math.BigDecimal;

public class StandardConsultationBillingStrategy implements BillingStrategy {

    @Override
    public BigDecimal calculateTreatmentCost(TreatmentType treatmentType) {
        return treatmentType.getBaseFee();
    }
}
