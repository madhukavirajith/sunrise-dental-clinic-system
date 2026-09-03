<% request.setAttribute("pageTitle", "Revenue by Treatment Type"); request.setAttribute("activeNav", "reports"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="dentalclinic.model.TreatmentRevenueSummary" %>

<div class="page-header">
    <h1>Revenue by Treatment Type</h1>
    <p>Which services generate the most income, based on all booked appointments.</p>
</div>

<%
    List<TreatmentRevenueSummary> summaries = (List<TreatmentRevenueSummary>) request.getAttribute("summaries");
    double maxRevenue = 0;
    for (TreatmentRevenueSummary s : summaries) {
        double value = s.getTotalRevenue().doubleValue();
        if (value > maxRevenue) maxRevenue = value;
    }
%>

<% if (summaries.isEmpty()) { %>
    <div class="card empty-state"><p>No appointment data yet.</p></div>
<% } else { %>
<div class="card">
    <% for (TreatmentRevenueSummary s : summaries) {
        double widthPercent = maxRevenue == 0 ? 0 : (s.getTotalRevenue().doubleValue() / maxRevenue) * 100;
    %>
    <div style="margin-bottom:16px;">
        <div style="display:flex;justify-content:space-between;font-size:0.9rem;margin-bottom:4px;">
            <span><strong><%= s.getTreatmentName() %></strong> (<%= s.getAppointmentCount() %> appointments)</span>
            <span class="num">LKR <%= s.getTotalRevenue() %></span>
        </div>
        <div style="background:var(--color-bg);border:1px solid var(--color-border);border-radius:6px;height:10px;overflow:hidden;">
            <div style="width:<%= widthPercent %>%;background:var(--color-primary);height:100%;"></div>
        </div>
    </div>
    <% } %>
</div>
<% } %>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>