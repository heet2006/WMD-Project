"""
Decision Tree Classification Engine for Educational Performance Prediction
Implements standard CART algorithm with Gini impurity, comprehensive model evaluation,
interpretable rule extraction, and personalized academic support recommendations.
Fully compatible with scikit-learn output structures.
"""

import math
import json
import os

FEATURE_NAMES = [
    "attendance_percentage",
    "study_hours_per_day",
    "previous_marks",
    "assignment_completion",
    "internal_marks",
    "missed_classes"
]

CLASS_NAMES = ["Low", "Average", "Good"]

class DecisionTreeNode:
    def __init__(self, feature=None, threshold=None, left=None, right=None, *, value=None, samples=0, distribution=None):
        self.feature = feature              # Feature index for splitting
        self.threshold = threshold          # Split threshold
        self.left = left                    # Left child node (feature <= threshold)
        self.right = right                  # Right child node (feature > threshold)
        self.value = value                  # Predicted class if leaf node
        self.samples = samples              # Number of training samples at this node
        self.distribution = distribution or {} # Class frequency distribution

    @property
    def is_leaf(self):
        return self.value is not None

    def to_dict(self):
        if self.is_leaf:
            return {
                "type": "leaf",
                "value": self.value,
                "samples": self.samples,
                "distribution": self.distribution
            }
        return {
            "type": "split",
            "feature_index": self.feature,
            "feature_name": FEATURE_NAMES[self.feature] if self.feature is not None else None,
            "threshold": round(self.threshold, 2) if self.threshold else None,
            "samples": self.samples,
            "distribution": self.distribution,
            "left": self.left.to_dict() if self.left else None,
            "right": self.right.to_dict() if self.right else None
        }

    @classmethod
    def from_dict(cls, data):
        if not data:
            return None
        if data.get("type") == "leaf":
            return cls(
                value=data.get("value"),
                samples=data.get("samples", 0),
                distribution=data.get("distribution", {})
            )
        node = cls(
            feature=data.get("feature_index"),
            threshold=data.get("threshold"),
            samples=data.get("samples", 0),
            distribution=data.get("distribution", {})
        )
        if "left" in data and data["left"]:
            node.left = cls.from_dict(data["left"])
        if "right" in data and data["right"]:
            node.right = cls.from_dict(data["right"])
        return node


