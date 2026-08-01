package lk.edu.icbt.dentalclinic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Presentation layer - handles the "User Authentication (Login)" requirement.
 *
 * TODO (student): wire this up to a StaffUserDAO + password hashing
 * (see util package) instead of the placeholder check below. Document your
 * hashing approach in the report under the ETHICAL / data protection
 * criterion in the brief.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // TODO (student): replace with real StaffUserDAO lookup + hashed
        // password verification. This placeholder exists only so the
        // skeleton compiles and the login flow can be demonstrated end to end.
        boolean authenticated = username != null && password != null
                && !username.isBlank() && !password.isBlank();

        if (authenticated) {
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedInUser", username);
            response.sendRedirect(request.getContextPath() + "/appointments/list");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
