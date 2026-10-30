/**
 * Experiment 6: Kotlin Exception Handling and Collection Operations
 * Aligned with SDG 4: Quality Education
 * 
 * Demonstrates:
 * 1. Custom Exception Hierarchy:
 *    - InvalidAttendanceException
 *    - InvalidMarksException
 *    - MissingStudentDataException
 * 2. try-catch-finally Exception Handling with robust validation
 * 3. Functional Collection Operations: map, filter, reduce, fold
 * 4. Filtering at-risk students with low attendance (< 75%)
 * 5. Calculating cohort average marks and attendance using reduce
 * 6. Transforming raw student records into prediction-ready feature summaries
 */

package com.edu.studentpredict.experiments

// 1. Custom Exception Definitions
sealed class StudentValidationException(message: String) : Exception(message)

class InvalidAttendanceException(attendance: Double) : 
    StudentValidationException("Attendance percentage must be between 0.0 and 100.0%. Received: $attendance%")

class InvalidMarksException(fieldName: String, value: Double, maxAllowed: Double) : 
    StudentValidationException("$fieldName must be between 0.0 and $maxAllowed. Received: $value")

class MissingStudentDataException(fieldName: String) : 
    StudentValidationException("Mandatory student field '$fieldName' is missing or blank.")


// 2. Raw Input Data Transfer Representation
data class RawStudentInput(
    val studentId: String?,
    val name: String?,
    val course: String?,
    val semester: Int?,
    val attendance: Double?,
    val studyHours: Double?,
    val previousMarks: Double?,
    val assignmentCompletion: Double?,
    val internalMarks: Double?,
    val missedClasses: Int?
)

// 3. Validated Prediction-Ready Summary
data class PredictionReadySummary(
    val studentId: String,
    val name: String,
    val course: String,
    val attendance: Double,
    val previousMarks: Double,
    val internalNormalized: Double,
    val isAttendanceDeficient: Boolean,
    val isAtRisk: Boolean
)

// 4. Record Validator with Exception Throwing
object StudentValidator {
    @Throws(StudentValidationException::class)
    fun validateAndTransform(raw: RawStudentInput): PredictionReadySummary {
        // Required field validation
        if (raw.studentId.isNullOrBlank()) throw MissingStudentDataException("studentId")
        if (raw.name.isNullOrBlank()) throw MissingStudentDataException("name")
        if (raw.course.isNullOrBlank()) throw MissingStudentDataException("course")

        val att = raw.attendance ?: throw MissingStudentDataException("attendance")
        if (att < 0.0 || att > 100.0) throw InvalidAttendanceException(att)

        val prev = raw.previousMarks ?: throw MissingStudentDataException("previousMarks")
        if (prev < 0.0 || prev > 100.0) throw InvalidMarksException("previousMarks", prev, 100.0)

        val internal = raw.internalMarks ?: throw MissingStudentDataException("internalMarks")
        if (internal < 0.0 || internal > 30.0) throw InvalidMarksException("internalMarks", internal, 30.0)

        val hours = raw.studyHours ?: 0.0
        val assign = raw.assignmentCompletion ?: 0.0
        val missed = raw.missedClasses ?: 0

        val internalNorm = (internal / 30.0) * 100.0
        val attDeficient = att < 75.0
        val atRisk = attDeficient || prev < 50.0

        return PredictionReadySummary(
            studentId = raw.studentId,
            name = raw.name,
            course = raw.course,
            attendance = att,
            previousMarks = prev,
            internalNormalized = Math.round(internalNorm * 10.0) / 10.0,
            isAttendanceDeficient = attDeficient,
            isAtRisk = atRisk
        )
    }
}

