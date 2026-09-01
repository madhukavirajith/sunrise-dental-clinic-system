<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Appointment - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h1>Search Appointment</h1>
    <% if (request.getAttribute("errorMessage") != null) { %>
        <p class="error"><%= request.getAttribute("errorMessage") %></p>
    <% } %>
    <form action="${pageContext.request.contextPath}/appointments/search" method="get">
        <label for="appointmentNumber">Appointment Number</label>
        <input type="text" id="appointmentNumber" name="appointmentNumber" placeholder="APT-000001">
        <button type="submit">Search</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/appointments/list">View all appointments</a></p>
</body>
</html>