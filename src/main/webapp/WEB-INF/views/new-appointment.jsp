<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register New Appointment - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h1>Register New Appointment</h1>

    <% if (request.getAttribute("errorMessage") != null) { %>
        <p class="error"><%= request.getAttribute("errorMessage") %></p>
    <% } %>

    <form action="${pageContext.request.contextPath}/appointments/new" method="post">

        <label for="patientName">Patient Name</label>
        <input type="text" id="patientName" name="patientName" value="${patientName}">
        <span class="error">${fieldErrors.patientName}</span>

        <label for="address">Address</label>
        <input type="text" id="address" name="address" value="${address}">
        <span class="error">${fieldErrors.address}</span>

        <label for="contactNumber">Contact Number</label>
        <input type="text" id="contactNumber" name="contactNumber" value="${contactNumber}" placeholder="0771234567">
        <span class="error">${fieldErrors.contactNumber}</span>

        <label for="email">Email (optional - for appointment confirmation)</label>
        <input type="email" id="email" name="email" value="${email}" placeholder="patient@example.com">
        <span class="error">${fieldErrors.email}</span>

        <label for="dentistId">Dentist</label>
        <select id="dentistId" name="dentistId">
            <option value="">-- Select a dentist --</option>
            <option value="1">Dr. Perera (General Dentistry)</option>
            <option value="2">Dr. Fernando (Orthodontics)</option>
        </select>
        <span class="error">${fieldErrors.dentistId}</span>

        <label for="treatmentTypeId">Treatment Type</label>
        <select id="treatmentTypeId" name="treatmentTypeId">
            <option value="">-- Select a treatment --</option>
            <option value="1">Routine Check-up</option>
            <option value="2">Teeth Cleaning</option>
            <option value="3">Root Canal</option>
            <option value="4">Tooth Extraction</option>
        </select>
        <span class="error">${fieldErrors.treatmentTypeId}</span>

        <label for="appointmentDate">Date</label>
        <input type="date" id="appointmentDate" name="appointmentDate">
        <span class="error">${fieldErrors.appointmentDate}</span>

        <label for="appointmentTime">Time</label>
        <input type="time" id="appointmentTime" name="appointmentTime">
        <span class="error">${fieldErrors.appointmentTime}</span>

        <button type="submit">Register Appointment</button>
    </form>
</body>
</html>