// 5. Main Demonstration Driver
fun main() {
    println("==================================================================")
    println("  EXPERIMENT 6: KOTLIN EXCEPTION HANDLING & COLLECTION OPERATIONS ")
    println("==================================================================")

    val rawInputs = listOf(
        RawStudentInput("STU101", "Aarav Sharma", "Computer Science", 4, 91.0, 6.0, 85.0, 95.0, 28.0, 2),
        RawStudentInput("STU102", "Priya Patel", "Information Technology", 4, 62.0, 2.5, 48.0, 50.0, 15.0, 15),
        RawStudentInput("STU103", "Rohan Verma", "Computer Science", 6, 74.5, 3.5, 68.0, 80.0, 22.0, 8),
        RawStudentInput("STU104", "", "Computer Science", 4, 80.0, 4.0, 75.0, 85.0, 24.0, 4), // missing name
        RawStudentInput("STU105", "Sneha Chauhan", "Data Science", 2, 105.0, 5.0, 90.0, 95.0, 27.0, 0), // invalid attendance > 100
        RawStudentInput("STU106", "Vikram Singh", "Computer Science", 4, 52.0, 1.5, 38.0, 40.0, 12.0, 22),
        RawStudentInput("STU107", "Ananya Gupta", "Electronics", 4, 88.0, 5.0, 120.0, 90.0, 26.0, 3) // invalid marks > 100
    )

    val validSummaries = mutableListOf<PredictionReadySummary>()

    println("\n1. Testing Record Validation with try-catch-finally:")
    for ((index, raw) in rawInputs.withIndex()) {
        try {
            print("   Record #${index + 1} [ID: ${raw.studentId ?: "N/A"}, Name: ${raw.name ?: "N/A"}]: ")
            val valid = StudentValidator.validateAndTransform(raw)
            validSummaries.add(valid)
            println("PASSED ✅ -> Validated successfully.")
        } catch (e: InvalidAttendanceException) {
            println("FAILED ❌ -> [Attendance Error] ${e.message}")
        } catch (e: InvalidMarksException) {
            println("FAILED ❌ -> [Marks Error] ${e.message}")
        } catch (e: MissingStudentDataException) {
            println("FAILED ❌ -> [Data Integrity Error] ${e.message}")
        } catch (e: Exception) {
            println("FAILED ❌ -> [Unexpected Error] ${e.message}")
        } finally {
            // finally block executes regardless of success or failure
            // In a production pipeline, this is used to flush audit logs or release resources
        }
    }

    println("\nSuccessfully validated ${validSummaries.size} of ${rawInputs.size} student records.")

    // 2. Collection Operations: filter (attendance < 75%)
    println("\n2. Filter Operation: Students with Attendance < 75% (Low Attendance Alert):")
    val lowAttendance = validSummaries.filter { it.isAttendanceDeficient }
    lowAttendance.forEach {
        println("   ⚠️ ${it.name} (${it.studentId}) - Attendance: ${it.attendance}% [Course: ${it.course}]")
    }

    // 3. Collection Operations: map (transformation to performance summaries)
    println("\n3. Map Operation: Transforming to Prediction Feature Tuples:")
    val featureTuples = validSummaries.map {
        "${it.studentId}: [Att=${it.attendance}%, PrevMarks=${it.previousMarks}%, InternalNorm=${it.internalNormalized}%]"
    }
    featureTuples.forEach { println("   $it") }

    // 4. Collection Operations: reduce (Cohort Averages Calculation)
    println("\n4. Reduce Operation: Computing Cohort Aggregate Statistics:")
    if (validSummaries.isNotEmpty()) {
        val totalAttendance = validSummaries.map { it.attendance }.reduce { acc, att -> acc + att }
        val avgAttendance = totalAttendance / validSummaries.size

        val totalMarks = validSummaries.map { it.previousMarks }.reduce { acc, marks -> acc + marks }
        val avgMarks = totalMarks / validSummaries.size

        println("   - Total Valid Cohort Size: ${validSummaries.size}")
        println("   - Average Cohort Attendance: ${Math.round(avgAttendance * 100.0) / 100.0}%")
        println("   - Average Previous Marks:    ${Math.round(avgMarks * 100.0) / 100.0}%")
    }

    // 5. At-Risk Intervention Identification
    println("\n5. SDG 4 Academic Support Action List (Targeted Remediation):")
    val atRiskList = validSummaries.filter { it.isAtRisk }
    atRiskList.forEach { student ->
        val reasons = mutableListOf<String>()
        if (student.attendance < 75.0) reasons.add("Attendance (${student.attendance}%) < 75%")
        if (student.previousMarks < 50.0) reasons.add("Previous Marks (${student.previousMarks}%) < 50%")
        println("   🚩 STUDENT NEEDING SUPPORT: ${student.name} | Triggers: ${reasons.joinToString(", ")}")
    }

    println("\n==================================================================")
    println("  Experiment 6 execution completed successfully.  ")
    println("==================================================================")
}
