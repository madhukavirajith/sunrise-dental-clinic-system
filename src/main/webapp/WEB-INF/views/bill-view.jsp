<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dentalclinic.model.Bill" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bill - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        @media print { .no-print { display: none; } }
        table { border-collapse: collapse; width: 100%; max-width: 500px; }
        td, th { border: 1px solid #ccc; padding: 8px; text-align: left; }
    </style>
</head>
<body>
<%
    Bill bill = (Bill) request.getAttribute("bill");
%>
    <h1>Sunrise Dental Clinic - Bill</h1>
    <p><strong>Appointment Number:</strong> <%= bill.getAppointment().getAppointmentNumber() %></p>
    <p><strong>Patient:</strong> <%= bill.getAppointment().getPatient().getName() %></p>
    <p><strong>Dentist:</strong> <%= bill.getAppointment().getDentist().getName() %></p>
    <p><strong>Treatment:</strong> <%= bill.getAppointment().getTreatmentType().getName() %></p>

    <table>
        <tr><th>Item</th><th>Amount (LKR)</th></tr>
        <tr><td>Consultation Fee</td><td><%= bill.getConsultationFee() %></td></tr>
        <tr><td>Treatment Cost</td><td><%= bill.getTreatmentCost() %></td></tr>
        <tr><td><strong>Total</strong></td><td><strong><%= bill.getTotalAmount() %></strong></td></tr>
    </table>

    <p class="no-print">
        <button onclick="window.print()">Print Bill</button>
        <a href="${pageContext.request.contextPath}/appointments/list">Back to appointments</a>
    </p>
</body>
</html>