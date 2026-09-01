<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dentalclinic.model.Appointment" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Daily Report - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        table { border-collapse: collapse; width: 100%; }
        td, th { border: 1px solid #ccc; padding: 6px; text-align: left; }
    </style>
</head>
<body>
    <h1>Daily Appointments Report</h1>
    <form action="${pageContext.request.contextPath}/reports/daily" method="get">
        <label for="date">Date</label>
        <input type="date" id="date" name="date" value="${reportDate}">
        <button type="submit">View Report</button>
    </form>

    <p><strong>Date:</strong> ${reportDate} &nbsp;
       <strong>Total Appointments:</strong> ${appointmentCount} &nbsp;
       <strong>Expected Revenue:</strong> LKR ${expectedRevenue}</p>

<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
%>
    <table>
        <tr><th>Number</th><th>Time</th><th>Patient</th><th>Dentist</th><th>Treatment</th></tr>
        <% for (Appointment a : appointments) { %>
        <tr>
            <td><%= a.getAppointmentNumber() %></td>
            <td><%= a.getAppointmentTime() %></td>
            <td><%= a.getPatient().getName() %></td>
            <td><%= a.getDentist().getName() %></td>
            <td><%= a.getTreatmentType().getName() %></td>
        </tr>
        <% } %>
    </table>

    <p><a href="${pageContext.request.contextPath}/appointments/list">Back to appointments</a></p>
</body>
</html>