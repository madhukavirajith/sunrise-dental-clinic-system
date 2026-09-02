package dentalclinic.servlet;

import dentalclinic.dao.AppointmentDAO;
import dentalclinic.dao.PatientDAO;
import dentalclinic.dao.impl.AppointmentDAOImpl;
import dentalclinic.dao.impl.PatientDAOImpl;
import dentalclinic.model.*;
import dentalclinic.service.notification.NotificationDispatcher;
import dentalclinic.util.CookieUtil;
import dentalclinic.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Presentation layer - handles "Register New Appointment",
 * "Display Appointment Details" (search by appointment number), and the
 * appointments listing. Also demonstrates session/cookie usage
 * (recently viewed list, remembered last search) and, after a
 * successful registration, dispatches confirmation notifications via
 * NotificationDispatcher (Observer pattern).
 */
@WebServlet("/appointments/*")
public class AppointmentServlet extends HttpServlet {

    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private final NotificationDispatcher notificationDispatcher = new NotificationDispatcher();

    private static final String RECENTLY_VIEWED_SESSION_KEY = "recentlyViewedAppointments";
    private static final int MAX_RECENTLY_VIEWED = 5;
    private static final String LAST_SEARCH_COOKIE_NAME = "lastSearchedAppointment";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/new")) {
            request.getRequestDispatcher("/WEB-INF/views/new-appointment.jsp").forward(request, response);

        } else if (pathInfo.equals("/search")) {
            handleSearch(request, response);

        } else if (pathInfo.equals("/list")) {
            handleList(request, response);

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appointmentNumber = request.getParameter("appointmentNumber");

        if (appointmentNumber == null || appointmentNumber.isBlank()) {
            CookieUtil.readCookie(request, LAST_SEARCH_COOKIE_NAME)
                    .ifPresent(value -> request.setAttribute("lastSearchedAppointment", value));
            request.getRequestDispatcher("/WEB-INF/views/appointment-search.jsp").forward(request, response);
            return;
        }

        Cookie searchCookie = CookieUtil.createCookie(
                LAST_SEARCH_COOKIE_NAME, appointmentNumber.trim(), 7 * 24 * 60 * 60, request.getContextPath() + "/"
        );
        response.addCookie(searchCookie);

        try {
            Optional<Appointment> found = appointmentDAO.findByAppointmentNumber(appointmentNumber.trim());
            if (found.isPresent()) {
                addToRecentlyViewed(request, appointmentNumber.trim());
                request.setAttribute("appointment", found.get());
                request.getRequestDispatcher("/WEB-INF/views/appointment-details.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "No appointment found with number " + appointmentNumber);
                request.getRequestDispatcher("/WEB-INF/views/appointment-search.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "A system error occurred.");
            request.getRequestDispatcher("/WEB-INF/views/appointment-search.jsp").forward(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Appointment> appointments = appointmentDAO.findAll();
            request.setAttribute("appointments", appointments);
            request.getRequestDispatcher("/WEB-INF/views/appointment-list.jsp").forward(request, response);
        } catch (SQLException e) {
            response.getWriter().println("Error loading appointments: " + e.getMessage());
        }
    }

    private void addToRecentlyViewed(HttpServletRequest request, String appointmentNumber) {
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        LinkedList<String> recentlyViewed = (LinkedList<String>) session.getAttribute(RECENTLY_VIEWED_SESSION_KEY);
        if (recentlyViewed == null) {
            recentlyViewed = new LinkedList<>();
        }
        recentlyViewed.remove(appointmentNumber);
        recentlyViewed.addFirst(appointmentNumber);
        while (recentlyViewed.size() > MAX_RECENTLY_VIEWED) {
            recentlyViewed.removeLast();
        }
        session.setAttribute(RECENTLY_VIEWED_SESSION_KEY, recentlyViewed);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!"/new".equals(request.getPathInfo())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String patientName = request.getParameter("patientName");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");
        String email = request.getParameter("email"); // optional - no validation error if blank
        String dentistIdRaw = request.getParameter("dentistId");
        String treatmentTypeIdRaw = request.getParameter("treatmentTypeId");
        String dateRaw = request.getParameter("appointmentDate");
        String timeRaw = request.getParameter("appointmentTime");

        Map<String, String> fieldErrors = new HashMap<>();

        if (ValidationUtil.isBlank(patientName)) fieldErrors.put("patientName", "Patient name is required.");
        if (ValidationUtil.isBlank(address)) fieldErrors.put("address", "Address is required.");
        if (!ValidationUtil.isValidContactNumber(contactNumber)) fieldErrors.put("contactNumber", "Enter a valid 10-digit number starting with 0 (e.g. 0771234567).");
        if (ValidationUtil.isBlank(dentistIdRaw)) fieldErrors.put("dentistId", "Please select a dentist.");
        if (ValidationUtil.isBlank(treatmentTypeIdRaw)) fieldErrors.put("treatmentTypeId", "Please select a treatment type.");

        LocalDate appointmentDate = null;
        try {
            appointmentDate = LocalDate.parse(dateRaw);
            if (!ValidationUtil.isTodayOrFutureDate(appointmentDate)) {
                fieldErrors.put("appointmentDate", "Appointment date cannot be in the past.");
            }
        } catch (DateTimeParseException | NullPointerException e) {
            fieldErrors.put("appointmentDate", "Please select a valid date.");
        }

        LocalTime appointmentTime = null;
        try {
            appointmentTime = LocalTime.parse(timeRaw);
        } catch (DateTimeParseException | NullPointerException e) {
            fieldErrors.put("appointmentTime", "Please select a valid time.");
        }

        if (!fieldErrors.isEmpty()) {
            request.setAttribute("fieldErrors", fieldErrors);
            request.setAttribute("patientName", patientName);
            request.setAttribute("address", address);
            request.setAttribute("contactNumber", contactNumber);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/new-appointment.jsp").forward(request, response);
            return;
        }

        try {
            Patient patient = new Patient(0, patientName, address, contactNumber);
            patient.setEmail(ValidationUtil.isBlank(email) ? null : email.trim());
            patientDAO.save(patient);

            Dentist dentist = new Dentist(Integer.parseInt(dentistIdRaw), null, null);
            TreatmentType treatmentType = new TreatmentType(Integer.parseInt(treatmentTypeIdRaw), null, null);

            Appointment appointment = new Appointment(
                    null, patient, dentist, treatmentType,
                    appointmentDate, appointmentTime, "SCHEDULED"
            );
            appointmentDAO.save(appointment);

            // Re-fetch with full JOIN data so the dispatcher's channels
            // have real dentist/treatment names available for the
            // message text, not just IDs.
            Appointment fullAppointment = appointmentDAO
                    .findByAppointmentNumber(appointment.getAppointmentNumber())
                    .orElse(appointment);
            notificationDispatcher.notifyAppointmentRegistered(fullAppointment);

            request.setAttribute("appointmentNumber", appointment.getAppointmentNumber());
            request.getRequestDispatcher("/WEB-INF/views/appointment-confirmation.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "A system error occurred while saving. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/new-appointment.jsp").forward(request, response);
        }
    }
}