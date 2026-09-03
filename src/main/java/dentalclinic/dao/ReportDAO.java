package dentalclinic.dao;

import dentalclinic.model.DentistWorkloadSummary;
import dentalclinic.model.StatusSummary;
import dentalclinic.model.TreatmentRevenueSummary;

import java.sql.SQLException;
import java.util.List;

public interface ReportDAO {

    List<TreatmentRevenueSummary> findRevenueByTreatmentType() throws SQLException;

    List<DentistWorkloadSummary> findDentistWorkload() throws SQLException;

    List<StatusSummary> findStatusBreakdown() throws SQLException;
}