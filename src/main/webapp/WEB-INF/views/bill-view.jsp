<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dentalclinic.model.Bill" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bill - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%
    Bill bill = (Bill) request.getAttribute("bill");
%>
<div class="bill-page">
    <p class="no-print"><a href="${pageContext.request.contextPath}/appointments/list">&larr; Back to appointments</a></p>

    <div class="bill-document">
        <div class="bill-letterhead">
            <svg class="bill-letterhead-mark" viewBox="0 0 40 40" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                <circle cx="20" cy="24" r="13" fill="#E8A33D"/>
                <path d="M4 24a16 16 0 0 1 32 0" fill="none" stroke="#0E6B62" stroke-width="3" stroke-linecap="round"/>
            </svg>
            <div class="bill-letterhead-text">
                <h2>Sunrise Dental Clinic</h2>
                <span>Patient Bill / Receipt</span>
            </div>
        </div>

        <dl class="bill-meta">
            <dt>Appointment Number</dt><dd><%= bill.getAppointment().getAppointmentNumber() %></dd>
            <dt>Date</dt><dd class="num"><%= bill.getAppointment().getAppointmentDate() %></dd>
            <dt>Patient</dt><dd><%= bill.getAppointment().getPatient().getName() %></dd>
            <dt>Dentist</dt><dd><%= bill.getAppointment().getDentist().getName() %></dd>
            <dt>Treatment</dt><dd><%= bill.getAppointment().getTreatmentType().getName() %></dd>
        </dl>

        <table class="data-table">
            <thead><tr><th>Item</th><th>Amount (LKR)</th></tr></thead>
            <tbody>
                <tr><td>Consultation Fee</td><td class="num"><%= bill.getConsultationFee() %></td></tr>
                <tr><td>Treatment Cost</td><td class="num"><%= bill.getTreatmentCost() %></td></tr>
                <tr class="bill-total-row"><td>Total</td><td class="num"><%= bill.getTotalAmount() %></td></tr>
            </tbody>
        </table>

        <p class="no-print bill-actions">
            <button class="btn btn-primary" onclick="window.print()">Print Bill</button>
            <a href="${pageContext.request.contextPath}/appointments/list" class="btn btn-secondary">Back to appointments</a>
        </p>
    </div>
</div>
</body>
</html>