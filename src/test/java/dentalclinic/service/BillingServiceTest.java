package dentalclinic.service;

import dentalclinic.model.Appointment;
import dentalclinic.model.TreatmentType;
import dentalclinic.model.Bill;
import dentalclinic.service.strategy.ProcedureBillingStrategy;
import dentalclinic.service.strategy.StandardConsultationBillingStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BillingServiceTest {

    // ---------- Happy path ----------

    @Test
    void standardConsultation_returnsBaseFeeUnchanged() {
        TreatmentType checkUp = new TreatmentType(1, "Routine Check-up", new BigDecimal("500.00"));
        BillingService billingService = new BillingService(new StandardConsultationBillingStrategy());
        Appointment appointment = new Appointment();
        appointment.setTreatmentType(checkUp);

        Bill bill = billingService.generateBill(appointment);

        assertEquals(new BigDecimal("500.00"), bill.getTreatmentCost());
    }

    @Test
    void procedureBilling_addsLabMaterialsSurcharge() {
        TreatmentType rootCanal = new TreatmentType(2, "Root Canal", new BigDecimal("5000.00"));
        BillingService billingService = new BillingService(new ProcedureBillingStrategy());
        Appointment appointment = new Appointment();
        appointment.setTreatmentType(rootCanal);

        Bill bill = billingService.generateBill(appointment);

        assertEquals(new BigDecimal("6500.00"), bill.getTreatmentCost());
    }

    @Test
    void generateBill_totalAmount_equalsConsultationPlusTreatment() {
        TreatmentType checkUp = new TreatmentType(1, "Routine Check-up", new BigDecimal("500.00"));
        BillingService billingService = new BillingService(new StandardConsultationBillingStrategy());
        Appointment appointment = new Appointment();
        appointment.setTreatmentType(checkUp);

        Bill bill = billingService.generateBill(appointment);

        // consultation fee (500.00, from BillingService's default) + treatment cost (500.00)
        assertEquals(new BigDecimal("1000.00"), bill.getTotalAmount());
    }

    // ---------- Corner cases ----------

    @Test
    void standardConsultation_zeroBaseFee_returnsZero() {
        TreatmentType freeCheckUp = new TreatmentType(1, "Free Promo Check-up", BigDecimal.ZERO);
        BillingService billingService = new BillingService(new StandardConsultationBillingStrategy());
        Appointment appointment = new Appointment();
        appointment.setTreatmentType(freeCheckUp);

        Bill bill = billingService.generateBill(appointment);

        assertEquals(0, BigDecimal.ZERO.compareTo(bill.getTreatmentCost()));
    }

    @Test
    void procedureBilling_zeroBaseFee_returnsOnlySurcharge() {
        TreatmentType zeroFeeTreatment = new TreatmentType(2, "Root Canal", BigDecimal.ZERO);
        BillingService billingService = new BillingService(new ProcedureBillingStrategy());
        Appointment appointment = new Appointment();
        appointment.setTreatmentType(zeroFeeTreatment);

        Bill bill = billingService.generateBill(appointment);

        // even with a zero base fee, the fixed surcharge still applies
        assertEquals(new BigDecimal("1500.00"), bill.getTreatmentCost());
    }

    // ---------- Unhappy path ----------

    @Test
    void calculateTreatmentCost_nullTreatmentType_throwsException() {
        StandardConsultationBillingStrategy strategy = new StandardConsultationBillingStrategy();

        // Documents current behaviour: a null TreatmentType is not
        // caught explicitly and propagates as a NullPointerException.
        // (Noted in the report as a known limitation - see Critical
        // Reflection - rather than silently allowed to pass unnoticed.)
        assertThrows(NullPointerException.class, () -> strategy.calculateTreatmentCost(null));
    }
}