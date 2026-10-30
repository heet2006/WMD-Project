# RESTful API Documentation

**Base URL**: `http://localhost:8080/api`  
**Swagger UI**: `http://localhost:8080/swagger-ui/index.html`  
**OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

---

## 1. Authentication Endpoints (Exp 8)

### Register User
- **Method**: `POST`
- **Path**: `/auth/register`
- **Request Body**:
```json
{
  "username": "faculty_user",
  "password": "Faculty@123",
  "fullName": "Dr. Priya Sen",
  "email": "priya.sen@campus.edu",
  "role": "ROLE_TEACHER"
}
```
- **Response**: `201 Created`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "username": "faculty_user",
  "fullName": "Dr. Priya Sen",
  "email": "priya.sen@campus.edu",
  "role": "ROLE_TEACHER",
  "expiresIn": 86400000
}
```

### Login
- **Method**: `POST`
- **Path**: `/auth/login`
- **Request Body**:
```json
{
  "username": "teacher",
  "password": "Teacher@123"
}
```
- **Response**: `200 OK` (returns JWT token and user profile)

---

## 2. Student Management Endpoints (Exp 2 & 7)

### Get All Students (Paginated & Filtered)
- **Method**: `GET`
- **Path**: `/students`
- **Query Parameters**:
  - `course` (optional, string)
  - `performance` (optional: `LOW`, `AVERAGE`, `GOOD`)
  - `search` (optional, string matching name or studentId)
  - `page` (default: 0)
  - `size` (default: 10)
  - `sort` (default: `id,desc`)
- **Response**: `200 OK` (Spring Data Page JSON)

### Get Single Student
- **Method**: `GET`
- **Path**: `/students/{id}`
- **Response**: `200 OK`
```json
{
  "id": 1,
  "studentId": "STU1001",
  "name": "Aarav Sharma",
  "email": "aarav.sharma@campus.edu",
  "course": "Computer Science",
  "semester": 4,
  "attendancePercentage": 94.0,
  "studyHoursPerDay": 6.5,
  "previousMarks": 88.0,
  "assignmentCompletion": 95.0,
  "internalMarks": 28.0,
  "missedClasses": 2,
  "predictedPerformance": "GOOD",
  "predictionConfidence": 0.98,
  "atRisk": false
}
```

### Create Student
- **Method**: `POST`
- **Path**: `/students`
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**:
```json
{
  "studentId": "STU1015",
  "name": "Kavita Reddy",
  "email": "kavita@campus.edu",
  "course": "Information Technology",
  "semester": 4,
  "attendancePercentage": 82.0,
  "studyHoursPerDay": 4.5,
  "previousMarks": 74.0,
  "assignmentCompletion": 85.0,
  "internalMarks": 23.0,
  "missedClasses": 4
}
```
- **Response**: `201 Created` (returns saved student with auto-computed Decision Tree prediction)

### Update Student
- **Method**: `PUT`
- **Path**: `/students/{id}`
- **Headers**: `Authorization: Bearer <token>`
- **Response**: `200 OK` (re-evaluates prediction and updates record)

### Delete Student
- **Method**: `DELETE`
- **Path**: `/students/{id}`
- **Headers**: `Authorization: Bearer <token>` (Requires `ROLE_ADMIN`)
- **Response**: `204 No Content`

### Get At-Risk Students
- **Method**: `GET`
- **Path**: `/students/at-risk`
- **Response**: `200 OK` (list of students with attendance < 75% or Low performance)

### Get Dashboard Statistics
- **Method**: `GET`
- **Path**: `/students/stats`
- **Response**: `200 OK`
```json
{
  "totalStudents": 10,
  "goodPerformanceCount": 4,
  "averagePerformanceCount": 3,
  "lowPerformanceCount": 3,
  "atRiskCount": 3,
  "averageAttendance": 78.4,
  "averagePreviousMarks": 68.2,
  "studentsByCourse": {
    "Computer Science": 4,
    "Information Technology": 2,
    "Data Science": 2,
    "Software Engineering": 2
  }
}
```

---

## 3. Decision Tree Machine Learning Endpoints

### Predict Single Performance
- **Method**: `POST`
- **Path**: `/predict`
- **Request Body**:
```json
{
  "studentId": "STU1020",
  "name": "Sample Candidate",
  "course": "Computer Science",
  "semester": 4,
  "attendancePercentage": 52.0,
  "studyHoursPerDay": 2.0,
  "previousMarks": 40.0,
  "assignmentCompletion": 45.0,
  "internalMarks": 10.0,
  "missedClasses": 18
}
```
- **Response**: `200 OK`
```json
{
  "studentId": "STU1020",
  "name": "Sample Candidate",
  "predictedPerformance": "LOW",
  "confidence": 0.96,
  "probabilities": {
    "Low": 0.96,
    "Average": 0.04,
    "Good": 0.00
  },
  "decisionPath": [
    "attendance_percentage (52.0%) <= 60.0",
    "previous_marks (40.0%) <= 42.0"
  ],
  "contributingFactors": [
    "Low attendance rate (52.0%) below recommended 75% threshold.",
    "Previous exam score (40.0%) indicates foundational concept gaps.",
    "Study time (2.0 hrs/day) is below optimal study threshold."
  ],
  "academicRecommendations": [
    "Mandatory attendance mentoring: Assign a faculty mentor to monitor weekly attendance.",
    "Remedial academic support: Enroll in peer-tutoring sessions for prerequisite subjects.",
    "Structured study planner: Guide student to allocate at least 3-4 hours daily for core coursework."
  ],
  "isAtRisk": true
}
```

### Get Model Metrics
- **Method**: `GET`
- **Path**: `/model/metrics`
- **Response**: `200 OK` (returns 97.92% accuracy, confusion matrix, precision, recall, F1-scores)
