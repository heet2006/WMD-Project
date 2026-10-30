# Academic Experiments Mapping & Submission Guide

This document maps all **11 practical curriculum experiments** to their exact implementation locations, architectural roles, and screenshots to capture for submission.

---

## Master Experiment Mapping Table

| Exp # | Experiment Title | Source Files & Directories | Live UI / Endpoint | Verification Command |
| :---: | :--- | :--- | :--- | :--- |
| **1** | **HTML5 & CSS3: Student Information System** | `frontend/src/pages/StudentInfoExp1.jsx`, `frontend/src/styles/exp1.css` | Route: `/experiments/exp1` | Open in browser, inspect semantic elements |
| **2** | **REST API Architecture & CRUD via Postman** | `backend/.../StudentController.java`, `postman/Student_Performance_System.postman_collection.json` | Endpoints: `GET`, `POST`, `PUT`, `DELETE /api/students` | Run Postman Collection against `http://localhost:8080` |
| **3** | **React Components & JSX** | `frontend/src/components/`, `frontend/src/pages/` | Route: `/dashboard`, `/students` | Vite build: `npm run build` |
| **4** | **Hooks, Context API & Form Validation** | `frontend/src/context/StudentContext.jsx`, `frontend/src/pages/StudentForm.jsx` | Route: `/students/add`, `/students/edit/:id` | Test client-side error states and ranges |
| **5** | **Kotlin Data Types, Collections & OOP** | `kotlin-experiments/src/main/kotlin/Exp5_OOP_Collections.kt` | Interactive card in `/experiments` | Run `./kotlin-experiments/run_experiments.sh` |
| **6** | **Kotlin Exception Handling & Collections** | `kotlin-experiments/src/main/kotlin/Exp6_Exceptions_Collections.kt` | Interactive card in `/experiments` | Run `./kotlin-experiments/run_experiments.sh` |
| **7** | **Spring Boot RESTful CRUD APIs** | `backend/src/main/java/.../`, `backend/pom.xml` | Swagger: `/swagger-ui/index.html` | Run `./backend/run_backend.sh` |
| **8** | **JWT / OAuth2 Authentication & Security** | `backend/.../security/`, `frontend/src/context/AuthContext.jsx` | Route: `/login`, Navbar role switcher | Authenticate with admin / teacher / student |
| **9** | **Cloud Deployment Preparation** | `render.yaml`, `deployment/Dockerfile.*`, `docker-compose.yml` | Health probe: `/api/health`, `/health` | `docker-compose up` or Render deployment |
| **10** | **Flutter Companion Mobile App** | `mobile/lib/`, `mobile/pubspec.yaml`, `mobile/test/` | Mobile screens: Home, List, Form, Result | Run `flutter test` or `flutter run` in `mobile/` |
| **11** | **Firebase Auth & Cloud Firestore** | `frontend/src/config/firebase.js`, `firebase/firestore.rules` | Route: `/prediction-history` | Test Firebase login toggle and Firestore sync |

---

## Detailed Experiment Breakdown & Screenshots Guide

### Experiment 1: HTML5 and CSS3 — Student Information System
- **Files**: `frontend/src/pages/StudentInfoExp1.jsx`, `frontend/src/styles/exp1.css`
- **Features Demonstrated**: Semantic `<form>`, `<fieldset>`, `<legend>`, `<label>`, `<input>`, `<select>`, `<button>`, `<table>`, `<thead>`, `<tbody>`, `<tr>`, `<th>`, `<td>`, `<ul>`, `<ol>`, `<li>`.
- **Screenshots to Capture**:
  1. The Student Information form with native HTML5 validation tooltips.
  2. The CSS3 striped table showing enrolled students with hover highlights.
  3. The unordered and ordered list cards demonstrating offered degree programs and the SDG 4 remedial roadmap.

---

### Experiment 2: REST API Architecture & CRUD via Postman
- **Files**: `backend/src/main/java/com/edu/studentpredict/controller/StudentController.java`, `postman/Student_Performance_System.postman_collection.json`
- **Features Demonstrated**: RESTful design, HTTP methods (`GET`, `POST`, `PUT`, `DELETE`), HTTP status codes (200, 201, 204, 400, 404), JSON serialization.
- **Screenshots to Capture**:
  1. Postman `GET /api/students` returning 200 OK with paginated JSON student array.
  2. Postman `POST /api/students` returning 201 Created with the auto-predicted performance tier.
  3. Postman `PUT /api/students/{id}` returning 200 OK.
  4. Postman `DELETE /api/students/{id}` returning 204 No Content.

---

### Experiment 3: React Components & JSX
- **Files**: `frontend/src/components/`, `frontend/src/pages/`
- **Features Demonstrated**: Component modularity, JSX expressions, props passing, reusable components (`StatCard`, `PerformanceBadge`, `DecisionPathViewer`, `AcademicSupportCard`).
- **Screenshots to Capture**:
  1. Main Dashboard showing KPI StatCards and Performance Distribution chart.
  2. Student Directory showing custom PerformanceBadge components.

