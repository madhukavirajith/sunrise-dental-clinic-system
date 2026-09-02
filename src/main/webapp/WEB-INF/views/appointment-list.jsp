<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="dentalclinic.model.Appointment" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>All Appointments - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        table { border-collapse: collapse; width: 100%; }
        td, th { border: 1px solid #ccc; padding: 6px; text-align: left; }
        .welcome-back { color: #1b6f6f; font-weight: bold; }
        .recently-viewed { margin: 10px 0; padding: 8px; background: #f4f4f4; }
        .recently-viewed a { margin-right: 10px; }
    </style>
</head>
<body>
    <h1>All Appointments</h1>

<%
    Object previousLoginTime = session.getAttribute("previousLoginTime");
%>
<% if (previousLoginTime != null) { %>
    <p class="welcome-back">Welcome back! Your last login was: <%= previousLoginTime %></p>
    <% session.removeAttribute("previousLoginTime"); %>
<% } %>

<%
    @SuppressWarnings("unchecked")
    LinkedList<String> recentlyViewed =
            (LinkedList<String>) session.getAttribute("recentlyViewedAppointments");
%>
<% if (recentlyViewed != null && !recentlyViewed.isEmpty()) { %>
<div class="recently-viewed">
    <strong>Recently Viewed:</strong>
    <% for (String num : recentlyViewed) { %>
        <a href="${pageContext.request.contextPath}/appointments/search?appointmentNumber=<%= num %>"><%= num %></a>
    <% } %>
</div>
<% } %>

    <p>
        <a href="${pageContext.request.contextPath}/appointments/new">Register New Appointment</a> |
        <a href="${pageContext.request.contextPath}/appointments/search">Search Appointment</a> |
        <a href="${pageContext.request.contextPath}/reports/daily">Daily Report</a> |
        <a href="${pageContext.request.contextPath}/notifications">Notification Center</a> |
        <a href="${pageContext.request.contextPath}/help.jsp">Help</a> |
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </p>
<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
%>
    <table>
        <tr><th>Number</th><th>Patient</th><th>Dentist</th><th>Treatment</th><th>Date</th><th>Time</th><th>Status</th></tr>
        <% for (Appointment a : appointments) { %>
        <tr>
            <td><a href="${pageContext.request.contextPath}/appointments/search?appointmentNumber=<%= a.getAppointmentNumber() %>"><%= a.getAppointmentNumber() %></a></td>
            <td><%= a.getPatient().getName() %></td>
            <td><%= a.getDentist().getName() %></td>
            <td><%= a.getTreatmentType().getName() %></td>
            <td><%= a.getAppointmentDate() %></td>
            <td><%= a.getAppointmentTime() %></td>
            <td><%= a.getStatus() %></td>
        </tr>
        <% } %>
    </table>
</body>
</html>