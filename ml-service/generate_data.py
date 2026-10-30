"""
Realistic Educational Dataset Generator for Student Academic Performance Prediction
Aligned with SDG 4: Quality Education
Generates statistically grounded student academic data with documented label derivation.
"""

import csv
import random
import os

SEED = 42
random.seed(SEED)

COURSES = [
    "Computer Science",
    "Information Technology",
    "Data Science",
    "Software Engineering",
    "Electronics Engineering"
]

FIRST_NAMES = [
    "Aarav", "Ananya", "Rohan", "Priya", "Aditya", "Sneha", "Vikram", "Neha",
    "Arjun", "Pooja", "Rahul", "Kavya", "Siddharth", "Isha", "Manish", "Divya",
    "Kunal", "Riya", "Gaurav", "Tanvi", "Amit", "Shruti", "Karan", "Meera",
    "Harsh", "Deepika", "Nikhil", "Simran", "Ayush", "Swati", "Mohit", "Anjali"
]

LAST_NAMES = [
    "Sharma", "Patel", "Verma", "Chauhan", "Gupta", "Singh", "Shah", "Mehta",
    "Kumar", "Joshi", "Deshmukh", "Iyer", "Nair", "Reddy", "Rao", "Mishra",
    "Malhotra", "Kapoor", "Bhatia", "Aggarwal", "Pandey", "Chopra", "Saxena"
]

def derive_performance_label(prev_marks, attendance, internal_marks, assignment_pct, study_hours, missed_classes):
    """
    Computes performance index and assigns target class.
    
    Composite Index:
      - 35% Previous Examination Marks (0-100)
      - 25% Attendance Percentage (0-100)
      - 20% Normalized Internal Marks (0-30 scaled to 0-100)
      - 15% Assignment Completion Percentage (0-100)
      - 5% Normalized Study Hours (0-10 scaled to 0-100)
      - Penalty for missed classes: 0.4 points per missed class
    
    Classification Thresholds:
      - Low: composite_score < 55 OR attendance < 60 OR prev_marks < 45
      - Average: 55 <= composite_score < 75
      - Good: composite_score >= 75
    """
    normalized_internal = (internal_marks / 30.0) * 100.0
    normalized_study = min(100.0, (study_hours / 10.0) * 100.0)
    
    composite = (
        0.35 * prev_marks +
        0.25 * attendance +
        0.20 * normalized_internal +
        0.15 * assignment_pct +
        0.05 * normalized_study -
        (0.40 * missed_classes)
    )
    
    # 5% realistic noise to simulate real-world classroom anomalies
    noise = random.gauss(0, 2.5)
    score = composite + noise
    
    if score < 55.0 or attendance < 60.0 or prev_marks < 42.0:
        return "Low", round(score, 2)
    elif score < 75.0:
        return "Average", round(score, 2)
    else:
        return "Good", round(score, 2)

def generate_student(idx):
    student_id = f"STU{1000 + idx}"
    first_name = random.choice(FIRST_NAMES)
    last_name = random.choice(LAST_NAMES)
    name = f"{first_name} {last_name}"
    course = random.choice(COURSES)
    semester = random.randint(1, 8)
    
    # Generate latent student capability archetype (low, mid, high achiever)
    archetype = random.choices(["struggling", "moderate", "excelling"], weights=[0.25, 0.45, 0.30])[0]
    
    if archetype == "struggling":
        attendance = round(random.uniform(42.0, 74.0), 1)
        study_hours = round(random.uniform(1.0, 3.5), 1)
        prev_marks = round(random.uniform(32.0, 58.0), 1)
        assignment_pct = round(random.uniform(35.0, 70.0), 1)
        internal_marks = round(random.uniform(8.0, 18.0), 1)
        missed_classes = random.randint(10, 28)
    elif archetype == "moderate":
        attendance = round(random.uniform(70.0, 88.0), 1)
        study_hours = round(random.uniform(2.5, 6.0), 1)
        prev_marks = round(random.uniform(55.0, 78.0), 1)
        assignment_pct = round(random.uniform(65.0, 90.0), 1)
        internal_marks = round(random.uniform(16.0, 25.0), 1)
        missed_classes = random.randint(3, 12)
    else: # excelling
        attendance = round(random.uniform(85.0, 99.0), 1)
        study_hours = round(random.uniform(5.0, 9.5), 1)
        prev_marks = round(random.uniform(75.0, 98.0), 1)
        assignment_pct = round(random.uniform(85.0, 100.0), 1)
        internal_marks = round(random.uniform(23.0, 30.0), 1)
        missed_classes = random.randint(0, 5)
        
    label, comp_score = derive_performance_label(
        prev_marks, attendance, internal_marks, assignment_pct, study_hours, missed_classes
    )
    
    return {
        "student_id": student_id,
        "name": name,
        "course": course,
        "semester": semester,
        "attendance_percentage": attendance,
        "study_hours_per_day": study_hours,
        "previous_marks": prev_marks,
        "assignment_completion": assignment_pct,
        "internal_marks": internal_marks,
        "missed_classes": missed_classes,
        "composite_score": comp_score,
        "performance_category": label
    }

def generate_dataset(num_records=1200, output_path="student_performance_dataset.csv"):
    os.makedirs(os.path.dirname(os.path.abspath(output_path)), exist_ok=True)
    records = [generate_student(i) for i in range(1, num_records + 1)]
    
    fieldnames = [
        "student_id", "name", "course", "semester",
        "attendance_percentage", "study_hours_per_day", "previous_marks",
        "assignment_completion", "internal_marks", "missed_classes",
        "composite_score", "performance_category"
    ]
    
    with open(output_path, mode="w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(records)
        
    counts = {"Low": 0, "Average": 0, "Good": 0}
    for r in records:
        counts[r["performance_category"]] += 1
        
    print(f"Dataset generated at {output_path} with {num_records} records.")
    print(f"Distribution: Low={counts['Low']}, Average={counts['Average']}, Good={counts['Good']}")
    return output_path

if __name__ == "__main__":
    out = os.path.join(os.path.dirname(__file__), "student_performance_dataset.csv")
    generate_dataset(1200, out)
