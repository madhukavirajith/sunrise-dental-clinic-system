<% request.setAttribute("pageTitle", "Appointment Details"); request.setAttribute("activeNav", "search"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>
<%@ page import="dentalclinic.model.Appointment" %>

<%
    Appointment a = (Appointment) request.getAttribute("appointment");
    String badgeClass = "SCHEDULED".equals(a.getStatus()) ? "badge-scheduled"
            : "COMPLETED".equals(a.getStatus()) ? "badge-completed" : "badge-cancelled";
    Object patientAppointmentCount = request.getAttribute("patientAppointmentCount");
%>

<div class="page-header">
    <h1><%= a.getAppointmentNumber() %></h1>
    <p><span class="badge <%= badgeClass %>"><%= a.getStatus() %></span></p>
</div>

<div class="card">
    <dl class="bill-meta">
        <dt>Patient</dt><dd><%= a.getPatient().getName() %> (<%= a.getPatient().getContactNumber() %>)</dd>
        <dt>Dentist</dt><dd><%= a.getDentist().getName() %></dd>
        <dt>Treatment</dt><dd><%= a.getTreatmentType().getName() %></dd>
        <dt>Date</dt><dd class="num"><%= a.getAppointmentDate() %></dd>
        <dt>Time</dt><dd class="num"><%= a.getAppointmentTime() %></dd>
        <% if (patientAppointmentCount != null) { %>
        <dt>Patient's Total Appointments</dt><dd class="num"><%= patientAppointmentCount %></dd>
        <% } %>
    </dl>

    <div class="bill-actions">
        <a href="${pageContext.request.contextPath}/billing?appointmentNumber=<%= a.getAppointmentNumber() %>" class="btn btn-primary">Generate Bill</a>
        <a href="${pageContext.request.contextPath}/notifications?appointmentId=<%= a.getAppointmentId() %>" class="btn btn-secondary">Notification History</a>
        <a href="${pageContext.request.contextPath}/appointments/list" class="btn btn-secondary">Back to list</a>
    </div>
</div>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>