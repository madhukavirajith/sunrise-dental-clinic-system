package dentalclinic.dao.impl;

import dentalclinic.dao.StaffUserDAO;
import dentalclinic.model.StaffUser;
import dentalclinic.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StaffUserDAOImpl implements StaffUserDAO {

    @Override
    public Optional<StaffUser> findByUsername(String username) throws SQLException {
        String sql = "SELECT user_id, username, password_hash, role FROM staff_user WHERE username = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StaffUser user = new StaffUser(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("role"));
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }
}