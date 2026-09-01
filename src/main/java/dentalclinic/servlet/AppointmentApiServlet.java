package dentalclinic.servlet;

import dentalclinic.dao.AppointmentDAO;
import dentalclinic.dao.impl.AppointmentDAOImpl;
import dentalclinic.model.Appointment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;

/**
 * A JSON web service endpoint - satisfies the "developed as a web
 * service" requirement using plain Java EE Servlets. JSON is built
 * manually (no library) since this project's allowed dependencies don't
 * include a JAX-RS runtime; the optional "serialization dependency"
 * allowance would justify adding a JSON library if the API surface grew
 * larger than this.
 *
 * Example: GET /api/appointments/APT-000002
 */
@WebServlet("/api/appointments/*")
public class AppointmentApiServlet extends HttpServlet {

    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo(); // e.g. "/APT-000002"
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeJsonError(response.getWriter(), "Missing appointment number in URL path.");
            return;
        }

        String appointmentNumber = pathInfo.substring(1);

        try {
            Optional<Appointment> maybeAppointment = appointmentDAO.findByAppointmentNumber(appointmentNumber);

            if (maybeAppointment.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                writeJsonError(response.getWriter(), "No appointment found with number " + appointmentNumber);
                return;
            }

            Appointment a = maybeAppointment.get();
            response.getWriter().write(AppointmentJsonMapper.toJson(a));

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeJsonError(response.getWriter(), "A system error occurred.");
        }
    }

    private void writeJsonError(PrintWriter writer, String message) {
        writer.write("{\"error\":" + jsonString(message) + "}");
    }

    /** Wraps a value in quotes and escapes characters that would break JSON. */
    private String jsonString(String value) {
        if (value == null) return "null";
        String escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
        return "\"" + escaped + "\"";
    }
}