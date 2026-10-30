package com.edu.studentpredict.repository;

import com.edu.studentpredict.model.PredictionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRecordRepository extends JpaRepository<PredictionRecord, Long> {
    List<PredictionRecord> findByStudentIdOrderByTimestampDesc(String studentId);
    Page<PredictionRecord> findAllByOrderByTimestampDesc(Pageable pageable);
}
