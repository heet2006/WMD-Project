import 'package:flutter_test/flutter_test.dart';
import 'package:student_performance_mobile/main.dart';
import 'package:student_performance_mobile/models/student.dart';

void main() {
  test('Student model serialization and deserialization test', () {
    final student = Student(
      id: 1,
      studentId: 'STU1001',
      name: 'Test Student',
      email: 'test@campus.edu',
      course: 'Computer Science',
      semester: 4,
      attendancePercentage: 92.0,
      studyHoursPerDay: 5.0,
      previousMarks: 85.0,
      assignmentCompletion: 90.0,
      internalMarks: 25.0,
      missedClasses: 2,
      predictedPerformance: 'GOOD',
      predictionConfidence: 0.95,
      atRisk: false,
    );

    final json = student.toJson();
    expect(json['studentId'], 'STU1001');
    expect(json['attendancePercentage'], 92.0);

    final reconstructed = Student.fromJson(json);
    expect(reconstructed.name, 'Test Student');
    expect(reconstructed.predictedPerformance, 'GOOD');
  });

  testWidgets('Mobile app smoke test renders title', (WidgetTester tester) async {
    await tester.pumpWidget(const EduPredictMobileApp());
    expect(find.text('EduPredict AI'), findsWidgets);
  });
}
