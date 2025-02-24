package com.example.exam.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ExerciseResultDto {
    private Long exerciseId;
    private Map<Long, String> answers; // questionId -> userAnswer
    private Integer score;
} 