<% request.setAttribute("pageTitle", "Appointment Confirmed"); request.setAttribute("activeNav", "new-appointment"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>

<div class="page-header">
    <h1>Appointment Registered</h1>
</div>

<div class="card">
    <p class="alert alert-info" style="margin-bottom:0;">
        Appointment number: <strong><%= request.getAttribute("appointmentNumber") %></strong>
    </p>
    <p style="margin-top:16px;">A confirmation has been sent through the clinic's notification system - check the Notification Center to see the delivery status.</p>
    <a href="${pageContext.request.contextPath}/appointments/new" class="btn btn-primary">Register another appointment</a>
    <a href="${pageContext.request.contextPath}/notifications" class="btn btn-secondary">View Notification Center</a>
</div>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>