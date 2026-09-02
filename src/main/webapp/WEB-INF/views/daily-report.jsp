<% request.setAttribute("pageTitle", "Daily Report"); request.setAttribute("activeNav", "reports"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="dentalclinic.model.Appointment" %>

<div class="page-header">
    <h1>Daily Appointments Report</h1>
</div>

<div class="card" style="margin-bottom:20px;">
    <form action="${pageContext.request.contextPath}/reports/daily" method="get" class="form-grid" style="max-width:320px;">
        <div>
            <label for="date">Date</label>
            <input type="date" id="date" name="date" value="${reportDate}">
        </div>
        <button type="submit" class="btn btn-primary">View Report</button>
    </form>
</div>

<div class="chip-row">
    <span class="chip">Date: ${reportDate}</span>
    <span class="chip">Appointments: ${appointmentCount}</span>
    <span class="chip">Expected Revenue: LKR ${expectedRevenue}</span>
</div>

<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
%>
<% if (appointments == null || appointments.isEmpty()) { %>
    <div class="card empty-state">
        <p>No appointments are scheduled for this date.</p>
    </div>
<% } else { %>
<table class="data-table">
    <thead><tr><th>Number</th><th>Time</th><th>Patient</th><th>Dentist</th><th>Treatment</th></tr></thead>
    <tbody>
        <% for (Appointment a : appointments) { %>
        <tr>
            <td><%= a.getAppointmentNumber() %></td>
            <td class="num"><%= a.getAppointmentTime() %></td>
            <td><%= a.getPatient().getName() %></td>
            <td><%= a.getDentist().getName() %></td>
            <td><%= a.getTreatmentType().getName() %></td>
        </tr>
        <% } %>
    </tbody>
</table>
<% } %>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>