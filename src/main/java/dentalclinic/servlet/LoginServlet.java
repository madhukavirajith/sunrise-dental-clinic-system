package dentalclinic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dentalclinic.dao.StaffUserDAO;
import dentalclinic.dao.impl.StaffUserDAOImpl;
import dentalclinic.model.StaffUser;
import dentalclinic.util.PasswordUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Presentation layer - handles the "User Authentication (Login)"
 * requirement. Now wired to StaffUserDAO and PasswordUtil instead of the
 * earlier placeholder logic.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Depends on the StaffUserDAO INTERFACE, not the concrete class -
    // matches the Dependency Inversion approach used across the DAO layer,
    // and matches what the class diagram shows.
    private final StaffUserDAO staffUserDAO = new StaffUserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("errorMessage", "Username and password are required.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            Optional<StaffUser> maybeUser = staffUserDAO.findByUsername(username);

            boolean authenticated = maybeUser.isPresent()
                    && PasswordUtil.verifyPassword(password, maybeUser.get().getPasswordHash());

            if (authenticated) {
                StaffUser user = maybeUser.get();
                HttpSession session = request.getSession(true);
                session.setAttribute("loggedInUser", user.getUsername());
                session.setAttribute("loggedInRole", user.getRole());
                response.sendRedirect(request.getContextPath() + "/appointments/list");
            } else {
                request.setAttribute("errorMessage", "Invalid username or password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            // TODO (student): replace with proper logging once a logging
            // approach is chosen; for now, fail safely rather than leaking
            // a stack trace to the user.
            request.setAttribute("errorMessage", "A system error occurred. Please try again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}