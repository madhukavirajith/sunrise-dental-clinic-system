# Sunrise Dental Clinic - Appointment and Patient Management System

CIS6003 Advanced Programming (WRIT1) - ICBT / Cardiff Met, 2025/26 Semester 1

A Java EE web application (developed as a web service, **no third-party frameworks**)
that replaces Sunrise Dental Clinic's paper-based appointment and patient
record system with a computerised, database-backed system.

## Tech Stack

| Layer          | Technology                                                               |
|----------------|--------------------------------------------------------------------------|
| Language       | Java 17                                                                  |
| Web layer      | Jakarta EE (Servlets, JSP, JSTL) |
| Frontend       | Hand-written JSP + CSS         |
| Database       | MySQL via WAMP                                                           |
| Build tool     | Maven                                                                    |
| Testing        | JUnit 5                                                                  |
| IDE            | IntelliJ IDEA (Community Edition + Smart Tomcat plugin)                  |
| CI             | GitHub Actions (`.github/workflows/ci.yml`)                              |


## Architecture

3-tier architecture:

```
Presentation tier    src/main/java/.../servlet, src/main/webapp (JSP/CSS)
Business logic tier  src/main/java/.../service (+ strategy, notification subpackages)
Data access tier     src/main/java/.../dao (+ impl subpackage)
```

### Design patterns implemented

| Pattern | Where | Why |
|---|---|---|
| **DAO** | `dao` / `dao.impl` | Decouples business logic from persistence details (Dependency Inversion) |
| **Singleton** | `util.DBConnectionManager` | One shared point of DB configuration |
| **Strategy** | `service.strategy.*`, used by `service.BillingService` | Treatment-dependent billing rules, extensible without modifying existing code (Open/Closed) |
| **Observer** | `service.notification.*` | `NotificationDispatcher` (subject) notifies `NotificationChannel` implementations (Email, SMS) without either side knowing about the other's internals |
| **Layered / 3-tier architecture** | overall package structure | Separation of concerns across presentation, business logic, and data access |

## Project Structure

```
sunrise-dental-clinic-system/
├── pom.xml
├── database/
│   ├── schema.sql                       
│   ├── procedures_and_triggers.sql      
│   ├── notifications_and_email.sql      
│   └── advanced_features_v2.sql         
├── docs/
│   ├── uml/                     
│   ├── test-plan/               
│   └── screenshots/            
├── src/main/java/dentalclinic/
│   ├── model/            
│   ├── dao/  dao/impl/   
│   ├── dao/mapper/       
│   ├── service/          
│   ├── service/strategy/ 
│   ├── service/notification/  
│   ├── servlet/          
│   ├── filter/           
│   ├── util/             
│   └── exception/
├── src/main/resources/
│   └── db.properties  
├── src/main/webapp/
│   ├── index.jsp, login.jsp, help.jsp
│   ├── css/style.css             
│   └── WEB-INF/
│       ├── web.xml
│       └── views/
│           ├── partials/app-header.jsp, app-footer.jsp  
│           └── *.jsp             
└── src/test/java/dentalclinic/ 
```

## Setup Instructions

1. **Start WAMP** and confirm MySQL is running (green tray icon / MySQL service started).

2. **Run the SQL scripts, in this exact order**, via phpMyAdmin → SQL tab → paste → Go:
   ```
   database/schema.sql
   database/procedures_and_triggers.sql
   database/notifications_and_email.sql
   database/advanced_features_v2.sql
   ```

3. **Configure credentials:**
   ```bash
   cp src/main/resources/db.properties.example src/main/resources/db.properties
   # edit db.properties
   ```

4. **Create a staff login account** (no self-registration exists - see Assumptions):
    - Run `PasswordHashGenerator.java` (right-click → Run) to generate a hashed password
    - In phpMyAdmin, insert a row into `staff_user` with that hash and `role` set to
      `RECEPTIONIST`, `DENTIST`, or `ADMIN`

5. **Open in IntelliJ IDEA** as a Maven project.

6. **Set up a local Tomcat 10.1.x server** (required for the `jakarta.servlet` namespace
   used throughout this project):
    - Community Edition users: install the **Smart Tomcat** plugin (Settings → Plugins → Marketplace)
    - Create a Smart Tomcat run configuration pointing at your Tomcat install, context path `/`

7. **Run tests:**
   ```bash
   mvn test      
   ```
   Integration tests (`*IT`) require a running local database and are run
   individually from IntelliJ, not via `mvn test` - see Testing section below.

## Functional Requirements

- [x] User Authentication (Login) - PBKDF2 password hashing, session-based, `AuthenticationFilter`
- [x] Register New Appointment - full validation, auto-generated appointment number, find-or-create patient
- [x] Display Appointment Details - search by appointment number
- [x] Calculate and Print Bill - Strategy-pattern billing, printable receipt view
- [x] Help Section
- [x] Exit System - implemented as session logout (see Assumptions)

## Additional Functionality

- **Web service endpoint** - `GET /api/appointments/{number}` returns hand-built JSON (see Assumptions on why not JAX-RS)
- **Sessions and cookies** - recently-viewed appointments (session), remembered last search and last-login "welcome back" message (cookies, `httpOnly`), distinguished session-expired message
- **Email/SMS notification system** - Observer pattern, simulated delivery, full audit trail via the Notification Center
- **Decision-making reports** - Revenue by Treatment Type, Dentist Workload, Appointment Status Breakdown (in addition to the operational Daily Schedule report)
- **Advanced database features:**
    - Stored procedure: `GetDailyRevenue(date, OUT revenue)`
    - Function: `GetPatientAppointmentCount(patientId) RETURNS INT`
    - Trigger: `after_bill_insert` — marks an appointment `COMPLETED` when billed
    - Trigger: `prevent_double_booking` — rejects a booking if the dentist already has one at that exact date/time
