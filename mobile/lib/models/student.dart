class Student {
  final int? id;
  final String studentId;
  final String name;
  final String email;
  final String course;
  final int semester;
  final double attendancePercentage;
  final double studyHoursPerDay;
  final double previousMarks;
  final double assignmentCompletion;
  final double internalMarks;
  final int missedClasses;
  final String predictedPerformance;
  final double? predictionConfidence;
  final bool atRisk;

  Student({
    this.id,
    required this.studentId,
    required this.name,
    required this.email,
    required this.course,
    required this.semester,
    required this.attendancePercentage,
    required this.studyHoursPerDay,
    required this.previousMarks,
    required this.assignmentCompletion,
    required this.internalMarks,
    required this.missedClasses,
    required this.predictedPerformance,
    this.predictionConfidence,
    this.atRisk = false,
  });

  factory Student.fromJson(Map<String, dynamic> json) {
    return Student(
      id: json['id'] as int?,
      studentId: json['studentId'] ?? json['student_id'] ?? '',
      name: json['name'] ?? '',
      email: json['email'] ?? '',
      course: json['course'] ?? '',
      semester: json['semester'] ?? 1,
      attendancePercentage: (json['attendancePercentage'] ?? json['attendance_percentage'] ?? 0.0).toDouble(),
      studyHoursPerDay: (json['studyHoursPerDay'] ?? json['study_hours_per_day'] ?? 0.0).toDouble(),
      previousMarks: (json['previousMarks'] ?? json['previous_marks'] ?? 0.0).toDouble(),
      assignmentCompletion: (json['assignmentCompletion'] ?? json['assignment_completion'] ?? 0.0).toDouble(),
      internalMarks: (json['internalMarks'] ?? json['internal_marks'] ?? 0.0).toDouble(),
      missedClasses: (json['missedClasses'] ?? json['missed_classes'] ?? 0) as int,
      predictedPerformance: json['predictedPerformance'] ?? json['predicted_performance'] ?? 'AVERAGE',
      predictionConfidence: json['predictionConfidence'] != null ? (json['predictionConfidence'] as num).toDouble() : null,
      atRisk: json['atRisk'] ?? json['is_at_risk'] ?? false,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      'studentId': studentId,
      'name': name,
      'email': email,
      'course': course,
      'semester': semester,
      'attendancePercentage': attendancePercentage,
      'studyHoursPerDay': studyHoursPerDay,
      'previousMarks': previousMarks,
      'assignmentCompletion': assignmentCompletion,
      'internalMarks': internalMarks,
      'missedClasses': missedClasses,
      'predictedPerformance': predictedPerformance,
    };
  }
}

class PredictionResult {
  final String studentId;
  final String name;
  final String predictedPerformance;
  final double confidence;
  final List<String> contributingFactors;
  final List<String> academicRecommendations;
  final List<String> decisionPath;
  final bool isAtRisk;

  PredictionResult({
    required this.studentId,
    required this.name,
    required this.predictedPerformance,
    required this.confidence,
    required this.contributingFactors,
    required this.academicRecommendations,
    required this.decisionPath,
    required this.isAtRisk,
  });

  factory PredictionResult.fromJson(Map<String, dynamic> json) {
    return PredictionResult(
      studentId: json['studentId'] ?? json['student_id'] ?? '',
      name: json['name'] ?? 'Student',
      predictedPerformance: json['predictedPerformance'] ?? json['predicted_performance'] ?? 'AVERAGE',
      confidence: (json['confidence'] ?? 0.90).toDouble(),
      contributingFactors: (json['contributingFactors'] ?? json['contributing_factors'] as List<dynamic>?)
              ?.map((e) => e.toString())
              .toList() ??
          [],
      academicRecommendations: (json['academicRecommendations'] ?? json['academic_recommendations'] as List<dynamic>?)
              ?.map((e) => e.toString())
              .toList() ??
          [],
      decisionPath: (json['decisionPath'] ?? json['decision_path'] as List<dynamic>?)
              ?.map((e) => e.toString())
              .toList() ??
          [],
      isAtRisk: json['isAtRisk'] ?? json['is_at_risk'] ?? false,
    );
  }
}
