package dentalclinic.service.strategy;

import dentalclinic.model.TreatmentType;

public class BillingStrategySelector {

    public static BillingStrategy select(TreatmentType treatmentType) {
        String name = treatmentType.getName() == null ? "" : treatmentType.getName().toLowerCase();
        if (name.contains("root canal") || name.contains("extraction")) {
            return new ProcedureBillingStrategy();
        }
        return new StandardConsultationBillingStrategy();
    }
}