class DecisionTreeModel:
    def __init__(self, max_depth=5, min_samples_split=10):
        self.max_depth = max_depth
        self.min_samples_split = min_samples_split
        self.root = None
        self.feature_importances_ = [0.0] * len(FEATURE_NAMES)
        self.metrics_ = {}

    def _gini(self, y):
        if not y:
            return 0.0
        counts = {}
        for label in y:
            counts[label] = counts.get(label, 0) + 1
        n = float(len(y))
        impurity = 1.0 - sum((count / n) ** 2 for count in counts.values())
        return impurity

    def _best_split(self, X, y):
        best_gain = -1.0
        split_idx, split_thresh = None, None
        current_gini = self._gini(y)
        n = len(y)

        for feat_idx in range(len(FEATURE_NAMES)):
            # Sort unique values for threshold candidates
            values = sorted(set(row[feat_idx] for row in X))
            if len(values) <= 1:
                continue

            for i in range(len(values) - 1):
                thresh = (values[i] + values[i + 1]) / 2.0
                left_y = [y[j] for j in range(n) if X[j][feat_idx] <= thresh]
                right_y = [y[j] for j in range(n) if X[j][feat_idx] > thresh]

                if not left_y or not right_y:
                    continue

                p_left = len(left_y) / n
                p_right = len(right_y) / n
                gain = current_gini - (p_left * self._gini(left_y) + p_right * self._gini(right_y))

                if gain > best_gain:
                    best_gain = gain
                    split_idx = feat_idx
                    split_thresh = thresh

        return split_idx, split_thresh, best_gain

    def _build_tree(self, X, y, depth=0):
        samples = len(y)
        counts = {c: y.count(c) for c in CLASS_NAMES if y.count(c) > 0}
        majority_class = max(counts.keys(), key=lambda c: counts[c])

        # Base conditions: pure node, max depth, or insufficient samples
        if depth >= self.max_depth or len(counts) <= 1 or samples < self.min_samples_split:
            return DecisionTreeNode(value=majority_class, samples=samples, distribution=counts)

        feat_idx, thresh, gain = self._best_split(X, y)

        if feat_idx is None or gain <= 0.0:
            return DecisionTreeNode(value=majority_class, samples=samples, distribution=counts)

        # Track feature importance proportional to impurity reduction
        self.feature_importances_[feat_idx] += gain * samples

        # Partition
        left_X, left_y, right_X, right_y = [], [], [], []
        for i in range(samples):
            if X[i][feat_idx] <= thresh:
                left_X.append(X[i])
                left_y.append(y[i])
            else:
                right_X.append(X[i])
                right_y.append(y[i])

        left_child = self._build_tree(left_X, left_y, depth + 1)
        right_child = self._build_tree(right_X, right_y, depth + 1)

        return DecisionTreeNode(
            feature=feat_idx,
            threshold=thresh,
            left=left_child,
            right=right_child,
            samples=samples,
            distribution=counts
        )

    def fit(self, X, y):
        self.feature_importances_ = [0.0] * len(FEATURE_NAMES)
        self.root = self._build_tree(X, y, depth=0)
        # Normalize feature importances
        total_imp = sum(self.feature_importances_)
        if total_imp > 0:
            self.feature_importances_ = [round(imp / total_imp, 4) for imp in self.feature_importances_]
        return self

    def _predict_single(self, node, x):
        if node.is_leaf:
            # Calculate distribution probabilities
            total = sum(node.distribution.values()) or 1
            probas = {c: round(node.distribution.get(c, 0) / total, 3) for c in CLASS_NAMES}
            return node.value, probas, []

        feat_idx = node.feature
        val = x[feat_idx]
        feat_name = FEATURE_NAMES[feat_idx]

        if val <= node.threshold:
            rule_str = f"{feat_name} ({val}) <= {round(node.threshold, 1)}"
            pred, probas, rules = self._predict_single(node.left, x)
        else:
            rule_str = f"{feat_name} ({val}) > {round(node.threshold, 1)}"
            pred, probas, rules = self._predict_single(node.right, x)

        return pred, probas, [rule_str] + rules

    def predict_one(self, x_dict):
        """
        Accepts dictionary of feature names -> values.
        Returns: predicted_class, probabilities_dict, decision_path, recommendations
        """
        x_vector = [
            float(x_dict.get("attendance_percentage", 0)),
            float(x_dict.get("study_hours_per_day", 0)),
            float(x_dict.get("previous_marks", 0)),
            float(x_dict.get("assignment_completion", 0)),
            float(x_dict.get("internal_marks", 0)),
            float(x_dict.get("missed_classes", 0))
        ]

        predicted_class, probas, path = self._predict_single(self.root, x_vector)
        factors, recommendations = self.explain_and_recommend(x_dict, predicted_class)

        return {
            "predicted_performance": predicted_class,
            "confidence": probas.get(predicted_class, 0.85),
            "probabilities": probas,
            "decision_path": path,
            "contributing_factors": factors,
            "academic_recommendations": recommendations,
            "is_at_risk": predicted_class == "Low" or float(x_dict.get("attendance_percentage", 0)) < 75.0
        }

    def predict(self, X):
        return [self._predict_single(self.root, x)[0] for x in X]

    def explain_and_recommend(self, features, predicted_class):
        factors = []
        recommendations = []

        att = float(features.get("attendance_percentage", 0))
        hours = float(features.get("study_hours_per_day", 0))
        prev = float(features.get("previous_marks", 0))
        assign = float(features.get("assignment_completion", 0))
        internal = float(features.get("internal_marks", 0))
        missed = float(features.get("missed_classes", 0))

        # Contributing factor analysis
        if att < 75.0:
            factors.append(f"Low attendance rate ({att}%) below recommended 75% threshold.")
            recommendations.append("Mandatory attendance mentoring: Assign a faculty mentor to monitor weekly attendance.")
        elif att >= 90.0:
            factors.append(f"Exceptional attendance rate ({att}%) provides solid classroom engagement.")

        if prev < 50.0:
            factors.append(f"Previous exam score ({prev}%) indicates foundational concept gaps.")
            recommendations.append("Remedial academic support: Enroll in peer-tutoring sessions for prerequisite subjects.")
        elif prev >= 80.0:
            factors.append(f"Strong historical academic foundation with {prev}% marks.")

        if hours < 3.0:
            factors.append(f"Study time ({hours} hrs/day) is below optimal study threshold.")
            recommendations.append("Structured study planner: Guide student to allocate at least 3-4 hours daily for core coursework.")

        if assign < 60.0:
            factors.append(f"Assignment completion rate is deficient ({assign}%).")
            recommendations.append("Assignment catch-up clinic: Schedule deadline support and practical lab review sessions.")

        if missed > 10:
            factors.append(f"High number of missed lectures ({missed} sessions).")
            recommendations.append("Lecture review plan: Provide recorded lectures and lecture slide study guides.")

        if predicted_class == "Good":
            recommendations.append("Honors / advanced coursework: Encourage participation in competitive programming, research, and technical competitions.")
        elif predicted_class == "Average" and not recommendations:
            recommendations.append("Targeted practice: Focus on high-weightage internal exam topics to transition into Good performance.")

        return factors, recommendations

    def get_rules(self, node=None, prefix=""):
        if node is None:
            node = self.root
        rules = []
        if node.is_leaf:
            rules.append(f"{prefix} => Predict: {node.value} (samples={node.samples}, dist={node.distribution})")
            return rules

        feat_name = FEATURE_NAMES[node.feature]
        thresh = round(node.threshold, 2)

        left_prefix = f"{prefix} IF {feat_name} <= {thresh}" if prefix else f"IF {feat_name} <= {thresh}"
        right_prefix = f"{prefix} IF {feat_name} > {thresh}" if prefix else f"IF {feat_name} > {thresh}"

        rules.extend(self.get_rules(node.left, left_prefix))
        rules.extend(self.get_rules(node.right, right_prefix))
        return rules

    def to_dict(self):
        return {
            "max_depth": self.max_depth,
            "min_samples_split": self.min_samples_split,
            "feature_names": FEATURE_NAMES,
            "class_names": CLASS_NAMES,
            "feature_importances": dict(zip(FEATURE_NAMES, self.feature_importances_)),
            "tree": self.root.to_dict() if self.root else None,
            "metrics": self.metrics_
        }

    def save(self, filepath):
        os.makedirs(os.path.dirname(os.path.abspath(filepath)), exist_ok=True)
        with open(filepath, "w", encoding="utf-8") as f:
            json.dump(self.to_dict(), f, indent=2)

    @classmethod
    def load(cls, filepath):
        with open(filepath, "r", encoding="utf-8") as f:
            data = json.load(f)
        model = cls(
            max_depth=data.get("max_depth", 5),
            min_samples_split=data.get("min_samples_split", 10)
        )
        model.root = DecisionTreeNode.from_dict(data.get("tree"))
        feat_imp_dict = data.get("feature_importances", {})
        model.feature_importances_ = [feat_imp_dict.get(fn, 0.0) for fn in FEATURE_NAMES]
        model.metrics_ = data.get("metrics", {})
        return model


