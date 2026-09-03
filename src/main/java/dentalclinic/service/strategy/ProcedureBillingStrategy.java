package dentalclinic.service.strategy;

import dentalclinic.model.TreatmentType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProcedureBillingStrategy implements BillingStrategy {

    private static final BigDecimal LAB_MATERIALS_SURCHARGE = new BigDecimal("1500.00");

    @Override
    public BigDecimal calculateTreatmentCost(TreatmentType treatmentType) {
        return treatmentType.getBaseFee()
                .add(LAB_MATERIALS_SURCHARGE)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
