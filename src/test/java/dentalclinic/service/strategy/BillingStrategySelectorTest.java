package dentalclinic.service.strategy;

import dentalclinic.model.TreatmentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class BillingStrategySelectorTest {

    @Test
    void selectsProcedureStrategy_forRootCanal() {
        TreatmentType t = new TreatmentType(1, "Root Canal", new BigDecimal("5000.00"));
        assertInstanceOf(ProcedureBillingStrategy.class, BillingStrategySelector.select(t));
    }

    @Test
    void selectsProcedureStrategy_forExtraction() {
        TreatmentType t = new TreatmentType(1, "Tooth Extraction", new BigDecimal("2500.00"));
        assertInstanceOf(ProcedureBillingStrategy.class, BillingStrategySelector.select(t));
    }

    @Test
    void selectsStandardStrategy_forRoutineCheckUp() {
        TreatmentType t = new TreatmentType(1, "Routine Check-up", new BigDecimal("500.00"));
        assertInstanceOf(StandardConsultationBillingStrategy.class, BillingStrategySelector.select(t));
    }

    @Test
    void selectionIsCaseInsensitive() {
        TreatmentType t = new TreatmentType(1, "ROOT CANAL", new BigDecimal("5000.00"));
        assertInstanceOf(ProcedureBillingStrategy.class, BillingStrategySelector.select(t));
    }
}