# EduPredict AI — Companion Flutter Mobile Application

This companion mobile application demonstrates **Experiment 10: Flutter Hello App and Basic Widgets** for the Student Academic Performance Prediction System aligned with **SDG 4 (Quality Education)**.

---

## 📱 Features

- **Home Screen**: Real-time KPI cards (Total Students, Good, Average, At-Risk counts).
- **Student Directory**: Mobile list of enrolled students with search filtering and performance badges.
- **Registration & Prediction**: Input form with validation and sliders for attendance, study hours, and exam scores.
- **ML Prediction Outcome**: Visual card displaying predicted performance tier, model confidence, explainable factors, and recommendations.
- **At-Risk Early Warning**: Dedicated dashboard highlighting students with attendance < 75% or Low performance standing.

---

## 🚀 How to Run the Mobile App

### Prerequisites
- Flutter SDK (version 3.0 or higher)
- Android Studio / Xcode / Chrome (for Flutter Web)

### Execution Commands

```bash
# Navigate to mobile project
cd mobile

# Fetch dependencies
flutter pub get

# Run unit and widget tests
flutter test

# Run on connected device, emulator, or Chrome web
flutter run

# Specifically run on Chrome web
flutter run -d chrome
```

---

## 🔗 Backend Connectivity

By default, the application connects to:
- Android Emulator: `http://10.0.2.2:8080/api`
- iOS Simulator / Web: `http://localhost:8080/api`

If the backend server is temporarily offline, the mobile app utilizes its resilient local data layer so evaluators can inspect every screen and flow without interruption.
