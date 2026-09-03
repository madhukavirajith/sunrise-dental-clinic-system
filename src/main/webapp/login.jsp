<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description"
            content="Staff login portal for Sunrise Dental Clinic - manage appointments, billing, and patient records.">
        <title>Staff Login - Sunrise Dental Clinic</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link rel="stylesheet" href="css/style.css">
    </head>

    <body>
        <div class="auth-split">
            <div class="auth-panel">
                <div class="auth-panel-grid" aria-hidden="true"></div>
                <img src="images/logo.png" alt="Sunrise Dental Clinic logo" class="auth-panel-mark" width="72"
                    height="72">
                <h1>Sunrise <span>Dental</span> Clinic</h1>
                <p>Sign in to manage appointments, patient records, and billing for the day.</p>
                <div class="auth-features" aria-hidden="true">
                    <div class="auth-feature"><span class="auth-feature-dot"></span>Appointment scheduling &amp;
                        management</div>
                    <div class="auth-feature"><span class="auth-feature-dot"></span>Patient records &amp; history</div>
                    <div class="auth-feature"><span class="auth-feature-dot"></span>Billing &amp; receipt generation
                    </div>
                    <div class="auth-feature"><span class="auth-feature-dot"></span>Reports &amp; analytics dashboard
                    </div>
                </div>
            </div>
            <div class="auth-form-panel">
                <div class="auth-form-card">
                    <h2>Welcome back</h2>
                    <p>Sign in to your staff account to continue.</p>

                    <% if ("true".equals(request.getParameter("sessionExpired"))) { %>
                        <p class="alert alert-info">Please log in to continue. Your session may have expired.</p>
                        <% } %>

                            <% if (request.getAttribute("errorMessage") !=null) { %>
                                <p class="alert alert-error">
                                    <%= request.getAttribute("errorMessage") %>
                                </p>
                                <% } %>

                                    <form action="${pageContext.request.contextPath}/login" method="post"
                                        class="form-grid">
                                        <div>
                                            <label for="username">Username</label>
                                            <input type="text" id="username" name="username" required autofocus
                                                autocomplete="username" placeholder="Enter your username">
                                        </div>
                                        <div>
                                            <label for="password">Password</label>
                                            <input type="password" id="password" name="password" required
                                                autocomplete="current-password" placeholder="Enter your password">
                                        </div>
                                        <button type="submit" class="btn btn-primary">Sign in</button>
                                    </form>
                </div>
            </div>
        </div>
    </body>

    </html>