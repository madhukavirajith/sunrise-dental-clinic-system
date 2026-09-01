<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Billing - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h1>Calculate Bill</h1>
    <% if (request.getAttribute("errorMessage") != null) { %>
        <p class="error"><%= request.getAttribute("errorMessage") %></p>
    <% } %>
    <form action="${pageContext.request.contextPath}/billing" method="get">
        <label for="appointmentNumber">Appointment Number</label>
        <input type="text" id="appointmentNumber" name="appointmentNumber" placeholder="APT-000001">
        <button type="submit">Get Bill</button>
    </form>
</body>
</html>