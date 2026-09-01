<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
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
    </style>
</head>
<body>
    <h1>All Appointments</h1>
    <p>
        <a href="${pageContext.request.contextPath}/appointments/new">Register New Appointment</a> |
        <a href="${pageContext.request.contextPath}/appointments/search">Search Appointment</a> |
        <a href="${pageContext.request.contextPath}/reports/daily">Daily Report</a> |
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