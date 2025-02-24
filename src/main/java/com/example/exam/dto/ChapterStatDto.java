package com.example.exam.dto;

import lombok.Data;

@Data
public class ChapterStatDto {
    private String name;
    private Integer exerciseCount;
    private Integer questionCount;
    private Double correctRate;
    private Double masteryRate;
} 