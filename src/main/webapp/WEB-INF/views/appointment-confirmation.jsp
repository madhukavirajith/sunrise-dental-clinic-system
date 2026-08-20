<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Appointment Confirmed - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h1>Appointment Registered Successfully</h1>
    <p>Appointment Number: <strong>${appointmentNumber}</strong></p>
    <p><a href="${pageContext.request.contextPath}/appointments/new">Register another appointment</a></p>
</body>
</html>