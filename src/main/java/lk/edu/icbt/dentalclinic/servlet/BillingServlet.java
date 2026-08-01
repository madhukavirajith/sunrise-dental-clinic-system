package lk.edu.icbt.dentalclinic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Presentation layer - handles "Calculate and Print Bill".
 *
 * TODO (student): inject BillingService (with the appropriate
 * BillingStrategy chosen for the appointment's treatment type), calculate
 * and forward the resulting Bill to a printable JSP view.
 */
@WebServlet("/billing/*")
public class BillingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().println("TODO: BillingServlet#doGet not yet implemented");
    }
}
