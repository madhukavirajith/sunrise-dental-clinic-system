package dentalclinic.servlet;

import dentalclinic.dao.AppointmentDAO;
import dentalclinic.dao.BillDAO;
import dentalclinic.dao.impl.AppointmentDAOImpl;
import dentalclinic.dao.impl.BillDAOImpl;
import dentalclinic.model.Appointment;
import dentalclinic.model.Bill;
import dentalclinic.service.BillingService;
import dentalclinic.service.strategy.BillingStrategySelector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/billing/*")
public class BillingServlet extends HttpServlet {

    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private final BillDAO billDAO = new BillDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appointmentNumber = request.getParameter("appointmentNumber");

        if (appointmentNumber == null || appointmentNumber.isBlank()) {
            request.getRequestDispatcher("/WEB-INF/views/billing-search.jsp").forward(request, response);
            return;
        }

        try {
            Optional<Appointment> maybeAppointment = appointmentDAO.findByAppointmentNumber(appointmentNumber.trim());

            if (maybeAppointment.isEmpty()) {
                request.setAttribute("errorMessage", "No appointment found with number " + appointmentNumber);
                request.getRequestDispatcher("/WEB-INF/views/billing-search.jsp").forward(request, response);
                return;
            }

            Appointment appointment = maybeAppointment.get();

            Optional<Bill> existingBill = billDAO.findByAppointmentId(appointment.getAppointmentId());
            Bill bill;
            if (existingBill.isPresent()) {
                bill = existingBill.get();
                bill.setAppointment(appointment); // attach full details for the view
            } else {
                BillingService billingService = new BillingService(
                        BillingStrategySelector.select(appointment.getTreatmentType())
                );
                bill = billingService.generateBill(appointment);
                billDAO.save(bill);
            }

            request.setAttribute("bill", bill);
            request.getRequestDispatcher("/WEB-INF/views/bill-view.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "A system error occurred. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/billing-search.jsp").forward(request, response);
        }
    }
}