# Sunrise Dental Clinic — Appointment and Patient Management System

CIS6003 Advanced Programming (WRIT1) — ICBT / Cardiff Met, 2024/25 Semester 1

A Java EE web application (built as a web service, **no third-party frameworks**)
that replaces Sunrise Dental Clinic's paper-based appointment and patient
record system.

## Tech Stack

| Layer          | Technology |
|----------------|------------|
| Language       | Java 17 |
| Web layer      | Jakarta EE (Servlets, JSP, JSTL) — **no Spring/Quarkus/other frameworks** |
| Database       | MySQL via XAMPP/WAMPP |
| Build tool     | Maven |
| Testing        | JUnit 5 |
| IDE            | IntelliJ IDEA |
| CI             | GitHub Actions (`.github/workflows/ci.yml`) |

Allowed backend dependencies only: Java EE (Jakarta Servlet/JSP/JSTL), MySQL
JDBC driver, JUnit, and (optionally, if used) a reporting or JSON
serialization library. No other third-party libraries are used, per the
assignment instructions.

## Architecture

3-tier architecture:

```
Presentation tier   src/main/java/.../servlet, src/main/webapp (JSP/CSS/JS)
Business logic tier  src/main/java/.../service (+ strategy subpackage)
Data access tier     src/main/java/.../dao (+ impl subpackage)
```

### Design / architectural patterns implemented (skeleton stage)

| Pattern | Where | Why |
|---|---|---|
| **DAO** | `dao` / `dao.impl` | Decouples business logic from persistence details (Dependency Inversion) |
| **Singleton** | `util.DBConnectionManager` | One shared point of DB configuration |
| **Strategy** | `service.strategy.*`, used by `service.BillingService` | Treatment-dependent billing rules, extensible without modifying existing code (Open/Closed) |
| **Layered / 3-tier architecture** | overall package structure | Separation of concerns across presentation, business logic, and data access |

Each pattern's class Javadoc includes the reasoning to justify in the report —
expand on it with your own analysis and critical evaluation.

## Project Structure

```
sunrise-dental-clinic-system/
├── pom.xml
├── database/
│   └── schema.sql              # run this against MySQL first
├── docs/
│   ├── uml/                    # Task A: use case / class / sequence diagrams
│   ├── test-plan/              # Task C: test plan, rationale, test data
│   └── screenshots/            # evidence for Task C/D (must be clear/legible)
├── src/main/java/lk/edu/icbt/dentalclinic/
│   ├── model/                  # domain entities
│   ├── dao/  dao/impl/         # DAO pattern
│   ├── service/  service/strategy/  # business logic + Strategy pattern
│   ├── servlet/                 # presentation layer controllers
│   ├── filter/                  # AuthenticationFilter (login enforcement)
│   ├── util/                    # DBConnectionManager (Singleton), etc.
│   └── exception/
├── src/main/resources/
│   └── db.properties.example    # copy to db.properties, fill in credentials
├── src/main/webapp/             # JSP views, CSS, JS
└── src/test/java/...            # JUnit test classes (Task C)
```

## Setup Instructions

1. **Start MySQL** via XAMPP or WAMPP.
2. **Create the schema:**
   ```bash
   mysql -u root -p < database/schema.sql
   ```
3. **Configure credentials:**
   ```bash
   cp src/main/resources/db.properties.example src/main/resources/db.properties
   # edit db.properties with your local MySQL username/password
   ```
4. **Open in IntelliJ IDEA** as a Maven project.
5. **Configure a local Tomcat run configuration** (Run → Edit Configurations →
   add Tomcat Server → deploy the WAR artifact).
6. **Run tests:**
   ```bash
   mvn test
   ```

> **Namespace check:** this skeleton uses `jakarta.servlet.*` (current
> Jakarta EE 9+/Tomcat 10+ namespace). If your IntelliJ-generated project
> defaults to the older `javax.servlet.*` namespace instead, update the
> `pom.xml` dependency and imports accordingly — it's a straightforward
> find-and-replace.

## Functional Requirements (from the assignment brief)

- [x] Project skeleton / architecture in place
- [ ] User Authentication (Login) — filter + servlet skeleton in place, DAO wiring pending
- [ ] Register New Appointment
- [ ] Display Appointment Details (search by appointment number)
- [ ] Calculate and Print Bill
- [ ] Help Section
- [ ] Exit System

## Assumptions

Document all design assumptions here as you make them (this is explicitly
rewarded in the marking criteria — be detailed and justify each one).

Example starting assumptions (edit/replace with your own reasoning):
- Each appointment is assigned a system-generated, human-readable
  appointment number (e.g. `APT-000123`) at registration time.
- Staff accounts are pre-provisioned by an administrator; there is no
  public self-registration endpoint (consistent with "only authorized
  staff can use the system").

## Testing

See `docs/test-plan/` for the full test plan, rationale, and derived test
data (Task C — target 30–40 test cases across happy/unhappy/corner cases,
covering at least 3 distinct test strategies, staying within SOLID
principles).

## Git Workflow

- `main` — stable, deployable
- `develop` — integration branch
- `feature/*` — one branch per feature area
- CI runs automatically on every push (see `.github/workflows/ci.yml`) —
  screenshot passing runs for the Documentation/GitHub submission.

## Author

[Your name] — [Your student ID]
CIS6003 Advanced Programming — ICBT Campus
