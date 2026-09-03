<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <% // Included (not forwarded) into the calling page, so it shares the // same request/session objects. Each page
        must set pageTitle and // activeNav as request attributes before including this file. String activeNav=(String)
        request.getAttribute("activeNav"); if (activeNav==null) activeNav="" ; String pageTitle=(String)
        request.getAttribute("pageTitle"); if (pageTitle==null) pageTitle="Sunrise Dental Clinic" ; %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <meta name="description"
                content="Sunrise Dental Clinic staff portal - manage appointments, patient records, billing, and reports.">
            <title>
                <%= pageTitle %> - Sunrise Dental Clinic
            </title>
            <link rel="preconnect" href="https://fonts.googleapis.com">
            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <div class="app-shell">
                <nav class="sidebar" aria-label="Main navigation">
                    <div class="sidebar-brand">
                        <img src="${pageContext.request.contextPath}/images/logo.png" alt="Sunrise Dental Clinic logo"
                            class="sidebar-brand-mark" width="40" height="40">
                        <span class="sidebar-brand-text">Sunrise<br>Dental Clinic</span>
                    </div>

                    <div class="sidebar-nav">
                        <a href="${pageContext.request.contextPath}/appointments/list" class="<%= "
                            dashboard".equals(activeNav) ? "active" : "" %>">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                <rect x="3" y="3" width="7" height="7" rx="1.5" />
                                <rect x="14" y="3" width="7" height="7" rx="1.5" />
                                <rect x="3" y="14" width="7" height="7" rx="1.5" />
                                <rect x="14" y="14" width="7" height="7" rx="1.5" />
                            </svg>
                            Appointments
                        </a>
                        <a href="${pageContext.request.contextPath}/appointments/new" class="<%= "
                            new-appointment".equals(activeNav) ? "active" : "" %>">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                <path d="M12 5v14M5 12h14" />
                            </svg>
                            New Appointment
                        </a>
                        <a href="${pageContext.request.contextPath}/appointments/search" class="<%= "
                            search".equals(activeNav) ? "active" : "" %>">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                <circle cx="11" cy="11" r="7" />
                                <path d="M21 21l-4.35-4.35" />
                            </svg>
                            Search
                        </a>

                        <div class="sidebar-divider"></div>

                        <a href="${pageContext.request.contextPath}/billing" class="<%= " billing".equals(activeNav)
                            ? "active" : "" %>">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                <rect x="3" y="5" width="18" height="14" rx="2" />
                                <path d="M3 10h18" />
                            </svg>
                            Billing
                        </a>
                        <a href="${pageContext.request.contextPath}/reports" class="<%= " reports".equals(activeNav)
                            ? "active" : "" %>">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                <path d="M4 19V9M12 19V5M20 19v-7" />
                            </svg>
                            Reports
                        </a>
                        <a href="${pageContext.request.contextPath}/notifications" class="<%= "
                            notifications".equals(activeNav) ? "active" : "" %>">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                <path d="M18 8a6 6 0 0 0-12 0c0 7-3 9-3 9h18s-3-2-3-9" />
                                <path d="M13.7 21a2 2 0 0 1-3.4 0" />
                            </svg>
                            Notifications
                        </a>
                        <a href="${pageContext.request.contextPath}/help.jsp" class="<%= " help".equals(activeNav)
                            ? "active" : "" %>">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                <circle cx="12" cy="12" r="9" />
                                <path d="M9.5 9a2.5 2.5 0 1 1 3.5 2.3c-.8.4-1 .9-1 1.7" />
                                <circle cx="12" cy="17" r="0.5" fill="currentColor" />
                            </svg>
                            Help
                        </a>
                    </div>

                    <div class="sidebar-user">
                        <strong>
                            <%= session.getAttribute("loggedInUser") !=null ? session.getAttribute("loggedInUser") : ""
                                %>
                        </strong>
                        <span>
                            <%= session.getAttribute("loggedInRole") !=null ? session.getAttribute("loggedInRole") : ""
                                %>
                        </span>
                        <a href="${pageContext.request.contextPath}/logout">
                            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
                                <polyline points="16 17 21 12 16 7" />
                                <line x1="21" y1="12" x2="9" y2="12" />
                            </svg>
                            Log out
                        </a>
                    </div>
                </nav>

                <main class="main">