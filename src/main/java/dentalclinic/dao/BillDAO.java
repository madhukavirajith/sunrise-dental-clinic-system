package dentalclinic.dao;

import dentalclinic.model.Bill;

import java.sql.SQLException;
import java.util.Optional;

public interface BillDAO {
    Bill save(Bill bill) throws SQLException;
    Optional<Bill> findByAppointmentId(int appointmentId) throws SQLException;
}