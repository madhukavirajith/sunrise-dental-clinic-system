<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h1>Staff Login</h1>

    <% if (request.getAttribute("errorMessage") != null) { %>
        <p class="error"><%= request.getAttribute("errorMessage") %></p>
    <% } %>
    <% if ("true".equals(request.getParameter("sessionExpired"))) { %>
        <p class="error">Please log in to continue. Your session may have expired.</p>
    <% } %>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Log in</button>
    </form>
</body>
</html>
