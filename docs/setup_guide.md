# Setup, Execution & Testing Guide

This guide provides step-by-step instructions to execute and test all modules of the **Student Academic Performance Prediction System**.

---

## 💻 System Prerequisites

- **Java Development Kit**: JDK 17 (Eclipse Temurin or OpenJDK 17)
- **Node.js**: v18+ (tested on Node v22.13.0) and npm
- **Python**: 3.10+ (tested on Python 3.13)
- **Optional**: Flutter 3.0+ (for mobile app), Docker (for containerized orchestration)

---

## 1. Machine Learning Service (Port 8000)

### Setup & Model Training
```bash
# Navigate to ML service directory
cd ml-service

# (Optional) Create and activate virtual environment
python3 -m venv .venv
source .venv/bin/activate  # On Windows: .venv\Scripts\activate

# Install dependencies (if online)
pip install -r requirements.txt

# Generate realistic educational dataset (1,200 records)
python3 generate_data.py

# Train & evaluate Decision Tree Classifier
python3 train_model.py
```

### Start ML Server
```bash
python3 main.py
```
- Health Check: `http://localhost:8000/health`
- Model Evaluation Metrics: `http://localhost:8000/model/metrics`

---

## 2. Spring Boot 3 REST Backend (Port 8080)

### Start Backend
```bash
cd backend

# Make runner executable and launch
chmod +x run_backend.sh
./run_backend.sh

# Or with Maven:
mvn spring-boot:run
```

- REST API Base: `http://localhost:8080/api`
- Swagger OpenAPI Documentation: `http://localhost:8080/swagger-ui/index.html`
- H2 Database Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:studentdb`, User: `sa`, Password: *empty*)

---

## 3. React Frontend Web Dashboard (Port 5173)

### Start Web Client
```bash
cd frontend

# Install dependencies
npm install

# Launch Vite development server
npm run dev

# Or build production bundle
npm run build
```

- Open in browser: `http://localhost:5173`

---

## 4. Kotlin Experiments (Experiments 5 & 6)

```bash
cd kotlin-experiments

# Run pre-configured test suite
chmod +x run_experiments.sh
./run_experiments.sh
```

---

## 5. Flutter Companion Mobile App

```bash
cd mobile

# Fetch packages
flutter pub get

# Run unit tests
flutter test

# Launch mobile application (simulator or web)
flutter run -d chrome
```

---

## 6. Docker Full-Stack Single Command

To spin up the entire ecosystem simultaneously:
```bash
cd deployment
docker-compose up --build
```
- Frontend: `http://localhost:3000`
- Backend: `http://localhost:8080`
- ML Service: `http://localhost:8000`

---

## 🔑 Default Credentials for Testing

| Username | Password | Role | Permissions |
| :--- | :--- | :--- | :--- |
| `teacher` | `Teacher@123` | `ROLE_TEACHER` | Full student creation, editing, evaluation |
| `admin` | `Admin@123` | `ROLE_ADMIN` | Full access including student deletion |
| `student` | `Student@123` | `ROLE_STUDENT` | Read-only access to academic standing |
