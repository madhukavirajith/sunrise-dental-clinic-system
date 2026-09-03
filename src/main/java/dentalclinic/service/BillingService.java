package dentalclinic.service;

import dentalclinic.model.Appointment;
import dentalclinic.model.Bill;
import dentalclinic.service.strategy.BillingStrategy;

import java.math.BigDecimal;

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
