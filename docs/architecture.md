# System Architecture & Technical Design

## Overview
**Predicting Student Academic Performance Using Decision Tree — Machine Learning for Educational Institutions**  
**SDG Alignment:** SDG 4 – Quality Education (Target 4.1 & 4.4)

The platform is designed as an explainable, data-informed educational early warning system. It integrates modern web technologies, machine learning inference, secure authentication, cloud databases, Kotlin OOP/exception demonstrations, and a companion Flutter mobile application into a unified institution dashboard.

---

## Architecture Diagram

```mermaid
graph TD
    subgraph ClientLayer["1. Client Presentation Tier"]
        ReactApp["React Web Dashboard (Vite + Tailwind CSS + Recharts)"]
        FlutterApp["Flutter Mobile Application (Dart / Material 3)"]
        Exp1Page["Exp 1 Semantic HTML5 / CSS3 Portal"]
    end

    subgraph SecurityTier["2. Identity & Access Management"]
        FirebaseAuth["Firebase Authentication (Sign-up / Sign-in)"]
        JWTFilter["Spring Boot JwtAuthenticationFilter (HMAC-SHA256)"]
        RoleRBAC["Role-Based Access Control (Admin / Teacher / Student)"]
    end

    subgraph ServiceTier["3. Business Logic & Persistence"]
        SpringAPI["Spring Boot 3 REST API Server (Port 8080)"]
        StudentService["Student Service (CRUD & Cohort Analytics)"]
        PredictionService["Prediction Orchestration Service"]
        H2DB[("H2 In-Memory / PostgreSQL Database")]
        FirestoreDB[("Cloud Firestore (Prediction Audit Trail)")]
    end

    subgraph MLTier["4. Machine Learning Inference Engine"]
        FastAPIService["Python Prediction Service (Port 8000)"]
        DecisionTree["CART DecisionTreeClassifier (Gini Impurity)"]
        ExplainEngine["Explainability & Academic Support Rule Engine"]
    end

    ReactApp -->|REST Calls + JWT| SpringAPI
    ReactApp -->|Auth / Session| FirebaseAuth
    FlutterApp -->|REST Calls| SpringAPI
    Exp1Page -->|Native Form Submit| ReactApp

    SpringAPI --> JWTFilter
    JWTFilter --> RoleRBAC
    SpringAPI --> StudentService
    SpringAPI --> PredictionService

    StudentService --> H2DB
    PredictionService -->|HTTP POST /predict| FastAPIService
    PredictionService --> FirestoreDB

    FastAPIService --> DecisionTree
    DecisionTree --> ExplainEngine
```

---

## Data Flow Pipeline

1. **Student Registration / Update**:
   - An educator enters student academic and attendance indicators via the React web form, the HTML5 Exp 1 form, or the Flutter mobile application.
   - The payload is validated using Jakarta Validation constraints (`@Min`, `@Max`, `@NotBlank`, `@Email`).
   - The Spring Boot backend persists the record and invokes the ML service.

2. **Decision Tree Classification**:
   - Features (`attendance_percentage`, `study_hours_per_day`, `previous_marks`, `assignment_completion`, `internal_marks`, `missed_classes`) are fed into the Decision Tree CART pipeline.
   - The model evaluates tree splits based on Gini impurity reduction.
   - The model classifies the student into **Low**, **Average**, or **Good** performance tier.

3. **Explainability & Pedagogical Action**:
   - The tree traversal path is recorded (e.g. `attendance_percentage <= 60%`).
   - Contributing risk factors and actionable recommendations (faculty attendance mentoring, peer-tutoring clinics, study scheduling) are generated.
   - The outcome is saved in the persistent database and logged to Cloud Firestore for audit compliance.

4. **Early Warning Dashboard**:
   - Aggregated metrics update in real-time.
   - Students with attendance `< 75%` or `Low` predicted tier appear on the **At-Risk Students** dashboard for faculty intervention.
