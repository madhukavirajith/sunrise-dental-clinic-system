package dentalclinic.servlet;

import dentalclinic.dao.AppointmentDAO;
import dentalclinic.dao.impl.AppointmentDAOImpl;
import dentalclinic.dao.mapper.AppointmentJsonMapper;
import dentalclinic.model.Appointment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

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
            response.getWriter().write(
                    AppointmentJsonMapper.toJsonError("Missing appointment number in URL path.")
            );
            return;
        }

        String appointmentNumber = pathInfo.substring(1);

        try {
            Optional<Appointment> maybeAppointment = appointmentDAO.findByAppointmentNumber(appointmentNumber);

            if (maybeAppointment.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(
                        AppointmentJsonMapper.toJsonError("No appointment found with number " + appointmentNumber)
                );
                return;
            }

            Appointment appointment = maybeAppointment.get();
            response.getWriter().write(AppointmentJsonMapper.toJson(appointment));

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                    AppointmentJsonMapper.toJsonError("A system error occurred.")
            );
        }
    }
}