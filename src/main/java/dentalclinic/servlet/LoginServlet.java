package dentalclinic.servlet;

import dentalclinic.dao.StaffUserDAO;
import dentalclinic.dao.impl.StaffUserDAOImpl;
import dentalclinic.model.StaffUser;
import dentalclinic.util.CookieUtil;
import dentalclinic.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Presentation layer - handles the "User Authentication (Login)"
 * requirement. Wired to StaffUserDAO and PasswordUtil, and also
 * demonstrates cookie usage: reads the previous login's "lastLoginTime"
 * cookie (to show a "welcome back" message once), then writes a fresh
 * one recording this login for next time.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

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

                // Read the PREVIOUS "last login" cookie before we overwrite
                // it, so we can show it back to the user this one time.
                Optional<String> previousLoginTime = CookieUtil.readCookie(request, "lastLoginTime");

                HttpSession session = request.getSession(true);
                session.setAttribute("loggedInUser", user.getUsername());
                session.setAttribute("loggedInRole", user.getRole());
                previousLoginTime.ifPresent(time -> session.setAttribute("previousLoginTime", time));

                // Write a NEW cookie recording this login, for next time.
                // 90-day expiry: long enough to be genuinely useful, but
                // not forever - a deliberate, justifiable choice.
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                Cookie lastLoginCookie = CookieUtil.createCookie(
                        "lastLoginTime", now, 90 * 24 * 60 * 60, request.getContextPath() + "/"
                );
                response.addCookie(lastLoginCookie);

                response.sendRedirect(request.getContextPath() + "/appointments/list");
            } else {
                request.setAttribute("errorMessage", "Invalid username or password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "A system error occurred. Please try again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}