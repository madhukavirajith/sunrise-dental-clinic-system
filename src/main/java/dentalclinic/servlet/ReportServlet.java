package dentalclinic.servlet;

import dentalclinic.dao.AppointmentDAO;
import dentalclinic.dao.ReportDAO;
import dentalclinic.dao.impl.AppointmentDAOImpl;
import dentalclinic.dao.impl.ReportDAOImpl;
import dentalclinic.model.Appointment;
import dentalclinic.util.DBConnectionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Handles the Reports section: a landing page listing every available
 * report, plus each individual report. Routed as a single servlet with
 * path-based dispatch (like AppointmentServlet), since all reports share
 * the same authentication protection (/reports/* in web.xml).
 */
@WebServlet("/reports/*")
public class ReportServlet extends HttpServlet {

    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private final ReportDAO reportDAO = new ReportDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            request.getRequestDispatcher("/WEB-INF/views/reports-landing.jsp").forward(request, response);

        } else if (pathInfo.equals("/daily")) {
            handleDailyReport(request, response);

        } else if (pathInfo.equals("/revenue-by-treatment")) {
            handleRevenueByTreatment(request, response);

        } else if (pathInfo.equals("/dentist-workload")) {
            handleDentistWorkload(request, response);

        } else if (pathInfo.equals("/status-breakdown")) {
            handleStatusBreakdown(request, response);

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleDailyReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dateParam = request.getParameter("date");
        LocalDate reportDate;
        try {
            reportDate = (dateParam == null || dateParam.isBlank())
                    ? LocalDate.now()
                    : LocalDate.parse(dateParam);
        } catch (DateTimeParseException e) {
            reportDate = LocalDate.now();
        }

        try {
            List<Appointment> appointments = appointmentDAO.findByDate(reportDate);
            BigDecimal expectedRevenue = getDailyRevenueViaStoredProcedure(reportDate);

            request.setAttribute("reportDate", reportDate);
            request.setAttribute("appointments", appointments);
            request.setAttribute("appointmentCount", appointments.size());
            request.setAttribute("expectedRevenue", expectedRevenue);
            request.getRequestDispatcher("/WEB-INF/views/daily-report.jsp").forward(request, response);

        } catch (SQLException e) {
            response.getWriter().println("Error generating report: " + e.getMessage());
        }
    }

    private void handleRevenueByTreatment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("summaries", reportDAO.findRevenueByTreatmentType());
            request.getRequestDispatcher("/WEB-INF/views/revenue-by-treatment.jsp").forward(request, response);
        } catch (SQLException e) {
            response.getWriter().println("Error generating report: " + e.getMessage());
        }
    }

    private void handleDentistWorkload(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("summaries", reportDAO.findDentistWorkload());
            request.getRequestDispatcher("/WEB-INF/views/dentist-workload.jsp").forward(request, response);
        } catch (SQLException e) {
            response.getWriter().println("Error generating report: " + e.getMessage());
        }
    }

    private void handleStatusBreakdown(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("summaries", reportDAO.findStatusBreakdown());
            request.getRequestDispatcher("/WEB-INF/views/status-breakdown.jsp").forward(request, response);
        } catch (SQLException e) {
            response.getWriter().println("Error generating report: " + e.getMessage());
        }
    }

    private BigDecimal getDailyRevenueViaStoredProcedure(LocalDate date) throws SQLException {
        String sql = "{CALL GetDailyRevenue(?, ?)}";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setDate(1, Date.valueOf(date));
            cs.registerOutParameter(2, Types.DECIMAL);
            cs.execute();

            BigDecimal revenue = cs.getBigDecimal(2);
            return revenue != null ? revenue : BigDecimal.ZERO;
        }
    }
}