- **Sophisticated, framework-free UI** — a hand-written design system (warm dental-clinic palette, no CDN font dependency, responsive sidebar shell)

## Assumptions

Documented here as required by the marking criteria, with reasoning for each.

### Domain / Business Rules
- **Appointment numbering:** system-generated in the format `APT-000001` (based on
  the database auto-increment ID via a two-step transaction), rather than entered
  manually, to guarantee uniqueness and avoid malformed identifiers.
- **Patients can have multiple appointments over time:** the Patient–Appointment
  relationship is one-to-many. Patients are found by contact number and reused
  on subsequent registrations rather than duplicated — this also makes the
  `GetPatientAppointmentCount` function meaningful.
- **A flat consultation fee applies to every appointment**, in addition to a
  treatment-specific cost calculated via the Strategy pattern, since the brief
  did not specify per-dentist pricing.
- **Double-booking is prevented at exact date+time granularity.** There is no
  appointment-duration field in the schema, so the `prevent_double_booking`
  trigger checks for an exact date/time match per dentist rather than a
  overlapping time window — a deliberate scope simplification.
- **The clinic operates as a single location.**

### Staff Roles & Access
- **Staff accounts are pre-provisioned** directly in the database rather than
  through public self-registration, consistent with "authorised staff only."
- **Three roles are modelled** — `RECEPTIONIST`, `DENTIST`, `ADMIN` — reflecting
  the actors identified in the Task A use case diagram. **Known limitation:**
  `AuthenticationFilter` currently checks only whether a user is logged in, not
  which role they hold — role-based authorization is not yet technically
  enforced, only structurally represented. This was accepted as a scope
  simplification (see Task A Critical Reflection).
- **Authentication is enforced centrally** via `AuthenticationFilter` on
  `/appointments/*`, `/billing/*`, `/reports/*`, and `/notifications`, rather
  than modelled as an `<<include>>` relationship on every individual use case —
  a more architecturally accurate representation of how login enforcement
  actually works in a Java EE application.
- **Passwords are stored as salted PBKDF2 hashes**, never plain text (see
  `util.PasswordUtil`), using only JDK-standard `javax.crypto` — no external
  hashing library dependency was needed or added.

### Technical Assumptions
- **"Exit System" is implemented as logging out** (session invalidation,
  returning to the login page), since a browser-based web application has no
  equivalent to a desktop application's process termination.
- **The `/api/appointments/{number}` web service endpoint is intentionally
  left outside `AuthenticationFilter`'s protection**, since it represents a
  machine-to-machine endpoint rather than a staff-facing page. A production
  system would require its own authentication (e.g. an API key) — out of
  scope here.
- **Email/SMS notifications are simulated, not actually delivered.** The
  approved dependency list does not include a mail library (Jakarta Mail) or
  an SMS gateway SDK, and genuine SMS delivery requires a paid third-party
  account. Each channel still constructs a real message and records a real
  outcome (`SIMULATED`/`FAILED`), viewable in the Notification Center. An
  optional, fully dependency-free real-SMTP implementation
  (`EmailSenderSmtp`, using only `javax.net.ssl`) is included but not wired
  in by default, given the network-dependency risk this introduces.
- **The web service endpoint uses hand-built JSON rather than JAX-RS**, since
  a JAX-RS runtime (e.g. Jersey) is not on the approved dependency list.
  `AppointmentJsonMapper` isolates this logic so it remains unit-testable.
- **Single-instance local deployment** (WAMP/XAMPP + one Tomcat instance),
  consistent with the assignment's local-development context.
- **The Java package was renamed** from the IntelliJ-default
  `lk.edu.icbt.dentalclinic` to `dentalclinic` partway through development,
  for simplicity — a straightforward IDE-assisted refactor with no
  functional impact.

## Testing

Two distinct categories, deliberately separated by naming convention:

- **Unit tests** (suffixed `Test`) — no external dependencies, cover business
  logic in isolation (billing strategies, validation, password hashing, JSON
  mapping, notification channels). These run automatically via `mvn test`
  and in CI.
- **Integration tests** (suffixed `IT`) — require a running local MySQL
  database (DAO classes, stored procedure/function/trigger behaviour). These
  are **not** picked up by Maven's default Surefire runner and are **not**
  run in CI, since GitHub Actions has no database available. Run these
  individually from IntelliJ during development.

See `docs/test-plan/` for the full test plan, rationale, and coverage table
(currently 65 test cases across both categories, covering happy, unhappy,
and corner cases, and at least 3 distinct testing strategies: unit,
integration, and boundary-value/equivalence partitioning).

## Git Workflow

- `main` — stable, deployable
- `develop` — integration branch
- `feature/*` — one branch per feature area (e.g. `feature/booking-and-auth`, `feature/testing`)
- CI runs automatically on every push (`.github/workflows/ci.yml`)
  passing runs for the Documentation/GitHub submission
- A tagged release (`v1.0`) marks the final submission version

## Author

K.A.Madhuka Virajith - CL/BSCSD/33/130

CIS6003 Advanced Programming - ICBT Campus