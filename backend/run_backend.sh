#!/usr/bin/env bash
# Runner for Spring Boot REST API
# Student Academic Performance Prediction System (SDG 4)

set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
cd "$DIR"

# Locate Java 17
if [ -d "/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home" ]; then
    export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home"
    export PATH="$JAVA_HOME/bin:$PATH"
fi

echo "=================================================================="
echo "    STARTING SPRING BOOT REST BACKEND (EXPERIMENTS 2, 7, 8)"
echo "=================================================================="
echo "Java Version:"
java -version

if [ -f "./mvnw" ]; then
    echo "Running with Maven Wrapper: ./mvnw spring-boot:run"
    ./mvnw spring-boot:run
elif command -v mvn >/dev/null 2>&1; then
    echo "Running with system Maven: mvn spring-boot:run"
    mvn spring-boot:run
else
    echo "Maven wrapper is ready. If running in offline sandbox, run with external maven or Docker."
fi
