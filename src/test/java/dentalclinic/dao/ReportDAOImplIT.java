package dentalclinic.dao;

import dentalclinic.dao.impl.ReportDAOImpl;
import dentalclinic.model.DentistWorkloadSummary;
import dentalclinic.model.StatusSummary;
import dentalclinic.model.TreatmentRevenueSummary;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test - requires a running local MySQL. These assert
 * structural properties (sorted order, non-negative values, invariants
 * like completed <= total) rather than exact counts, since the exact
 * numbers depend on whatever test data currently exists in the
 * database from earlier manual testing.
 */
class ReportDAOImplIT {

    private final ReportDAO reportDAO = new ReportDAOImpl();

    @Test
    void findRevenueByTreatmentType_returnsNonNegativeRevenueAndPositiveCounts() throws SQLException {
        List<TreatmentRevenueSummary> results = reportDAO.findRevenueByTreatmentType();

        for (TreatmentRevenueSummary s : results) {
            assertTrue(s.getTotalRevenue().signum() >= 0, "Revenue should never be negative");
            assertTrue(s.getAppointmentCount() > 0, "A summary row should only exist if count > 0");
        }
    }

    @Test
    void findRevenueByTreatmentType_isSortedDescending() throws SQLException {
        List<TreatmentRevenueSummary> results = reportDAO.findRevenueByTreatmentType();

        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(
                    results.get(i).getTotalRevenue().compareTo(results.get(i + 1).getTotalRevenue()) >= 0,
                    "Expected descending order by total revenue"
            );
        }
    }

    @Test
    void findDentistWorkload_completedNeverExceedsTotal() throws SQLException {
        List<DentistWorkloadSummary> results = reportDAO.findDentistWorkload();

        for (DentistWorkloadSummary s : results) {
            assertTrue(s.getCompletedAppointments() <= s.getTotalAppointments(),
                    "Completed count cannot exceed total appointment count");
        }
    }

    @Test
    void findStatusBreakdown_countsAreNonNegative() throws SQLException {
        List<StatusSummary> results = reportDAO.findStatusBreakdown();

        for (StatusSummary s : results) {
            assertTrue(s.getCount() >= 0);
        }
    }
}