---

### Experiment 4: Hooks, Context API, Form Handling & Validation
- **Files**: `frontend/src/context/StudentContext.jsx`, `frontend/src/context/AuthContext.jsx`, `frontend/src/pages/StudentForm.jsx`
- **Features Demonstrated**: `useState` for form fields, `useEffect` for syncing data, `useContext` for global student and auth state, real-time numeric range checks (attendance 0-100%, marks 0-100%, study hours 0-24).
- **Screenshots to Capture**:
  1. Student registration form showing active red error banners when an out-of-range number (e.g. attendance 150%) is entered.
  2. Successful form submission toast and instant synchronization with the dashboard.

---

### Experiment 5: Kotlin Data Types, Collections & OOP
- **Files**: `kotlin-experiments/src/main/kotlin/Exp5_OOP_Collections.kt`
- **Features Demonstrated**: Base class `Person`, derived class `Student`, data class `AcademicRecord`, `List`, `Set` of unique courses, `Map` with `groupBy` (by course and by `PerformanceLevel`).
- **Screenshots to Capture**:
  1. Terminal output of `./kotlin-experiments/run_experiments.sh` showing Experiment 5 execution.
  2. Source code view of `Exp5_OOP_Collections.kt`.

---

### Experiment 6: Kotlin Exception Handling & Collection Operations
- **Files**: `kotlin-experiments/src/main/kotlin/Exp6_Exceptions_Collections.kt`
- **Features Demonstrated**: Custom exceptions (`InvalidAttendanceException`, `InvalidMarksException`, `MissingStudentDataException`), `try-catch-finally`, `filter` (< 75% attendance), `map` (summary projection), `reduce` (cohort average calculation).
- **Screenshots to Capture**:
  1. Terminal output of `./kotlin-experiments/run_experiments.sh` showing Experiment 6 execution with passed vs failed records.
  2. Source code view of `Exp6_Exceptions_Collections.kt`.

---

### Experiment 7: Spring Boot RESTful CRUD APIs
- **Files**: `backend/src/main/java/com/edu/studentpredict/`
- **Features Demonstrated**: Spring Boot 3, Spring Data JPA, H2 database, Jakarta validation, Global Exception Handling, Swagger OpenAPI documentation.
- **Screenshots to Capture**:
  1. Swagger UI at `http://localhost:8080/swagger-ui/index.html` displaying all REST endpoints.
  2. H2 Console at `http://localhost:8080/h2-console` displaying the `STUDENTS` table.

---

### Experiment 8: JWT / OAuth2 Authentication & Postman
- **Files**: `backend/src/main/java/com/edu/studentpredict/security/`, `frontend/src/context/AuthContext.jsx`
- **Features Demonstrated**: BCrypt password hashing, HMAC-SHA256 JWT tokens, role-based authorization (`ROLE_ADMIN`, `ROLE_TEACHER`, `ROLE_STUDENT`), token expiration.
- **Screenshots to Capture**:
  1. Login page with quick demo account selection.
  2. Postman login request extracting the Bearer JWT token into the environment.
  3. Navbar role-switcher demonstrating dynamic permission changes.

---

### Experiment 9: Cloud Deployment Preparation
- **Files**: `render.yaml`, `deployment/Dockerfile.*`, `deployment/docker-compose.yml`, `.env.example`
- **Features Demonstrated**: Multi-stage Docker containers, Render cloud blueprint with persistent PostgreSQL, health check endpoints (`/api/health`, `/health`), zero-secret `.env.example`.
- **Screenshots to Capture**:
  1. `render.yaml` configuration file.
  2. Terminal output of `docker-compose up` or cloud deployment dashboard.

---

### Experiment 10: Flutter Companion Mobile App
- **Files**: `mobile/lib/`, `mobile/pubspec.yaml`, `mobile/test/widget_test.dart`
- **Features Demonstrated**: Material 3 mobile UI, HomeScreen KPI cards, StudentListScreen, StudentFormScreen with sliders, PredictionResultScreen with explainability cards, unit and widget tests.
- **Screenshots to Capture**:
  1. Flutter mobile HomeScreen running on simulator / web.
  2. Flutter StudentFormScreen with attendance and exam mark sliders.
  3. Flutter PredictionResultScreen displaying the Decision Tree prediction.
  4. Terminal output of `flutter test`.

---

### Experiment 11: Firebase Authentication & Cloud Firestore
- **Files**: `frontend/src/config/firebase.js`, `firebase/firestore.rules`
- **Features Demonstrated**: Firebase Auth integration, Cloud Firestore synchronization for prediction audit history, role enforcement rules in `firestore.rules`.
- **Screenshots to Capture**:
  1. Prediction History page displaying real-time Firestore prediction audit records.
  2. `firebase/firestore.rules` configuration.
