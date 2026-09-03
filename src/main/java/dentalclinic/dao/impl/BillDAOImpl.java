package dentalclinic.dao.impl;

import dentalclinic.dao.BillDAO;
import dentalclinic.model.Bill;
import dentalclinic.util.DBConnectionManager;

import java.sql.*;
import java.util.Optional;

public class BillDAOImpl implements BillDAO {

    @Override
    public Bill save(Bill bill) throws SQLException {
        String sql = "INSERT INTO bill (appointment_id, consultation_fee, treatment_cost, total_amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, bill.getAppointment().getAppointmentId());
            ps.setBigDecimal(2, bill.getConsultationFee());
            ps.setBigDecimal(3, bill.getTreatmentCost());
            ps.setBigDecimal(4, bill.getTotalAmount());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    bill.setBillId(keys.getInt(1));
                }
            }
        }
        return bill;
    }

    @Override
    public Optional<Bill> findByAppointmentId(int appointmentId) throws SQLException {
        String sql = "SELECT bill_id, consultation_fee, treatment_cost, total_amount FROM bill WHERE appointment_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setConsultationFee(rs.getBigDecimal("consultation_fee"));
                    bill.setTreatmentCost(rs.getBigDecimal("treatment_cost"));
                    bill.setTotalAmount(rs.getBigDecimal("total_amount"));
                    return Optional.of(bill);
                }
            }
        }
        return Optional.empty();
    }
}