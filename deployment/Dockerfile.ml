# Dockerfile for Python Decision Tree Machine Learning Service
FROM python:3.11-slim
WORKDIR /app

ENV PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    PORT=8000

COPY ml-service/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY ml-service/ .

# Generate data & train model on container build
RUN python generate_data.py && python train_model.py

EXPOSE 8000
CMD ["python", "main.py"]
