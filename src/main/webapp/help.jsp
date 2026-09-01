<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Help - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h1>Help</h1>
    <p>Welcome to the Sunrise Dental Clinic system. Quick guide for new staff:</p>
    <ul>
        <li><strong>Register New Appointment</strong> - use the "Register New Appointment" link to book a patient in. All fields are required and validated.</li>
        <li><strong>Search Appointment</strong> - look up an existing appointment using its appointment number (format: APT-000001).</li>
        <li><strong>Calculate Bill</strong> - from an appointment's details page, click "Generate Bill", or go to Billing and enter the appointment number directly.</li>
        <li><strong>Daily Report</strong> - view all appointments and expected revenue for any given date.</li>
        <li><strong>Logout</strong> - always log out when you're done, especially on a shared computer.</li>
    </ul>
    <p><a href="${pageContext.request.contextPath}/appointments/list">Back to appointments</a></p>
</body>
</html>