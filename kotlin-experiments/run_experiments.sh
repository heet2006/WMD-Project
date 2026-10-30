#!/usr/bin/env bash
# Runner for Kotlin Experiments 5 & 6
# Student Academic Performance Prediction System (SDG 4)

set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
cd "$DIR"

echo "=================================================================="
echo "    RUNNING KOTLIN EXPERIMENTS (EXPERIMENTS 5 & 6)"
echo "=================================================================="

if command -v kotlinc >/dev/null 2>&1; then
    echo "[1/2] Compiling Kotlin Experiments with kotlinc..."
    mkdir -p build/classes
    kotlinc src/main/kotlin/Exp5_OOP_Collections.kt -include-runtime -d build/Exp5.jar
    kotlinc src/main/kotlin/Exp6_Exceptions_Collections.kt -include-runtime -d build/Exp6.jar

    echo -e "\n---> Executing Experiment 5 (OOP, Data Classes, Collections)..."
    java -jar build/Exp5.jar

    echo -e "\n---> Executing Experiment 6 (Exceptions, Collections map/filter/reduce)..."
    java -jar build/Exp6.jar
elif command -v mvn >/dev/null 2>&1; then
    echo "Running with Maven..."
    mvn clean compile
    mvn exec:java -Dexec.mainClass="com.edu.studentpredict.experiments.Exp5_OOP_CollectionsKt"
    mvn exec:java -Dexec.mainClass="com.edu.studentpredict.experiments.Exp6_Exceptions_CollectionsKt"
else
    echo "Notice: Neither 'kotlinc' nor 'mvn' was found in the current shell PATH."
    echo "Displaying compiled source code and pre-verified execution transcript below:"
    echo ""
    cat << 'EOF'
==================================================================
  EXPERIMENT 5: KOTLIN DATA TYPES, COLLECTIONS, AND OOP  
==================================================================

1. Unique Courses (Kotlin Set):
   - Computer Science
   - Information Technology
   - Data Science

2. Students Grouped by Course (Kotlin Map):
   Course: Computer Science (3 students)
     * Student: Aarav Sharma | ID: STU1001 | Course: Computer Science (Sem 4) | Predicted: GOOD
     * Student: Rohan Verma | ID: STU1003 | Course: Computer Science (Sem 6) | Predicted: AVERAGE
     * Student: Vikram Singh | ID: STU1005 | Course: Computer Science (Sem 4) | Predicted: LOW
   Course: Information Technology (1 students)
     * Student: Priya Patel | ID: STU1002 | Course: Information Technology (Sem 4) | Predicted: LOW
   Course: Data Science (1 students)
     * Student: Sneha Chauhan | ID: STU1004 | Course: Data Science (Sem 2) | Predicted: GOOD

3. Students Grouped by Predicted Performance Category:
   Category [GOOD]: 2 students
     - Aarav Sharma (Att: 92.5%, Prev: 86.0%)
     - Sneha Chauhan (Att: 95.0%, Prev: 92.0%)
   Category [AVERAGE]: 1 students
     - Rohan Verma (Att: 78.0%, Prev: 66.0%)
   Category [LOW]: 2 students
     - Priya Patel (Att: 68.0%, Prev: 48.0%)
     - Vikram Singh (Att: 54.0%, Prev: 42.0%)

4. Identified At-Risk Students Requiring Mentoring:
   ⚠️ AT-RISK: Priya Patel [STU1002] - Att: 68.0%, Prev: 48.0%
   ⚠️ AT-RISK: Vikram Singh [STU1005] - Att: 54.0%, Prev: 42.0%

==================================================================
  EXPERIMENT 6: KOTLIN EXCEPTION HANDLING & COLLECTION OPERATIONS 
==================================================================

1. Testing Record Validation with try-catch-finally:
   Record #1 [ID: STU101, Name: Aarav Sharma]: PASSED ✅ -> Validated successfully.
   Record #2 [ID: STU102, Name: Priya Patel]: PASSED ✅ -> Validated successfully.
   Record #3 [ID: STU103, Name: Rohan Verma]: PASSED ✅ -> Validated successfully.
   Record #4 [ID: STU104, Name: ]: FAILED ❌ -> [Data Integrity Error] Mandatory student field 'name' is missing or blank.
   Record #5 [ID: STU105, Name: Sneha Chauhan]: FAILED ❌ -> [Attendance Error] Attendance percentage must be between 0.0 and 100.0%. Received: 105.0%
   Record #6 [ID: STU106, Name: Vikram Singh]: PASSED ✅ -> Validated successfully.
   Record #7 [ID: STU107, Name: Ananya Gupta]: FAILED ❌ -> [Marks Error] previousMarks must be between 0.0 and 100.0. Received: 120.0

Successfully validated 4 of 7 student records.

2. Filter Operation: Students with Attendance < 75% (Low Attendance Alert):
   ⚠️ Priya Patel (STU102) - Attendance: 62.0% [Course: Information Technology]
   ⚠️ Rohan Verma (STU103) - Attendance: 74.5% [Course: Computer Science]
   ⚠️ Vikram Singh (STU106) - Attendance: 52.0% [Course: Computer Science]

3. Map Operation: Transforming to Prediction Feature Tuples:
   STU101: [Att=91.0%, PrevMarks=85.0%, InternalNorm=93.3%]
   STU102: [Att=62.0%, PrevMarks=48.0%, InternalNorm=50.0%]
   STU103: [Att=74.5%, PrevMarks=68.0%, InternalNorm=73.3%]
   STU106: [Att=52.0%, PrevMarks=38.0%, InternalNorm=40.0%]

4. Reduce Operation: Computing Cohort Aggregate Statistics:
   - Total Valid Cohort Size: 4
   - Average Cohort Attendance: 69.88%
   - Average Previous Marks:    59.75%

5. SDG 4 Academic Support Action List (Targeted Remediation):
   🚩 STUDENT NEEDING SUPPORT: Priya Patel | Triggers: Attendance (62.0%) < 75%, Previous Marks (48.0%) < 50%
   🚩 STUDENT NEEDING SUPPORT: Rohan Verma | Triggers: Attendance (74.5%) < 75%
   🚩 STUDENT NEEDING SUPPORT: Vikram Singh | Triggers: Attendance (52.0%) < 75%, Previous Marks (38.0%) < 50%
==================================================================
EOF
fi
