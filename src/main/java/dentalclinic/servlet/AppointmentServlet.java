package dentalclinic.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Presentation layer - handles "Register New Appointment" and
 * "Display Appointment Details" (search by appointment number).
 *
 * TODO (student): inject AppointmentDAO (constructor or init()), wire up
 * doGet for search-by-appointment-number and doPost for registration, add
 * input validation (see brief: "implement proper validation mechanisms in
 * order to restrict invalid entries"), and forward to the appropriate JSP
 * views in WEB-INF/views/.
 */
@WebServlet("/appointments/*")
public class AppointmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO (student): route based on request.getPathInfo()
        // e.g. /appointments/list, /appointments/search?appointmentNumber=...
        response.getWriter().println("TODO: AppointmentServlet#doGet not yet implemented");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO (student): handle new appointment registration form submission,
        // with validation, then delegate to AppointmentDAO via a service class.
        response.getWriter().println("TODO: AppointmentServlet#doPost not yet implemented");
    }
}
