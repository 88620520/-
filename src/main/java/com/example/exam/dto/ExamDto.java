package com.example.exam.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExamDto {
    private Long id;
    private String name;
    private List<QuestionDto> questions;
    private Integer totalScore;
} 