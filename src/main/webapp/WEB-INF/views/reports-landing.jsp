<% request.setAttribute("pageTitle", "Reports"); request.setAttribute("activeNav", "reports"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>

<div class="page-header">
    <h1>Reports</h1>
    <p>Reports intended to support day-to-day and business decisions, not just record-keeping.</p>
</div>

<div class="card">
    <h3><a href="${pageContext.request.contextPath}/reports/daily">Daily Schedule</a></h3>
    <p>Appointments and expected revenue for a chosen date - operational planning for the day ahead.</p>
</div>

<div class="card">
    <h3><a href="${pageContext.request.contextPath}/reports/revenue-by-treatment">Revenue by Treatment Type</a></h3>
    <p>Which services generate the most income - supports pricing and service-mix decisions.</p>
</div>

<div class="card">
    <h3><a href="${pageContext.request.contextPath}/reports/dentist-workload">Dentist Workload</a></h3>
    <p>Appointment volume per dentist - supports staffing and scheduling decisions.</p>
</div>

<div class="card">
    <h3><a href="${pageContext.request.contextPath}/reports/status-breakdown">Appointment Status Breakdown</a></h3>
    <p>Scheduled vs. completed vs. cancelled - surfaces cancellation-rate issues worth addressing.</p>
</div>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>