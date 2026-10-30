import 'package:flutter/material.dart';
import '../services/api_service.dart';
import 'prediction_result_screen.dart';

class StudentFormScreen extends StatefulWidget {
  const StudentFormScreen({super.key});

  @override
  State<StudentFormScreen> createState() => _StudentFormScreenState();
}

class _StudentFormScreenState extends State<StudentFormScreen> {
  final _formKey = GlobalKey<FormState>();
  final ApiService _apiService = ApiService();

  String _studentId = 'STU1030';
  String _name = '';
  String _email = '';
  String _course = 'Computer Science';
  int _semester = 4;
  double _attendance = 80.0;
  double _studyHours = 4.0;
  double _previousMarks = 70.0;
  double _assignment = 85.0;
  double _internal = 22.0;
  int _missedClasses = 4;

  bool _submitting = false;

  final List<String> _courses = [
    'Computer Science',
    'Information Technology',
    'Data Science',
    'Software Engineering',
    'Electronics Engineering',
  ];

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    _formKey.currentState!.save();

    setState(() => _submitting = true);

    final payload = {
      'studentId': _studentId,
      'name': _name,
      'email': _email.isNotEmpty ? _email : '${_studentId.toLowerCase()}@campus.edu',
      'course': _course,
      'semester': _semester,
      'attendancePercentage': _attendance,
      'studyHoursPerDay': _studyHours,
      'previousMarks': _previousMarks,
      'assignmentCompletion': _assignment,
      'internalMarks': _internal,
      'missedClasses': _missedClasses,
    };

    final result = await _apiService.predictPerformance(payload);
    await _apiService.createStudent(payload);

    setState(() => _submitting = false);

    if (mounted) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (_) => PredictionResultScreen(result: result),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Register & Predict Student', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              TextFormField(
                initialValue: _studentId,
                decoration: const InputDecoration(labelText: 'Student ID *', border: OutlineInputBorder()),
                validator: (val) => val == null || val.isEmpty ? 'Student ID required' : null,
                onSaved: (val) => _studentId = val!,
              ),
              const SizedBox(height: 12),
              TextFormField(
                decoration: const InputDecoration(labelText: 'Full Name *', border: OutlineInputBorder()),
                validator: (val) => val == null || val.isEmpty ? 'Full name required' : null,
                onSaved: (val) => _name = val!,
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<String>(
                value: _course,
                decoration: const InputDecoration(labelText: 'Course Program', border: OutlineInputBorder()),
                items: _courses.map((c) => DropdownMenuItem(value: c, child: Text(c))).toList(),
                onChanged: (val) => setState(() => _course = val!),
              ),
              const SizedBox(height: 16),
              const Text('Academic Observations (ML Inputs):', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
              const SizedBox(height: 8),

              // Attendance Slider
              Text('Attendance: ${_attendance.toStringAsFixed(1)}% (Threshold: 75%)', style: const TextStyle(fontSize: 12)),
              Slider(
                value: _attendance,
                min: 30,
                max: 100,
                divisions: 70,
                label: '${_attendance.round()}%',
                activeColor: _attendance < 75 ? Colors.rose : Colors.indigo,
                onChanged: (v) => setState(() => _attendance = v),
              ),

              // Previous Marks Slider
              Text('Previous Exam Score: ${_previousMarks.toStringAsFixed(1)}%', style: const TextStyle(fontSize: 12)),
              Slider(
                value: _previousMarks,
                min: 20,
                max: 100,
                divisions: 80,
                label: '${_previousMarks.round()}%',
                onChanged: (v) => setState(() => _previousMarks = v),
              ),

              // Study Hours
              Text('Study Hours/Day: ${_studyHours.toStringAsFixed(1)} hrs', style: const TextStyle(fontSize: 12)),
              Slider(
                value: _studyHours,
                min: 0,
                max: 12,
                divisions: 24,
                label: '${_studyHours.toStringAsFixed(1)}h',
                onChanged: (v) => setState(() => _studyHours = v),
              ),

              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.indigo,
                    foregroundColor: Colors.white,
                    padding: const EdgeInsets.symmetric(vertical: 14),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  ),
                  onPressed: _submitting ? null : _submit,
                  child: _submitting
                      ? const SizedBox(width: 20, height: 20, child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2))
                      : const Text('Evaluate with Decision Tree ML', style: TextStyle(fontWeight: FontWeight.bold)),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
