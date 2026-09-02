<% request.setAttribute("pageTitle", "Appointments"); request.setAttribute("activeNav", "dashboard"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="dentalclinic.model.Appointment" %>

<div class="page-header">
    <h1>Appointments</h1>
    <p>Today's schedule and recent activity.</p>
</div>

<%
    Object previousLoginTime = session.getAttribute("previousLoginTime");
%>
<% if (previousLoginTime != null) { %>
    <div class="welcome-banner">Welcome back! Your last login was <%= previousLoginTime %>.</div>
    <% session.removeAttribute("previousLoginTime"); %>
<% } %>

<%
    @SuppressWarnings("unchecked")
    LinkedList<String> recentlyViewed =
            (LinkedList<String>) session.getAttribute("recentlyViewedAppointments");
%>
<% if (recentlyViewed != null && !recentlyViewed.isEmpty()) { %>
<div class="chip-row">
    <% for (String num : recentlyViewed) { %>
        <a class="chip" href="${pageContext.request.contextPath}/appointments/search?appointmentNumber=<%= num %>"><%= num %></a>
    <% } %>
</div>
<% } %>

<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
%>
<% if (appointments == null || appointments.isEmpty()) { %>
    <div class="card empty-state">
        <p>No appointments have been registered yet.</p>
        <a href="${pageContext.request.contextPath}/appointments/new" class="btn btn-primary">Register the first appointment</a>
    </div>
<% } else { %>
<table class="data-table">
    <thead>
        <tr><th>Number</th><th>Patient</th><th>Dentist</th><th>Treatment</th><th>Date</th><th>Time</th><th>Status</th></tr>
    </thead>
    <tbody>
        <% for (Appointment a : appointments) {
            String badgeClass = "SCHEDULED".equals(a.getStatus()) ? "badge-scheduled"
                    : "COMPLETED".equals(a.getStatus()) ? "badge-completed" : "badge-cancelled";
        %>
        <tr>
            <td><a href="${pageContext.request.contextPath}/appointments/search?appointmentNumber=<%= a.getAppointmentNumber() %>"><%= a.getAppointmentNumber() %></a></td>
            <td><%= a.getPatient().getName() %></td>
            <td><%= a.getDentist().getName() %></td>
            <td><%= a.getTreatmentType().getName() %></td>
            <td class="num"><%= a.getAppointmentDate() %></td>
            <td class="num"><%= a.getAppointmentTime() %></td>
            <td><span class="badge <%= badgeClass %>"><%= a.getStatus() %></span></td>
        </tr>
        <% } %>
    </tbody>
</table>
<% } %>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>