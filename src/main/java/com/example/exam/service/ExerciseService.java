package com.example.exam.service;

import com.example.exam.dto.ExerciseRequestDto;
import com.example.exam.dto.ExerciseResultDto;
import com.example.exam.entity.ExerciseRecord;
import com.example.exam.entity.ExerciseQuestionRecord;
import java.util.List;

public interface ExerciseService {
    ExerciseResultDto startExercise(ExerciseRequestDto requestDto);
    void submitExercise(ExerciseResultDto resultDto);
    void saveExerciseRecord(ExerciseRecord record);
    void saveExerciseQuestionRecords(List<ExerciseQuestionRecord> records);
} 