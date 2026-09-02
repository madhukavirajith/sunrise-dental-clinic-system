<% request.setAttribute("pageTitle", "Register New Appointment"); request.setAttribute("activeNav", "new-appointment"); %>
<%@ include file="/WEB-INF/views/partials/app-header.jsp" %>

<div class="page-header">
    <h1>Register New Appointment</h1>
    <p>All fields except email are required.</p>
</div>

<% if (request.getAttribute("errorMessage") != null) { %>
    <p class="alert alert-error"><%= request.getAttribute("errorMessage") %></p>
<% } %>

<div class="card">
    <form action="${pageContext.request.contextPath}/appointments/new" method="post" class="form-grid">

        <div>
            <label for="patientName">Patient Name</label>
            <input type="text" id="patientName" name="patientName" value="${patientName}">
            <span class="field-error">${fieldErrors.patientName}</span>
        </div>

        <div>
            <label for="address">Address</label>
            <input type="text" id="address" name="address" value="${address}">
            <span class="field-error">${fieldErrors.address}</span>
        </div>

        <div>
            <label for="contactNumber">Contact Number</label>
            <input type="text" id="contactNumber" name="contactNumber" value="${contactNumber}" placeholder="0771234567">
            <span class="field-error">${fieldErrors.contactNumber}</span>
        </div>

        <div>
            <label for="email">Email (optional - for appointment confirmation)</label>
            <input type="email" id="email" name="email" value="${email}" placeholder="patient@example.com">
            <span class="field-error">${fieldErrors.email}</span>
        </div>

        <div>
            <label for="dentistId">Dentist</label>
            <select id="dentistId" name="dentistId">
                <option value="">Select a dentist</option>
                <option value="1">Dr. Perera (General Dentistry)</option>
                <option value="2">Dr. Fernando (Orthodontics)</option>
            </select>
            <span class="field-error">${fieldErrors.dentistId}</span>
        </div>

        <div>
            <label for="treatmentTypeId">Treatment Type</label>
            <select id="treatmentTypeId" name="treatmentTypeId">
                <option value="">Select a treatment</option>
                <option value="1">Routine Check-up</option>
                <option value="2">Teeth Cleaning</option>
                <option value="3">Root Canal</option>
                <option value="4">Tooth Extraction</option>
            </select>
            <span class="field-error">${fieldErrors.treatmentTypeId}</span>
        </div>

        <div>
            <label for="appointmentDate">Date</label>
            <input type="date" id="appointmentDate" name="appointmentDate">
            <span class="field-error">${fieldErrors.appointmentDate}</span>
        </div>

        <div>
            <label for="appointmentTime">Time</label>
            <input type="time" id="appointmentTime" name="appointmentTime">
            <span class="field-error">${fieldErrors.appointmentTime}</span>
        </div>

        <button type="submit" class="btn btn-primary">Register Appointment</button>
    </form>
</div>

<%@ include file="/WEB-INF/views/partials/app-footer.jsp" %>