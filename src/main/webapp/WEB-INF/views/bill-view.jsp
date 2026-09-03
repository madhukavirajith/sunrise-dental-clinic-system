<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="dentalclinic.model.Bill" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <meta name="description" content="Patient bill and receipt - Sunrise Dental Clinic.">
            <title>Bill - Sunrise Dental Clinic</title>
            <link rel="preconnect" href="https://fonts.googleapis.com">
            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <% Bill bill=(Bill) request.getAttribute("bill"); %>
                <div class="bill-page">
                    <p class="no-print"><a href="${pageContext.request.contextPath}/appointments/list">&larr; Back to
                            appointments</a></p>

                    <div class="bill-document">
                        <div class="bill-letterhead">
                            <img src="${pageContext.request.contextPath}/images/logo.png"
                                alt="Sunrise Dental Clinic logo" class="bill-letterhead-mark" width="52" height="52">
                            <div class="bill-letterhead-text">
                                <h2>Sunrise Dental Clinic</h2>
                                <span>Patient Bill / Receipt</span>
                            </div>
                        </div>

                        <dl class="bill-meta">
                            <dt>Appointment Number</dt>
                            <dd>
                                <%= bill.getAppointment().getAppointmentNumber() %>
                            </dd>
                            <dt>Date</dt>
                            <dd class="num">
                                <%= bill.getAppointment().getAppointmentDate() %>
                            </dd>
                            <dt>Patient</dt>
                            <dd>
                                <%= bill.getAppointment().getPatient().getName() %>
                            </dd>
                            <dt>Dentist</dt>
                            <dd>
                                <%= bill.getAppointment().getDentist().getName() %>
                            </dd>
                            <dt>Treatment</dt>
                            <dd>
                                <%= bill.getAppointment().getTreatmentType().getName() %>
                            </dd>
                        </dl>

                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Item</th>
                                    <th>Amount (LKR)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Consultation Fee</td>
                                    <td class="num">
                                        <%= bill.getConsultationFee() %>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Treatment Cost</td>
                                    <td class="num">
                                        <%= bill.getTreatmentCost() %>
                                    </td>
                                </tr>
                                <tr class="bill-total-row">
                                    <td>Total</td>
                                    <td class="num">
                                        <%= bill.getTotalAmount() %>
                                    </td>
                                </tr>
                            </tbody>
                        </table>

                        <div class="no-print bill-actions">
                            <button class="btn btn-primary" onclick="window.print()">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                    stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                    <polyline points="6 9 6 2 18 2 18 9" />
                                    <path
                                        d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2" />
                                    <rect x="6" y="14" width="12" height="8" />
                                </svg>
                                Print Bill
                            </button>
                            <a href="${pageContext.request.contextPath}/appointments/list"
                                class="btn btn-secondary">Back to appointments</a>
                        </div>
                    </div>
                </div>
        </body>

        </html>