def compute_classification_metrics(y_true, y_pred):
    """
    Computes rigorous classification metrics:
    - Confusion Matrix (3x3)
    - Accuracy
    - Precision, Recall, F1 (per-class, macro, weighted)
    """
    labels = CLASS_NAMES
    n = len(y_true)
    
    # 3x3 Confusion matrix: rows = true, cols = predicted
    matrix = {true_c: {pred_c: 0 for pred_c in labels} for true_c in labels}
    for t, p in zip(y_true, y_pred):
        if t in matrix and p in matrix[t]:
            matrix[t][p] += 1

    correct = sum(matrix[c][c] for c in labels)
    accuracy = round(correct / n, 4) if n > 0 else 0.0

    per_class = {}
    macro_p, macro_r, macro_f1 = 0.0, 0.0, 0.0
    weighted_p, weighted_r, weighted_f1 = 0.0, 0.0, 0.0

    for c in labels:
        tp = matrix[c][c]
        fp = sum(matrix[other][c] for other in labels if other != c)
        fn = sum(matrix[c][other] for other in labels if other != c)
        support = sum(matrix[c].values())

        p = round(tp / (tp + fp), 4) if (tp + fp) > 0 else 0.0
        r = round(tp / (tp + fn), 4) if (tp + fn) > 0 else 0.0
        f1 = round(2 * p * r / (p + r), 4) if (p + r) > 0 else 0.0

        per_class[c] = {
            "precision": p,
            "recall": r,
            "f1_score": f1,
            "support": support
        }

        macro_p += p
        macro_r += r
        macro_f1 += f1

        weighted_p += p * support
        weighted_r += r * support
        weighted_f1 += f1 * support

    num_classes = len(labels)
    metrics = {
        "accuracy": accuracy,
        "macro_precision": round(macro_p / num_classes, 4),
        "macro_recall": round(macro_r / num_classes, 4),
        "macro_f1": round(macro_f1 / num_classes, 4),
        "weighted_precision": round(weighted_p / n, 4) if n > 0 else 0.0,
        "weighted_recall": round(weighted_r / n, 4) if n > 0 else 0.0,
        "weighted_f1": round(weighted_f1 / n, 4) if n > 0 else 0.0,
        "per_class": per_class,
        "confusion_matrix": {
            "labels": labels,
            "matrix": [[matrix[r][c] for c in labels] for r in labels]
        },
        "total_test_samples": n
    }
    return metrics
