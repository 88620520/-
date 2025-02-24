package com.example.exam.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ExerciseSubmitDto {
    private Long chapterId;
    private Map<Long, String> answers;  // key: questionId, value: userAnswer
} 