package com.example.exam.dto;

import lombok.Data;

@Data
public class QuestionDto {
    private Long id;
    private Long chapterId;
    private String type;
    private String content;
    private String options;
    private String correctAnswer;
    private String difficulty;
    private String analysis;
    private String chapterName;
} 