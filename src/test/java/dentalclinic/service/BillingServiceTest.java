package dentalclinic.service;

import dentalclinic.model.Appointment;
import dentalclinic.model.TreatmentType;
import dentalclinic.service.strategy.ProcedureBillingStrategy;
import dentalclinic.service.strategy.StandardConsultationBillingStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Example test class illustrating TDD-style unit tests for the billing
 * Strategy pattern. This is a STARTING POINT, not your full 30-40 test
 * cases - expand with more treatment types, boundary values (e.g. zero/
 * negative base fee), and unhappy-path cases (e.g. null treatment type)
 * as required by Task C.
 */
class BillingServiceTest {

    @Test
    void standardConsultation_returnsBaseFeeUnchanged() {
        // Arrange
        TreatmentType checkUp = new TreatmentType(1, "Routine Check-up", new BigDecimal("500.00"));
        BillingService billingService = new BillingService(new StandardConsultationBillingStrategy());
        Appointment appointment = new Appointment();
        appointment.setTreatmentType(checkUp);

        // Act
        var bill = billingService.generateBill(appointment);

        // Assert (happy path)
        assertEquals(new BigDecimal("500.00"), bill.getTreatmentCost());
    }

    @Test
    void procedureBilling_addsLabMaterialsSurcharge() {
        // Arrange
        TreatmentType rootCanal = new TreatmentType(2, "Root Canal", new BigDecimal("5000.00"));
        BillingService billingService = new BillingService(new ProcedureBillingStrategy());
        Appointment appointment = new Appointment();
        appointment.setTreatmentType(rootCanal);

        // Act
        var bill = billingService.generateBill(appointment);

        // Assert (happy path) - base fee (5000.00) + surcharge (1500.00)
        assertEquals(new BigDecimal("6500.00"), bill.getTreatmentCost());
    }

    // TODO (student): add corner-case tests, e.g.:
    // - treatment type with a zero base fee
    // - null TreatmentType passed to calculateTreatmentCost (should this
    //   throw? decide and test the decision)
}
