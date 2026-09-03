package dentalclinic.servlet;

import dentalclinic.dao.NotificationDAO;
import dentalclinic.dao.impl.NotificationDAOImpl;
import dentalclinic.model.Notification;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/notifications")
public class NotificationServlet extends HttpServlet {

    private final NotificationDAO notificationDAO = new NotificationDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appointmentIdParam = request.getParameter("appointmentId");

        try {
            List<Notification> notifications;
            if (appointmentIdParam != null && !appointmentIdParam.isBlank()) {
                int appointmentId = Integer.parseInt(appointmentIdParam);
                notifications = notificationDAO.findByAppointmentId(appointmentId);
                request.setAttribute("filtered", true);
            } else {
                notifications = notificationDAO.findAll();
                request.setAttribute("filtered", false);
            }

            request.setAttribute("notifications", notifications);
            request.getRequestDispatcher("/WEB-INF/views/notification-center.jsp").forward(request, response);

        } catch (SQLException e) {
            response.getWriter().println("Error loading notifications: " + e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid appointmentId");
        }
    }
}