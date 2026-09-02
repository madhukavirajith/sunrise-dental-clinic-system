<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dentalclinic.model.Notification" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Notification Center - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        table { border-collapse: collapse; width: 100%; }
        td, th { border: 1px solid #ccc; padding: 6px; text-align: left; }
        .status-SIMULATED { color: #b8860b; }
        .status-SENT { color: #1b6f6f; font-weight: bold; }
        .status-FAILED { color: #b00020; }
    </style>
</head>
<body>
    <h1>Notification Center</h1>
    <p>
        <% if ((Boolean) request.getAttribute("filtered")) { %>
            Showing notifications for one appointment.
            <a href="${pageContext.request.contextPath}/notifications">View all notifications</a>
        <% } else { %>
            Showing all notification history across the system.
        <% } %>
    </p>

<%
    List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
%>
    <table>
        <tr><th>Appointment</th><th>Channel</th><th>Recipient</th><th>Message</th><th>Status</th></tr>
        <% for (Notification n : notifications) { %>
        <tr>
            <td><%= n.getAppointment().getAppointmentNumber() %></td>
            <td><%= n.getChannel() %></td>
            <td><%= n.getRecipient() %></td>
            <td><%= n.getMessage() %></td>
            <td class="status-<%= n.getStatus() %>"><%= n.getStatus() %></td>
        </tr>
        <% } %>
        <% if (notifications.isEmpty()) { %>
        <tr><td colspan="5">No notifications found.</td></tr>
        <% } %>
    </table>

    <p><a href="${pageContext.request.contextPath}/appointments/list">Back to appointments</a></p>
</body>
</html>