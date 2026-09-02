<% request.setAttribute("pageTitle", "Billing"); request.setAttribute("activeNav", "billing"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>

<div class="page-header">
    <h1>Calculate Bill</h1>
    <p>Enter an appointment number to view or generate its bill.</p>
</div>

<% if (request.getAttribute("errorMessage") != null) { %>
    <p class="alert alert-error"><%= request.getAttribute("errorMessage") %></p>
<% } %>

<div class="card">
    <form action="${pageContext.request.contextPath}/billing" method="get" class="form-grid" style="max-width:360px;">
        <div>
            <label for="appointmentNumber">Appointment Number</label>
            <input type="text" id="appointmentNumber" name="appointmentNumber" placeholder="APT-000001">
        </div>
        <button type="submit" class="btn btn-primary">Get Bill</button>
    </form>
</div>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>