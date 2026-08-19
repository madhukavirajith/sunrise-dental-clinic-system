package dentalclinic.service.strategy;

import dentalclinic.model.TreatmentType;

import java.math.BigDecimal;

/**
 * STRATEGY PATTERN (interface half).
 *
 * Justification (cite this reasoning in your report): billing rules differ
 * by treatment type - a routine check-up is a flat consultation fee, but a
 * procedure like a root canal might need additional cost components (e.g.
 * lab fees). Hardcoding this as an if/else chain in BillingService would
 * violate the Open/Closed Principle (the "O" in SOLID): every new pricing
 * rule would require editing existing, already-tested code. With Strategy,
 * BillingService depends only on this interface, and each concrete
 * strategy is added as a new class - open for extension, closed for
 * modification.
 */
public interface BillingStrategy {

    BigDecimal calculateTreatmentCost(TreatmentType treatmentType);
}
