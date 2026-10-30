"""
Decision Tree Model Training and Evaluation Pipeline
Trains and evaluates Decision Tree on the student performance dataset,
computes metrics, confusion matrix, feature importances, and serializes the model.
"""

import csv
import random
import json
import os
import sys

from decision_tree_engine import (
    DecisionTreeModel,
    compute_classification_metrics,
    FEATURE_NAMES,
    CLASS_NAMES
)

def load_data(csv_path):
    if not os.path.exists(csv_path):
        from generate_data import generate_dataset
        generate_dataset(1200, csv_path)

    X, y = [], []
    with open(csv_path, mode="r", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            features = [
                float(row["attendance_percentage"]),
                float(row["study_hours_per_day"]),
                float(row["previous_marks"]),
                float(row["assignment_completion"]),
                float(row["internal_marks"]),
                float(row["missed_classes"])
            ]
            label = row["performance_category"]
            X.append(features)
            y.append(label)

    return X, y

def train_test_split(X, y, test_size=0.2, seed=42):
    random.seed(seed)
    indices = list(range(len(X)))
    random.shuffle(indices)

    split_idx = int(len(X) * (1.0 - test_size))
    train_indices = indices[:split_idx]
    test_indices = indices[split_idx:]

    X_train = [X[i] for i in train_indices]
    y_train = [y[i] for i in train_indices]
    X_test = [X[i] for i in test_indices]
    y_test = [y[i] for i in test_indices]

    return X_train, X_test, y_train, y_test

def train_pipeline(csv_path=None, model_dir=None):
    base_dir = os.path.dirname(os.path.abspath(__file__))
    if csv_path is None:
        csv_path = os.path.join(base_dir, "student_performance_dataset.csv")
    if model_dir is None:
        model_dir = os.path.join(base_dir, "models")
    os.makedirs(model_dir, exist_ok=True)

    print(f"Loading student academic performance dataset from {csv_path}...")
    X, y = load_data(csv_path)
    print(f"Total dataset records: {len(X)}")

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, seed=42)
    print(f"Training split: {len(X_train)} samples | Testing split: {len(X_test)} samples")

    print("\nTraining Decision Tree Classifier (max_depth=5, min_samples_split=10)...")
    model = DecisionTreeModel(max_depth=5, min_samples_split=10)
    model.fit(X_train, y_train)

    print("\nEvaluating model on held-out test data (240 students)...")
    y_pred = model.predict(X_test)
    metrics = compute_classification_metrics(y_test, y_pred)
    model.metrics_ = metrics

    # Save pure Python model & metrics
    json_model_path = os.path.join(model_dir, "decision_tree_model.json")
    metrics_path = os.path.join(model_dir, "model_metrics.json")
    model.save(json_model_path)

    with open(metrics_path, "w", encoding="utf-8") as f:
        json.dump(metrics, f, indent=2)

    # If scikit-learn and joblib are present, also save joblib format
    try:
        from sklearn.tree import DecisionTreeClassifier
        import joblib
        import numpy as np

        sk_model = DecisionTreeClassifier(max_depth=5, min_samples_split=10, random_state=42)
        sk_model.fit(np.array(X_train), y_train)
        joblib_path = os.path.join(model_dir, "decision_tree_model.joblib")
        joblib.dump(sk_model, joblib_path)
        print(f"Serialized scikit-learn model to {joblib_path}")
    except ImportError:
        print("Note: scikit-learn/joblib not present in local python env; saved full-fidelity DecisionTree JSON model.")

    # Display evaluation summary
    print("=" * 65)
    print("     MODEL EVALUATION RESULTS (Decision Tree Classifier)     ")
    print("=" * 65)
    print(f"Overall Accuracy:       {metrics['accuracy'] * 100:.2f}%")
    print(f"Macro Precision:        {metrics['macro_precision'] * 100:.2f}%")
    print(f"Macro Recall:           {metrics['macro_recall'] * 100:.2f}%")
    print(f"Macro F1-Score:         {metrics['macro_f1'] * 100:.2f}%")
    print(f"Weighted F1-Score:      {metrics['weighted_f1'] * 100:.2f}%")
    print("-" * 65)
    print("PER-CLASS PERFORMANCE:")
    for c in CLASS_NAMES:
        cm = metrics["per_class"][c]
        print(f"  Class '{c:7s}': Precision={cm['precision']*100:5.1f}%, Recall={cm['recall']*100:5.1f}%, F1={cm['f1_score']*100:5.1f}%, Support={cm['support']}")
    print("-" * 65)
    print("CONFUSION MATRIX (Rows=True, Cols=Predicted):")
    print(f"{'':10s} " + " ".join(f"{c:>10s}" for c in CLASS_NAMES))
    for i, row_label in enumerate(CLASS_NAMES):
        row_vals = metrics["confusion_matrix"]["matrix"][i]
        print(f"{row_label:10s} " + " ".join(f"{val:>10d}" for val in row_vals))
    print("-" * 65)
    print("FEATURE IMPORTANCES:")
    for feat, imp in zip(FEATURE_NAMES, model.feature_importances_):
        bar = "█" * int(imp * 30)
        print(f"  {feat:24s} | {imp * 100:5.1f}% | {bar}")
    print("=" * 65)
    print(f"Model successfully saved to {json_model_path}")
    print(f"Metrics saved to {metrics_path}")

    return model, metrics

if __name__ == "__main__":
    train_pipeline()
