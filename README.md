# Employee Onboarding Automation System

## 📌 Project Overview
The Employee Onboarding Automation System is an internal HR platform designed to automate and secure the onboarding process of new employees.  
It centralizes employee data, documents, and onboarding workflows, replacing manual processes such as emails, spreadsheets, and shared folders.

The system improves security, visibility, and efficiency by using JWT-based authentication, role-based access control, and automated notifications.

---

## 🏢 Business Problem
Earlier, employee onboarding was handled manually using:
- Emails
- Excel sheets
- Shared folders

This caused:
- Missing or misplaced documents
- No clear onboarding status
- Frequent HR follow-ups
- Security risks for sensitive employee data

To solve this, we built a centralized and secure Employee Onboarding System.

---

## ✅ Current Impact
- Actively used by the HR team
- Reduced manual follow-ups
- Improved document security
- Clear onboarding status visibility for every employee

---

## 🛠️ Tech Stack

### Backend
- Java 17
- Spring Boot 3.2
- Spring Security 6
- JWT (Access & Refresh Tokens)
- Spring Data JPA (Hibernate ORM)
- MySQL
- Maven

### Frontend
- Angular 16
- Reactive Forms

### Other Tools
- SLF4J + Logback (Logging)
- JavaMailSender (Email notifications)

---

## 🔐 Authentication & Authorization
The system uses JWT-based authentication with Role-Based Access Control (RBAC).

### Roles
- Admin
  - Manages users and roles
- HR
  - Creates onboarding requests
  - Uploads employee documents
  - Tracks onboarding status
- Employee
  - Views onboarding status
  - Uploads required documents

JWT ensures the application is stateless, scalable, and secure.

---

## ✨ Key Features

### 🔑 JWT Authentication
- Secure login using JWT access tokens
- Refresh token mechanism for session continuity
- Custom JWT filter (OncePerRequestFilter) for request validation

### 📂 Secure Document Upload
- Upload sensitive documents:
  - Aadhar
  - PAN
  - Resume
- File type and size validation
- Files stored securely (not publicly accessible)
- Documents linked to employee records in the database

### 📧 Email Notifications
- Automatic emails for onboarding creation and status updates
- Reduces manual HR communication

### 📊 Onboarding Workflow & Status Tracking
- Tracks onboarding steps in real time
- HR can view pending, completed, and overall onboarding progress

---

## 🧱 Architecture
The project follows a layered architecture:

Controller → Service → Repository → Database

### Responsibilities
- Controller: Handles HTTP requests and responses
- Service: Business logic
- Repository: Database operations using JPA
- Config: Security and application configuration
- Util: JWT and helper utilities

---

## 👨‍💻 My Responsibilities

### Backend
- Designed application architecture
- Implemented JWT authentication and RBAC
- Developed secure document upload feature
- Implemented refresh token mechanism
- Designed MySQL schema and optimized queries
- Centralized exception handling
- Logging using SLF4J and Logback

### Frontend
- Developed Angular 16 UI using Reactive Forms
- Integrated secured backend APIs

---

## ⚠️ Challenges & Solutions

### Challenge: Role-Based Access Control
Managing access for Admin, HR, and Employee roles.

Solution:
Defined clear roles and enforced access using Spring Security filters and @PreAuthorize.

### Challenge: Secure Handling of Sensitive Documents
Handling sensitive files securely.

Solution:
Implemented file validation, restricted access, and secure storage linked to employee records.

---

## 🚀 Future Enhancements
- Redis for token caching
- Cloud storage (AWS S3) for documents
- Audit logging
- OAuth2 / OpenID Connect integration

---

## 🧪 How to Run the Project

### Backend
mvn clean spring-boot:run

### Frontend
npm install  
ng serve

---

## 🔒 Configuration
Sensitive configuration values are excluded from GitHub.
Use application.properties.example to configure:
- Database credentials
- JWT secret
- Email credentials

---

## 📌 One-Line Summary
A secure Employee Onboarding System built using Spring Boot and Angular, featuring JWT authentication, role-based access, document management, email notifications, and real-time onboarding tracking.

---

## 📄 License
This project is for internal and educational purposes.
