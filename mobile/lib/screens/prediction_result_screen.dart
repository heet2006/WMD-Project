import 'package:flutter/material.dart';
import '../models/student.dart';
import '../widgets/stat_badge.dart';

class PredictionResultScreen extends StatelessWidget {
  final PredictionResult result;

  const PredictionResultScreen({super.key, required this.result});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('ML Prediction Outcome', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Outcome Card
            Card(
              elevation: 0,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
                side: BorderSide(color: Colors.grey.shade200),
              ),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.between,
                      children: [
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(result.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                            Text('ID: ${result.studentId}', style: TextStyle(color: Colors.grey.shade600, fontSize: 12)),
                          ],
                        ),
                        PerformanceBadgeWidget(level: result.predictedPerformance, isLarge: true),
                      ],
                    ),
                    const Divider(height: 24),
                    Text(
                      'Model Confidence: ${(result.confidence * 100).round()}%',
                      style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 13),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      result.isAtRisk
                          ? '⚠️ Tagged for Academic Support Intervention (SDG 4)'
                          : '✅ On track for satisfactory degree progression',
                      style: TextStyle(
                        color: result.isAtRisk ? Colors.red.shade700 : Colors.green.shade700,
                        fontWeight: FontWeight.w600,
                        fontSize: 12,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),

            // Contributing Factors
            if (result.contributingFactors.isNotEmpty) ...[
              const Text('Key Contributing Factors', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
              const SizedBox(height: 8),
              ...result.contributingFactors.map((f) => Padding(
                    padding: const EdgeInsets.only(bottom: 6.0),
                    child: Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text('• ', style: TextStyle(fontWeight: FontWeight.bold)),
                        Expanded(child: Text(f, style: const TextStyle(fontSize: 12))),
                      ],
                    ),
                  )),
              const SizedBox(height: 16),
            ],

            // Academic Recommendations
            if (result.academicRecommendations.isNotEmpty) ...[
              const Text('Academic Recommendations', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
              const SizedBox(height: 8),
              ...result.academicRecommendations.map((r) => Card(
                    margin: const EdgeInsets.only(bottom: 8),
                    color: Colors.grey.shade50,
                    elevation: 0,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10),
                      side: BorderSide(color: Colors.grey.shade200),
                    ),
                    child: Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: Row(
                        children: [
                          const Icon(Icons.check_circle_outline, color: Colors.green, size: 18),
                          const SizedBox(width: 8),
                          Expanded(child: Text(r, style: const TextStyle(fontSize: 12))),
                        ],
                      ),
                    ),
                  )),
            ],

            const SizedBox(height: 20),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.indigo,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 12),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                onPressed: () => Navigator.pop(context),
                child: const Text('Back to Home'),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
