# Ubuntu Health — Member 4 (Administrator & Notification Service)

This repository contains the backend implementation for **Member 4** of the **Ubuntu Health** clinic management system. This module serves as the administrative and communication hub, handling clinic-wide announcements and automated appointment reminders.

---

## 🛠️ Tech Stack & Dependencies

* **Language:** Java 17
* **Framework:** Spring Boot 3
* **Database:** PostgreSQL
* **API Documentation:** Springdoc OpenAPI (Swagger UI)
* **Security:** Spring Security (configured with local developer bypass)
* **Scheduling:** Spring Task Scheduling (`@EnableScheduling`)

---

## 🔌 API Endpoints

Once the application is running, you can access and test all endpoints interactively via the Swagger UI dashboard.

### 📢 Announcement Controller
Allows administrators to manage public system announcements.
* **`GET /api/announcements`** — Fetches all active clinic announcements.
* **`POST /api/announcements`** — Publishes a new announcement (Admin only).
* **`DELETE /api/announcements/{id}`** — Removes an outdated announcement (Admin only).

### ⏰ Reminder Controller
Allows scheduling and tracking of patient appointment notifications.
* **`POST /api/reminders`** — Schedules a new appointment reminder.
* **`GET /api/reminders/patient/{patientId}`** — Retrieves all reminders scheduled for a specific patient.

---

## ⏱️ Automated Background Scheduler

The application includes a background worker (`ReminderScheduler`) that automates dispatch actions:
* **Frequency:** Scans the database automatically every **60 seconds**.
* **Action:** Queries for any pending reminders matching the current time, simulates the notification dispatch (printed to the IntelliJ console log), and updates their statuses to prevent duplicate sending.

---

## ⚙️ How to Setup and Run Locally

### 1. Database Configuration
Ensure your local PostgreSQL service is running and you have a database matching your configurations. Update your `src/main/resources/application.properties` with your credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ubuntu_health_db
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password