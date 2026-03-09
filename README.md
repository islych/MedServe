# 🏥 MedServe — Medical Appointment Management System


**A full-stack web application for managing medical appointments between patients, doctors, and assistants — built with Spring Boot, Spring Security (JWT), JPA/Hibernate, and Thymeleaf.**


## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Security Design](#-security-design)
- [Data Model](#-data-model)
- [Getting Started](#-getting-started)
- [Project Structure](#-project-structure)
- [Use Case Diagram](#-use-case-diagram)
- [Screenshots](#-screenshots)

---

## 🌟 Overview

**MedServe** is a role-based medical appointment platform supporting three types of users: **Patients**, **Doctors**, and **Medical Assistants**. The system handles the full lifecycle of an appointment — from booking to confirmation, rejection, or completion — with real-time status tracking and notification support.

This project demonstrates:
- Secure multi-role authentication with **stateless JWT sessions**
- Clean **MVC architecture** with a layered service/repository pattern
- **Domain-driven design** with proper entity relationships
- File upload management for doctor profile photos
- Appointment filtering by status, urgency, date range, and role

---

## ✅ Features

### 👤 Authentication & Authorization
- Role-based access control: `PATIENT`, `DOCTOR`, `ASSISTANCE`
- Stateless JWT authentication stored in **HttpOnly cookies**
- Token revocation on logout (persisted via `RevokedToken` entity)
- Secure password hashing with **BCrypt**

### 🧑‍⚕️ Patient
- Register and manage personal profile
- Browse available doctors by specialty
- Book, view, and cancel appointments
- Receive notifications on appointment status changes

### 🩺 Doctor
- Manage profile with photo upload
- View and filter all appointments (pending, confirmed, completed)
- Calendar view of confirmed appointments via FullCalendar integration
- Register and manage a personal medical assistant account

### 🗂️ Medical Assistant
- View and manage appointments on behalf of the doctor
- Confirm or reject pending appointments with a rejection reason
- Automatic patient notification on status change

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| **Backend Framework** | Spring Boot 3.4.4 |
| **Language** | Java 21 |
| **Security** | Spring Security + JWT (jjwt 0.11.5) |
| **Persistence** | Spring Data JPA + Hibernate |
| **Database** | MySQL 8 |
| **Template Engine** | Thymeleaf + Thymeleaf Spring Security Extras |
| **Build Tool** | Maven |
| **Utilities** | Lombok |
| **File Storage** | Local file system (`/uploads`) |
| **Validation** | Jakarta Bean Validation |

---

## 🏛 Architecture

The application follows a classic **layered MVC architecture**:

```
┌────────────────────────────────────────────┐
│              Thymeleaf Views               │
├────────────────────────────────────────────┤
│         Controllers (MVC Layer)            │
│  AuthController / DoctorController /       │
│  PatientController / AssistanceController  │
├────────────────────────────────────────────┤
│           Service Layer                    │
│  Business logic, validation, orchestration │
├────────────────────────────────────────────┤
│         Repository Layer (JPA)             │
│  Custom JPQL queries + Spring Data methods │
├────────────────────────────────────────────┤
│               MySQL Database               │
└────────────────────────────────────────────┘
```

---

## 🔐 Security Design

Security is implemented end-to-end using **Spring Security with a custom JWT filter**:

1. User logs in via `POST /auth/login` → receives a signed JWT
2. JWT is stored in an **HttpOnly, Secure cookie** (not localStorage)
3. `JwtAuthenticationFilter` (extends `OncePerRequestFilter`) intercepts every request, extracts and validates the token from the cookie or `Authorization` header
4. On logout, the token is saved to the `revoked_token` table — preventing reuse even before expiry
5. Method-level security is enabled via `@EnableMethodSecurity`

```
Request → JwtAuthenticationFilter → SecurityContextHolder → Controller
                                          ↓
                               Token validated against
                               UserDetailsService + RevokedTokenRepository
```

---

## 🗄 Data Model

```
User (base entity)
 ├── Patient       → has many Rendezvous, Notifications
 ├── Doctor        → has many Rendezvous, has one Assistance
 └── Assistance    → belongs to one Doctor

Rendezvous (Appointment)
 ├── belongs to Patient
 ├── belongs to Doctor
 ├── Statut: En_Attend | Confirme | Annule | Termine
 └── Urgency: FAIBLE | MOYENNE | HAUTE | URGENTE

Notification
 └── belongs to Patient

RevokedToken
 └── stores invalidated JWTs for stateless logout
```

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8.0+

### 1. Clone the repository

```bash
git clone https://github.com/your-username/medserve.git
cd medserve
```

### 2. Configure the database

Create a MySQL database:

```sql
CREATE DATABASE medserve;
```

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/medserve
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run the application

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8081**

### 4. Access the app

| URL | Description |
|---|---|
| `/auth/login` | Login page |
| `/auth/register/patient` | Patient registration |
| `/auth/register/doctor` | Doctor registration |
| `/patients/home` | Patient dashboard |
| `/doctor/home` | Doctor dashboard |
| `/assistance/home` | Assistant dashboard |

---

## 📁 Project Structure

```
src/main/java/com/medical/appointment/
├── Auth/
│   ├── AuthenticationController.java   # Registration & login endpoints
│   └── AuthenticationService.java      # Auth business logic
├── Conf/
│   ├── SecurityConfig.java             # Security filter chain & route rules
│   ├── JwtAuthentificationFilter.java  # JWT extraction & validation filter
│   ├── JwtService.java                 # Token generation, validation, revocation
│   ├── ApplicationConfig.java          # UserDetailsService, PasswordEncoder beans
│   └── WebConfig.java                  # Static resource handlers (uploads)
├── Controller/
│   ├── DoctorController.java           # Doctor dashboard, profile, appointments
│   ├── PatientController.java          # Patient dashboard, profile, doctor search
│   ├── AssistanceController.java       # Assistant dashboard, appointment management
│   └── RendezvousController.java       # Appointment CRUD and status updates
├── Model/
│   ├── User.java                       # Base UserDetails entity
│   ├── Doctor.java / Patient.java / Assistance.java
│   ├── Rendezvous.java                 # Appointment entity
│   ├── Notification.java
│   ├── RevokedToken.java
│   └── enums/                          # Role, Statut, Urgency, Specialty
├── Repository/                         # Spring Data JPA repositories + JPQL queries
├── Service/                            # Business logic layer
└── DTO/                                # Request/Response data transfer objects
```

---

## 📊 Use Case Diagram

```
┌─────────────────────────────────────────────────────────┐
│              Medical Appointment System                 │
│                                                         │
│  Patient ──► Book Appointment                           │
│          ──► Cancel Appointment                         │
│          ──► View Appointments                          │
│          ──► Browse Doctors                             │
│                                                         │
│  Doctor  ──► View Agenda / Calendar                     │
│          ──► Manage Profile                             │
│          ──► Manage Assistant Account                   │
│                                                         │
│  Assistant ──► Confirm / Reject Appointments            │
│            ──► Notify Patient                           │
│            ──► View Doctor's Planning                   │
│                                                         │
│  All Users ──► Register / Login / Manage Profile        │
└─────────────────────────────────────────────────────────┘
```

A full PlantUML use-case diagram is available at:
`src/main/resources/static/diagrams/use-case.puml`

---

## 💡 Key Implementation Highlights

- **Stateless JWT with cookie-based transport** — no session state on the server, tokens are HttpOnly to prevent XSS
- **Token revocation table** — solves the JWT logout problem without switching to stateful sessions
- **Polymorphic user hierarchy** — `Patient`, `Doctor`, and `Assistance` all extend a single `User` JPA entity using single-table inheritance
- **Role-scoped appointment updates** — assistants can only update `statut` and `rejectionReason`; full edits are restricted to authorized roles
- **Custom JPQL fetch queries** — eager-loading patient data within appointment queries to avoid N+1 problems
- **Secure file uploads** — UUID-prefixed filenames, directory traversal prevention, old file cleanup on profile photo update

---

## 👨‍💻 Author

Built with ❤️ as a practical demonstration of Spring Boot, Spring Security, and JPA/Hibernate best practices.

Feel free to open issues or submit pull requests!

---

<div align="center">
<sub>MedServe — Connecting Patients and Healthcare Professionals</sub>
</div>
