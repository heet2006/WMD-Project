# UN Sustainable Development Goal Alignment: SDG 4 (Quality Education)

## 🎯 Primary Goal
**SDG 4: Ensure inclusive and equitable quality education and promote lifelong learning opportunities for all.**

This project addresses systemic educational retention challenges through explainable, transparent machine learning that serves educators as a decision-support early warning system.

---

## 📌 Targeted SDG 4 Targets

### Target 4.1: Free, equitable, and quality primary and secondary/tertiary education leading to relevant and effective learning outcomes
- **Problem**: In traditional classroom management, academic struggles often remain unnoticed until end-of-semester examination failures occur, resulting in irreversible course dropouts or academic probation.
- **Solution**: The Decision Tree model evaluates multiple continuous indicators (`attendance_percentage`, `study_hours_per_day`, `assignment_completion`, `internal_marks`, `missed_classes`) to detect early distress signals weeks before final exams.
- **Measurable Impact**: Early alerts enable timely interventions, lowering cohort attrition and improving completion rates.

### Target 4.4: Substantially increase the number of youth and adults who have relevant skills, including technical and vocational skills
- **Explainable Pedagogical Support**: Rather than acting as a black-box scoring algorithm, our system generates human-interpretable Decision Tree split rules and specific remedial action plans (peer tutoring, study scheduling, mentor check-ins).
- **Growth Pathways**: High-performing students are identified for advanced honors coursework, hackathons, and research mentorship.

---

## ⚖️ Ethical Machine Learning Framework

1. **Non-Punitive Design**: Model classifications (`Low`, `Average`, `Good`) are strictly advisory. The system enforces policy warnings that algorithmic output should never replace human educators' empathy and contextual understanding.
2. **Exclusion of Sensitive Attributes**: Personal, socioeconomic, religious, or protected demographic traits are strictly excluded from the feature space, preventing algorithmic bias.
3. **Data Transparency & Auditing**: Every prediction records an immutable decision path in Cloud Firestore, guaranteeing institutional accountability.
