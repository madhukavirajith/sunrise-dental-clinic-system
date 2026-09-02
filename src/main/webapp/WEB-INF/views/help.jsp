<% request.setAttribute("pageTitle", "Help"); request.setAttribute("activeNav", "help"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>

<div class="page-header">
    <h1>Help</h1>
    <p>A quick guide for new staff.</p>
</div>

<div class="card">
    <h3>Register New Appointment</h3>
    <p>Use "New Appointment" in the sidebar to book a patient in. All fields except email are required and validated before saving.</p>

    <h3>Search Appointment</h3>
    <p>Look up an existing appointment using its appointment number, in the format APT-000001.</p>

    <h3>Calculate Bill</h3>
    <p>From an appointment's details page, choose "Generate Bill", or go to Billing and enter the appointment number directly.</p>

    <h3>Daily Report</h3>
    <p>View all appointments and expected revenue for any given date.</p>

    <h3>Notification Center</h3>
    <p>Every email and SMS alert the system has attempted is logged here, including whether it succeeded, was simulated, or failed.</p>

    <h3>Log Out</h3>
    <p>Always log out when finished, especially on a shared computer.</p>
</div>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>