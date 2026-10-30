/**
 * Experiment 5: Kotlin Data Types, Collections, and Object-Oriented Programming (OOP)
 * Aligned with SDG 4: Quality Education
 * 
 * Demonstrates:
 * 1. Data classes (Student, AcademicRecord)
 * 2. Inheritance: Base class Person -> Derived class Student
 * 3. Immutable & Mutable Collections (List, Set, Map)
 * 4. Advanced collection transformations: groupBy, filter, sortedBy
 */

package com.edu.studentpredict.experiments

// 1. Base Class Demonstrating OOP Inheritance
open class Person(
    val id: String,
    val name: String,
    val email: String
) {
    open fun displayRole(): String = "Person: $name (ID: $id)"
}

// 2. Data Class for Academic Records
data class AcademicRecord(
    val attendancePercentage: Double,
    val studyHoursPerDay: Double,
    val previousMarks: Double,
    val assignmentCompletion: Double,
    val internalMarks: Double,
    val missedClasses: Int
) {
    fun isAttendanceDeficient(): Boolean = attendancePercentage < 75.0
    fun isAtRisk(): Boolean = attendancePercentage < 75.0 || previousMarks < 50.0
}

enum class PerformanceLevel {
    LOW, AVERAGE, GOOD
}

// 3. Derived Class inheriting from Person
class Student(
    id: String,
    name: String,
    email: String,
    val studentId: String,
    val course: String,
    val semester: Int,
    val academicRecord: AcademicRecord
) : Person(id, name, email) {

    // Derived property evaluating performance category using domain rules
    val predictedPerformance: PerformanceLevel
        get() {
            val score = 0.35 * academicRecord.previousMarks +
                        0.25 * academicRecord.attendancePercentage +
                        0.20 * (academicRecord.internalMarks / 30.0 * 100.0) +
                        0.15 * academicRecord.assignmentCompletion +
                        0.05 * (academicRecord.studyHoursPerDay / 10.0 * 100.0) -
                        (0.40 * academicRecord.missedClasses)

            return when {
                score < 55.0 || academicRecord.attendancePercentage < 60.0 || academicRecord.previousMarks < 45.0 -> PerformanceLevel.LOW
                score < 75.0 -> PerformanceLevel.AVERAGE
                else -> PerformanceLevel.GOOD
            }
        }

    override fun displayRole(): String {
        return "Student: $name | ID: $studentId | Course: $course (Sem $semester) | Predicted: $predictedPerformance"
    }

    override fun toString(): String {
        return "Student(studentId='$studentId', name='$name', course='$course', sem=$semester, performance=$predictedPerformance)"
    }
}

// 4. Main Demonstration Driver
fun main() {
    println("==================================================================")
    println("  EXPERIMENT 5: KOTLIN DATA TYPES, COLLECTIONS, AND OOP  ")
    println("==================================================================")

    // Creating sample students with varied academic records
    val studentsList: List<Student> = listOf(
        Student("1", "Aarav Sharma", "aarav@edu.org", "STU1001", "Computer Science", 4,
            AcademicRecord(attendancePercentage = 92.5, studyHoursPerDay = 6.5, previousMarks = 86.0, assignmentCompletion = 95.0, internalMarks = 27.5, missedClasses = 2)),
        Student("2", "Priya Patel", "priya@edu.org", "STU1002", "Information Technology", 4,
            AcademicRecord(attendancePercentage = 68.0, studyHoursPerDay = 2.0, previousMarks = 48.0, assignmentCompletion = 55.0, internalMarks = 14.0, missedClasses = 14)),
        Student("3", "Rohan Verma", "rohan@edu.org", "STU1003", "Computer Science", 6,
            AcademicRecord(attendancePercentage = 78.0, studyHoursPerDay = 4.0, previousMarks = 66.0, assignmentCompletion = 80.0, internalMarks = 20.0, missedClasses = 6)),
        Student("4", "Sneha Chauhan", "sneha@edu.org", "STU1004", "Data Science", 2,
            AcademicRecord(attendancePercentage = 95.0, studyHoursPerDay = 7.0, previousMarks = 92.0, assignmentCompletion = 98.0, internalMarks = 29.0, missedClasses = 1)),
        Student("5", "Vikram Singh", "vikram@edu.org", "STU1005", "Computer Science", 4,
            AcademicRecord(attendancePercentage = 54.0, studyHoursPerDay = 1.5, previousMarks = 42.0, assignmentCompletion = 40.0, internalMarks = 11.0, missedClasses = 20))
    )

    // Collection Operation 1: Set of unique courses
    val uniqueCourses: Set<String> = studentsList.map { it.course }.toSet()
    println("\n1. Unique Courses (Kotlin Set):")
    uniqueCourses.forEach { println("   - $it") }

    // Collection Operation 2: Grouping Students by Course (Kotlin Map<String, List<Student>>)
    println("\n2. Students Grouped by Course (Kotlin Map):")
    val studentsByCourse: Map<String, List<Student>> = studentsList.groupBy { it.course }
    studentsByCourse.forEach { (course, list) ->
        println("   Course: $course (${list.size} students)")
        list.forEach { println("     * ${it.displayRole()}") }
    }

    // Collection Operation 3: Grouping Students by Performance Level
    println("\n3. Students Grouped by Predicted Performance Category:")
    val studentsByPerformance: Map<PerformanceLevel, List<Student>> = studentsList.groupBy { it.predictedPerformance }
    studentsByPerformance.forEach { (perf, list) ->
        println("   Category [$perf]: ${list.size} students")
        list.forEach { s ->
            println("     - ${s.name} (Att: ${s.academicRecord.attendancePercentage}%, Prev: ${s.academicRecord.previousMarks}%)")
        }
    }

    // Collection Operation 4: At-Risk Filtering
    println("\n4. Identified At-Risk Students Requiring Mentoring:")
    val atRiskStudents = studentsList.filter { it.academicRecord.isAtRisk() }
    atRiskStudents.forEach {
        println("   ⚠️ AT-RISK: ${it.name} [${it.studentId}] - Att: ${it.academicRecord.attendancePercentage}%, Prev: ${it.academicRecord.previousMarks}%")
    }

    println("\n==================================================================")
    println("  Experiment 5 execution completed successfully.  ")
    println("==================================================================")
}
