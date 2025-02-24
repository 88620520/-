package com.example.exam.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExerciseRecordDto {
    private Long id;
    private String type;
    private Integer questionCount;
    private Double correctRate;
    private LocalDateTime createdAt;
} 