package dentalclinic.service.strategy;

import dentalclinic.model.TreatmentType;

/**
 * Decides which BillingStrategy applies to a given treatment. Kept as its
 * own small class (rather than an if/else buried in the servlet) so the
 * decision logic is easy to find, test, and extend later.
 */
public class BillingStrategySelector {

    public static BillingStrategy select(TreatmentType treatmentType) {
        String name = treatmentType.getName() == null ? "" : treatmentType.getName().toLowerCase();
        if (name.contains("root canal") || name.contains("extraction")) {
            return new ProcedureBillingStrategy();
        }
        return new StandardConsultationBillingStrategy();
    }
}