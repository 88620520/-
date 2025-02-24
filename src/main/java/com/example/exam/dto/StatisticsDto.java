package com.example.exam.dto;

import lombok.Data;

@Data
public class StatisticsDto {
    private Integer totalExercises;
    private Integer totalQuestions;
    private Double averageScore;
    private Integer totalTime;
    private Double correctRate;
} 