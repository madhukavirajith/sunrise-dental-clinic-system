package dentalclinic.service.strategy;

import dentalclinic.model.TreatmentType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * STRATEGY PATTERN (concrete strategy).
 * Example of a DIFFERENT billing rule for more involved procedures
 * (e.g. root canal, extraction) - adds a fixed lab/materials surcharge on
 * top of the base fee. Demonstrates why Strategy earns its place: this
 * class was added without touching StandardConsultationBillingStrategy or
 * BillingService at all.
 *
 * TODO (student): replace the flat surcharge with real clinic pricing
 * rules once you define your treatment catalogue, and justify the numbers
 * in your report (state them as an assumption if not otherwise specified).
 */
public class ProcedureBillingStrategy implements BillingStrategy {

    private static final BigDecimal LAB_MATERIALS_SURCHARGE = new BigDecimal("1500.00");

    @Override
    public BigDecimal calculateTreatmentCost(TreatmentType treatmentType) {
        return treatmentType.getBaseFee()
                .add(LAB_MATERIALS_SURCHARGE)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
