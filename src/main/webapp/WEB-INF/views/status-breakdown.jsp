<% request.setAttribute("pageTitle", "Status Breakdown"); request.setAttribute("activeNav", "reports"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="dentalclinic.model.StatusSummary" %>

<div class="page-header">
    <h1>Appointment Status Breakdown</h1>
    <p>A high cancellation rate here would be worth investigating - this report exists to surface that.</p>
</div>

<%
    List<StatusSummary> summaries = (List<StatusSummary>) request.getAttribute("summaries");
    int total = 0;
    for (StatusSummary s : summaries) total += s.getCount();
%>

<% if (summaries.isEmpty()) { %>
    <div class="card empty-state"><p>No appointment data yet.</p></div>
<% } else { %>
<div class="chip-row">
    <% for (StatusSummary s : summaries) {
        double percent = total == 0 ? 0 : (s.getCount() * 100.0 / total);
        String badgeClass = "SCHEDULED".equals(s.getStatus()) ? "badge-scheduled"
                : "COMPLETED".equals(s.getStatus()) ? "badge-completed" : "badge-cancelled";
    %>
    <span class="badge <%= badgeClass %>"><%= s.getStatus() %>: <%= s.getCount() %> (<%= String.format("%.0f", percent) %>%)</span>
    <% } %>
</div>
<% } %>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>