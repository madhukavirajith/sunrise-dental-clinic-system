<% request.setAttribute("pageTitle", "Dentist Workload"); request.setAttribute("activeNav", "reports"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="dentalclinic.model.DentistWorkloadSummary" %>

<div class="page-header">
    <h1>Dentist Workload</h1>
    <p>Appointment volume per dentist - useful for staffing and scheduling decisions.</p>
</div>

<%
    List<DentistWorkloadSummary> summaries = (List<DentistWorkloadSummary>) request.getAttribute("summaries");
%>
<% if (summaries.isEmpty()) { %>
    <div class="card empty-state"><p>No appointment data yet.</p></div>
<% } else { %>
<table class="data-table">
    <thead><tr><th>Dentist</th><th>Total Appointments</th><th>Completed</th><th>Completion Rate</th></tr></thead>
    <tbody>
        <% for (DentistWorkloadSummary s : summaries) {
            double rate = s.getTotalAppointments() == 0 ? 0
                    : (s.getCompletedAppointments() * 100.0 / s.getTotalAppointments());
        %>
        <tr>
            <td><%= s.getDentistName() %></td>
            <td class="num"><%= s.getTotalAppointments() %></td>
            <td class="num"><%= s.getCompletedAppointments() %></td>
            <td class="num"><%= String.format("%.0f", rate) %>%</td>
        </tr>
        <% } %>
    </tbody>
</table>
<% } %>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>