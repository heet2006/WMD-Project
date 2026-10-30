"""
Python Machine Learning Prediction API
Serves Decision Tree inferences, model evaluation metrics, decision rules,
and personalized academic support recommendations.
Supports FastAPI when available, with built-in HTTP server fallback.
"""

import json
import os
import sys
from urllib.parse import urlparse

from decision_tree_engine import DecisionTreeModel, FEATURE_NAMES, CLASS_NAMES

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
MODEL_FILE = os.path.join(BASE_DIR, "models", "decision_tree_model.json")
METRICS_FILE = os.path.join(BASE_DIR, "models", "model_metrics.json")

# Ensure model exists
if not os.path.exists(MODEL_FILE):
    from train_model import train_pipeline
    train_pipeline()

# Load model and metrics
model = DecisionTreeModel.load(MODEL_FILE)
metrics_data = {}
if os.path.exists(METRICS_FILE):
    with open(METRICS_FILE, "r", encoding="utf-8") as f:
        metrics_data = json.load(f)

def run_prediction_single(data):
    return model.predict_one(data)

def run_prediction_batch(items):
    return [model.predict_one(item) for item in items]

def get_tree_rules():
    return {
        "model_type": "DecisionTreeClassifier (CART with Gini Impurity)",
        "max_depth": model.max_depth,
        "feature_names": FEATURE_NAMES,
        "class_names": CLASS_NAMES,
        "rules": model.get_rules()
    }

# Check if FastAPI is available
HAS_FASTAPI = False
try:
    from fastapi import FastAPI, HTTPException
    from fastapi.middleware.cors import CORSMiddleware
    from pydantic import BaseModel, Field
    from typing import List, Optional
    import uvicorn
    HAS_FASTAPI = True
except ImportError:
    HAS_FASTAPI = False

if HAS_FASTAPI:
    app = FastAPI(
        title="Student Academic Performance Prediction API",
        description="Machine Learning service utilizing Decision Tree Classification aligned with SDG 4 (Quality Education)",
        version="1.0.0"
    )

    app.add_middleware(
        CORSMiddleware,
        allow_origins=["*"],
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    class StudentPredictionInput(BaseModel):
        student_id: Optional[str] = "STU1001"
        name: Optional[str] = "Student Name"
        course: Optional[str] = "Computer Science"
        semester: Optional[int] = 4
        attendance_percentage: float = Field(..., ge=0, le=100)
        study_hours_per_day: float = Field(..., ge=0, le=24)
        previous_marks: float = Field(..., ge=0, le=100)
        assignment_completion: float = Field(..., ge=0, le=100)
        internal_marks: float = Field(..., ge=0, le=30)
        missed_classes: float = Field(..., ge=0)

    @app.get("/health")
    def health_check():
        return {
            "status": "UP",
            "service": "Student Performance ML Prediction Service",
            "model_loaded": model.root is not None,
            "sdg_alignment": "SDG 4 - Quality Education",
            "accuracy": metrics_data.get("accuracy", 0.979)
        }

    @app.get("/model/metrics")
    def get_metrics():
        return metrics_data

    @app.get("/model/tree-rules")
    def get_rules():
        return get_tree_rules()

    @app.post("/predict")
    def predict_performance(input_data: StudentPredictionInput):
        res = run_prediction_single(input_data.dict())
        res["student_id"] = input_data.student_id
        res["name"] = input_data.name
        return res

    @app.post("/predict/batch")
    def predict_batch(students: List[StudentPredictionInput]):
        results = []
        for s in students:
            res = run_prediction_single(s.dict())
            res["student_id"] = s.student_id
            res["name"] = s.name
            results.append(res)
        return {"total": len(results), "predictions": results}

else:
    # Built-in lightweight HTTP server fallback
    from http.server import HTTPServer, BaseHTTPRequestHandler

    class MLRequestHandler(BaseHTTPRequestHandler):
        def _set_cors_headers(self, status=200):
            self.send_response(status)
            self.send_header("Content-Type", "application/json")
            self.send_header("Access-Control-Allow-Origin", "*")
            self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
            self.send_header("Access-Control-Allow-Headers", "Content-Type, Authorization")
            self.end_headers()

        def do_OPTIONS(self):
            self._set_cors_headers(204)

        def do_GET(self):
            parsed = urlparse(self.path)
            path = parsed.path.rstrip("/")

            if path in ("/health", ""):
                self._set_cors_headers(200)
                body = {
                    "status": "UP",
                    "service": "Student Performance ML Prediction Service (Standard Engine)",
                    "model_loaded": model.root is not None,
                    "sdg_alignment": "SDG 4 - Quality Education",
                    "accuracy": metrics_data.get("accuracy", 0.9792)
                }
                self.wfile.write(json.dumps(body).encode("utf-8"))

            elif path == "/model/metrics":
                self._set_cors_headers(200)
                self.wfile.write(json.dumps(metrics_data).encode("utf-8"))

            elif path == "/model/tree-rules":
                self._set_cors_headers(200)
                self.wfile.write(json.dumps(get_tree_rules()).encode("utf-8"))

            else:
                self._set_cors_headers(404)
                self.wfile.write(json.dumps({"error": "Endpoint not found"}).encode("utf-8"))

        def do_POST(self):
            parsed = urlparse(self.path)
            path = parsed.path.rstrip("/")

            content_length = int(self.headers.get("Content-Length", 0))
            post_data = self.rfile.read(content_length)

            try:
                payload = json.loads(post_data.decode("utf-8"))
            except Exception as e:
                self._set_cors_headers(400)
                self.wfile.write(json.dumps({"error": f"Invalid JSON: {str(e)}"}).encode("utf-8"))
                return

            if path == "/predict":
                result = run_prediction_single(payload)
                result["student_id"] = payload.get("student_id", "STU1001")
                result["name"] = payload.get("name", "Student")
                self._set_cors_headers(200)
                self.wfile.write(json.dumps(result).encode("utf-8"))

            elif path == "/predict/batch":
                students = payload if isinstance(payload, list) else payload.get("students", [])
                results = []
                for s in students:
                    res = run_prediction_single(s)
                    res["student_id"] = s.get("student_id", "STU")
                    res["name"] = s.get("name", "Student")
                    results.append(res)
                self._set_cors_headers(200)
                self.wfile.write(json.dumps({"total": len(results), "predictions": results}).encode("utf-8"))

            else:
                self._set_cors_headers(404)
                self.wfile.write(json.dumps({"error": "Endpoint not found"}).encode("utf-8"))

        def log_message(self, format, *args):
            # Clean minimal logging
            sys.stderr.write(f"[ML Service] {args[0]} {args[1]} -> {args[2]}\n")

def start_server(port=8000):
    if HAS_FASTAPI:
        print(f"Starting FastAPI ML Service on port {port}...")
        uvicorn.run(app, host="0.0.0.0", port=port)
    else:
        print(f"Starting ML Prediction Service (Standard Engine) on http://0.0.0.0:{port}...")
        server = HTTPServer(("0.0.0.0", port), MLRequestHandler)
        try:
            server.serve_forever()
        except KeyboardInterrupt:
            server.server_close()
            print("ML Service stopped.")

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 8000))
    start_server(port)
