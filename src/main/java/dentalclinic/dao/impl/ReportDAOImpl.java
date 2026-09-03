package dentalclinic.dao.impl;

import dentalclinic.dao.ReportDAO;
import dentalclinic.model.DentistWorkloadSummary;
import dentalclinic.model.StatusSummary;
import dentalclinic.model.TreatmentRevenueSummary;
import dentalclinic.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public List<TreatmentRevenueSummary> findRevenueByTreatmentType() throws SQLException {
        List<TreatmentRevenueSummary> results = new ArrayList<>();
        String sql = "SELECT t.name AS treatment_name, COUNT(*) AS appointment_count, " +
                "SUM(t.base_fee) AS total_revenue " +
                "FROM appointment a " +
                "JOIN treatment_type t ON a.treatment_type_id = t.treatment_type_id " +
                "GROUP BY t.treatment_type_id, t.name " +
                "ORDER BY total_revenue DESC";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(new TreatmentRevenueSummary(
                        rs.getString("treatment_name"),
                        rs.getInt("appointment_count"),
                        rs.getBigDecimal("total_revenue")
                ));
            }
        }
        return results;
    }

    @Override
    public List<DentistWorkloadSummary> findDentistWorkload() throws SQLException {
        List<DentistWorkloadSummary> results = new ArrayList<>();
        String sql = "SELECT d.name AS dentist_name, COUNT(*) AS total_count, " +
                "SUM(CASE WHEN a.status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_count " +
                "FROM appointment a " +
                "JOIN dentist d ON a.dentist_id = d.dentist_id " +
                "GROUP BY d.dentist_id, d.name " +
                "ORDER BY total_count DESC";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(new DentistWorkloadSummary(
                        rs.getString("dentist_name"),
                        rs.getInt("total_count"),
                        rs.getInt("completed_count")
                ));
            }
        }
        return results;
    }

    @Override
    public List<StatusSummary> findStatusBreakdown() throws SQLException {
        List<StatusSummary> results = new ArrayList<>();
        String sql = "SELECT status, COUNT(*) AS count FROM appointment GROUP BY status ORDER BY count DESC";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(new StatusSummary(
                        rs.getString("status"),
                        rs.getInt("count")
                ));
            }
        }
        return results;
    }
}