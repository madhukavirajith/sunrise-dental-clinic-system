<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Staff Login - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="auth-split">
    <div class="auth-panel">
        <svg class="auth-panel-mark" viewBox="0 0 40 40" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
            <circle cx="20" cy="24" r="13" fill="#E8A33D"/>
            <path d="M4 24a16 16 0 0 1 32 0" fill="none" stroke="#FAF8F4" stroke-width="3" stroke-linecap="round"/>
        </svg>
        <h1>Sunrise Dental Clinic</h1>
        <p>Sign in to manage appointments, patient records, and billing for the day.</p>
    </div>
    <div class="auth-form-panel">
        <div class="auth-form-card">
            <h2>Staff Login</h2>

            <% if ("true".equals(request.getParameter("sessionExpired"))) { %>
                <p class="alert alert-info">Please log in to continue. Your session may have expired.</p>
            <% } %>

            <% if (request.getAttribute("errorMessage") != null) { %>
                <p class="alert alert-error"><%= request.getAttribute("errorMessage") %></p>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="post" class="form-grid">
                <div>
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" required autofocus>
                </div>
                <div>
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary">Log in</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>