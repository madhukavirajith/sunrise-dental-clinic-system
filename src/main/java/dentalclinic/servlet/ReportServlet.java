package dentalclinic.servlet;

import dentalclinic.dao.AppointmentDAO;
import dentalclinic.dao.impl.AppointmentDAOImpl;
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

@WebServlet("/reports/daily")
public class ReportServlet extends HttpServlet {

    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

    /**
     * Calls the GetDailyRevenue MySQL stored procedure (added Tue Aug 25 -
     * see database/procedures_and_triggers.sql) instead of summing in
     * Java, so this report is a genuine, working use of an advanced
     * database feature. NOTE: run tomorrow's SQL script before testing
     * this, or this call will fail with "PROCEDURE does not exist."
     */
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