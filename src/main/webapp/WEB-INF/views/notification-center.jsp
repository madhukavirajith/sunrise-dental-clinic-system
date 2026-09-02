<% request.setAttribute("pageTitle", "Notification Center"); request.setAttribute("activeNav", "notifications"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="dentalclinic.model.Notification" %>

<div class="page-header">
    <h1>Notification Center</h1>
    <p>
        <% if (Boolean.TRUE.equals(request.getAttribute("filtered"))) { %>
            Showing notifications for one appointment. <a href="${pageContext.request.contextPath}/notifications">View all</a>
        <% } else { %>
            Showing all notification history across the system.
        <% } %>
    </p>
</div>

<%
    List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
%>
<% if (notifications == null || notifications.isEmpty()) { %>
    <div class="card empty-state">
        <p>No notifications found.</p>
    </div>
<% } else { %>
<table class="data-table">
    <thead><tr><th>Appointment</th><th>Channel</th><th>Recipient</th><th>Message</th><th>Status</th></tr></thead>
    <tbody>
        <% for (Notification n : notifications) {
            String badgeClass = "SENT".equals(n.getStatus()) ? "badge-sent"
                    : "SIMULATED".equals(n.getStatus()) ? "badge-simulated" : "badge-failed";
        %>
        <tr>
            <td><%= n.getAppointment().getAppointmentNumber() %></td>
            <td><%= n.getChannel() %></td>
            <td><%= n.getRecipient() %></td>
            <td><%= n.getMessage() %></td>
            <td><span class="badge <%= badgeClass %>"><%= n.getStatus() %></span></td>
        </tr>
        <% } %>
    </tbody>
</table>
<% } %>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>