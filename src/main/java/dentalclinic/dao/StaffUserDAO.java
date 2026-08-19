package dentalclinic.dao;

import dentalclinic.model.StaffUser;

import java.sql.SQLException;
import java.util.Optional;

public interface StaffUserDAO {

    Optional<StaffUser> findByUsername(String username) throws SQLException;
}