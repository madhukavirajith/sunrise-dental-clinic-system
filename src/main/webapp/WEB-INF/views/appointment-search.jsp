<% request.setAttribute("pageTitle", "Search Appointment"); request.setAttribute("activeNav", "search"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>

<div class="page-header">
    <h1>Search Appointment</h1>
    <p>Look up an appointment by its number.</p>
</div>

<% if (request.getAttribute("errorMessage") != null) { %>
    <p class="alert alert-error"><%= request.getAttribute("errorMessage") %></p>
<% } %>

<div class="card">
    <form action="${pageContext.request.contextPath}/appointments/search" method="get" class="form-grid" style="max-width:360px;">
        <div>
            <label for="appointmentNumber">Appointment Number</label>
            <input type="text" id="appointmentNumber" name="appointmentNumber"
                   placeholder="APT-000001" value="${lastSearchedAppointment}">
        </div>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>
</div>

<p style="margin-top:16px;"><a href="${pageContext.request.contextPath}/appointments/list">View all appointments</a></p>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>