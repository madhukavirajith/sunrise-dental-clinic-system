<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dentalclinic.model.Appointment" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Appointment Details - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%
    Appointment a = (Appointment) request.getAttribute("appointment");
%>
    <h1>Appointment Details</h1>
    <p><strong>Number:</strong> <%= a.getAppointmentNumber() %></p>
    <p><strong>Patient:</strong> <%= a.getPatient().getName() %> (<%= a.getPatient().getContactNumber() %>)</p>
    <p><strong>Dentist:</strong> <%= a.getDentist().getName() %></p>
    <p><strong>Treatment:</strong> <%= a.getTreatmentType().getName() %></p>
    <p><strong>Date/Time:</strong> <%= a.getAppointmentDate() %> at <%= a.getAppointmentTime() %></p>
    <p><strong>Status:</strong> <%= a.getStatus() %></p>

    <p>
        <a href="${pageContext.request.contextPath}/billing?appointmentNumber=<%= a.getAppointmentNumber() %>">Generate Bill</a>
        | <a href="${pageContext.request.contextPath}/notifications?appointmentId=<%= a.getAppointmentId() %>">View Notification History</a>
        | <a href="${pageContext.request.contextPath}/appointments/list">Back to list</a>
    </p>
</body>
</html>