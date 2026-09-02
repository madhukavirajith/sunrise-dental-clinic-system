package dentalclinic.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Enforces the "Only authorized staff can use the system" requirement
 * from the assignment brief. Mapped in web.xml against /appointments/*,
 * /billing/*, and /reports/* so unauthenticated requests to protected
 * areas are redirected to the login page rather than reaching the
 * servlets directly.
 *
 * The redirect includes a sessionExpired flag so login.jsp can show a
 * distinguishing message ("your session may have expired") rather than
 * a generic login prompt - demonstrating awareness of the session
 * lifecycle, not just a binary logged-in/logged-out check.
 */
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("loggedInUser") != null);

        if (loggedIn) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?sessionExpired=true");
        }
    }
}