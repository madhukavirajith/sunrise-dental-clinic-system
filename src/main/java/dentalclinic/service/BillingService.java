package dentalclinic.service;

import dentalclinic.model.Appointment;
import dentalclinic.model.Bill;
import dentalclinic.service.strategy.BillingStrategy;

import java.math.BigDecimal;

/**
 * Business logic layer (middle tier of the 3-tier architecture).
 * Depends on the BillingStrategy abstraction, not a concrete strategy -
 * the concrete strategy is chosen by the caller (see TODO below) and
 * injected in, which is what makes this Strategy rather than just a
 * hardcoded calculation.
 *
 * TODO (student): add a StrategySelector/Factory that picks the right
 * BillingStrategy based on TreatmentType (e.g. by a "category" field on
 * TreatmentType), so calling code doesn't need to know which strategy
 * class to instantiate. That would layer a Factory pattern on top of this
 * Strategy pattern - discuss that combination explicitly in your report
 * if you implement it, since combining named patterns deliberately is
 * exactly the kind of thing the "critical evaluation" marks reward.
 */
public class BillingService {

    private static final BigDecimal DEFAULT_CONSULTATION_FEE = new BigDecimal("500.00");

    private final BillingStrategy billingStrategy;

    public BillingService(BillingStrategy billingStrategy) {
        this.billingStrategy = billingStrategy;
    }

    public Bill generateBill(Appointment appointment) {
        BigDecimal treatmentCost = billingStrategy.calculateTreatmentCost(appointment.getTreatmentType());
        return new Bill(appointment, DEFAULT_CONSULTATION_FEE, treatmentCost);
    }
}
